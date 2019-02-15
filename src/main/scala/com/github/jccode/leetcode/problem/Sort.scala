package com.github.jccode.leetcode.problem

/**
  * Sort
  *
  * @author 01372461
  */
object Sort {

  /**
    * 归并排序  时间复杂度: O(n*log(n))
    *
    * 归并排序是建立在归并操作是一个有效的排序算法上.
    * 归并操作是指将两个已排序的数组合并成一个排序的数组.
    *
    * 这样,归并排序的递归算法可以描述为:
    * 1. 一个数组可以等分为两个子数组
    * 2. 每个子数组本身分别排序
    * 3. 最后把两个已排序的子数组进行归并操作.
    * 4. 典型的分治法,且每层分治可以进行.
    *
    *
    * 下面的实现只是体现思路,由于还用了slice方法,并不高效.
    *
    * @param nums
    * @return
    */
  def mergeSortBad(nums: Array[Int]): Array[Int] = {

    // 注意:基本情况; 归并排序会把数组拆成两个,基本情况包括两种:0个元素和1个元素.
    if (nums.isEmpty) return Array.empty
    if (nums.length == 1) return nums

    val middle = nums.length / 2
    val left = mergeSortBad(nums.slice(0, middle))
    val right = mergeSortBad(nums.slice(middle, nums.length))

    // merge 2 sorted array

    MergeTwoSortedArray.mergeTwoSortedArray2(left, right)
  }


  def mergeSort(nums: Array[Int]): Array[Int] = {
    ???
  }



  /**
    * 快排  时间复杂度: O(n*log(n))
    *
    * 分治法, 递归实现.
    *
    * 1. 从数组中任意取一个值(取如: quickSort中取第1个, quickSort取中间一个, 不影响结果)
    * 2. 则整个数组(除掉这个数本身),可以分成两个子数组(小于这个数的,记为left; 大于这个数的,记为right)
    * 3. 每个小的数组可以按照这种分法递归下去.
    * 4. 最后合并,就是把小的(left)放在最左边,然后是元素自身,把大的(right)放在最右边.
    *
    * @param nums
    * @return
    */
  def quickSort(nums: Array[Int]): Array[Int] = {
    nums match {
      case Array() => Array.empty
      case Array(head, tail @ _*) =>
        val left = quickSort((for (x <- tail if x <= head) yield x).toArray)
        val right = quickSort((for (x <- tail if x > head) yield x).toArray)
        left ++ Array(head) ++ right
    }
  }

  /**
    * 取数组中的任意一个值(记为n),下面的实现取数组中间的元素.
    *
    * 一个快排,可以看成整个数组是由,
    * 一个小于n的已经排序的数组 + 等于n的已经排好序的数组 + 大于n的已经排好序的数组.
    *
    * @param nums
    * @return
    */
  def quickSort2(nums: Array[Int]): Array[Int] = {
    if (nums.isEmpty) return Array.empty
    val pivot = nums(nums.length / 2)
    quickSort2(nums.filter(_ < pivot)) ++ nums.filter(_ == pivot) ++ quickSort2(nums.filter(_ > pivot))
  }


  /**
    * 冒泡  时间复杂度: O(n*n)
    *
    * 1. 从前往后,拿当值与后面的所有值,依次比较,
    * 2. 如果当前值比后面的值大,就说明它应该排在后面,跟它交换位置,一直比到最后一个.
    * 3. 接下来第2个元素,依此类推.
    * 4. 一直到最后一个,则整个数组是已排序的数组.
    *
    * @param nums
    * @return
    */
  def bubbleSort(nums: Array[Int]): Array[Int] = {
    val result = nums.clone()

    val swap = (i: Int, j: Int) => {
      val t = result(i)
      result(i) = result(j)
      result(j) = t
    }

    for (i <- result.indices) {
      for (j <- i until result.length) {
        if (result(i) > result(j)) {
          swap(i, j)
        }
      }
    }

    result
  }

  def main(args: Array[String]): Unit = {
    val r = scala.util.Random
    val randomArr = (for (i <- 1 to 20) yield r.nextInt(50)).toArray
    val sampleArr = Array(1,5,3,8,7,10,2)
    val arr = sampleArr

    println("Origin: \t\t" + arr.mkString(","))
    println("Standard: \t\t" + arr.sorted.mkString(","))
    println("BubbleSort: \t" + bubbleSort(arr).mkString(","))
    println("QuickSort: \t\t" + quickSort(arr).mkString(","))
    println("QuickSort2: \t" + quickSort2(arr).mkString(","))
    println("MergeSort: \t\t" + mergeSortBad(arr).mkString(","))
  }
}
