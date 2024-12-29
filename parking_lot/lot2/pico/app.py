from rfid_reader import RFIDReader
from api_client import ApiClient
from ir_sensor import IRSensor
from lcd_i2c import LCD
from servo_motor import Servo
from wifi_connection import WiFiConnection
import time


wifi = WiFiConnection('Thinh', '19216811').connect()
api_client = ApiClient()

rfid_in = RFIDReader(spi_id=0, sck=6, miso=4, mosi=7, cs=5, rst=22)
rfid_out = RFIDReader(spi_id=0, sck=6, miso=4, mosi=7, cs=15, rst=22)

ir = IRSensor(14)

servo = Servo(13)

lcd = LCD()

url_in = 'https://local.thinhtran.online/car-in'
url_out = 'https://local.thinhtran.online/car-out' 

lcd.lcd_clear()


print("System is ready. Bring the RFID tag closer...")
lcd.lcd_clear()
time.sleep(0.1) 
lcd.lcd_display_string("System Ready", 1)
lcd.lcd_display_string("Scan RFID", 2)

while True:
    # Check if an RFID tag is detected by the entry RFID reader
    card_id_in = rfid_in.read_rfid()
    if card_id_in:
        print("RFID detected - CARD ID: " + str(card_id_in))
        
        body = {"rfid_code": str(card_id_in)}
        car_plate = api_client.send_request(url_in, 'post', body)
        
        if isinstance(car_plate, str) and "404" in car_plate:
            lcd.lcd_clear()
            time.sleep(0.4)  
            lcd.lcd_display_string("RFID Conflic", 1)
            lcd.lcd_display_string("Cannot Exit", 2)
            time.sleep(3)  
            
            
            lcd.lcd_clear()
            time.sleep(0.4) 
            lcd.lcd_display_string("System Ready", 1)
            lcd.lcd_display_string("Scan RFID", 2)
            continue  # Skip the rest of the loop to wait for the next RFID scan
        
        # Update LCD for RFID detection
        lcd.lcd_clear()
        time.sleep(0.4)  
        lcd.lcd_display_string(str(car_plate), 1)
        lcd.lcd_display_string("Opening gate...", 2)
        
        # Rotate servo to 120 degrees to open the gate
        servo.control(120)

        # Wait for IR sensor to detect an obstacle passing through
        print("Waiting for obstacle to pass through...")
        lcd.lcd_clear()
        time.sleep(0.1)
        lcd.lcd_display_string("Gate Open", 1)
        lcd.lcd_display_string("Waiting...", 2)
        
        while not ir.is_obstacle_detected():
            time.sleep(0.1) 
        
        print("Obstacle detected. Closing gate...")
        
       
        lcd.lcd_clear()
        time.sleep(0.1)
        lcd.lcd_display_string("Obstacle Detected", 1)
        lcd.lcd_display_string("Closing gate...", 2)
        
        
        servo.control(-120)
        
       
        time.sleep(1)
        lcd.lcd_clear()
        time.sleep(0.1)
        lcd.lcd_display_string("System Ready", 1)
        lcd.lcd_display_string("Scan RFID", 2)
    
 
    card_id_out = rfid_out.read_rfid()
    if card_id_out:
        print("RFID detected at exit - CARD ID: " + str(card_id_out))
        
      
        body = {"rfid_code": str(card_id_out)}
        response = api_client.send_request(url_out, 'post', body)
        
      
        if isinstance(response, str) and "404" in response:
            lcd.lcd_clear()
            time.sleep(0.4)  
            lcd.lcd_display_string("Car Not Match", 1)
            lcd.lcd_display_string("Cannot Exit", 2)
            time.sleep(3)  
            
            # Clear the LCD and reset to the default state
            lcd.lcd_clear()
            time.sleep(0.2)  
            lcd.lcd_display_string("System Ready", 1)
            lcd.lcd_display_string("Scan RFID", 2)
            continue  
        
        # Update LCD for valid exit RFID detection
        lcd.lcd_clear()
        time.sleep(0.1) 
        lcd.lcd_display_string(str(response), 1)
        lcd.lcd_display_string("Exiting...", 2)
        
       
        servo.control(120)
        
        # Wait for IR sensor to detect obstacle exiting
        print("Waiting for obstacle to exit...")
        lcd.lcd_clear()
        time.sleep(0.1)
        lcd.lcd_display_string("Gate Open", 1)
        lcd.lcd_display_string("Waiting...", 2)
        
        while not ir.is_obstacle_detected():
            time.sleep(0.1) 
        
        print("Obstacle exited. Closing gate...")
        
        # Update LCD for obstacle exit detection
        lcd.lcd_clear()
        time.sleep(0.1)
        lcd.lcd_display_string("Exit Detected", 1)
        lcd.lcd_display_string("Closing gate...", 2)
        
       
        servo.control(-120)
        
        
        time.sleep(1)
        lcd.lcd_clear()
        time.sleep(0.1)
        lcd.lcd_display_string("System Ready", 1)
        lcd.lcd_display_string("Scan RFID", 2)
