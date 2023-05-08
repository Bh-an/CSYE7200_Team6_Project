# CSYE7200_Team6_Project


## Introduction

- The project aims to develop a deep learning model to accurately detect buildings in satellite images using Scala and PyTorch.
-  The project leverages the SpaceNet Rotterdam dataset, which is a collection of high-resolution satellite images of Rotterdam,
Netherlands, with detailed annotations of building footprints.
- The project includes a post-evaluation inference image processing and analysis component.

## Goals

Develop a tool for:
  1) train a Dynamic U-Net in PyTorch and evaluate a part of the data (Python), 
	2) perform image processing for analysis (Scala), 
	3) analyse the images (Scala)
  
## More Details

1) Input satellite image(s) and prediction mask(s).The image processing and analysis component will load the images, and check their sizes. It will additionally check for image format to ensure it is PNG.  It will then convert the images to 256 x 256 px size. After making the images smaller it will convert them to grayscale. Following grayscale conversion, the image and mask are analysed for overlap and saved. The main analysis is computation of Dice and IoU scores.

## How to train the model and visualize the predictions
1) Build a conda environment using the requirements.txt file and set the Python interpreter for project
2) Sign-up for an API key at Radiant MLHub, and store the key in root/api_key.txt or you also use download.py
3) Download the Spacenet6 dataset using torchgeo in spacenet_detect/detect.ipynb
4) Run the code block under the "Make masks for all images" heading in spacenet_detect/detect.ipynb
5) Run the code block under the "Train model" heading in spacenet_detect/detect.ipynb
6) Run the code block under the "Evaluate model" heading in spacenet_detect/detect.ipynb
7) Use visualize_predictions.ipynb to visualize the predictions

## Running image proccessing
1) Navgate to root project directory and input to terminal: ```sbt compile```
2) Once build is done, to start program input ```sbt run```
3) Commands:

```
proccimages: process and output images
showprocc: show processing steps fro images in a swing window
exit: close program
```


## Individual contributions 
1) Bhan - Code for processing images, proccessing testsuite, logging for acceptance criteria checks, main app functionality, lazy fucntions, graphics.
2) Shamin - Downloaded the Dataset using python, created functions for image processing in scala and contributed in the readme and presentations.
3) Nina - For Python code: spacenet_detect module; main parts: building the dataset module, and the training and evaluation pipeline. For Scala code: ImageAnalysis.sc and ImageAnalysis.spec. Where my contributions affected the acceptance criteria: training Dice loss for validation set (0.49), Dice score (0.73). IoU was not used for training because it is not differentiable (addendum: some methods have updated this, it was not entirely necessary to use them here). The Dice and IoU scores (0.7 or greater for test image).

