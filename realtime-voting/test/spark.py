from pyspark.sql import SparkSession
from pyspark.sql.functions import col

# Tạo SparkSession với Kafka package
spark = SparkSession.builder \
    .appName("VotingSystem") \
    .config("spark.jars.packages", "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.0") \
    .getOrCreate()

# Kết nối với Kafka và đọc dữ liệu
kafka_stream = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "voting") \
    .load()

# Chuyển đổi dữ liệu Kafka từ dạng byte thành string
votes_df = kafka_stream.selectExpr("CAST(value AS STRING)").withColumn("candidate", col("value"))

# Tính tổng số phiếu cho mỗi ứng viên
vote_counts_df = votes_df.groupBy("candidate").count()

# Cấu hình để stream kết quả vào console
query = vote_counts_df \
    .writeStream \
    .outputMode("complete") \
    .format("console") \
    .start()

query.awaitTermination()
