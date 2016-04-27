package com.ggl.game2048.model

import com.ggl.game2048.controller.SortGameReplaysActionListener;

class ScalaSort(sort: SortGameReplaysActionListener) {

  def sort(maxNumber: Array[Int], nameOfGame: Array[String]) {
    def swap(firstFileID: Int, secondFileID: Int) {
      val temp = maxNumber(firstFileID)
      maxNumber(firstFileID) = maxNumber(secondFileID)
      maxNumber(secondFileID) = temp

      val string = nameOfGame(firstFileID);
      nameOfGame(firstFileID) = nameOfGame(secondFileID);
      nameOfGame(secondFileID) = string;
    }

    def quickSort(sortRangeFrom: Int, sortRangeTo: Int) {
      val pivot = maxNumber((sortRangeFrom + sortRangeTo) / 2)
      var i = sortRangeFrom
      var j = sortRangeTo
      while (i <= j) {
        while (maxNumber(i) > pivot) i += 1
        while (maxNumber(j) < pivot) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (sortRangeFrom < j) quickSort(sortRangeFrom, j)
      if (j < sortRangeTo) quickSort(i, sortRangeTo)
    }
    quickSort(0, maxNumber.length - 1)
  }
}