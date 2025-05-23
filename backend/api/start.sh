#!/usr/bin/env bash
aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 537408061242.dkr.ecr.us-east-2.amazonaws.com
docker-compose down -v
git pull origin main
docker-compose pull
docker pull jrottenberg/ffmpeg
docker pull tesseractshadow/tesseract4re
docker pull dpokidov/imagemagick
docker pull 537408061242.dkr.ecr.us-east-2.amazonaws.com/whisperai:latest
docker-compose up --force-recreate --build -d
