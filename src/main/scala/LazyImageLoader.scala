import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Path
import scala.io.StdIn.readLine
object LazyImageLoader {

  def getDirectoryPathFromUser(prompt: String): Path = {
    var directoryPath: Path = null
    var validPath = false
    while (!validPath) {
      val directory = new File(readLine(prompt))
      if (directory.isDirectory) {
        directoryPath = directory.toPath
        validPath = true
      } else {
        println("Invalid directory path")
      }
    }
    directoryPath
  }
  def loadInputImages(dir_path: String): LazyList[BufferedImage] = {
    val directory = new File(dir_path)
    val imageFiles = directory.listFiles().filter(_.getName.endsWith(".tif"))

    // Load images lazily
    val images = imageFiles.to(LazyList).map(file => {
      val image = ImageIO.read(file)
      image
    })

    // Process each image
//    images.foreach(processImage)
    images
  }

  def loadPredImages(dir_path: String): LazyList[BufferedImage] = {
    val directory = new File(dir_path)
    val imageFiles = directory.listFiles().filter(_.getName.endsWith(".png"))

    // Load images lazily
    val images = imageFiles.to(LazyList).map(file => {
      val image = ImageIO.read(file)
      image
    })

    // Process each image
    //    images.foreach(processImage)
    images
  }

  def loadLabelImages(dir_path: String): LazyList[BufferedImage] = {
    val directory = new File(dir_path)
    val imageFiles = directory.listFiles().filter(_.getName.endsWith(".tif"))

    // Load images lazily
    val images = imageFiles.to(LazyList).map(file => {
      val image = ImageIO.read(file)
      image
    })

    // Process each image
    //    images.foreach(processImage)
    images
  }



  private def processImage(image: BufferedImage): Unit = {
    // Do something with the image


    println(s"Loaded image with dimensions ${image.getWidth}x${image.getHeight}")
  }
}