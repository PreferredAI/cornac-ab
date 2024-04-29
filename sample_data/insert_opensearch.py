from opensearchpy import OpenSearch
from opensearchpy.helpers import bulk, parallel_bulk
import csv
import os
from dateutil import parser
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
    # for item in data:
    #     action = {
    #         "_op_type": "index",
    #         "_index": index_name,
    #         "_id": item["id"],
    #         "_source": item,
    #     }
    #     actions.append(action)
    print("> Inserting bulk data into OpenSearch...")
    response = bulk(es, actions)  # Perform bulk insert operation

    return response

def str_to_array(s):
    return s[1:-1].split(',')

def read_recommendations():
    file_path = "recommendations.csv"

    df = pd.read_csv(file_path)
    
    df["rec_ids"] = df["rec_ids"].apply(str_to_array)
    df["experiment_id"] = [1] * len(df)
    df["model"] = df["user_id"].apply(lambda x: model_list[int(x) % len(model_list)])
    df["timestamp"] = pd.to_datetime(df["timestamp"])
    
    data = df.to_dict(orient="records")
    
    # data = []
    # with open(file_path, "r") as f:
    #     reader = csv.reader(f)
    #     next(reader)
    #     for row in reader:
    #         rec_ids = row[2].replace('\'', '"')
    #         rec_ids = json.loads(rec_ids)

    #         model_idx = int(row[1]) % len(model_list)
    #         model = model_list[model_idx]

    #         data.append({
    #             "id": row[0],
    #             "user_id": row[1],
    #             "rec_ids": rec_ids,
    #             "model": model,
    #             "timestamp": parser.parse(row[3]),
    #             "experiment_id": 1
    #         })

    return data

def read_feedback():
    file_path = "feedbacks.csv"

    df = pd.read_csv(file_path)
    
    df["experiment_id"] = [1] * len(df)
    df["rating"] = df["rating"].apply(lambda x: int(x))
    df["model"] = df["user_id"].apply(lambda x: model_list[int(x) % len(model_list)])
    df["timestamp"] = pd.to_datetime(df["timestamp"])
    
    data = df.to_dict(orient="records")
    # data = []
    # with open(file_path, "r") as f:
    #     reader = csv.reader(f)
    #     next(reader)
    #     for row in reader:
    #         model_idx = int(row[2]) % len(model_list)
    #         model = model_list[model_idx]

    #         data.append({
    #             "id": row[0],
    #             "experiment_id": 1,
    #             "recommendation_id": row[1],
    #             "user_id": row[2],
    #             "item_id": row[3],
    #             "rating": int(row[4]),
    #             "model": model,
    #             "timestamp": parser.parse(row[5])
    #         })

    return data

def read_users():
    # file_path = "users.csv"

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

def add_to_opensearch():
    print("reading recommendations...")
    rec_data = read_recommendations()
    print("inserting recommendations data...")
    bulk_insert_data_parallel("recommendations", rec_data)
    
    print("reading feedbacks...")
    feedback_data = read_feedback()
    print("inserting feedbacks data...")
    bulk_insert_data_parallel("feedback", feedback_data)
    
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