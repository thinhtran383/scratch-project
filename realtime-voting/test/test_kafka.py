import sys

if sys.version_info >= (3, 12, 0):
    import six
    sys.modules['kafka.vendor.six.moves'] = six.moves

from kafka_pro import KafkaConsumer, KafkaProducer
from threading import Thread
import time

# Cấu hình Kafka
KAFKA_BROKER = 'localhost:9092'
TOPIC_NAME = 'test_topic'

# Hàm gửi tin nhắn (Producer)
def producer_job():
    producer = KafkaProducer(bootstrap_servers=KAFKA_BROKER)
    for i in range(5):
        message = f"Message {i} from producer".encode()
        producer.send(TOPIC_NAME, message)
        print(f"Producer sent: {message.decode()}")
        time.sleep(1)  # Giãn cách gửi tin nhắn
    producer.close()

# Hàm nhận tin nhắn (Consumer)
def consumer_job():
    consumer = KafkaConsumer(
        TOPIC_NAME,
        bootstrap_servers=KAFKA_BROKER,
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id='test_group'
    )
    print(f"Consumer listening to topic '{TOPIC_NAME}'...")
    for message in consumer:
        print(f"Consumer received: {message.value.decode()}")

# Chạy cả Producer và Consumer trong các luồng riêng
if __name__ == '__main__':
    # Tạo luồng cho Consumer
    consumer_thread = Thread(target=consumer_job)
    consumer_thread.start()

    # Đợi vài giây để Consumer sẵn sàng
    time.sleep(2)

    # Chạy Producer
    producer_job()

    # Chờ Consumer hoàn tất
    consumer_thread.join()
