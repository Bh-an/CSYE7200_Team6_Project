import ImageAnalysis.{computeDice, computeIoU}

import java.awt.image.BufferedImage

object LazyAnalysisFuncs {

 def convert2array(images: LazyList[BufferedImage]): LazyList[Array[Array[Int]]] = {
    images.map { img =>
      ImageAnalysis.convert2array(img)
    }
  }

  // Convert 2D array to image
  def convert2img(arr: LazyList[Array[Array[Int]]]): LazyList[BufferedImage] = {
    arr.map { subArr =>
      ImageAnalysis.convert2img(subArr)
    }
  }

  // Convert 2D array to 1D set
  def array2set(arr: LazyList[Array[Array[Int]]]): LazyList[Set[Int]] = {
    arr.map { subArr =>
      subArr.flatten.toSet
    }
  }

 // Compute Dice score
  def computeDicefromlist(set1: LazyList[Set[Int]], set2: LazyList[Set[Int]]): LazyList[Double] = {
    set1.zip(set2).map { case (s1, s2) => computeDice(s1, s2) }
  }

  // Compute Jaccard index
  def computeIoUfromlist(set1: LazyList[Set[Int]], set2: LazyList[Set[Int]]): LazyList[Double] = {
    set1.zip(set2).map { case (s1, s2) => computeIoU(s1, s2) }
  }


}
