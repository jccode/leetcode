package com.github.jccode.leetcode.linklist;

public class Q92 {
    public static void main(String[] args) {
        Q92 q = new Q92();
        ListNode list = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        System.out.println(list);
        System.out.println(q.reverseBetween(list, 2, 4));
    }

    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || head.next == null || left == right) return head;
        ListNode pLeft, pRight, pLeftPrev, pRightSucc;

        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        pLeftPrev = dummy;
        pLeft = head;

        // 定位pLeft, pLeftPrev
        int i = left;
        while(i-- > 1) {
            pLeft = pLeft.next;
            pLeftPrev = pLeftPrev.next;
        }

        // 定位pRight
        int s = right - left;
        pRight = pLeft;
        while(s-- > 0 && pRight.next != null) {
            pRight = pRight.next;
        }
        pRightSucc = pRight.next;

        // 断开
        pLeftPrev.next = null;
        pRight.next = null;

        // 反转
        reverse(pLeft, pRight);

        // 连接
        pLeftPrev.next = pRight;
        pLeft.next = pRightSucc;

        return dummy.next;
    }

    private void reverse(ListNode left, ListNode right) {
        ListNode prev = left, curr = left.next, t;
        while (prev != right) {
            t = curr.next;
            curr.next = prev;
            prev = curr;
            curr = t;
        }
    }
}
