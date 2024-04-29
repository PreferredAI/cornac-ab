# import csv
import random
import datetime
# import uuid
import pandas as pd
# from dateutil import parser

def random_timestamp_before(timestamp):
    return timestamp - random.random() * datetime.timedelta(minutes=2)

def generate_rec_ids(item_id):
    item_ids = popular_books.copy()
    if item_id not in item_ids:
        # replace random item from the list
        random_index = random.randint(0, len(item_ids) - 1)
        item_ids[random_index] = item_id
    return item_ids

def generate_recommendations_csv():
    global popular_books
    
    # Define the input and output CSV file paths
    input_csv_file_path = 'feedbacks.csv'
    output_csv_file_path = 'recommendations.csv'

    popular_books_csv_file_path = 'popular_books.csv'

    print("processing popular books..")
    # Get top 50 popular books
    df_popular_books = pd.read_csv(popular_books_csv_file_path)
    popular_books = df_popular_books['book_id'].tolist()

    print("processing feedbacks..")
    df_feedbacks = pd.read_csv(input_csv_file_path)

    series_ids = df_feedbacks["recommendation_id"]
    series_timestamps = df_feedbacks["timestamp"]
    series_item_ids = df_feedbacks["item_id"]
    series_user_ids = df_feedbacks["user_id"]

    print("> applying transformations..")
    print("> converting timestamps to datetime..")
    series_timestamps = pd.to_datetime(series_timestamps)
    print("> generating timestamps..")
    series_timestamps = series_timestamps.apply(random_timestamp_before)

    print("> transforming recommendation ids..")
    series_recommendation_ids = series_item_ids.apply(generate_rec_ids)

    df_recommendations = pd.DataFrame({
        "id": series_ids,
        "user_id": series_user_ids,
        "rec_ids": series_recommendation_ids,
        "timestamp": series_timestamps
    })

    # add additional recommendations
    num_additional_recommendations = 1000000

    print("> adding additional recommendations..")

    add_series_ids = pd.Series([*range(len(df_recommendations), (len(df_recommendations) + num_additional_recommendations))])
    add_user_ids = pd.Series([random.randint(1, 53424) for _ in range(num_additional_recommendations)])
    add_rec_ids = pd.Series([popular_books for _ in range(num_additional_recommendations)])
    add_timestamps = pd.Series([datetime.datetime.today() - random.random() * datetime.timedelta(days = 365) for _ in range(num_additional_recommendations)])

    df_add_recommendations = pd.DataFrame({
        "id": add_series_ids,
        "user_id": add_user_ids,
        "rec_ids": add_rec_ids,
        "timestamp": add_timestamps
    })

    df_recommendations = pd.concat([df_recommendations, df_add_recommendations])

    print("writing to csv..")
    df_recommendations.to_csv(output_csv_file_path, index=False)


if __name__ == "__main__":
    generate_recommendations_csv()
# # Create recommendations
# recommendations = []

# with open(input_csv_file_path, 'r') as input_csv_file:
#     reader = csv.reader(input_csv_file)
#     next(reader)  # Skip the header row
#     for row in reader:
#         feedback_id = row[0]
#         recommendation_id = row[1]
#         user_id = row[2]
#         book_id = row[3]
#         rating = int(row[4])
#         timestamp = parser.parse(row[5])

#         item_ids = popular_books.copy()
#         if book_id not in item_ids:
#             # replace random item from the list
#             random_index = random.randint(0, len(item_ids) - 1)
#             item_ids[random_index] = book_id
        
#         rec_timestamp = timestamp - random.random() * datetime.timedelta(minutes=2)

#         recommendations.append({
#             "id": recommendation_id,
#             "user_id" : user_id,
#             "rec_ids": item_ids,
#             "timestamp": rec_timestamp
#         })

# # add additional recommendations
# num_additional_recommendations = 1000000

# for i in range(num_additional_recommendations):
#     recommendation_id = uuid.uuid4()
#     user_id = random.randint(1, 53424)
#     item_ids = popular_books.copy()
    
#     rec_timestamp = datetime.datetime.today() - random.random() * datetime.timedelta(days = 365)

#     recommendations.append({
#         "id": recommendation_id,
#         "user_id" : user_id,
#         "rec_ids": item_ids,
#         "timestamp": rec_timestamp
#     })

# # Write the feedbacks into the output CSV file
# with open(output_csv_file_path, 'w', newline='') as output_csv_file:
#     writer = csv.writer(output_csv_file)
#     writer.writerow(['id', 'user_id', 'rec_ids', 'timestamp'])  # Write the header row
#     for recommendation in recommendations:
#         writer.writerow([recommendation['id'], recommendation['user_id'], recommendation['rec_ids'], recommendation['timestamp']])





