#!/bin/bash

pip install Cython numpy scipy
pip install cornac Flask gunicorn
MODEL_PATH='save_dir/MF' MODEL_CLASS='cornac.models.MF' gunicorn -b localhost:5000 serve-app:app