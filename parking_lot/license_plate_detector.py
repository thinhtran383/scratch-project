import cv2
from ultralytics import YOLO
from paddleocr import PaddleOCR


# yolo: phat hien noi chua bien so xe
# paddle: doc ki tu trong bien so
class LicensePlateDetector:
    def __init__(self, model_path):
        self.ocr = PaddleOCR(use_angle_cls=True, lang='en', show_log=False)
        self.model = YOLO(model_path)

    def get_car_plate(self):
        #(index 0 for default camera)
        cap = cv2.VideoCapture(0)

        if not cap.isOpened():
            print("Không thể mở webcam.")
            return None

        detected = False  # Flag

        while True:
            ret, frame = cap.read()
            if not ret:
                print("Không thể đọc frame từ webcam.")
                break

            # detect car plate
            results = self.model(frame)

            # get bounding box and crop the car plate
            for result in results:
                boxes = result.boxes.xyxy  #  bounding box ([x_min, y_min, x_max, y_max])
                
                for box in boxes:
                    x_min, y_min, x_max, y_max = map(int, box[:4])

                    # Crop 
                    cropped_image = frame[y_min:y_max, x_min:x_max]

                    #  crop convert to RGB for PaddleOCR
                    cropped_image_rgb = cv2.cvtColor(cropped_image, cv2.COLOR_BGR2RGB)

                    # get text from cropped image
                    ocr_results = self.ocr.ocr(cropped_image_rgb, cls=True)

                    plate_text = []
                    for line in ocr_results:
                        if line:
                            line_text = ''.join([word_info[1][0] for word_info in line if word_info[1] is not None])
                            plate_text.append(line_text)

                    if plate_text:
                        final_plate = ''.join(plate_text).replace(" ", "").replace(".", "").replace("-", "")  # delete space, dot, dash (-)
                        
                        cv2.putText(frame, final_plate, (x_min, y_min - 10), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
                        cv2.rectangle(frame, (x_min, y_min), (x_max, y_max), (0, 255, 0), 2)

                        detected = True  

                        # print(f"Biển số nhận diện: {final_plate}")

                        cap.release()
                        cv2.destroyAllWindows()
                        return final_plate

            cv2.imshow('License Plate Detection', frame)

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        cap.release()
        cv2.destroyAllWindows()
        return None

