# import csv
# import uuid
import datetime
import random
import pandas as pd

def generate_feedbacks_csv():
    # Define the input and output CSV file paths
    input_rating_csv_file_path = "ratings.csv"
    input_click_csv_file_path = 'to_read.csv'
    output_csv_file_path = 'feedbacks.csv'

    print("processing ratings..")

    df_ratings = pd.read_csv(input_rating_csv_file_path)

    # filter rows for user_ids between 100 to 199
    df_ratings = df_ratings[(df_ratings['user_id'] >= 100) & (df_ratings['user_id'] < 200)]

    column_rename_mapping = {
        'user_id': 'user_id',
        'book_id': 'item_id',
        'rating': 'rating'
    }

    df_ratings = df_ratings.rename(columns=column_rename_mapping)

    feedback_ids = [*range(len(df_ratings))]
    recommendation_ids = [*range(len(df_ratings))]
    actions = ['rating'] * len(df_ratings)

    num_days = 180
    date_base = datetime.datetime.today() - datetime.timedelta(days=365)

    # D-365 ~ D-185
    timestamps = [ date_base + random.random() * datetime.timedelta(days = num_days) for _ in range(len(df_ratings)) ]
    timestamps.sort()

    df_ratings["id"] = feedback_ids
    df_ratings["recommendation_id"] = recommendation_ids
    df_ratings["action"] = actions
    df_ratings["timestamp"] = timestamps

    print("processing to_read(clicks)..")

    df_clicks = pd.read_csv(input_click_csv_file_path)

    column_rename_mapping = {
        'user_id': 'user_id',
        'book_id': 'item_id'
    }

    df_clicks = df_clicks.rename(columns=column_rename_mapping)

    feedback_ids = [*range(len(df_ratings), (len(df_ratings) + len(df_clicks)))]
    recommendation_ids = [*range(len(df_ratings), (len(df_ratings) + len(df_clicks)))]
    actions = ['click'] * len(df_clicks)
    ratings = [1] * len(df_clicks)

    num_days = 180
    date_base = datetime.datetime.today() - datetime.timedelta(days=num_days)

    # D-180 ~ D-0
    timestamps = [ date_base + random.random() * datetime.timedelta(days = num_days) for _ in range(len(df_clicks)) ]
    timestamps.sort()

    df_clicks["rating"] = ratings
    df_clicks["id"] = feedback_ids
    df_clicks["recommendation_id"] = recommendation_ids
    df_clicks["action"] = actions
    df_clicks["timestamp"] = timestamps

    print("writing to csv..")

    stacked_df = pd.concat([df_ratings, df_clicks], axis=0)
    stacked_df.to_csv(output_csv_file_path, index=False)


if __name__ == "__main__":
    generate_feedbacks_csv()

# with open(input_rating_csv_file_path, 'r') as input_csv_file:
#     reader = csv.reader(input_csv_file)
#     next(reader)  # Skip the header row
#     for row in reader:
#         feedback_id = uuid.uuid4()
#         recommendation_id = uuid.uuid4()
#         user_id = row[0]
#         book_id = row[1]
#         rating = row[2]
#         rating_feedbacks.append({
#             "id": feedback_id,
#             "recommendation_id": recommendation_id,
#             "user_id" : user_id,
#             "item_id" : book_id,
#             "rating": rating,
#             "action": "rating"
#         })

# num_days = 180
# date_base = datetime.datetime.today() - datetime.timedelta(days=365)

# # D-365 ~ D-185
# date_list = [ date_base + random.random() * datetime.timedelta(days = num_days) for _ in range(len(rating_feedbacks)) ]
# date_list.sort()

# for i in range(len(rating_feedbacks)):
#     rating_feedbacks[i]['timestamp'] = date_list[i]


# with open(input_click_csv_file_path, 'r') as input_csv_file:
#     reader = csv.reader(input_csv_file)
#     next(reader)  # Skip the header row
#     for row in reader:
#         feedback_id = uuid.uuid4()
#         recommendation_id = uuid.uuid4()
#         user_id = row[0]
#         book_id = row[1]
#         click_feedbacks.append({
#             "id": feedback_id,
#             "recommendation_id": recommendation_id,
#             "user_id" : user_id,
#             "item_id" : book_id,
#             "rating": 1,
#             "action": "click"
#         })

# # add timestamps
# num_days = 180
# date_base = datetime.datetime.today() - datetime.timedelta(days=num_days)

# # D-180 ~ D-0
# date_list = [ date_base + random.random() * datetime.timedelta(days = num_days) for _ in range(len(click_feedbacks)) ]
# date_list.sort()

# for i in range(len(click_feedbacks)):
#     click_feedbacks[i]['timestamp'] = date_list[i]
    
# # Write the feedbacks into the output CSV file
# with open(output_csv_file_path, 'w', newline='') as output_csv_file:
#     writer = csv.writer(output_csv_file)
#     writer.writerow(['id', 'recommendation_id', 'user_id', 'item_id', 'rating', 'timestamp', 'action'])  # Write the header row
#     for feedback in rating_feedbacks + click_feedbacks:
#         writer.writerow([
#             feedback['id'],
#             feedback['recommendation_id'],
#             feedback['user_id'],
#             feedback['item_id'],
#             feedback['rating'],
#             feedback['timestamp'],
#             feedback['action']
#         ])





