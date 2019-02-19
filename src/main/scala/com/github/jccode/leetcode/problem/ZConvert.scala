package com.github.jccode.leetcode.problem

/**
  * ZConvert
  *
  * @author 01372461
  */
object ZConvert {

  def zconvert(s: String, numRows: Int): String = {
    if (s.isEmpty) return ""
    if (numRows == 1) return s

    val cog = numRows + (numRows - 2)
    val nog = s.length / cog
    val mod = s.length % cog
    val nol = if (mod == 0) 0 else if (mod <= numRows) 1 else mod - numRows + 1
    val coeg = numRows - 1
    val toc = nog * coeg + nol

    var si = 0
    var r = new Array[Array[Char]](toc)
    for (c <- 0 until toc) {
      r(c) = new Array[Char](numRows)
      if (c % coeg == 0) { // 填满
        for (i <- r(c).indices) {
          if (si < s.length) {
            r(c)(i) = s(si)
            si += 1
          }
        }
      } else { // 只填一个
        r(c)(numRows - c%coeg -1) = s(si)
        si += 1
      }
    }

    // debug
    // println(r.map(_.mkString(",")).mkString(";"))

    // print
    var ret = new StringBuilder(s.length)
    for (i <- 0 until numRows) {
      for (j <- 0 until toc) {
        if (r(j)(i) != 0) {
          ret.append(r(j)(i))
        }
      }
    }
    ret.toString()
  }


  def zconvert2(s: String, numRows: Int): String = {
    if (s.isEmpty) return ""
    if (numRows == 1) return s

    var r = List.empty[StringBuilder]
    for (i <- 0 until numRows) {
      r = r :+ new StringBuilder
    }

    var curRow = 0
    var goingDown = false
    for (i <- s.indices) {
      r(curRow).append(s(i))
      if (curRow == 0 || curRow == numRows - 1) goingDown = !goingDown
      curRow += (if (goingDown) 1 else -1)
    }

    r.mkString("")
  }


  def main(args: Array[String]): Unit = {
    val s = "LEETCODEISHIRING"
    zconvert2(s, 3)

    assert(zconvert(s, 3) == "LCIRETOESIIGEDHN")
    assert(zconvert2(s, 3) == "LCIRETOESIIGEDHN")
    assert(zconvert(s, 4) == "LDREOEIIECIHNTSG")
    assert(zconvert2(s, 4) == "LDREOEIIECIHNTSG")

    val s2 = "PAYPALISHIRING"
    zconvert(s2, 3)

    val s3 = ""
    zconvert(s3, 1)
  }
}
