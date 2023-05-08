import com.opencsv.CSVWriter
import java.awt.image.BufferedImage
import scala.concurrent.duration.Duration
import java.io._
import scala.language.experimental.macros
import java.io.{File, FileOutputStream}
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.{Base64, UUID}
import javax.imageio.{IIOImage, ImageIO, ImageWriteParam}
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.ImageOutputStream

object LogFuncs {


  def checkSpeed[T](name: String)(func: => T): T = {
    val startTime = System.nanoTime()
    val result = func
    val endTime = System.nanoTime()
    val duration = Duration.fromNanos(endTime - startTime).toMillis
    val arr = Array(name, duration.toString + "ms")
    writeArrayToCSVFile(arr)
    println(s"Function '$name' completed in $duration ms")
    result
  }

  def logdicescore[T](name: String)(func: => T): T = {
    val startTime = System.nanoTime()
    val result = func
    val endTime = System.nanoTime()
    val duration = Duration.fromNanos(endTime - startTime).toMillis
    val arr = Array(name, duration.toString + "ms")
    writeArrayToCSVFile(arr)
    println(s"Function '$name' completed in $duration ms")
    result
  }

  def logdIOUscore[T](name: String)(func: => T): T = {
    val startTime = System.nanoTime()
    val result = func
    val endTime = System.nanoTime()
    val duration = Duration.fromNanos(endTime - startTime).toMillis
    val arr = Array(name, duration.toString + "ms")
    writeArrayToCSVFile(arr)
    println(s"Function '$name' completed in $duration ms")
    result
  }
  def createResultLog(): Unit = {
    val filePath = "/home/bhan/College/Scala/project/CSYE7200_Project_Team6/logs/resultlog.csv"
    val file = new File(filePath)
    if (file.exists()) file.delete()
    file.createNewFile()
  }

  def createLog(): Unit = {
    val filePath = "logs/durationlog.csv"
    val file = new File(filePath)
    if (file.exists()) file.delete()
    file.createNewFile()
  }

  def writeArrayToCSVFile(data: Array[String]): Unit = {
    val filePath = "logs/durationlog.csv"
    val file = new File(filePath)

    val writer = new FileWriter(file, true)
    val csvWriter = new CSVWriter(writer)
    csvWriter.writeNext(data)
    csvWriter.close()
  }

  def saveImages(images: LazyList[BufferedImage], directory: String): Unit = {
    val dir = new File(directory)
    if (!dir.exists()) {
      dir.mkdir()
    }
    images.zipWithIndex.foreach { case (image, index) =>
      val filename = s"image-${UUID.randomUUID().toString}.png"
      val file = new File(s"$directory/$filename")
      ImageIO.write(image, "png", file)
    }
  }

  def outputLazyListToFiles(lazyList: LazyList[BufferedImage], fileNameTemplate: String, fileNameFunction: BufferedImage => String): Unit = {
    lazyList.zipWithIndex.foreach { case (image, index) =>
      val outputFile = new File(s"/home/bhan/College/Scala/project/CSYE7200_Project_Team6/output/${fileNameTemplate.format(fileNameFunction(image), index)}")
      val ios: ImageOutputStream = ImageIO.createImageOutputStream(new FileOutputStream(outputFile))
      val writer = ImageIO.getImageWritersByFormatName("png").next()
      writer.setOutput(ios)
      val params = writer.getDefaultWriteParam.asInstanceOf[JPEGImageWriteParam]
      params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
      params.setCompressionQuality(0.75f)
      writer.write(null, new IIOImage(image, null, null), params)
      ios.close()
      writer.dispose()
    }
  }

  def generateFileName(image: BufferedImage): String = {
    val md = MessageDigest.getInstance("MD5")
    val baos = new ByteArrayOutputStream()
    ImageIO.write(image, "png", baos)
    val data = baos.toByteArray
    md.update(data)
    val hash = Base64.getEncoder.encodeToString(md.digest()).toLowerCase()
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
    s"$hash-$timestamp"
  }

}
