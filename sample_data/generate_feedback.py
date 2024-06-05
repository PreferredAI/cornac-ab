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





