import java.awt.image.BufferedImage


object LazyProcFuncs {

  //function tile images into 480x480 and apply greyscale filter
  def convertImagesToTiles(images: LazyList[BufferedImage], tileSize: Int): LazyList[(BufferedImage, BufferedImage, (Int, Int))] = {
//    val tileSize = 480
    val numTiles = images.head.getWidth() / tileSize

    images.flatMap(image => {
      val tiles = for (y <- 0 until numTiles; x <- 0 until numTiles) yield {
        val tileImage = ImageProcessing.cropImage(image, x * tileSize, y * tileSize, tileSize, tileSize)
        val greyTileImage = ImageProcessing.toGrayscale(tileImage)
        val tilePos = (x * tileSize, y * tileSize)
        (tileImage, greyTileImage, tilePos)
      }
      tiles.to(LazyList)
    })
  }


  //function to sharpen images

  def sharperImages(images: LazyList[BufferedImage]): LazyList[BufferedImage] = {
    images.map { img =>
      ImageProcessing.sharpenImage(img)
    }
  }

  def grescaledImages(images: LazyList[BufferedImage]): LazyList[BufferedImage] = {
    images.map { img =>
      ImageProcessing.toGrayscale(img)
    }
  }

}
