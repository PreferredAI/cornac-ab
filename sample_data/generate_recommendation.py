import random
import datetime
import pandas as pd

def random_timestamp_before(timestamp):
    return timestamp - random.random() * datetime.timedelta(minutes=2)

def generate_rec_ids(item_id):
    item_ids = popular_books.copy()
    if item_id not in item_ids:
        # replace random item from the list
        random_index = random.randint(0, len(item_ids) - 1)
        item_ids[random_index] = item_id
    return item_ids

def generate_recommendations_csv(save_csv=True, df_feedbacks=None):
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
    if df_feedbacks is None:
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

    if save_csv:
        print("writing to csv..")
        df_recommendations.to_csv(output_csv_file_path, index=False)
    
    return df_recommendations


if __name__ == "__main__":
    generate_recommendations_csv()


