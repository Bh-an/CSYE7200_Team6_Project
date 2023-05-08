import java.awt.{Color, Point, Polygon, RenderingHints}
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.{BufferedImage, Kernel}
import org.apache.commons.math3.complex.{Complex, ComplexUtils}

import scala.math._
import scala.collection.mutable.{ArrayBuffer, Queue}
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer}
object ImageProcessing {

  //function to crop image in 256x256
  def cropImage(image: BufferedImage, x: Int, y: Int, width: Int, height: Int): BufferedImage = {
    val croppedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = croppedImage.createGraphics()
    graphics.drawImage(image, 0, 0, width, height, x, y, x + width, y + height, null)
    graphics.dispose()
    croppedImage
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


  //function for sharpening Image
  def sharpenImage(image: BufferedImage): BufferedImage = {
    val kernelData = Array(
      0f, -1f, 0f,
      -1f, 5f, -1f,
      0f, -1f, 0f
    )

    val kernel = new Kernel(3, 3, kernelData)
    val op = new java.awt.image.ConvolveOp(kernel)
    val sharpened = op.filter(image, null)

    val g2d = sharpened.createGraphics()
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g2d.dispose()

    sharpened
  }

  def deblurImage(image: BufferedImage): BufferedImage = {

    // Define the kernel
    val kernel = createGaussianKernel(5, 1)

    // Define the noise variance
    val noiseVariance = 1.0

    val width = image.getWidth
    val height = image.getHeight

    // Convert kernel to Complex array
    val kernelComplex = kernel.map(_.map(new Complex(_, 0.0)))

    // Pad kernel and image to the same size
    val paddedKernel = padKernel(kernelComplex, width, height)
    val paddedImage = padImage(image, width, height)

    // Convert image to Complex array
    val imageComplex = imageToComplexArray(paddedImage)

    println(imageComplex.size)
    println(paddedKernel.size)

    // Compute the FFT of the padded image and kernel
    val fft = new FastFourierTransformer(DftNormalization.STANDARD)
    val flattenedImage = imageComplex.flatten.map(_.getReal)
    val fftImage = fft.transform(flattenedImage, org.apache.commons.math3.transform.TransformType.FORWARD)
    val fftKernel = fft.transform(paddedKernel.flatten.map(_.getReal), org.apache.commons.math3.transform.TransformType.FORWARD)

    val nwidth = (sqrt(fftImage.size)).toInt
    val nheight = (sqrt(fftImage.size)).toInt

    // Reshape the FFT of the padded image to a two-dimensional array
    val fftImage2D = fftImage.grouped(nwidth).toArray
    val fftKernel2D = fftKernel.grouped(nwidth).toArray

    //
    val fftDeconvolved = new Array[Array[Complex]](nheight)
    for (i <- 0 until nheight) {
      fftDeconvolved(i) = new Array[Complex](nwidth)
      for (j <- 0 until nwidth) {
        val fftValue = fftImage2D(i)(j)
        val fftKernelValue = fftKernel2D(i)(j)
        val den = fftKernelValue.multiply(fftKernelValue).add(noiseVariance)
        fftDeconvolved(i)(j) = fftValue.multiply(fftKernelValue).divide(den)
      }
    }

    val deconvolved = fft.transform(fftDeconvolved.flatten, org.apache.commons.math3.transform.TransformType.INVERSE)

    // Normalize the deconvolved image
    val maxMagnitude = deconvolved.map(_.abs).max
    val normDeconvolved = deconvolved.map(_.divide(maxMagnitude))
    val normDeconvolved2D = normDeconvolved.grouped(width).toArray


    // Convert the normalized deconvolved image back to a BufferedImage and return it
    val result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        val value = (normDeconvolved2D(i)(j).multiply(255)).getReal.toInt
        result.setRGB(j, i, value.toInt << 16 | value.toInt << 8 | value.toInt)
      }
    }
    result
  }

  def convert2TIF(img: BufferedImage): BufferedImage = {
    val writer = ImageIO.getImageWritersByFormatName("tif").next()
    val imageFormat = ImageIO.getImageReader(writer).getFormatName
    if (imageFormat == "tif") {
      img
    } else {
      val pngImage = new BufferedImage(img.getWidth, img.getHeight, BufferedImage.TYPE_INT_ARGB)
      val g = pngImage.createGraphics()
      g.drawImage(img, 0, 0, null)
      g.dispose()
      pngImage
    }
  }


  private def padKernel(kernel: Array[Array[Complex]], width: Int, height: Int): Array[Array[Complex]] = {
    var paddedWidth = width
    var paddedHeight = height
    if (!isPowerOf2(width)) {
      paddedWidth = nextPowerOf2(width)
    }
    if (!isPowerOf2(height)) {
      paddedHeight = nextPowerOf2(height)
    }
    val paddedKernel = Array.fill[Complex](paddedHeight, paddedWidth)(Complex.ZERO)
    for (i <- kernel.indices; j <- kernel(i).indices) {
      paddedKernel(i)(j) = kernel(i)(j)
    }
    paddedKernel
  }

  def padImage(image: BufferedImage, width: Int, height: Int): BufferedImage = {
    var paddedWidth = width
    var paddedHeight = height
    if (!isPowerOf2(width)) {
      paddedWidth = nextPowerOf2(width)
    }
    if (!isPowerOf2(height)) {
      paddedHeight = nextPowerOf2(height)
    }
    val paddedImage = new BufferedImage(paddedWidth, paddedHeight, BufferedImage.TYPE_BYTE_GRAY)
    val g = paddedImage.getGraphics
    g.drawImage(image, 0, 0, null)
    g.dispose()
    paddedImage
  }

  private def isPowerOf2(n: Int): Boolean = (n & (n - 1)) == 0

  def nextPowerOf2(n: Int): Int = {
    var p = 1
    while (p < n) {
      p <<= 1
    }
    p
  }

  def imageToComplexArray(image: BufferedImage): Array[Array[Complex]] = {
    val width = image.getWidth
    val height = image.getHeight
    val pixeldata = new Array[Int](width * height)
    val data = image.getData.getPixels(0, 0, 1, 1, pixeldata)
    val complexArray = Array.ofDim[Complex](height, width)
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        complexArray(i)(j) = new Complex(data(i * width + j), 0.0)
      }
    }
    complexArray
  }

  def complexArrayToImage(complexArray: Array[Array[Complex]]): BufferedImage = {
    val width = complexArray.length
    val height = complexArray(0).length

    val image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
    val pixelArray = image.getRaster().getPixels(0, 0, width, height, null.asInstanceOf[Array[Int]])

    for (x <- 0 until width; y <- 0 until height) {
      val magnitude = complexArray(x)(y).abs().toFloat
      pixelArray(y * width + x) = (magnitude * 255).toInt
    }

    image.getRaster().setPixels(0, 0, width, height, pixelArray)

    image
  }

  def createGaussianKernel(size: Int, sigma: Double): Array[Array[Double]] = {
    val kernel = Array.ofDim[Double](size, size)
    val center = size / 2
    val div = 2 * sigma * sigma
    var sum = 0.0
    for (i <- 0 until size; j <- 0 until size) {
      kernel(i)(j) = exp(-1 * (pow(i - center, 2) + pow(j - center, 2)) / div)
      sum += kernel(i)(j)
    }
    for (i <- 0 until size; j <- 0 until size) {
      kernel(i)(j) /= sum
    }
    kernel
  }

  def findBoundaryPolygons(img: BufferedImage): List[List[Point]] = {
    val width = img.getWidth
    val height = img.getHeight
    val visited = Array.ofDim[Boolean](width, height)
    val polygons = new ArrayBuffer[List[Point]]()

    // Find the first black pixel
    var start = findFirstBlackPixel(img)

    while (start != null) {
      val poly = new ArrayBuffer[Point]()
      poly += start
      visited(start.x)(start.y) = true

      // Trace the boundary polygon using contour tracing algorithm
      var curr = getNextBoundaryPixel(img, visited, start.x, start.y, 7)
      while (curr != start && curr != null) {
        poly += curr
        visited(curr.x)(curr.y) = true
        curr = getNextBoundaryPixel(img, visited, curr.x, curr.y, (7 + 4) % 8)
      }
      poly += start
      visited(start.x)(start.y) = true

      // Add the polygon to the list of polygons
      polygons += poly.toList
      start = findFirstBlackPixel(img, visited)
    }

    // Return the list of polygons
    polygons.toList
  }

  def findFirstBlackPixel(img: BufferedImage, visited: Array[Array[Boolean]] = null): Point = {
    val width = img.getWidth
    val height = img.getHeight
    var p: Point = null
    for (x <- 0 until width) {
      for (y <- 0 until height) {
        if (isEdge(img, x, y)) {
          val pixel = new Point(x, y)
          if (visited == null || !visited(x)(y)) {
            p = pixel
            return p
          }
        }
      }
    }
    p
  }

  def getNextBoundaryPixel(img: BufferedImage, visited: Array[Array[Boolean]], x: Int, y: Int, startDir: Int): Point = {
    val width = img.getWidth
    val height = img.getHeight
    var dir = startDir
    var curr = new Point(x, y)
    var next: Point = null
    do {
      dir = (dir + 1) % 8
      next = getNeighbor(curr, dir)
    } while (!isValid(next, width, height) || visited(next.x)(next.y) || !isEdge(img, next.x, next.y))
    next
  }

  def getNeighbor(p: Point, d: Int): Point = {
    val offsets = Array((-1, 1), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1), (-1, 0))
    val (dx, dy) = offsets(d)
    new Point(p.x + dx, p.y + dy)
  }

  def isValid(p: Point, width: Int, height: Int): Boolean = {
    p.x >= 0 && p.y >= 0 && p.x < width && p.y < height
  }

  def isEdge(image: BufferedImage, x: Int, y: Int): Boolean = {
    val color = new Color(image.getRGB(x, y))
    val gray = (color.getRed + color.getGreen + color.getBlue) / 3
    val threshold = 128 // adjustable
    gray < threshold
  }

}
