# ir_sensor.py

from machine import Pin
import time

class IRSensor:
    def __init__(self, pin_number):
        self.sensor_pin = Pin(pin_number, Pin.IN)

    def is_obstacle_detected(self):
        
        return self.sensor_pin.value() == 0

    def wait_for_obstacle(self, delay=0.5):
        
        while True:
            if self.is_obstacle_detected():
                print("Có vật cản!")
            else:
                print("Không có vật cản.")
            time.sleep(delay)


#ir = IRSensor(pin_number=14)

#ir.wait_for_obstacle()
