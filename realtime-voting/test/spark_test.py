from pyspark.sql import SparkSession
from pyspark.sql.functions import expr

def process_kafka_stream():
    # Khởi tạo SparkSession
    spark = SparkSession.builder \
        .appName("KafkaVotingApp") \
            .config("spark.jars.packages", "org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.3") \
        .getOrCreate()

    # Đọc dữ liệu từ Kafka topic
    kafka_df = spark.readStream \
        .format("kafka") \
        .option("kafka.bootstrap.servers", "localhost:9092") \
        .option("subscribe", "voting_topic") \
        .load()

    # Kafka data sẽ chứa hai cột: key và value (dạng byte), chúng ta cần chuyển giá trị từ byte thành string
    kafka_df = kafka_df.selectExpr("CAST(value AS STRING) as candidate_code")

    # Xử lý dữ liệu (hiển thị mã ứng viên)
    query = kafka_df.writeStream \
        .outputMode("append") \
        .format("console") \
        .start()

    query.awaitTermination()

if __name__ == "__main__":
    process_kafka_stream()
