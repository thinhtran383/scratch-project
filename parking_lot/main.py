from pico_controller import PicoController 
from license_plate_detector import LicensePlateDetector 
from database import Database  
from datetime import datetime  
from app import ParkingManagementApp
import tkinter as tk
from tkinter import ttk

if __name__ == "__main__":
    pico = PicoController("COM3")  
    detector = LicensePlateDetector('best.pt') 
    db = Database()  
    db.delete_all_records()
    
    # Create the main window
    # root = tk.Tk()
    # app = ParkingManagementApp(root)

# Run the application
    # root.mainloop()

    # db.delete_all_records()  # Xóa tất 
    
    try:
        while True:
            response = pico.read_message()
            if response:
                print(f"Phản hồi từ Pico: {response}")
                
                # Get RFID code from Pico
                if response.startswith("RFID: "):
                    rfid_code = response.split("RFID: ")[1].strip()
                    print(f"RFID Code: {rfid_code}")
                    
                    # Check if RFID code is in the database
                    active_record = db.fetch_active_record(rfid_code)
                    
                    if active_record:
                        # Get time_in from the active record
                        plate_number = detector.get_car_plate()
                        if plate_number:
                            print(f"Biển số nhận diện: {plate_number}")
                            
                            saved_plate = active_record[1]  
                            if plate_number == saved_plate:
                                # Update time_out in the database
                                db.update_time_out(rfid_code)
                                print(f"Đã cập nhật time_out cho thẻ: {rfid_code}")
                                pico.send_message("out")
                            else:
                                # Car plate does not match the saved plate
                                print(f"Biển số không khớp! Đã lưu: {saved_plate}, Nhận diện: {plate_number}")
                                pico.send_message("false")
                        else:
                            print("Không phát hiện biển số nào.")
                    else:
                        # New car, save the record to the database
                        plate_number = detector.get_car_plate()
                        if plate_number:
                            print(f"Biển số nhận diện: {plate_number}")
                            
                            # Gửi biển số về Pico để hiển thị lên LCD
                            pico.send_message(f'in {plate_number}')

                            # Persist the record to the database
                            time_in = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                            db.insert_record(plate_number, rfid_code, time_in)
                            print(f"Đã lưu vào cơ sở dữ liệu cho thẻ: {rfid_code}")
                        else:
                            print("Không phát hiện biển số nào.")
                
    finally:
        pico.close()
        db.close()
