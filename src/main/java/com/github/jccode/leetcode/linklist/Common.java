package com.github.jccode.leetcode.linklist;

public class Common {

    public static void main(String[] args) {
        ListNode list = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))));
        System.out.println(list);
        Common q = new Common();
        System.out.println(q.findLastK(list, 1));
    }

    public ListNode findLastK(ListNode head, int k) {
        ListNode prev = head, post = head;
        int i = k;
        while (i-- > 0) {
            prev = prev.next;
        }
        while (prev.next != null) {
            prev = prev.next;
            post = post.next;
        }
        return post;
    }
}
