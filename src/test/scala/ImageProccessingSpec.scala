import ImageAnalysis.{getImageType, testImageSize}
import ImageProcessing.{createGaussianKernel, cropImage, getNeighbor, nextPowerOf2, padImage, toGrayscale}
import LazyImageLoader.loadInputImages
import LazyProcFuncs.convertImagesToTiles
import org.scalactic.Tolerance.convertNumericToPlusOrMinusWrapper
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.be
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import java.awt.Point
import java.io.File
import javax.imageio.ImageIO

class TestSuite extends AnyFunSuite {

  val filePath = "src/test/resources/PS-RGB.tif"
  val dirpath = "spacenet6_images"

  // Read images
  val image = ImageIO.read(new File(filePath))
  val images = loadInputImages(dirpath)

  test("Test image crop to 480x480") {
    val croppedImage = cropImage(image, 0, 0, 480, 480)
    val (x, y) = testImageSize(croppedImage)
    assert(x == 480 && y == 480)
    }

  test("Image padded to 1000x1000 pixels should be of size 1024x1024(power of 2)") {
    val paddedImage = padImage(image, 1000, 1000)
    val (x, y) = testImageSize(paddedImage)
    assert(x == 1024 && y == 1024)
  }

  test("Image should be converted to grayscale") {
    val before = getImageType(image)
    val greyscaleimg = toGrayscale(image)
    val after = getImageType(greyscaleimg)

    assert(before != "TYPE_BYTE_GRAY")
    assert(after == "TYPE_BYTE_GRAY")
  }

  test("getNeighbor should return the correct neighbor in each direction") {
    val point = new Point(5, 5)
    val neighbors = List(
      new Point(4, 6), // northwest
      new Point(5, 6), // north
      new Point(6, 6), // northeast
      new Point(6, 5), // east
      new Point(6, 4), // southeast
      new Point(5, 4), // south
      new Point(4, 4), // southwest
      new Point(4, 5) // west
    )

    for (dir <- 0 to 7) {
      val expectedNeighbor = neighbors(dir)
      val actualNeighbor = getNeighbor(point, dir)
      assert(actualNeighbor==expectedNeighbor)
    }
  }

  test("Gaussian kernels dimensions") {
    val kernel = createGaussianKernel(5, 1.0)
    kernel.length shouldBe 5
    kernel(0).length shouldBe 5
  }

  test("Gaussian kernel sum of values should be ~1") {
    val kernel = createGaussianKernel(7, 2.0)
    val sum = kernel.flatten.sum
    sum shouldBe (1.0+- 0.001)
  }

  test("A Gaussian kernel's values should decrease outwards from the centre") {
    val kernel = createGaussianKernel(9, 1.5)
    kernel(1)(1) should be > kernel(0)(0)
    kernel(2)(2) should be > kernel(1)(1)
    kernel(3)(3) should be > kernel(2)(2)
    kernel(4)(4) should be > kernel(3)(3)
    kernel(5)(5) should be < kernel(4)(4)
    kernel(6)(6) should be < kernel(5)(5)
    kernel(7)(7) should be < kernel(5)(5)
  }

  test("nextPowerOf2 should return the next power of 2 greater than the input") {
    val inputs = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
    val expectedOutputs = Seq(1, 2, 4, 4, 8, 8, 8, 8, 16, 16, 16, 16, 16, 16, 16, 16, 32, 32, 32, 32)

    inputs.zip(expectedOutputs).foreach { case (input, expected) =>
      val result = nextPowerOf2(input)
      result shouldEqual expected
    }
  }

  test("Tiles should be 450x450 and 2^2 the amount from 900x900 images") {
    val tiledImages = convertImagesToTiles(images, 450)

    assert(testImageSize(tiledImages.map(_._1).head) == (450,450))
    assert(tiledImages.length == (4*images.length))

  }

}
