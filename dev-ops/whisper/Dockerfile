FROM  pytorch/pytorch:1.12.1-cuda11.3-cudnn8-runtime
WORKDIR /

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git

RUN python3 -m pip install --upgrade pip
RUN python -m pip install regex tqdm Pillow
RUN pip install git+https://github.com/openai/whisper.git
