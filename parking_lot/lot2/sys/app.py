from flask import Flask, render_template, request, redirect, url_for, flash, jsonify
from flask_socketio import SocketIO
from flask_login import LoginManager, UserMixin, login_user, logout_user, login_required
from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.orm import sessionmaker, scoped_session, declarative_base
from flask_cors import CORS
import datetime
import uuid
from license_plate_detector import LicensePlateDetector
import paho.mqtt.client as mqtt

app = Flask(__name__)
CORS(app)
app.config['SECRET_KEY'] = 'your_secret_key'
socketio = SocketIO(app)

# Thiết lập Flask-Login
login_manager = LoginManager()
login_manager.init_app(app)

# Kết nối tới cơ sở dữ liệu
DATABASE_URL = 'sqlite:///parking_sys.db'
engine = create_engine(DATABASE_URL)
Base = declarative_base()
Session = scoped_session(sessionmaker(bind=engine))
session = Session()

# 
mqtt_broker = "localhost"
mqtt_port = 1883
mqtt_topic = "gate"
client = mqtt.Client()
client.connect(mqtt_broker, mqtt_port)

# ========== Model ==========
class User(UserMixin, Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True, autoincrement=True)
    username = Column(String(50), unique=True, nullable=False)
    password = Column(String(100), nullable=False)

class Checking(Base):
    __tablename__ = 'checking'
    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4())) 
    rfid_code = Column(String(50), unique=True, nullable=False)
    license_plate = Column(String(50), nullable=False)
    check_in_time = Column(DateTime, default=datetime.datetime.utcnow)
    check_out_time = Column(DateTime, nullable=True)
    image_path = Column(String(100), nullable=True)
    
    def to_dict(self):
        return {
            'id': self.id,
            'rfid_code': self.rfid_code,
            'license_plate': self.license_plate,
            'check_in_time': self.check_in_time.strftime('%d-%m-%Y %H:%M:%S'),
            'check_out_time': self.check_out_time.strftime('%d-%m-%Y %H:%M:%S') if self.check_out_time else None,
            'image_path': self.image_path
        }

# ========== Load User ==========

@login_manager.user_loader
def load_user(user_id):
    return session.query(User).get(user_id)

# ========== Route Đăng Nhập ==========

@app.route('/', methods=['GET', 'POST'])
def login():
    error = None
    
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        
        user = session.query(User).filter_by(username=username).first()
        
        if user and user.password == password: 
            login_user(user)
            return redirect(url_for('checking_info')) 
        else:
            error = 'Tên đăng nhập hoặc mật khẩu không đúng.'
    return render_template('login.html', error=error)

# ========== Route Hiển Thị Thông Tin Checking ==========
@app.route('/checking', methods=['GET'])
@login_required
def checking_info():
    checkings = session.query(Checking).all()  
    
    # Định dạng lại ngày giờ cho từng bản ghi
    formatted_checkings = [
        {
            'id': c.id,
            'rfid_code': c.rfid_code,
            'license_plate': c.license_plate,
            'check_in_time': c.check_in_time.strftime('%d-%m-%Y %H:%M:%S'),
            'check_out_time': c.check_out_time.strftime('%d-%m-%Y %H:%M:%S') if c.check_out_time else None,
            'image_path': c.image_path
        } for c in checkings
    ]
    
    return render_template('checking_info.html', checkings=formatted_checkings)


# ========== Route Đăng Xuất ==========
@app.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('login'))

# API ghi nhận thông tin xe ra
@app.route('/car-out', methods=['POST'])
def car_out():
    data = request.get_json()
    rfid_code = data.get('rfid_code')
    license_plate = LicensePlateDetector('best.pt').get_car_plate()
    
    if not rfid_code or not license_plate:
        return jsonify({"error": "Thiếu mã thẻ RFID hoặc biển số xe."}), 400
    
    checking_record = session.query(Checking).filter_by(
        rfid_code=rfid_code,
        license_plate=license_plate,
        check_out_time=None  
    ).first()
    
    if not checking_record:
        socketio.emit('error', {'license_plate':"Không tìm thấy bản ghi nào phù hợp hoặc xe đã được ghi nhận đã ra."})
        
        return jsonify({"error": "Không tìm thấy bản ghi nào phù hợp hoặc xe đã được ghi nhận đã ra."}), 404
    
    checking_record.check_out_time = datetime.datetime.now()
    checking_record.rfid_code = None
    
    session.commit()
    
    socketio.emit('car_out_success', {'license_plate': checking_record.license_plate})
    
    return jsonify({
        "message": checking_record.license_plate,
        "checking_id": checking_record.id,
        "license_plate": checking_record.license_plate,
        "check_in_time": checking_record.check_in_time.strftime('%d-%m-%Y %H:%M:%S'),
        "check_out_time": checking_record.check_out_time.strftime('%d-%m-%Y %H:%M:%S')
    }), 201

# API ghi nhận thông tin xe vào
@app.route('/car-in', methods=['POST'])
def car_in():
    data = request.get_json()
    rfid_code = data.get('rfid_code')
    license_plate = LicensePlateDetector('best.pt').get_car_plate()
    
    if not rfid_code or not license_plate:
        return jsonify({"error": "Thiếu mã thẻ RFID hoặc biển số xe."}), 400
    
    existing_checking = session.query(Checking).filter_by(
        rfid_code=rfid_code,
        check_out_time=None  
    ).first()
    
    if existing_checking:
        socketio.emit('error', {'license_plate':"RFID đã tồn tại, đã có xe sử dụng thẻ này."})
        
        return jsonify({"error": "RFID đã tồn tại. Xe đã ở trong bãi."}), 404
    
    new_checking = Checking(
        rfid_code=rfid_code,
        license_plate=license_plate,
        check_in_time=datetime.datetime.now(),
        check_out_time=None,
        image_path=license_plate + '.jpg'
    )
    
    session.add(new_checking)
    session.commit()
    
    socketio.emit('car_in_success', {'license_plate': new_checking.license_plate})
    
    return jsonify({
        "message": new_checking.license_plate, 
        "checking_id": new_checking.id,
        "license_plate": new_checking.license_plate
    }), 201

@socketio.on('connect')
def handle_connect():
    print("Client connected")

@socketio.on('disconnect')
def handle_disconnect():
    print("Client disconnected")

@app.route('/checkings', methods=['GET'])
def get_checkings():
    checkings = session.query(Checking).all() 
    return jsonify([checking.to_dict() for checking in checkings])

@app.route('/search', methods=['GET'])
def search():
    license_plate = request.args.get('license_plate', '')
    date = request.args.get('date', '')
    filter = request.args.get('filter', '') 

    if filter == 'in':
        checkings = session.query(Checking).filter(Checking.check_out_time.is_(None)).all()
    elif filter == 'out':
        checkings = session.query(Checking).filter(Checking.rfid_code.is_(None)).all() 
    else:
        checkings = session.query(Checking).all()

    if license_plate:
        checkings = [c for c in checkings if license_plate in c.license_plate]
    if date:
        try:
            parsed_date = datetime.datetime.strptime(date, '%d-%m-%Y')
            checkings = [c for c in checkings if c.check_in_time.date() == parsed_date.date()]
        except ValueError:
            return jsonify({"error": "Invalid date format, please use dd-mm-yyyy."}), 400

    return jsonify([checking.to_dict() for checking in checkings])

@app.route('/search_by_date', methods=['GET'])
def search_by_date():
    date = request.args.get('date')
    checkings = session.query(Checking).filter(Checking.check_in_time.contains(date)).all()
    return jsonify([checking.to_dict() for checking in checkings])

@app.route("/gate/open", methods=["POST"])
def open_gate():
    client.publish(mqtt_topic, "open")
    return jsonify({"status": "success", "message": "Lệnh mở cổng đã được gửi"})

@app.route("/gate/close", methods=["POST"])
def close_gate():
    client.publish(mqtt_topic, "close")
    return jsonify({"status": "success", "message": "Lệnh đóng cổng đã được gửi"})

@app.route('/statistics', methods=['GET'])
def get_statistics():
    cars_in_parking = session.query(Checking).filter(Checking.check_out_time.is_(None)).count()

    cars_out_parking = session.query(Checking).filter(Checking.check_out_time.isnot(None)).count()

    today = datetime.datetime.today().date()
    today_checkings = session.query(Checking).filter(
        Checking.check_in_time >= datetime.datetime(today.year, today.month, today.day)
    ).count()

    return jsonify({
        "cars_in_parking": cars_in_parking,
        "cars_out_parking": cars_out_parking,
        "today_checkings": today_checkings
    })

# ========== Chạy ứng dụng ==========
if __name__ == '__main__':
    Base.metadata.create_all(engine)
    app.run(debug=True)
