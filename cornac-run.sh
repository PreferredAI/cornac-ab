#!/bin/bash
port="${1:-5000}"
pip install Cython numpy scipy
pip install cornac Flask gunicorn
echo "launching on port: $port, model path: $2, model class: $3"
MODEL_PATH="$2" MODEL_CLASS="$3" gunicorn -b localhost:"$port" serve-app:app