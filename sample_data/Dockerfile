FROM python:3.11-slim

WORKDIR /app

# Install the pip dependencies
RUN pip install --no-cache-dir pandas opensearch-py

# Copy the Python script to the container
COPY ./ /app/

# Run the Python script
CMD ["python", "insert_sample_data.py"]