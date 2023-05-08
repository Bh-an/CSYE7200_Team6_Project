
import ImageGraphics.{showprocessing}
import LazyProcFuncs.{convertImagesToTiles, grescaledImages, sharperImages}
import LogFuncs.{checkSpeed, createLog,saveImages}

import java.awt.image.BufferedImage
import scala.io.StdIn.readLine
object Main extends App {

  createLog()

  var running = true
  while (running) {
    println("Enter a command:")
    val input = readLine().toLowerCase.trim

    input match {
      case "proccimages" => proccimages()
      case "showprocc" => showprocc()
      case "exit" => running = false
      case _ => println("Invalid command")
    }
  }

  def showprocc(): Unit = {
    println("Running showprocc...")
    val path = LazyImageLoader.getDirectoryPathFromUser("Enter image directory path")

    val inputimages = checkSpeed("LoadBigInputImages")(LazyImageLoader.loadInputImages(path.toString))
    val input = inputimages
    val grey = grescaledImages(input)
    val sharpened = sharperImages(grey)
    val tiled = convertImagesToTiles(sharpened, 480).map(_._1)

    showprocessing(input(2), grey(2), sharpened(2), tiled(2))

  }

  def proccimages(): Unit = {
    println("Running proccsmallimages...")
    val path = LazyImageLoader.getDirectoryPathFromUser("Enter image directory path")
    val inputimages = checkSpeed("Loading Images")(LazyImageLoader.loadInputImages(path.toString))
    val tiled = checkSpeed("Proccesing images")(applyfilters(inputimages))

    val outputpath = LazyImageLoader.getDirectoryPathFromUser("Enter output directory path")

    checkSpeed("Saving Images")(saveImages(tiled, outputpath.toString))
  }

  private def applyfilters(inputimages: LazyList[BufferedImage]): LazyList[BufferedImage] = {
    val grey = grescaledImages(inputimages)
    val sharpened = sharperImages(grey)
    val result = convertImagesToTiles(sharpened, 480)
    return result.map(_._1)
  }
}