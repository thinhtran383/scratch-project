import cv2
from ultralytics import YOLO
from paddleocr import PaddleOCR

def detect_license_plate_image(model_path, image_path):
    ocr = PaddleOCR(use_angle_cls=True, lang='en', show_log=False)
    model = YOLO(model_path)

    frame = cv2.imread(image_path)
    if frame is None:
        print("Không thể đọc ảnh từ đường dẫn đã cung cấp.")
        return

    results = model(frame)

    for result in results:
        boxes = result.boxes.xyxy
        for box in boxes:
            x_min, y_min, x_max, y_max = map(int, box[:4])

            cropped_image = frame[y_min:y_max, x_min:x_max]
            cropped_image_rgb = cv2.cvtColor(cropped_image, cv2.COLOR_BGR2RGB)

            ocr_results = ocr.ocr(cropped_image_rgb, cls=True)

            plate_text = []
            for line in ocr_results:
                if line:
                    line_text = ''.join([word_info[1][0] for word_info in line if word_info[1] is not None])
                    plate_text.append(line_text)

            if plate_text:
                final_plate = ''.join(plate_text).replace(" ", "").replace(".", "").replace("-", "")
                cv2.putText(frame, final_plate, (x_min, y_min - 10), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
                cv2.rectangle(frame, (x_min, y_min), (x_max, y_max), (0, 255, 0), 2)

    cv2.imshow('License Plate Detection', frame)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

detect_license_plate_image("best.pt", "plate.jpg")
