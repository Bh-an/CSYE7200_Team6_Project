# CSYE7200_Team6_Project


## Introduction

● The project aims to develop a deep learning model to accurately detect buildings
in satellite images using GeoTrellis (Scala library), Spark and PyTorch.
● The project leverages the COCO and SpaceNet Rotterdam datasets, the latter of
which is a collection of high-resolution satellite images of Rotterdam,
Netherlands, with detailed annotations of building footprints.
● Additionally, the project aims to optimize the model's performance by exploring
various machine learning algorithms, hyperparameter tuning, and feature
engineering.

## Use Case - Predict Buildings

● User uploads a satellite image to the system
● System ingests the image and transforms it into a format recognisable by the model,
which then returns the building data present in it
● System maps the results onto the image provided and returns the generated result 
back to the user

## Goals

Develop a tool for:
- ingesting satellite image data,
- predicting building structures on a given image, and
- giving a prediction mask back to the user
