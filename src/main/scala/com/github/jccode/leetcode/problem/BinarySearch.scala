package com.github.jccode.leetcode.problem

/**
  * BinarySearch
  *
  * @author 01372461
  */
object BinarySearch {

  def binarySearch_(a: Array[Int], e: Int, m: Int, n: Int): Int = {
    if (a.isEmpty) return -1
    if (m == n) {
      return if(a(m) == e) m else -1
    }
    val h = (n + m) / 2

    val left = binarySearch_(a, e, m, h)
    val right = binarySearch_(a, e, h+1, n)

    Math.max(left, right)
  }

  def binarySearch(a: Array[Int], e: Int) = binarySearch_(a, e, 0, a.length - 1)

  def main(args: Array[String]): Unit = {
    println(binarySearch(Array(1, 2, 3, 9, 8, 7, 6, 5, 4), 7))
  }

}
