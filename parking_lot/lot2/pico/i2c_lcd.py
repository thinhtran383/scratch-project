from machine import I2C, Pin
from time import sleep

# Địa chỉ I2C của LCD
ADDRESS = 0x27

# Thiết lập I2C trên Pico W với SDA ở GPIO 0 và SCL ở GPIO 1
i2c = I2C(0, scl=Pin(21), sda=Pin(20))

# Các lệnh LCD
LCD_CLEARDISPLAY = 0x01
LCD_RETURNHOME = 0x02
LCD_ENTRYMODESET = 0x04
LCD_DISPLAYCONTROL = 0x08
LCD_CURSORSHIFT = 0x10
LCD_FUNCTIONSET = 0x20
LCD_SETCGRAMADDR = 0x40
LCD_SETDDRAMADDR = 0x80

LCD_ENTRYLEFT = 0x02
LCD_DISPLAYON = 0x04
LCD_2LINE = 0x08
LCD_5x8DOTS = 0x00
LCD_BACKLIGHT = 0x08
En = 0b00000100  # Bit Enable
Rs = 0b00000001  # Bit Register select

class LCD:
    def __init__(self):
        self.lcd_write(0x03)
        self.lcd_write(0x03)
        self.lcd_write(0x03)
        self.lcd_write(0x02)

        self.lcd_write(LCD_FUNCTIONSET | LCD_2LINE | LCD_5x8DOTS)
        self.lcd_write(LCD_DISPLAYCONTROL | LCD_DISPLAYON)
        self.lcd_write(LCD_CLEARDISPLAY)
        self.lcd_write(LCD_ENTRYMODESET | LCD_ENTRYLEFT)
        sleep(0.2)

    def lcd_strobe(self, data):
        i2c.writeto(ADDRESS, bytes([data | En | LCD_BACKLIGHT]))
        sleep(0.0005)
        i2c.writeto(ADDRESS, bytes([(data & ~En) | LCD_BACKLIGHT]))
        sleep(0.0001)

    def lcd_write_four_bits(self, data):
        i2c.writeto(ADDRESS, bytes([data | LCD_BACKLIGHT]))
        self.lcd_strobe(data)

    def lcd_write(self, cmd, mode=0):
        self.lcd_write_four_bits(mode | (cmd & 0xF0))
        self.lcd_write_four_bits(mode | ((cmd << 4) & 0xF0))

    def lcd_write_char(self, charvalue, mode=1):
        self.lcd_write_four_bits(mode | (charvalue & 0xF0))
        self.lcd_write_four_bits(mode | ((charvalue << 4) & 0xF0))

    def lcd_display_string(self, string, line=1, pos=0):
        pos_new = pos
        if line == 2:
            pos_new = 0x40 + pos
        elif line == 3:
            pos_new = 0x14 + pos
        elif line == 4:
            pos_new = 0x54 + pos

        self.lcd_write(0x80 + pos_new)

        for char in string:
            self.lcd_write(ord(char), Rs)

    def lcd_clear(self):
        self.lcd_write(LCD_CLEARDISPLAY)
        self.lcd_write(LCD_RETURNHOME)

    def backlight(self, state):
        if state:
            i2c.writeto(ADDRESS, bytes([LCD_BACKLIGHT]))
        else:
            i2c.writeto(ADDRESS, bytes([0x00]))


'''
# Khởi tạo LCD và hiển thị chuỗi "Hello"
lcd = LCD()
lcd.lcd_display_string("Hello", 1)
'''

