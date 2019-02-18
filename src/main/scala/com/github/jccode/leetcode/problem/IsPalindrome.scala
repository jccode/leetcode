package com.github.jccode.leetcode.problem
import scala.collection.mutable.ListBuffer

/**
  * IsPalindrome
  *
  * @author 01372461
  */
object IsPalindrome {

  def isPalindrome(x: Int): Boolean = {
    if (x < 0) return false

    val buffer = ListBuffer.empty[Int]
    var a = x
    while ( a != 0 ) {
      buffer += (a % 10)
      a = a / 10
    }

    val array = buffer.toArray
    var (i, j) = (0, array.length-1)
    while (i <= j) {
      if (array(i) != array(j)) {
        return false
      }
      i += 1
      j -= 1
    }
    true
  }

  def isPalindrome2(x: Int): Boolean = {
    if (x < 0) return false
    val s = x.toString
    var (i, j) = (0, s.length-1)
    while (i <= j) {
      if (s(i) != s(j)) {
        return false
      }
      i += 1
      j -= 1
    }
    true
  }

  def isPalindrome3(x: Int): Boolean = {
    if (x < 0) return false
    val s = x.toString
    s.reverse == s
  }

  /**
    * 官方解答
    *
    * @param x
    * @return
    */
  def isPalindrome4(x: Int): Boolean = {
    if (x < 0 || (x % 10 == 0 && x != 0)) return false

    var a = x
    var revertNumber = 0
    while (x > revertNumber) { // a从前往后,一直除; revert从后往前,一直乘; 两头同时进行; 最终跳出循环时,数字为原来的一半;
      revertNumber = revertNumber * 10 + (a % 10)
      a /= 10
    }

    x == revertNumber || x == revertNumber / 10
  }


  def main(args: Array[String]): Unit = {
    println(List(121, -121, 10, 132231).map(isPalindrome).mkString(","))
    println(List(121, -121, 10, 132231).map(isPalindrome2).mkString(","))
    println(List(121, -121, 10, 132231).map(isPalindrome3).mkString(","))
    println(List(121, -121, 10, 132231).map(isPalindrome4).mkString(","))
  }

}
