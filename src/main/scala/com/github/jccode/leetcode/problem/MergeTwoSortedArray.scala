package com.github.jccode.leetcode.problem

/**
  * MergeTwoSortedArray
  *
  * @author 01372461
  */
object MergeTwoSortedArray {

  /**
    * 合并两个已排序的数组
    *
    * 1. 申请空间，使其大小为两个已经排序序列之和，该空间用来存放合并后的序列
    * 2. 设定两个指针，最初位置分别为两个已经排序序列的起始位置
    * 3. 比较两个指针所指向的元素，选择相对小的元素放入到合并空间，并移动指针到下一位置
    * 4. 重复步骤3直到某一指针到达序列尾
    * 5. 将另一序列剩下的所有元素直接复制到合并序列尾
    *
    * 参考: https://zh.wikipedia.org/zh-hans/%E5%BD%92%E5%B9%B6%E6%8E%92%E5%BA%8F
    *
    * @param arr1
    * @param arr2
    * @return
    */
  def mergeTwoSortedArray(arr1: Array[Int], arr2: Array[Int]): Array[Int] = {
    val result = new Array[Int](arr1.length + arr2.length)
    var (i, j, k) = (0, 0, 0)
    while (i < arr1.length && j < arr2.length) {
      if (arr1(i) < arr2(j)) {
        result(k) = arr1(i)
        i += 1
      } else {
        result(k) = arr2(j)
        j += 1
      }
      k += 1
    }

    if (i == arr1.length) {
      while (k < result.length) {
        result(k) = arr2(j)
        k += 1
        j += 1
      }
    } else {
      while (k < result.length) {
        result(k) = arr1(i)
        k += 1
        i += 1
      }
    }

    result
  }


  /**
    * merge1 变种; 可以省略到后面两个 if 判断;
    *
    * @param arr1
    * @param arr2
    * @return
    */
  def mergeTwoSortedArray2(arr1: Array[Int], arr2: Array[Int]): Array[Int] = {
    val result = new Array[Int](arr1.length + arr2.length)
    var (i, j, k) = (0, 0, 0)
    while (i < arr1.length && j < arr2.length) {
      if (arr1(i) < arr2(j)) {
        result(k) = arr1(i)
        i += 1
      } else {
        result(k) = arr2(j)
        j += 1
      }
      k += 1
    }

    while (i < arr1.length) {
      result(k) = arr1(i)
      i += 1
      k += 1
    }

    while (j < arr2.length) {
      result(k) = arr2(j)
      j += 1
      k += 1
    }

    result
  }


  /**
    * 函数式 递归版本
    *
    * @param arr1
    * @param arr2
    * @return
    */
  def mergeTwoSortedArrayFpRecursive(arr1: Array[Int], arr2: Array[Int]): Array[Int] = {
    (arr1, arr2) match {
      case (Array(), right) => right
      case (left, Array()) => left
      case (Array(h1, t1 @ _*), Array(h2, t2 @ _*)) =>
        if (h1 < h2) Array(h1) ++ mergeTwoSortedArrayFpRecursive(t1.toArray, arr2)
        else Array(h2) ++ mergeTwoSortedArrayFpRecursive(arr1, t2.toArray)
    }
  }

  def mergeTwoSortedArrayFpRecursive(list1: List[Int], list2: List[Int]): List[Int] = {
    (list1, list2) match {
      case (Nil, right) => right
      case (left, Nil) => left
      case (lHead::lTail, rHead::rTail) =>
        if (lHead < rHead) lHead :: mergeTwoSortedArrayFpRecursive(lTail, list2)
        else rHead :: mergeTwoSortedArrayFpRecursive(list1, rTail)
    }
  }


  def main(args: Array[String]): Unit = {
    val arr1 = Array(2,5,8,9,14,16)
    val arr2 = Array(3,5,7,11)
    println(mergeTwoSortedArray(arr1, arr2).mkString(","))
    println(mergeTwoSortedArray2(arr1, arr2).mkString(","))
    println(mergeTwoSortedArrayFpRecursive(arr1, arr2).mkString(","))
    println(mergeTwoSortedArrayFpRecursive(arr1.toList, arr2.toList).mkString(","))
  }
}
