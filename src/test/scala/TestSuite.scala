import ImageProcessing._
import org.scalatest.funsuite.AnyFunSuite
import LazyImageLoader._
import javax.swing.WindowConstants

import java.io.File
import java.util.logging.Logger
import javax.imageio.ImageIO
import java.awt.{BorderLayout, Image}
import javax.swing.{ImageIcon, JFrame, JLabel}
import java.awt.image.BufferedImage
import java.lang.Thread.sleep

class TestSuite extends AnyFunSuite {

  val filePath1 = "src/main/resources/preds.png"
//  val filePath2 = "src/main/resources/lbls.png"
//
//  // Read images
  val preds = ImageIO.read(new File(filePath1))
//  val lbls = ImageIO.read(new File(filePath2))
//  val logger = Logger.getLogger(getClass.getName)

//  val image_name_List = LazyImageLoader.loadImages("/home/bhan/College/Scala/project/CSYE7200_Project_Team6/spacenet6 test images")
  val imageList = LazyImageLoader.loadImages("/home/bhan/College/Scala/project/CSYE7200_Project_Team6/spacenet6 test images")

  //  val imageList: LazyList[BufferedImage] = image_name_List.map(_._1)

  test("Array should have size 900") {
    val test = imageList.map(_._1)
    print(test.head.getType)
    print(test.head.getHeight)
    print(test.head.getWidth)
    val pixelArrays = LazyAnalysisFuncs.convert2array(imageList.map(_._1))
    pixelArrays.foreach { pixelArray =>
      assert(pixelArray.length == 900)
    }
  }

  test("Output image") {
//    val image: BufferedImage = imageList.head._1
//
//    val label = new JLabel(new ImageIcon(image))
//
//    // Create a JFrame to hold the JLabel
//    val frame = new JFrame()
//    frame.getContentPane.setLayout(new BorderLayout())
//    frame.getContentPane.add(label, BorderLayout.CENTER)
//
//    // Set the JFrame size and make it visible
//    frame.setSize(image.getWidth + 50, image.getHeight + 50)
//    frame.setVisible(true)
//
//    sleep(6500000)

    val procImages : LazyList[(BufferedImage, BufferedImage, (Int, Int))] = LazyProcFuncs.convertImagesToTiles(imageList.map(_._1))

    val image1 = procImages.head._1
    val image2 = procImages.head._2
    val image3 = ImageGraphics.drawboundary(preds)

    ImageGraphics.showImages(image1,preds,image3)
    sleep(9999)

  }


//  test("Set should have size 2") {
//    val x = ImageAnalysis.array2set(ImageAnalysis.convert2array(preds))
//    assert(x.size == 2)
//    }

//  test("Image should have height 480") {
//    val x = ImageAnalysis.convert2img(ImageAnalysis.convert2array(preds))
//    assert(x.getHeight == 480)
//  }
//
//  test("checkSpeed should run in less than 1 seconds") {
//    val start = System.currentTimeMillis()
//    checkSpeed {
//      val croppedImg = cropImage(filePath1)
//      val grayscaleImg = toGrayscale(croppedImg)
//      val transformedImg = transformImage(grayscaleImg)
//      saveImage(transformedImg, "src/main/resources/transformed_image.png")
//    val end = System.currentTimeMillis()
//    val time = end - start
//    assert(time < 1000)
//   }
//  }
//
//  test("testImageSize should run in less than 1 seconds") {
//    val start = System.currentTimeMillis()
//    ImageProcessing.testImageSize(filePath1)
//    val end = System.currentTimeMillis()
//    val time = end - start
//    assert(time < 1000)
//  }
//
//  test("testImageType should run in less than 1 seconds") {
//    val start = System.currentTimeMillis()
//    ImageProcessing.testImageType(filePath1)
//    val end = System.currentTimeMillis()
//    val time = end - start
//    assert(time < 1000)
//  }
//
//  test("Dice score should be 0.7 or greater") {
//    val a = ImageAnalysis.array2set(ImageAnalysis.convert2array(preds))
//    val b = ImageAnalysis.array2set(ImageAnalysis.convert2array(lbls))
//    val x = ImageAnalysis.computeDice(a, b)
//    val y = (x * 10).round / 10.toDouble
//    require(y >= 0.7)
//  }
//
//  test("IoU score should be 0.7 or greater") {
//    val a = ImageAnalysis.array2set(ImageAnalysis.convert2array(preds))
//    val b = ImageAnalysis.array2set(ImageAnalysis.convert2array(lbls))
//    val x = ImageAnalysis.computeIoU(a, b)
//    require(x >= 0.7)
//  }

}