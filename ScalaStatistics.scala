package com.ggl.game2048.model

class ScalaStatistics {
  def totalPointsEarned(recordList: Array[Int]): Int = {
    (for (i <- recordList) yield i).sum
  }

  def findMaxScore(recordList: Array[Int]): Int = {
    (for (i <- recordList) yield i).max
  }

  def findMinScore(recordList: Array[Int]): Int = {
    (for (i <- recordList) yield i).min
  }

  def calculateAverageScore(recordList: Array[Int]): Int = {
    val list = for (i <- recordList) yield i
    list.sum / list.length
  }

  def findHighestCell(cellList: Array[Int]): Int = {
    (for (i <- cellList) yield i).max
  }
  
    def countWins(scoreList: Array[Int]): Int = {
    (for (e <- scoreList if e == 5) yield e).length
  }

  def countClicks(clickList: Array[Int], coordinates: Int): Int = {
    (for (i <- clickList if i == coordinates) yield i).length
  }
}