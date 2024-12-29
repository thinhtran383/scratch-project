import time
import random
import requests

# Địa chỉ URL của server Flask
FLASK_SERVER_URL = 'http://127.0.0.1:5000/api/logs'  # Thay đổi địa chỉ nếu Flask chạy trên server khác

def read_rfid():
    # Giả lập việc đọc RFID bằng cách tạo ngẫu nhiên một mã RFID
    rfids = ['123456', '234567', '345678', '456789']
    return random.choice(rfids)

def send_to_server(rfid, action):
    try:
        data = {
            'rfid': rfid,
            'action': action
        }
        response = requests.post(FLASK_SERVER_URL, json=data)
        if response.status_code == 200:
            print('Dữ liệu đã được gửi thành công.')
        else:
            print('Lỗi khi gửi dữ liệu:', response.text)
    except Exception as e:
        print('Không thể kết nối tới server:', e)

if __name__ == '__main__':
    while True:
        rfid = read_rfid()
        print(f'RFID đọc được: {rfid}')
        action = random.choice(['Entry', 'Exit'])
        send_to_server(rfid, action)
        time.sleep(5)  # Chờ 5 giây trước khi đọc RFID tiếp theo
import RPi.GPIO as GPIO
import time
import serial
import requests

# Cấu hình GPIO
GPIO.setmode(GPIO.BCM)
RELAY_PIN = 18
GPIO.setup(RELAY_PIN, GPIO.OUT)

# Cấu hình kết nối UART cho module RFID
ser = serial.Serial('/dev/ttyS0', 9600)

# URL API của web để ghi log
API_URL = 'http://<IP_SERVER>:5000/api/logs'  # Thay <IP_SERVER> bằng IP máy chủ Flask

def open_gate():
    GPIO.output(RELAY_PIN, GPIO.HIGH)  # Kích hoạt relay để mở cổng
    time.sleep(2)  # Mở cổng trong 2 giây
    GPIO.output(RELAY_PIN, GPIO.LOW)   # Tắt relay để đóng cổng

def send_log(rfid, action):
    try:
        data = {'rfid': rfid, 'action': action}
        response = requests.post(API_URL, json=data)
        if response.status_code == 200:
            print('Log đã được ghi lại.')
        else:
            print('Ghi log thất bại:', response.json())
    except Exception as e:
        print('Không thể kết nối tới server:', e)

try:
    while True:
        if ser.in_waiting > 0:
            rfid = ser.readline().decode('utf-8').strip()
            print('RFID được quét:', rfid)
            open_gate()
            send_log(rfid, 'Entry')  # Ghi log vào trang web
            time.sleep(2)  # Đợi một khoảng thời gian trước khi đọc RFID tiếp theo
except KeyboardInterrupt:
    print("Đã dừng chương trình")
finally:
    GPIO.cleanup()
    ser.close()
