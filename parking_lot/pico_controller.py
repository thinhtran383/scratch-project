import serial
import time

#  thuc hien viec gui nhan messsage
class PicoController: 
    def __init__(self, port, baud_rate=9600):
        print(f"Đang kết nối với Pico ở cổng {port} với baud rate {baud_rate}...")
        self.port = port
        self.baud_rate = baud_rate
        self.s = serial.Serial(self.port, self.baud_rate)  
        time.sleep(2)
        
        print("Đã kết nối với Pico.")

    def read_message(self):
        """Đọc thông điệp từ Pico."""
        if self.s.in_waiting > 0: 
            response = self.s.readline().decode().strip() 
            return response
        return None

    def send_message(self, message):
        """Gửi thông điệp đến Pico."""
        self.s.write((message + '\n').encode())  # Send message to Pico
        print(f"Đã gửi lệnh: {message}")

    def close(self):
        """Đóng cổng serial."""
        self.s.close()

# if __name__ == "__main__":
#     pico = PicoController("COM3")  

#     try:
#         while True:
#             # Đọc phản hồi từ Pico
#             response = pico.send_message("Hello, Pico!")
#             if response:
#                 print(f"Phản hồi từ Pico: {response}")
#     finally:
#         pico.close()  
