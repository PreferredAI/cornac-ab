#!/bin/bash
port="${1:-5000}"
pip install Cython numpy scipy
pip install cornac Flask gunicorn
echo "launching on port: $port"
MODEL_PATH='save_dir/MF' MODEL_CLASS='cornac.models.MF' gunicorn -b localhost:$port serve-app:app