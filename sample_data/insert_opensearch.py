from opensearchpy import OpenSearch
from opensearchpy.helpers import bulk, parallel_bulk
import csv
import os
import pandas as pd

host = os.environ.get("OPENSEARCH_HOST", "localhost")
port = os.environ.get("OPENSEARCH_PORT", 9200)

model_list = ["BPR", "BiVAECF", "LightGCN"]

def bulk_insert_data_parallel(index_name, data):
    es = OpenSearch(
        hosts = [{'host': host, 'port': port}],
    )  # Connect to Opensearch cluster

    print("> Converting to bulk insert format...")
    actions = [{
            "_op_type": "index",
            "_index": index_name,
            "_id": item["id"],
            "_source": item,
        }
        for item in data]

    print("> Inserting bulk data into OpenSearch...")
    # response = bulk(es, actions)  # Perform bulk insert operation
    succeeded = []
    failed = []
    
    for success, item in parallel_bulk(
        es,
        actions=actions,
    ):
        if success:
            succeeded.append(item)
        else:
            failed.append(item)
        
    if succeeded:
        print(f"Successfully inserted {len(succeeded)} items")
    
    if failed:
        print(f"Failed to insert {len(failed)} items")


def bulk_insert_data(index_name, data):
    es = OpenSearch(
        hosts = [{'host': host, 'port': port}],
    )  # Connect to Opensearch cluster

    print("> Converting to bulk insert format...")
    actions = [{
            "_op_type": "index",
            "_index": index_name,
            "_id": item["id"],
            "_source": item,
        }
        for item in data]
    print("> Inserting bulk data into OpenSearch...")
    response = bulk(es, actions)  # Perform bulk insert operation

    return response

def str_to_array(s):
    return s[1:-1].split(', ')

def read_recommendations(df):    
    if df is None:
        file_path = "recommendations.csv"
        df = pd.read_csv(file_path)
        df["rec_ids"] = df["rec_ids"].apply(str_to_array)
    
    df["experiment_id"] = [1] * len(df)
    df["model"] = df["user_id"].apply(lambda x: model_list[int(x) % len(model_list)])
    df["user_id"] = df["user_id"].apply(lambda x: str(x))
    df["timestamp"] = pd.to_datetime(df["timestamp"])
    
    return df.to_dict(orient="records")

def read_feedback(df):
    if df is None:
        file_path = "feedbacks.csv"
        df = pd.read_csv(file_path)
    
    df["experiment_id"] = [1] * len(df)
    df["rating"] = df["rating"].apply(lambda x: int(x))
    df["model"] = df["user_id"].apply(lambda x: model_list[int(x) % len(model_list)])
    df["user_id"] = df["user_id"].apply(lambda x: str(x))
    df["item_id"] = df["item_id"].apply(lambda x: str(x))
    df["recommendation_id"] = df["recommendation_id"].apply(lambda x: str(x))
    df["timestamp"] = pd.to_datetime(df["timestamp"])
    
    return df.to_dict(orient="records")

def read_users():
    data = [ { "id": str(i), "userId": str(i) } for i in range(1, 53424)]
    return data

def read_items():
    file_path = "books.csv"

    data = []
    with open(file_path, "r") as f:
        reader = csv.reader(f)
        next(reader)
        for row in reader:
            data.append({
                "id": row[0],
                "itemId": row[0],
                "attributes": {
                    "isbn": row[5],
                    "authors": row[7],
                    "year": row[8],
                    "original_title": row[9],
                    "title": row[10],
                    "rating": row[12],
                    "image": row[21]
                }
            })

    return data

def add_to_opensearch(df_recommendations = None, df_feedbacks = None):
    print("reading recommendations...")
    rec_data = read_recommendations(df_recommendations)
    print("inserting recommendations data...")
    for i in range(0, len(rec_data), 500000):
        print(f"Inserting {i} to {i+500000} records")
        bulk_insert_data_parallel("recommendations", rec_data[i:i+500000])
    # bulk_insert_data_parallel("recommendations", rec_data)
    
    print("reading feedbacks...")
    feedback_data = read_feedback(df_feedbacks)
    print("inserting feedbacks data...")
    for i in range(0, len(feedback_data), 500000):
        print(f"Inserting {i} to {i+500000} records")
        bulk_insert_data_parallel("feedback", feedback_data[i:i+500000])
    # bulk_insert_data_parallel("feedback", feedback_data)
    
    print("reading users...")
    user_data = read_users()
    print("inserting users data...")
    response = bulk_insert_data("users", user_data)
    print(response)
    
    print("reading items...")
    item_data = read_items()
    print("inserting items data...")
    response = bulk_insert_data("items", item_data)
    print(response)
    
if __name__ == "__main__":
    add_to_opensearch()