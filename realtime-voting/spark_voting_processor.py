import sys
import os
import json

if sys.version_info >= (3, 12, 0):
    import six
    sys.modules['kafka.vendor.six.moves'] = six.moves

from pyspark.sql import SparkSession
from pyspark.sql.functions import col, from_json
from pyspark.sql.types import StructType, StringType

KAFKA_INPUT_TOPIC = "voting"
KAFKA_SERVER = "localhost:9092"

OUTPUT_FILE_PATH = "vote_results.json"
CHECKPOINT_DIR = "checkpoint_dir" 

spark = SparkSession.builder \
    .appName("VotingProcessor") \
    .master("local[*]") \
    .config("spark.jars.packages", "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.0") \
    .getOrCreate()

schema = StructType().add("candidate", StringType())

votes_stream = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", KAFKA_SERVER) \
    .option("subscribe", KAFKA_INPUT_TOPIC) \
    .load()

votes = votes_stream.selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.candidate")

vote_counts = votes.groupBy("candidate").count()

def save_to_json(df, epoch_id):
    results = df.collect()
    vote_dict = {row["candidate"]: row["count"] for row in results}
    
    if os.path.exists(OUTPUT_FILE_PATH):
        with open(OUTPUT_FILE_PATH, "r", encoding="utf-8") as f:
            existing_data = json.load(f)
    else:
        existing_data = {}
    
    existing_data.update(vote_dict)
    
    with open(OUTPUT_FILE_PATH, "w", encoding="utf-8") as f:
        json.dump(existing_data, f, ensure_ascii=False, indent=4)

vote_counts.writeStream \
    .outputMode("complete") \
    .foreachBatch(save_to_json) \
    .option("checkpointLocation", CHECKPOINT_DIR) \
    .start() \
    .awaitTermination()
