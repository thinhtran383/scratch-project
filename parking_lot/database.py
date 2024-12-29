import sqlite3
from datetime import datetime

class Database:
    def __init__(self, db_name="car_parking.db"):
        self.conn = sqlite3.connect(db_name)
        self.cursor = self.conn.cursor()
        self.create_table()

    def create_table(self):
        """Tạo bảng nếu chưa tồn tại."""
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS parking_records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                car_place TEXT,
                rfid TEXT,
                time_in TEXT,
                time_out TEXT
            )
        ''')
        self.conn.commit()

    def insert_record(self, car_place, rfid, time_in=None, time_out=None):
        """Thêm một bản ghi mới vào cơ sở dữ liệu."""
        if time_in is None:
            time_in = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        self.cursor.execute('''
            INSERT INTO parking_records (car_place, rfid, time_in, time_out)
            VALUES (?, ?, ?, ?)
        ''', (car_place, rfid, time_in, time_out))
        self.conn.commit()

    def update_time_out(self, rfid, time_out=None):
        """Cập nhật thời gian ra và xóa RFID sau khi quẹt thẻ lần hai."""
        if time_out is None:
            time_out = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        self.cursor.execute('''
            UPDATE parking_records
            SET time_out = ?, rfid = NULL
            WHERE rfid = ? AND time_out IS NULL
        ''', (time_out, rfid))
        self.conn.commit()

    def fetch_active_record(self, rfid):
        """Tìm kiếm bản ghi đang hoạt động (chưa có time_out) dựa trên RFID."""
        self.cursor.execute('SELECT * FROM parking_records WHERE rfid = ? AND time_out IS NULL', (rfid,))
        return self.cursor.fetchone()

    def fetch_all_records(self):
        """Lấy tất cả các bản ghi trong cơ sở dữ liệu."""
        self.cursor.execute('SELECT * FROM parking_records')
        return self.cursor.fetchall()
    
    def delete_all_records(self):
        """Xóa tất cả các bản ghi."""
        self.cursor.execute('DELETE FROM parking_records')
        self.conn.commit()

    def close(self):
        """Đóng kết nối với cơ sở dữ liệu."""
        self.conn.close()

