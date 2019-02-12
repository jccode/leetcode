package com.github.jccode.leetcode.problem

object AddTwoNumbers {

  class ListNode(var _x: Int = 0) {
    var next: ListNode = null
    var x: Int = _x
    override def toString: String = x.toString + (if (next != null) s" -> $next" else "")
  }

  object ListNode {
    def apply(digit: Int) = new ListNode(digit)
  }


  def _add(l1: ListNode, l2: ListNode, cf: Boolean = false): (ListNode, Boolean) = {
    //def value(l: ListNode) = if (l != null) l.x else 0
    val value = (l: ListNode) => if (l != null) l.x else 0
    var s = value(l1) + value(l2)
    if (cf) s += 1
    (ListNode(s % 10), s >= 10)
  }

  def addTwoNumbers(l1: ListNode, l2: ListNode): ListNode = {
    var (result, cf) = _add(l1, l2)
    var (n1, n2) = (l1.next, l2.next)
    var ref = result
    while (n1 != null || n2 != null || cf) {
      val (tl, tcf) = _add(n1, n2, cf)
      ref.next = tl
      ref = tl
      cf = tcf
      if (n1 != null) n1 = n1.next
      if (n2 != null) n2 = n2.next
    }
    result
  }


  def createListNode(x: Int): ListNode = {
    if (x < 10) return ListNode(x)

    val result = ListNode(x % 10)
    var t = x / 10
    var ref = result
    while (t != 0) {
      ref.next = ListNode(t % 10)
      ref = ref.next
      t = t / 10
    }
    result
  }


  def addTwoNumbers2(l1: ListNode, l2: ListNode): ListNode = {
    val _x = (l: ListNode) => if (l == null) 0 else l.x

    val dummyHead = new ListNode(0)
    var (n1, n2) = (l1, l2)
    var carry = 0
    var ref: ListNode = dummyHead
    while (n1 != null || n2 != null) {
      var sum = _x(n1) + _x(n2)
      if (carry > 0) {
        sum += 1
      }
      ref.next = new ListNode(sum % 10)
      ref = ref.next
      carry = if (sum >= 10) 1 else 0
      if (n1 != null) n1 = n1.next
      if (n2 != null) n2 = n2.next
    }
    if (carry > 0) {
      ref.next = new ListNode(carry)
    }

    dummyHead.next
  }

  def main(args: Array[String]): Unit = {

    val l1 = createListNode(342)
    val l2 = createListNode(465)
    val l3 = addTwoNumbers(l1, l2)
    List(l1, l2, l3).foreach(println)

    val l4 = createListNode(5)
    val l5 = addTwoNumbers(l4, l4)
    List(l4, l5).foreach(println)
  }
}
