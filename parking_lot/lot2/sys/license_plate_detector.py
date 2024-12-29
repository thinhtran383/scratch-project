import os
import cv2
from ultralytics import YOLO
from paddleocr import PaddleOCR

class LicensePlateDetector:
    def __init__(self, model_path, camera_url):
        self.ocr = PaddleOCR(use_angle_cls=True, lang='en', show_log=False)
        self.model = YOLO(model_path)
        self.camera_url = camera_url

    def get_car_plate(self):
        cap = cv2.VideoCapture(self.camera_url)

        if not cap.isOpened():
            print("Không thể mở camera IP.")
            return None

        detected = False  

        while True:
            ret, frame = cap.read()
            if not ret:
                print("Không thể đọc frame từ camera IP.")
                break

            results = self.model(frame)

            for result in results:
                boxes = result.boxes.xyxy 
                
                for box in boxes:
                    x_min, y_min, x_max, y_max = map(int, box[:4])

                    cropped_image = frame[y_min:y_max, x_min:x_max]

                    cropped_image_rgb = cv2.cvtColor(cropped_image, cv2.COLOR_BGR2RGB)

                    ocr_results = self.ocr.ocr(cropped_image_rgb, cls=True)

                    plate_text = []
                    for line in ocr_results:
                        if line:
                            line_text = ''.join([word_info[1][0] for word_info in line if word_info[1] is not None])
                            plate_text.append(line_text)

                    if plate_text:
                        final_plate = ''.join(plate_text).replace(" ", "").replace(".", "").replace("-", "")
                        
                        # cv2.putText(frame, final_plate, (x_min, y_min - 10), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
                        # cv2.rectangle(frame, (x_min, y_min), (x_max, y_max), (0, 255, 0), 2)

                        os.makedirs("static/images", exist_ok=True)

                        cropped_image_path = f"static/images/{final_plate}.jpg"
                        cv2.imwrite(cropped_image_path, cropped_image)

                        detected = True

                        cap.release()
                        cv2.destroyAllWindows()
                        return final_plate

            cv2.imshow('License Plate Detection', frame)

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        cap.release()
        cv2.destroyAllWindows()
        return None


# Thay đổi URL camera IP ở đây
camera_url = "http://192.168.1.232:8080/video" 
car = LicensePlateDetector("best.pt", camera_url)
plate = car.get_car_plate()
