#!/bin/sh

# curl and get result
COUNT=$(python /tmp/check_sample_data.py)
echo "COUNT: $COUNT"

if [ $COUNT -eq "0" ]; then
    echo "Inserting sample data..."

    # Process generate_feedback.py
    python /tmp/generate_feedback.py

    # Process generate_recommendation.py
    python /tmp/generate_recommendation.py

    # Process insert_opensearch.py
    python /tmp/insert_opensearch.py

else
    echo "Sample data already exists. Exiting..."
fi

# # Process generate_feedback.py
# python /tmp/generate_feedback.py

# # Process generate_recommendation.py
# python /tmp/generate_recommendation.py

# # Process insert_opensearch.py
# python /tmp/insert_opensearch.py