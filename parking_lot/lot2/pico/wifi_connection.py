import network
import time

class WiFiConnection:
    def __init__(self, ssid, password):
        self.ssid = ssid
        self.password = password
        self.wlan = network.WLAN(network.STA_IF)

    def connect(self):
       
        self.wlan.active(True)
        self.wlan.connect(self.ssid, self.password)
        
        
        while not self.wlan.isconnected():
            print("Đang kết nối tới Wi-Fi...")
            time.sleep(1)
        
        print("Kết nối Wi-Fi thành công:", self.wlan.ifconfig())
        return self.wlan.ifconfig()  
