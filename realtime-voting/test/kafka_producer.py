import sys

if sys.version_info >= (3, 12, 0):
    import six
    sys.modules['kafka.vendor.six.moves'] = six.moves

from kafka import KafkaProducer
import json
import random
import time

producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],  
    value_serializer=lambda v: json.dumps(v).encode('utf-8')  
)

candidates = [1, 2, 3]

def send_votes():
    try:
        print("Bắt đầu gửi dữ liệu votes đến Kafka...")
        while True:
            vote = {"candidate": random.choice(candidates)}
            
            producer.send('voting', value=vote)
            print(f"Gửi vote: {vote}")
            
           
            time.sleep(random.uniform(0.5, 2))
    except KeyboardInterrupt:
        print("Dừng gửi dữ liệu.")
    finally:
        producer.close()

if __name__ == "__main__":
    send_votes()
