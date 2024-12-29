from machine import Pin, PWM
import time

class Servo:
    def __init__(self, gpio_pin):
        self.servo_pin = Pin(gpio_pin)
        self.pwm = PWM(self.servo_pin)
        self.pwm.freq(50)  # Tần số PWM cho servo

    def control(self, angle):
        # Xác định độ rộng xung PWM tương ứng với góc
        if angle > 0:
            # Quay từ trái sang phải (0-180 độ)
            duty = 40 + (angle / 180) * (115 - 40)  # Tính toán duty cycle cho góc dương
        else:
            # Quay từ phải sang trái (-180 đến 0 độ)
            duty = 115 + (abs(angle) / 180) * (40 - 115)  # Tính toán duty cycle cho góc âm

        # Giới hạn giá trị duty để tránh lỗi
        duty = max(40, min(115, duty))

        self.pwm.duty_u16(int(duty * 65535 / 100))  # Chuyển đổi sang giá trị 16 bit
        time.sleep(1.5)  # Chờ servo quay hết
        self.stop()

    def stop(self):
        self.pwm.deinit()  # Dừng PWM khi không sử dụng


