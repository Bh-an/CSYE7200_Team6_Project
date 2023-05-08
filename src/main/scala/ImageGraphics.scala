import java.awt.geom.{Path2D, Point2D, Rectangle2D}
import java.awt.image.{BufferedImage, Kernel}
import java.awt.{Color, Dimension, Graphics, Graphics2D, GridLayout, Polygon, RenderingHints}
import javax.swing.{ImageIcon, JFrame, JLabel, JPanel, JScrollPane, SwingUtilities, WindowConstants}
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import java.awt.image._
import java.awt.{BorderLayout, GridLayout}

object ImageGraphics {

  def showprocessing(img1: BufferedImage, img2: BufferedImage, img3: BufferedImage, img4: BufferedImage): Unit = {
    val panel = new JPanel(new GridLayout(1, 3))
    panel.add(new ImagePanel(img1))
    panel.add(new ImagePanel(img2))
    panel.add(new ImagePanel(img3))
    panel.add(new ImagePanel(img4))


    val frame = new JFrame()
    frame.setTitle("Images")
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.getContentPane.add(panel)
    frame.pack()
    frame.setVisible(true)
  }

  def showResults(images: LazyList[BufferedImage], labels1: LazyList[Double], labels2: LazyList[Double]): Unit = {
    val frame = new JFrame("4x4 Swing Frame Example")
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    val panel = new JPanel(new BorderLayout())

    // Create grid panel for images and labels
    val gridPanel = new JPanel(new GridLayout(2, 2))
    panel.add(gridPanel, BorderLayout.CENTER)

    // Create image label 1
    val imageLabel1 = new JLabel()
    gridPanel.add(imageLabel1)

    // Create label 1
    val label1 = new JLabel()
    gridPanel.add(label1)

    // Create image label 2
    val imageLabel2 = new JLabel()
    gridPanel.add(imageLabel2)

    // Create label 2
    val label2 = new JLabel()
    gridPanel.add(label2)

    // Display the first images and labels
    val firstImage1 = images.head
    val firstImage2 = images.tail.head
    val firstLabel1 = labels1.head.toString
    val firstLabel2 = labels2.head.toString
    imageLabel1.setIcon(new ImageIcon(firstImage1))
    label1.setText(firstLabel1)
    imageLabel2.setIcon(new ImageIcon(firstImage2))
    label2.setText(firstLabel2)

    // Display the second images and labels
    val secondImage1 = images.tail.tail.head
    val secondImage2 = images.tail.tail.tail.head
    val secondLabel1 = labels1.tail.head.toString
    val secondLabel2 = labels2.tail.head.toString
    val imageLabel3 = new JLabel()
    val label3 = new JLabel()
    val imageLabel4 = new JLabel()
    val label4 = new JLabel()
    imageLabel3.setIcon(new ImageIcon(secondImage1))
    label3.setText(secondLabel1)
    imageLabel4.setIcon(new ImageIcon(secondImage2))
    label4.setText(secondLabel2)
    gridPanel.add(imageLabel3)
    gridPanel.add(label3)
    gridPanel.add(imageLabel4)
    gridPanel.add(label4)

    // Start the frame
    frame.getContentPane.add(panel)
    frame.pack()
    frame.setVisible(true)
  }

  

  def showImages(imgs: LazyList[BufferedImage]): Unit = {
    SwingUtilities.invokeLater(() => {
      val frame = new JFrame("Image Viewer")
      val panel = new JPanel(new GridLayout(0, 2))
      imgs.foreach { img =>
        val label = new JLabel(new ImageIcon(img))
        panel.add(label)
      }
      frame.getContentPane.add(new JScrollPane(panel))
      frame.pack()
      frame.setLocationRelativeTo(null)
      frame.setVisible(true)
    })
  }

  class ImagePanel(val image: BufferedImage) extends JPanel {
    setPreferredSize(new Dimension(image.getWidth, image.getHeight))

    override def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      g.drawImage(image, 0, 0, null)
    }
  }

  def polygonImage(img: BufferedImage): BufferedImage = {
    val width = img.getWidth
    val height = img.getHeight

    // Create a new BufferedImage with TYPE_BYTE_BINARY type
    val polygonImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY)
    val g2d = polygonImg.createGraphics()

    // Set the background color to white
    g2d.setColor(Color.WHITE)
    g2d.fillRect(0, 0, width, height)

    // Set the drawing color to black
    g2d.setColor(Color.BLACK)

    // Find the boundary polygons using contour tracing algorithm
    val polygons = ImageProcessing.findBoundaryPolygons(img)

    // Draw the boundary polygons
    polygons.foreach(poly => {
      val path = new Path2D.Double()
      poly.foreach(p => {
        val x = p.getX()
        val y = p.getY()
        if (path.getCurrentPoint == null) {
          path.moveTo(x, y)
        } else {
          path.lineTo(x, y)
        }
      })
      path.closePath()
      g2d.draw(path)
    })

    // Dispose the graphics context
    g2d.dispose()

    // Return the new BufferedImage
    polygonImg
  }

  def showpolygonImage(img: BufferedImage): BufferedImage = {
    val width = img.getWidth
    val height = img.getHeight
    val polygons = new ListBuffer[Polygon]()
    val visited = Array.fill[Boolean](width, height)(false)

    // Find the edges of each polygon
    for (x <- 0 until width) {
      for (y <- 0 until height) {
        val color = new Color(img.getRGB(x, y))
        if (color.getRed < 128 && !visited(x)(y)) {
          val polygon = new Polygon()
          findPolygon(x, y, width, height, img, visited, polygon)
          polygons += polygon
        }
      }
    }

    val polygonImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY)
    val g2d = polygonImg.createGraphics()

    // Set the background color to white
    g2d.setColor(Color.WHITE)
    g2d.fillRect(0, 0, width, height)

    // Set the drawing color to black
    g2d.setColor(Color.BLACK)

    // Draw the polygons onto the destination image

    for (polygon <- polygons) {
      g2d.drawPolygon(polygon)
    }
    g2d.dispose()

    polygonImg
  }


  def drawPolygonImage(img: BufferedImage): BufferedImage = {
    val width = img.getWidth
    val height = img.getHeight

    // Find the polygon vertices using the existing findPolygon function
    val visited = Array.fill(width, height)(false)
    val polygon = new Polygon()
    findPolygon(0, 0, width, height, img, visited, polygon)

    // Draw the polygon boundary onto a new image
    val boundaryImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY)
    val g = boundaryImg.getGraphics.asInstanceOf[Graphics]
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, width, height)
    g.setColor(Color.BLACK)
    g.drawPolygon(polygon)

    boundaryImg
  }

  def drawboundary(img: BufferedImage): BufferedImage = {

    // Create a new BufferedImage with the same dimensions as the original but with grayscale color space
    val grayImg = new BufferedImage(img.getWidth, img.getHeight, BufferedImage.TYPE_BYTE_GRAY)

    // Create a Graphics2D object to draw the original image onto the new image
    val g2d = grayImg.createGraphics()
    g2d.drawImage(img, 0, 0, null)
    g2d.dispose()

    // Convert the image to a binary image
    val binaryImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY)
    val graphics = binaryImage.createGraphics()
    graphics.drawImage(grayImg, 0, 0, null)
    graphics.dispose()



    // Dilate the binary image to thicken the white regions
    val kernelSize = 3
    val kernel = Array.ofDim[Byte](kernelSize, kernelSize)
    for (i <- 0 until kernelSize) {
      for (j <- 0 until kernelSize) {
        kernel(i)(j) = 1
      }
    }
    val kernelFloat = kernel.map(_.map(_.toFloat))
    val dilateOp = new DilationOp(kernelFloat)
    val dilatedImage = dilateOp.filter(binaryImage, null)

    // Erode the dilated image to reduce the thickness of the white boundaries
    val erodeOp = new ErosionOp(kernelFloat)
    val erodedImage = erodeOp.filter(dilatedImage, null)

    // Subtract the eroded image from the dilated image to obtain the boundaries
    val diffImage = new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY)
    val diffOp = new DifferenceOp(erodedImage)
    diffOp.filter(dilatedImage, diffImage)

    // Find the boundary pixels using a modified BFS algorithm
    val boundaryPixels = findBoundaryPixels(diffImage)

    // Draw the boundary pixels onto a new image
    val boundaryImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY)
    val g = boundaryImg.getGraphics.asInstanceOf[Graphics]
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, img.getWidth(), img.getHeight())
    g.setColor(Color.BLACK)
    for ((x, y) <- boundaryPixels) {
      g.drawLine(x, y, x, y)
    }

    boundaryImg
  }

  def findBoundaryPixels(img: BufferedImage): Seq[(Int, Int)] = {
    val pixels = for {
      x <- 0 until img.getWidth
      y <- 0 until img.getHeight
    } yield (x, y)
    pixels.filter { case (x, y) =>
      val isWhite = (img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF
      val hasBlackNeighbor = {
        val neighbors = for {
          i <- -1 to 1
          j <- -1 to 1
          if i != 0 || j != 0
          if x + i >= 0 && x + i < img.getWidth && y + j >= 0 && y + j < img.getHeight
        } yield (x + i, y + j)
        neighbors.exists { case (nx, ny) =>
          (img.getRGB(nx, ny) & 0xFFFFFF) == 0x000000
        }
      }
      isWhite && hasBlackNeighbor
    }
  }

  private def findPolygon(x1: Int, y1: Int, x2: Int, y2: Int, img: BufferedImage, visited: Array[Array[Boolean]], polygon: Polygon): Unit = {
    // Define a case class to represent the state of each recursive call
    case class State(x1: Int, y1: Int, x2: Int, y2: Int)

    // Create an empty stack and push the initial state onto it
    var stack = List(State(x1, y1, x2, y2))

    // Continue processing states on the stack until it is empty
    @tailrec
    def loop(): Unit = stack match {
      case State(x1, y1, x2, y2) :: rest =>
        // Check if all pixels in the region are black
        var allBlack = true
        for (x <- x1 until x2; y <- y1 until y2) {
          if (!visited(x)(y) && isWhite(img.getRGB(x, y))) {
            allBlack = false
          }
        }

        // If all pixels are black, add the region to the polygon
        if (allBlack) {
          polygon.addPoint(x1, y1)
          polygon.addPoint(x2 - 1, y1)
          polygon.addPoint(x2 - 1, y2 - 1)
          polygon.addPoint(x1, y2 - 1)
        } else {
          // Otherwise, push the quadrants onto the stack to be processed later
          val xm = (x1 + x2) / 2
          val ym = (y1 + y2) / 2
          stack = State(x1, y1, xm, ym) :: State(xm, y1, x2, ym) :: State(xm, ym, x2, y2) :: State(x1, ym, xm, y2) :: rest
        }

        // Mark the current region as visited
        for (x <- x1 until x2; y <- y1 until y2) {
          visited(x)(y) = true
        }

        // Process the next state on the stack
        loop()

      case Nil => // Stack is empty, return
    }

    loop()
  }

  private def isWhite(rgb: Int): Boolean = {
    val red = (rgb >> 16) & 0xFF
    val green = (rgb >> 8) & 0xFF
    val blue = rgb & 0xFF
    red == 255 && green == 255 && blue == 255
  }

  private class ErosionOp(kernel: Array[Array[Float]]) extends ConvolveOp(new Kernel(kernel.length, kernel.length, kernel.flatten)) {
    val scaleFactor: Float = kernel.length * kernel.length

     def filtered(src: BufferedImage, dst: BufferedImage): BufferedImage = {
      val grayscaleOp = new ColorConvertOp(null)
      val graySrc = grayscaleOp.filter(src, null)
      val eroded = super.filter(graySrc, null)
      val erodedScaled = new BufferedImage(src.getWidth, src.getHeight, BufferedImage.TYPE_BYTE_GRAY)
      val erodedRaster = eroded.getRaster
      val erodedScaledRaster = erodedScaled.getRaster
      for (y <- 0 until eroded.getHeight; x <- 0 until eroded.getWidth) {
        val pixel = erodedRaster.getPixel(x, y, Array.ofDim[Int](1))
        val scaledPixel = (pixel(0) / scaleFactor).toInt
        erodedScaledRaster.setPixel(x, y, Array(scaledPixel))
      }
      erodedScaled
    }
  }

  private class DilationOp(kernel: Array[Array[Float]]) extends ConvolveOp(new Kernel(kernel.length, kernel.length, kernel.flatten)) {
    def filtered(src: BufferedImage, dst: BufferedImage): BufferedImage = {
      val grayscaleOp = new ColorConvertOp(null)
      val graySrc = grayscaleOp.filter(src, null)
      super.filter(graySrc, null)
    }
  }

  private class DifferenceOp(subtractor: BufferedImage) extends BufferedImageOp {
    override def filter(src: BufferedImage, dst: BufferedImage): BufferedImage = {
      val width = src.getWidth
      val height = src.getHeight
      val diffImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY)
      for (y <- 0 until height; x <- 0 until width) {
        val diff = math.max(0, subtractor.getRGB(x, y) - src.getRGB(x, y))
        diffImg.setRGB(x, y, diff)
      }
      diffImg
    }

    override def getBounds2D(bufferedImage: BufferedImage): Rectangle2D = ???

    override def createCompatibleDestImage(bufferedImage: BufferedImage, colorModel: ColorModel): BufferedImage = ???

    override def getPoint2D(point2D: Point2D, point2D1: Point2D): Point2D = ???

    override def getRenderingHints: RenderingHints = ???
  }



}
