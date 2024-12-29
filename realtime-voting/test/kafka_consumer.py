import sys

if sys.version_info >= (3, 12, 0):
    import six
    sys.modules['kafka.vendor.six.moves'] = six.moves

from kafka import KafkaConsumer
import json

consumer = KafkaConsumer(
    'voting_result', 
    bootstrap_servers=['localhost:9092'],  
    value_deserializer=lambda x: json.loads(x.decode('utf-8')), 
    group_id='vote-consumer-group'  
)

def consume_votes():
    try:
        print("Bắt đầu nhận dữ liệu votes từ Kafka...")
        for message in consumer:
            vote = message.value
            print(f"Nhận vote: {vote}")
    except KeyboardInterrupt:
        print("Dừng nhận dữ liệu.")
    finally:
        consumer.close()

if __name__ == "__main__":
    consume_votes()
