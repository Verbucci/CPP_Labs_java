package com.ggl.game2048.model

import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._

class ScalaSort() {

  def sort(recordList: Array[Int], replayFileID: Array[Int]) {

    def swap(i: Int, j: Int) {
      val recordTemp = recordList(i)
      recordList(i) = recordList(j)
      recordList(j) = recordTemp

      val fileIDTemp = replayFileID(i)
      replayFileID(i) = replayFileID(j)
      replayFileID(j) = fileIDTemp
    }

    def quickSort(leftBorder: Int, rightBorder: Int) {
      val middleValue = recordList((leftBorder + rightBorder) / 2)
      var i = leftBorder
      var j = rightBorder
      while (i <= j) {
        while (recordList(i) < middleValue) i += 1
        while (recordList(j) > middleValue) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (leftBorder < j) quickSort(leftBorder, j)
      if (j < rightBorder) quickSort(i, rightBorder)
    }
    quickSort(0, recordList.length - 1)
  }

  def printPseudocode(listOfSteps: Array[Cell]) {
    val res = ArrayBuffer[(String, String)]()
    val playerSets = "Cell value is: "
    for (e <- listOfSteps) {
      e match {
        case e => res.+=((playerSets, e.getValue.toString()))
      }
    }
    res.foreach { x => println(x._1 + " [" + x._2 + "] ") }
  }

}