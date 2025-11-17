FROM python:3.6-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /opt/calc

WORKDIR /opt/calc

COPY .coveragerc .pylintrc pyproject.toml pytest.ini requires ./
COPY app ./app
COPY test ./test

RUN pip install -r requires
