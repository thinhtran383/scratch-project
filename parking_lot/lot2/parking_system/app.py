from flask import Flask, render_template, request, redirect, url_for, flash, session
from flask_login import LoginManager, UserMixin, login_user, logout_user, login_required, current_user
import sqlite3
from datetime import datetime
import base64

app = Flask(__name__)
app.secret_key = "supersecretkey"

# Cấu hình Flask-Login
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'

# Define and register the base64 encoding function as a filter
def b64encode(value):
    return base64.b64encode(value).decode('utf-8')

app.jinja_env.filters['b64encode'] = b64encode  # Register filter here
# User class for Flask-Login
class User(UserMixin):
    def __init__(self, id, username):
        self.id = id
        self.username = username

@login_manager.user_loader
def load_user(user_id):
    conn = get_db_connection()
    user = conn.execute('SELECT * FROM users WHERE id = ?', (user_id,)).fetchone()
    conn.close()
    if user:
        return User(user['id'], user['name'])
    return None

# Database connection setup
def get_db_connection():
    conn = sqlite3.connect('parking_system.db')
    conn.row_factory = sqlite3.Row
    return conn

# Initialize database with only users and checking tables
def create_database():
    conn = get_db_connection()
    cursor = conn.cursor()

    # Users table with a default "admin" account
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT UNIQUE,
            password TEXT
        );
    ''')
    cursor.execute('INSERT OR IGNORE INTO users (name, password) VALUES (?, ?)', ('admin', 'admin'))

    # Checking table to log entry and exit times
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS checking (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            rfid TEXT UNIQUE,
            license_plate TEXT UNIQUE,
            entry_time DATETIME DEFAULT CURRENT_TIMESTAMP,
            exit_time DATETIME
        );
    ''')

    conn.commit()
    conn.close()

# Call to create the database on server start
create_database()

# Route for registration (currently limited to admin only)
@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']

        if not username or not password:
            flash('Username and password are required!')
        else:
            try:
                conn = get_db_connection()
                conn.execute('INSERT INTO users (name, password) VALUES (?, ?)', (username, password))
                conn.commit()
                conn.close()
                flash('Registration successful!')
                return redirect(url_for('login'))
            except sqlite3.IntegrityError:
                flash('Username already exists!')

    return render_template('register.html')

# Route for login
@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        conn = get_db_connection()
        user = conn.execute('SELECT * FROM users WHERE name = ? AND password = ?', (username, password)).fetchone()
        conn.close()

        if user:
            user_obj = User(user['id'], user['name'])
            login_user(user_obj)
            return redirect(url_for('index'))
        else:
            flash('Incorrect username or password!')

    return render_template('login.html')

# Route for logout
@app.route('/logout')
@login_required
def logout():
    logout_user()
    flash('You have been logged out successfully!')
    return redirect(url_for('login'))

# Homepage - displays parking logs
@app.route('/')
@login_required
def index():
    conn = get_db_connection()
    logs = conn.execute('SELECT * FROM checking ORDER BY entry_time DESC').fetchall()
    conn.close()
    return render_template('index.html', logs=logs)

# Entry route
@app.route('/entry', methods=['POST'])
@login_required
def entry():
    rfid = request.form['rfid']
    license_plate = request.form['license_plate']

    conn = get_db_connection()
    conn.execute('''
        INSERT INTO checking (rfid, license_plate, entry_time)
        VALUES (?, ?, datetime('now'))
    ''', (rfid, license_plate))
    conn.commit()
    conn.close()

    flash('Vehicle entry recorded successfully!')
    return redirect(url_for('index'))

# Exit route
@app.route('/exit', methods=['POST'])
@login_required
def exit():
    rfid = request.form['rfid']
    license_plate = request.form['license_plate']

    conn = get_db_connection()
    log = conn.execute('SELECT * FROM checking WHERE rfid = ? AND license_plate = ? AND exit_time IS NULL', 
                       (rfid, license_plate)).fetchone()

    if log:
        # Record the exit time
        exit_time = datetime.now()
        conn.execute('''
            UPDATE checking
            SET exit_time = ?
            WHERE id = ?
        ''', (exit_time, log['id']))
        conn.commit()
        conn.close()

        flash('Vehicle exit recorded successfully!')
    else:
        flash('Vehicle not found or has already exited!')
    return redirect(url_for('index'))

# Route for resetting password
@app.route('/reset_password', methods=['GET', 'POST'])
def reset_password():
    if request.method == 'POST':
        username = request.form['username']
        new_password = request.form['new_password']

        if not username or not new_password:
            flash('Username and new password are required!')
        else:
            conn = get_db_connection()
            user = conn.execute('SELECT * FROM users WHERE name = ?', (username,)).fetchone()
            if user:
                conn.execute('UPDATE users SET password = ? WHERE name = ?', (new_password, username))
                conn.commit()
                conn.close()
                flash('Password updated successfully!')
                return redirect(url_for('login'))
            else:
                flash('Username does not exist!')
                conn.close()

    return render_template('reset_password.html')
@app.route('/user_info')
@login_required
def user_info():
    conn = get_db_connection()
    user = conn.execute('SELECT * FROM users WHERE id = ?', (current_user.id,)).fetchone()
    conn.close()
    return render_template('user_info.html', user=user)

# Route for changing the logged-in user's password
@app.route('/change_password', methods=['GET', 'POST'])
@login_required
def change_password():
    if request.method == 'POST':
        current_password = request.form['current_password']
        new_password = request.form['new_password']

        conn = get_db_connection()
        user = conn.execute('SELECT * FROM users WHERE id = ?', (current_user.id,)).fetchone()
        if user and user['password'] == current_password:
            conn.execute('UPDATE users SET password = ? WHERE id = ?', (new_password, current_user.id))
            conn.commit()
            conn.close()
            flash('Password updated successfully!')
            return redirect(url_for('index'))
        else:
            flash('Current password is incorrect!')
            conn.close()

    return render_template('change_password.html')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
