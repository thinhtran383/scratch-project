from mfrc522 import MFRC522
from machine import Pin, I2C
import time
import sys
import select  # Để kiểm tra có dữ liệu từ stdin hay không
from i2c_lcd import I2cLcd
from servo import Servo

DEFAULT_I2C_ADDR = 0x27  # Địa chỉ I2C của LCD
i2c = I2C(0, sda=Pin(0), scl=Pin(1), freq=400000)
lcd = I2cLcd(i2c, DEFAULT_I2C_ADDR, 2, 26)  # Khởi tạo màn hình LCD

s1 = Servo(10)
ir1 = Pin(2, Pin.IN, Pin.PULL_UP)  # Khởi tạo cảm biến hồng ngoại

mfrc522 = MFRC522(spi_id=0, sck=6, miso=4, mosi=7, cs=5, rst=22)  # Khởi tạo module đọc thẻ RFID

def servo_map(x, in_min, in_max, out_min, out_max):
    return (x - in_min) * (out_max - out_min) / (in_max - out_min) + out_min

def servo_angle(angle):
    """Điều khiển góc servo"""
    if angle < 0:
        angle = 0
    if angle > 180:
        angle = 180
    s1.goto(round(servo_map(angle, 0, 180, 0, 1024)))  # Chuyển đổi góc sang giá trị servo

def get_status_ir():
    """Lấy giá trị cảm biến hồng ngoại (0: có vật, 1: không có vật)"""
    return ir1.value()

def check_for_input(): # doc message tu host(lap)
    """Kiểm tra xem có lệnh từ host hay không, không chặn chương trình"""
    if select.select([sys.stdin], [], [], 0)[0]:  # Kiểm tra input từ stdin
        return sys.stdin.readline().strip()  # Đọc và trả về dữ liệu từ stdin
    return None  # Không có dữ liệu, trả về None

def read_rfid():
    """Chức năng chính để quét RFID, điều khiển servo và hiển thị thông tin trên LCD"""
    
    # Hiển thị thông báo chào mừng ban đầu trên LCD
    lcd.clear()
    lcd.putstr('Welcome to the')
    lcd.move_to(0, 1)
    lcd.putstr('car park')
    
    while True:
        # # Kiểm tra xem có lệnh từ host không (lệnh từ máy tính hoặc thiết bị khác)
        # message = check_for_input()  # Gọi hàm không chặn
        # if message:
        #     lcd.clear()  # Xóa màn hình
        #     lcd.putstr(message.lower())  # Hiển thị thông điệp từ host lên LCD
        lcd.clear()
        lcd.putstr('Welcome to the')
        lcd.move_to(0, 1)
        lcd.putstr('car park')
        
        # Quét thẻ RFID
        (status, _) = mfrc522.request(mfrc522.REQALL)
        if status == mfrc522.OK:
            # Lấy UID của thẻ
            (status, uid) = mfrc522.anticoll(mfrc522.PICC_ANTICOLL1)
            if status == mfrc522.OK:
                # Chuyển đổi UID thành chuỗi số để dễ đọc
                uid_string = ''.join([str(i) for i in uid])
                print("RFID:", uid_string)
                
                
                # Kiểm tra cảm biến hồng ngoại
                while True:
                          # Nhận biển số từ host và hiển thị trên LCD
                    plate_number = check_for_input()  # Giả sử plate number gửi từ host sau khi nhận diện
                    if plate_number == 'false':
                        lcd.clear()
                        lcd.putstr("Car plate:")
                        lcd.move_to(0, 1)
                        lcd.putstr("mismatch")
                        
                        time.sleep(2)
                        
                        lcd.clear()
                        lcd.putstr('Welcome to the')
                        lcd.move_to(0, 1)
                        lcd.putstr('car park')
                        
                        break
                        
                    elif plate_number == 'out':
                        lcd.clear()
                        lcd.putstr("Car plate:")
                        lcd.move_to(0, 1)
                        lcd.putstr("Car out")
                        servo_angle(100)
                    
                    elif plate_number:
                        if plate_number.startswith("in "):
                            plate_number = plate_number.split("in ")[1]
                        
                            lcd.clear()
                            lcd.putstr("Car plate:")
                            lcd.move_to(0, 1)
                            lcd.putstr(plate_number) # Hiển thị biển số xe lên LCD
                            servo_angle(100)
                    
                    
                    if get_status_ir() == 0:  # Cảm biến có giá trị 0 khi có vật đi qua
                        time.sleep(1.7)  # Chờ 1.7 giây trước khi đóng cổng
                        
                        # Quay lại màn hình Welcome
                        lcd.clear()
                        lcd.putstr('Welcome to the')
                        lcd.move_to(0, 1)
                        lcd.putstr('car park')
                        
                        # Đóng servo (góc -100 độ)
                        servo_angle(-100)
                        break  # Thoát khỏi vòng lặp kiểm tra cảm biến
            else:
                print("Không thể lấy UID thẻ.")
        
        time.sleep(1)  # Chờ một chút trước khi quét lại

# Bắt đầu chương trình đọc RFID
read_rfid()