from mfrc522 import MFRC522
import utime

class RFIDReader:
    def __init__(self, spi_id, sck, miso, mosi, cs, rst):
        
        self.reader = MFRC522(spi_id=spi_id, sck=sck, miso=miso, mosi=mosi, cs=cs, rst=rst)

    def read_rfid(self):
        
        self.reader.init()
        (stat, tag_type) = self.reader.request(self.reader.REQIDL)
        if stat == self.reader.OK:
            (stat, uid) = self.reader.SelectTagSN()
            if stat == self.reader.OK:
                card_id = int.from_bytes(bytes(uid), "little", False)
                return card_id
        return None

'''

rfid1 = RFIDReader(spi_id=0, sck=6, miso=4, mosi=7, cs=5, rst=22)
rfid2 = RFIDReader(spi_id=0, sck=6, miso=4, mosi=7, cs=15, rst=22)

print("Bring TAG closer...")

while True:
  
    card_id1 = rfid1.read_rfid()
    if card_id1:
        print("RFID1 - CARD ID: " + str(card_id1))
    
   
    card_id2 = rfid2.read_rfid()
    if card_id2:
        print("RFID2 - CARD ID: " + str(card_id2))
    
    utime.sleep_ms(500)
'''
