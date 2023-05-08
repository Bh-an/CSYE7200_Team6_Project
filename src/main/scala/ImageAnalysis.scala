
import java.awt.image.BufferedImage

object ImageAnalysis {

  // Convert image to 2D array
  def convert2array(img: BufferedImage): Array[Array[Int]] = {
    val w = img.getWidth
    val h = img.getHeight
    val arr = Array.ofDim[Int](h, w)
    for (y <- 0 until h) {
      for (x <- 0 until w) {
        arr(y)(x) = img.getRGB(x, y)
      }
    }
    arr
  }

  // Convert 2D array to image
  def convert2img(arr: Array[Array[Int]]): BufferedImage = {
    val w = arr(0).length
    val h = arr.length
    val img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    for (y <- 0 until h) {
      for (x <- 0 until w) {
        img.setRGB(x, y, arr(y)(x))
      }
    }
    img
  }

  // Convert 2D array to 1D set
  def array2set(arr: Array[Array[Int]]): Set[Int] = {
    arr.flatten.toSet
  }

  // Get intersection of two sets
  def setIntersection(set1: Set[Int], set2: Set[Int]): Set[Int] = {
    set1.intersect(set2)
  }

  // Add two sets
  def setUnion(set1: Set[Int], set2: Set[Int]): Set[Int] = {
    set1.++(set2)
  }

  // Compute Dice score
  def computeDice(set1: Set[Int], set2: Set[Int]): Double = {
    val intersection = setIntersection(set1, set2)
    val union = setUnion(set1, set2)
    2.0 * intersection.size / union.size
  }

  // Compute Jaccard index
  def computeIoU(set1: Set[Int], set2: Set[Int]): Double = {
    val intersection = setIntersection(set1, set2)
    val union = setUnion(set1, set2)
    intersection.size / ((set1.size + set2.size) - union.size)
  }

  def testImageSize(image: BufferedImage): (Int, Int) = {
    val (x, y) = (image.getWidth(), image.getHeight)
    (x, y)
  }


  //  test image type
  def getImageType(image: BufferedImage): String = {
    val imageType = image.getType()
    imageType match {
      case BufferedImage.TYPE_INT_RGB => "TYPE_INT_RGB"
      case BufferedImage.TYPE_INT_ARGB => "TYPE_INT_ARGB"
      case BufferedImage.TYPE_INT_ARGB_PRE => "TYPE_INT_ARGB_PRE"
      case BufferedImage.TYPE_INT_BGR => "TYPE_INT_BGR"
      case BufferedImage.TYPE_3BYTE_BGR => "TYPE_3BYTE_BGR"
      case BufferedImage.TYPE_4BYTE_ABGR => "TYPE_4BYTE_ABGR"
      case BufferedImage.TYPE_4BYTE_ABGR_PRE => "TYPE_4BYTE_ABGR_PRE"
      case BufferedImage.TYPE_USHORT_565_RGB => "TYPE_USHORT_565_RGB"
      case BufferedImage.TYPE_USHORT_555_RGB => "TYPE_USHORT_555_RGB"
      case BufferedImage.TYPE_BYTE_GRAY => "TYPE_BYTE_GRAY"
      case BufferedImage.TYPE_USHORT_GRAY => "TYPE_USHORT_GRAY"
      case BufferedImage.TYPE_BYTE_BINARY => "TYPE_BYTE_BINARY"
      case BufferedImage.TYPE_BYTE_INDEXED => "TYPE_BYTE_INDEXED"
      case _ => "Unknown Image Type"
    }
  }

}
