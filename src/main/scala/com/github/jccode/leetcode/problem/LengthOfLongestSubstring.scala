package com.github.jccode.leetcode.problem

/**
  * LengthOfLongestSubstring
  *
  *
  * @author 01372461
  */
object LengthOfLongestSubstring {

  /**
    *
    * 1. 一个循环,从前往后走.发现如果有重复,说明当前是一个不重复的子串,跳出;
    *    用一个变量记录当前的最长子串的值, 如果每次跳出时,长度比上一个长,就替换.
    *    一直走完就OK了.
    *
    * 2. 如何判断是否存在重复.一个哈稀表就可以解决.当遇到重复(假设为c),不能全部清空.
    *    遇到重复,表示以当前重复字符(c)开头的最长子串;
    *    因此,要做的是,清掉在c前边的所有子串,然后继续往后走.
    *
    *    例如:
    *    如果全部清空,则 dvdf 得出的最长子串是2. 而实际应该是 3.
    *
    * 3. 别忘了循环跳出来后,哈稀表不为空,此时,需要再做一次判断.
    *
    * @param s
    * @return
    */
  def lengthOfLongestSubstring(s: String): Int = {
    import scala.collection.mutable
    if (s == null || s.length == 0) return 0

    val ss = s.toCharArray
    var result = 0
    var map = mutable.Map.empty[Char, Int]
    for (i <- 0 until ss.length) {
      if (map.contains(ss(i))) {
        val len = map.size

        // reset map
        val idx = map(ss(i))
        map = mutable.Map.empty
        for (j <- idx until i) {
          map += (ss(j) -> j)
        }

        if (len > result) {
          result = len
        }
      }
      map += (ss(i) -> i)
    }
    if (map.size > result) {
      result = map.size
    }
    result
  }


  /**
    * 上面在 reset map 这一部,需要一个循环,这一块是可以优化的.
    * reset map 的想法,是为了把当前重复的字符串,前边的子串给清掉.
    * 这样,每次取到map的size就是当前不重复子串的长度.
    *
    * 其实,我们换个思路,就不需要清空了.
    * 我们只需要记住最左边的位置(left),和当前下标的位置,这两个下标一减就可以得到子串的长度.
    *
    * 当遇到重复的字符(记为:c)时:
    * 1. 计算长度,更新长度(如果比原有累计的长度还长)
    * 2. 更新左侧的位置.正常这个值为重复字符的下一位 idx(c)+1.
    *    但要考虑一种情况是(像abba), left的值可能比"idx(c)+1"还大,这种情况下,不能更新下标.
    *
    * @param s
    * @return
    */
  def lengthOfLongestSubstring2(s: String): Int = {
    import scala.collection.mutable
    if (s == null || s.length == 0) return 0

    val ss = s.toCharArray
    var result = 0
    var left = 0
    var map = mutable.Map.empty[Char, Int]
    for (i <- 0 until ss.length) {
      if (map.contains(ss(i))) {
        val idx = map(ss(i))
        val len = i - left

        if (idx + 1 > left) {
          left = idx+1
        }
        map -= ss(i)

        if (len > result) {
          result = len
        }
      }
      map += (ss(i) -> i)
    }

    val len = ss.length - left
    if (len > result) {
      result = len
    }

    result
  }


  /**
    * 滑动窗口解法.
    *
    * 滑动窗口 [i, j), 向右滑动.
    *
    * @param s
    * @return
    */
  def lengthOfLongestSubstring3(s: String): Int = {
    import scala.collection.mutable
    var (i, j, result) = (0, 0, 0)   // 滑动窗口下标
    val set = mutable.Set.empty[Int]  // 存放滑动窗口中的元素
    while (i < s.length && j < s.length) {
      if (!set.contains(s(j))) {
        set += s(j)
        result = Math.max(result, j - i + 1)
        j += 1
      } else {
        set -= s(i)
        i += 1
      }
    }

    result
  }


  /**
    * 优化的滑动窗口. [i,j)
    *
    * i 不需要每次一位位地移.
    * 使用map记录重复数据的下标, 发现重复, 取出重复元素的下标, 跳到其下一位.
    *
    * @param s
    * @return
    */
  def lengthOfLongestSubstring4(s: String): Int = {
    import scala.collection.mutable
    var (i, j, result) = (0, 0, 0)
    val map = mutable.Map.empty[Char, Int]
    while (i < s.length && j < s.length) {
      if (!map.contains(s(j))) {
        map += (s(j) -> j)
      } else {
        i = Math.max(map(s(j)) + 1, i) // 这个地方,有可能取出来的下标比当前i的值还小,这种情况下,不能往回跳.
        map(s(j)) = j
      }
      j += 1
      result = Math.max(result, j - i)
    }
    result
  }


  def main(args: Array[String]): Unit = {
    lengthOfLongestSubstring4("tmmzuxt")

    val samples = List("abcabcbb", "bbbbb", "pwwkew", " ", "  ", "a", "aa", "dvdf", "abba", "bbtablud", "tmmzuxt")
    samples.map(lengthOfLongestSubstring).foreach(print)
    println()
    samples.map(lengthOfLongestSubstring2).foreach(print)
    println()
    samples.map(lengthOfLongestSubstring3).foreach(print)
    println()
    samples.map(lengthOfLongestSubstring4).foreach(print)
  }
}
