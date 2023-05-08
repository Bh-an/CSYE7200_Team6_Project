import ImageAnalysis.{getImageType, testImageSize}
import ImageProcessing.{complexArrayToImage, imageToComplexArray}
import org.scalatest.funsuite.AnyFunSuite
import java.io.File
import javax.imageio.ImageIO

class ListFunSuite extends AnyFunSuite {

  val filePath1 = "src/test/resources/preds.png"
  val filePath2 = "src/test/resources/lbls.png"
  val filePath3 = "src/test/resources/PS-RGB.tif"

  // Read images
  val preds = ImageIO.read(new File(filePath1))
  val lbls = ImageIO.read(new File(filePath2))
  val rgb = ImageIO.read(new File(filePath3))

  test("Array should have size 480") {
    val x = ImageAnalysis.convert2array(preds)
    assert(x.length == 480)
  }

  test("Array should have size 900") {
    val x = imageToComplexArray(rgb)
    assert(x.length == 900)
  }

  test("Set should have size 2") {
    val x = ImageAnalysis.array2set(ImageAnalysis.convert2array(preds))
    assert(x.size == 2)
    }

  test("Image should have height 480") {
    val x = ImageAnalysis.convert2img(ImageAnalysis.convert2array(preds))
    assert(x.getHeight == 480)
  }

  test("Image should have height 900") {
    val x = complexArrayToImage(imageToComplexArray(rgb))
    assert(x.getHeight == 900)
  }

  test("Image should be type greyscale") {
    val imagetype: String = getImageType(preds)
    assert(imagetype == "TYPE_BYTE_GRAY")
  }

  test("Image Size should be 900x900") {
    val (x, y) = testImageSize(preds)
    assert(x == 480 && y == 480)
  }


  test("Dice score should be 0.7 or greater") {
    val a = ImageAnalysis.array2set(ImageAnalysis.convert2array(preds))
    val b = ImageAnalysis.array2set(ImageAnalysis.convert2array(lbls))
    val x = ImageAnalysis.computeDice(a, b)
    val y = (x * 10).round / 10.toDouble
    require(y >= 0.7)
  }

  test("IoU score should be 0.7 or greater") {
    val a = ImageAnalysis.array2set(ImageAnalysis.convert2array(preds))
    val b = ImageAnalysis.array2set(ImageAnalysis.convert2array(lbls))
    val x = ImageAnalysis.computeIoU(a, b)
    require(x >= 0.7)
  }

}