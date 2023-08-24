package com.github.jccode.leetcode.linklist;

public class Q61 {

    public static void main(String[] args) {
        ListNode list = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))));
        System.out.println(list);
        Q61 q = new Q61();
        System.out.println(q.rotateRight(list, 5));
    }

    public ListNode rotateRight(ListNode head, int k) {
        // 如果k大于链表的长度，需要取模做兼容
        int n = 0;
        ListNode p = head;
        while (p != null) {
            p = p.next;
            n++;
        }
        if (k > n) {
            k = k % n;
        }

        // 找到最后第k个节点，将链表分成了两段, 后一段挪到最前面，前一段拼到最后面;
        ListNode lastK = head, lastK_1 = head, last = head;

        // 使用快慢指针找出 lastK, lastK_1, last;
        ListNode fast = head, slow = head;
        while (k-- > 0) {
            fast = fast.next;
        }
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        lastK_1 = slow;
        lastK = lastK_1.next;
        last = fast;

        // 交换
        ListNode dummyHead = new ListNode(-1);
        dummyHead.next = lastK;
        lastK_1.next = null;
        last.next = head; // 最后一个节点要指回首节点

        return dummyHead.next;
    }

    public ListNode findLastK(ListNode head, int k) {
        return null;
    }
}
