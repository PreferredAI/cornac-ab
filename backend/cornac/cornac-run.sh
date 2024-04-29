#!/bin/bash
port="${1:-5000}"
pip install Cython numpy scipy
pip install cornac Flask gunicorn
echo "launching on port: $port, model path: $2, model class: $3"
MODEL_PATH="$2" MODEL_CLASS="$3" gunicorn -b 0.0.0.0:"$port" --timeout 300 serve-app:app