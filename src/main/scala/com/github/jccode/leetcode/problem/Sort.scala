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


  /**
    * 解决上面的slice问题
    *
    * @param nums
    * @return
    */
  def mergeSort(nums: Array[Int]): Array[Int] = {

    def mergeSortRecursive(arr: Array[Int], start: Int, end: Int): Array[Int] = {
      if (arr.isEmpty) return Array.empty

      // 基本条件(存在0个元素 和 1个元素 两种基本情形)
      if (start == end) return Array(arr(start))

      // 算法步骤
      val middle = (start + end) / 2

      val startLeft = start
      val endLeft = middle
      val startRight = middle+1
      val endRight = end

      val left = mergeSortRecursive(arr, startLeft, endLeft)
      val right = mergeSortRecursive(arr, startRight, endRight)


      // merge sorted array: left & right
      val len = end - start + 1
      val lenLeft = endLeft - startLeft + 1
      val lenRight = endRight - startRight + 1
      val result = new Array[Int](len)
      var (i, j, k) = (0, 0, 0)
      while (i < lenLeft && j < lenRight) {
        if (left(i) < right(j)) {
          result(k) = left(i)
          i += 1
        } else {
          result(k) = right(j)
          j += 1
        }
        k += 1
      }
      while (i < lenLeft) {
        result(k) = left(i)
        i += 1
        k += 1
      }
      while (j < lenRight) {
        result(k) = right(j)
        j += 1
        k += 1
      }
      result
    }

    mergeSortRecursive(nums, 0, nums.length-1)
  }


  def mergeSort2(nums: Array[Int]): Array[Int] = {

    /**
      * 这个方法由于会修改"输入的数组" arr. 使用时,最后clone一下.
      *
      * 这个版本的实现,空间上会省一点.
      * 参考: https://zh.wikipedia.org/zh-hans/%E5%BD%92%E5%B9%B6%E6%8E%92%E5%BA%8F
      *
      * @param arr
      * @param result
      * @param start
      * @param end
      */
    def mergeSortRecursive(arr: Array[Int], result: Array[Int], start: Int, end: Int): Unit = {
      // 基本条件(存在0个元素 和 1个元素 两种基本情形)
      if (start == end) {
        return
      }

      // 算法步骤
      val middle = (start + end) / 2

      val startLeft = start
      val endLeft = middle
      val startRight = middle+1
      val endRight = end

      mergeSortRecursive(arr, result, startLeft, endLeft)
      mergeSortRecursive(arr, result, startRight, endRight)

      // merge sorted array: left & right
      var (i, j, k) = (startLeft, startRight, start)
      while (i <= endLeft && j <= endRight) {
        if (arr(i) < arr(j)) {
          result(k) = arr(i)
          i += 1
        } else {
          result(k) = arr(j)
          j += 1
        }
        k += 1
      }
      while (i <= endLeft) {
        result(k) = arr(i)
        i += 1
        k += 1
      }
      while (j <= endRight) {
        result(k) = arr(j)
        j += 1
        k += 1
      }

      // 合并时,result归并的结果写入arr, 这样递归时,arr的值才能保存中间的计算结果
      var o = start
      while (o <= end) {
        arr(o) = result(o)
        o += 1
      }
    }

    val clone = nums.clone()
    val result = new Array[Int](nums.length)
    mergeSortRecursive(clone, result, 0, nums.length - 1)
    result
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
    println("MergeSortBad: \t" + mergeSortBad(arr).mkString(","))
    println("MergeSort: \t\t" + mergeSort(arr).mkString(","))
    println("MergeSort2: \t" + mergeSort2(arr).mkString(","))
    println("Origin: \t\t" + arr.mkString(","))
  }
}
