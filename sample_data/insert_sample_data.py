import requests
import os
import sys
from generate_feedback import generate_feedbacks_csv
from generate_recommendation import generate_recommendations_csv
from insert_opensearch import add_to_opensearch

host = os.environ.get("OPENSEARCH_HOST", "localhost")
port = os.environ.get("OPENSEARCH_PORT", 9200)

data = requests.get(f"http://{host}:{port}/recommendations/_count")  # Checks if data already exists

if data.status_code == 200 and data.json()["count"] > 0:
    print("Data already exists. Skipping data insertion.")
    sys.exit(0)

print("Inserting sample data...")

generate_feedbacks_csv()  # Generate sample feedbacks
generate_recommendations_csv()  # Generate sample recommendations
add_to_opensearch()  # Add sample data to Opensearch

print("Inserting sample data completed!")