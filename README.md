# Image processing program in Scala

- The program is part of a bigger project aiming to develop a deep learning model to accurately detect buildings in satellite images using Scala
-  The image processing program tiles RGB .tif images and carries out various transforms to make it a better fit for ML pipeline
-  It also contains various helper functions like drawing boundary polygons, displaying the transformation pipeline etc
- The program includes a post-evaluation inference image processing and analysis component
  
## Running image proccessing
1) Navgate to root project directory and input to terminal: ```sbt compile```
2) Once build is done, to start program input ```sbt run```
3) Commands:

```
proccimages: process and output images
showprocc: show processing steps for images in a swing window
exit: close program
```

*This is a learner project exploring Scala as a language and it's use in large dataset projects due to it's inherently explicit lazy mechanisms and functional nature*

