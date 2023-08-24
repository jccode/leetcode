package com.github.jccode.leetcode;

import java.util.Arrays;

public class Q55 {
    public static void main(String[] args) {
        Greedy g = new Greedy();
        System.out.println(g.canJump(new int[]{2, 5, 0, 0}));
    }

    /**
     * 贪心算法
     * 基于：如果x是一个可到达的位置，则对于任意一个y (y <= x+nums[x]) 也一定是可到达的位置。
     * 因此，只需要维护一个最远可到达的位置，最远可到达的位置大于数组的最后1位所在位置，则最终的结果可达。g
     */
    static class Greedy {
        public boolean canJump(int[] nums) {
            int n = nums.length;
            if (n == 0) return false;
            if (n == 1) return true;
            int maxPos = 0; // 记录从开始出发，能到达的最远的位置
            for (int i = 0; i < n; i++) {
                // 当前下标必须是最远距离内，才能计算更新最大距离；
                if (i <= maxPos) {
                    maxPos = Math.max(maxPos, i + nums[i]);
                    if (maxPos >= n-1) {
                        return true;
                    }
                }
                // 如果超出了，说明中间有位置断开了。这种情况不能更新最大距离
            }
            return false;
        }
    }

    /**
     * 无脑回溯；
     * 每一种情况都尝试一下；
     * 但是在数组超长的情况下，会有栈溢出，这个算法不通过。
     */
    static class Backtrace {
        public static boolean canJump(int[] nums) {
            if (nums.length == 0) return false;
            if (nums.length == 1) return true;

            int[] path = new int[nums.length];
            boolean result = jump(nums, 0, path);
            System.out.println(Arrays.toString(path));
            return result;
        }

        public static boolean jump(int[] nums, int i, int[] path) {
            if (i == nums.length-1) return true;
            if (path[i] > 0) return false;
            for (int j = 1; j <= nums[i]; j++) {
                path[i] = j;
                if(jump(nums, (i+j)%nums.length, path)) {
                    return true;
                }
                path[i] = 0;
            }
            return false;
        }
    }
}
