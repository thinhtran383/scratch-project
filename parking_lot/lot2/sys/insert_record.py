import sqlite3
import datetime

# Đường dẫn đến cơ sở dữ liệu
DATABASE_PATH = 'parking_sys.db'

# Kết nối đến cơ sở dữ liệu SQLite
connection = sqlite3.connect(DATABASE_PATH)
cursor = connection.cursor()

id = "123456"
rfid_code = "RFID123456"
license_plate = "ABC-1234"
check_in_time = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
check_out_time = None
image_path = "plate.jpg"

sql_insert = """
INSERT INTO checking (id, rfid_code, license_plate, check_in_time, check_out_time, image_path)
VALUES (?, ?, ?, ?, ?, ?)
"""

cursor.execute(sql_insert, (id,rfid_code, license_plate, check_in_time, check_out_time, image_path))
connection.commit()

print("Đã thêm bản ghi mới vào bảng checking.")

# Đóng kết nối
connection.close()
