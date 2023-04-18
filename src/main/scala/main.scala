import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import java.util.logging.{Level, Logger}

//function to crop image in 256x256
def cropImage(filePath: String): BufferedImage = {
  val img: BufferedImage = ImageIO.read(new File(filePath))
  val (width, height) = (img.getWidth, img.getHeight)
  if (width > 256 || height > 256) {
    val x = if (width > 256) (width - 256) / 2 else 0
    val y = if (height > 256) (height - 256) / 2 else 0
    img.getSubimage(x, y, 256, 256)
  } else {
    img
  }
}
//function to convert to greyscale
def toGrayscale(img: BufferedImage): BufferedImage = {
  val grayscaleImg = new BufferedImage(img.getWidth, img.getHeight, BufferedImage.TYPE_BYTE_GRAY)
  for (x <- 0 until img.getWidth; y <- 0 until img.getHeight) {
    val color = new Color(img.getRGB(x, y))
    val gray = (color.getRed * 0.299 + color.getGreen * 0.587 + color.getBlue * 0.114).toInt
    grayscaleImg.setRGB(x, y, new Color(gray, gray, gray).getRGB)
  }
  grayscaleImg
}
//function to load image
def loadImage(filePath: String): BufferedImage = {
  ImageIO.read(new File(filePath))
}
//function to save image
def saveImage(img: BufferedImage, filePath: String): Unit = {
  ImageIO.write(img, "jpg", new File(filePath))
}
//function to transform image
def transformImage(img: BufferedImage): BufferedImage = {
  // some image transformation code for deblurring
  img
}

def checkSpeed[T](func: => T): T = {
  val startTime = System.nanoTime()
  val result = func
  val endTime = System.nanoTime()
  val duration = Duration.fromNanos(endTime - startTime).toMillis
  println(s"Function completed in $duration ms")
  result
}
//testing
//testing function to test size
def testImageSize(filePath: String): Unit = {
  val (width, height) = (cropImage(filePath).getWidth, cropImage(filePath).getHeight)
  println(s"Image width: $width, height: $height")
}
//test image type
def testImageType(filePath: String): Unit = {
  val imageFormat = ImageIO.read(new File(filePath)).getFormatName
  if (imageFormat == "JPEG" || imageFormat == "PNG") {
    println("Image type is either JPEG or PNG")
  } else {
    println("Image type is not JPEG or PNG")
  }
}

val logger = Logger.getLogger(getClass.getName)

val filePath = "path/to/image.jpg"

checkSpeed {
  val croppedImg = cropImage(filePath)
  val grayscaleImg = toGrayscale(croppedImg)
  val transformedImg = transformImage(grayscaleImg)
  saveImage(transformedImg, "path/to/transformed_image.jpg")
}

testImageSize(filePath)
testImageType(filePath)
