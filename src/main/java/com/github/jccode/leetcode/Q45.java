package com.github.jccode.leetcode;

public class Q45 {
    public static void main(String[] args) {
        Solution2 t = new Solution2();
        System.out.println(t.jump(new int[]{1,2,3}));
        System.out.println(t.jump(new int[]{2,3,1,1,4}));
        System.out.println(t.jump(new int[]{2,3,0,1,4}));
        System.out.println(t.jump(new int[]{7,0,9,6,9,6,1,7,9,0,1,2,9,0,3}));
        System.out.println(t.jump(new int[]{2,3,1,1,4}));
    }

    /**
     * 官方的做法;
     * 在一个循环里，通过end边界引入，完成每一轮的计算;
     */
    static class Solution2 {

        public int jump(int[] nums) {
            int n = nums.length;
            int step = 0; // 跳转的步数
            int end = 0; // 下次的最右起跳点;
            int maxPos = 0; // 能到达的最大位置;
            // 数组最后一位n-1，不需要执行for内部的运算，因为已经是终点，不需要step++.
            for (int i = 0; i < n - 1; i++) {
                maxPos = Math.max(maxPos, i + nums[i]);
                // 到达上次的最右起跳点;
                if (i == end) {
                    end = maxPos; // 更新最右起跳点;
                    step++; // 开始下一跳;
                }
            }
            return step;
        }
    }

    /**
     * 分段计算，对于当前下标x，计算出能到达的最远距离，作为下一跳的位置（贪心）。
     * 不停地更新下一跳的位置找到最短路径。
     */
    static class Solution1 {

        public int jump(int[] nums) {
            int n = nums.length;
            int i = 0;
            int step = 0; // 起跳次数;
            // 如果到达n-1，就已经到末尾了。不需要再跳，也不需要增加起跳次数;
            while (i < n-1) {
                // 计算最优下一跳，并一直往下跳
                int next = nextJump(i, nums);
                // 如果下一跳位置原地不动，说明遇到0，跳不下去了。直接返回.
                if (i == next) {
                    return 0;
                }
                else {
                    i = next;
                    step++;
                }
            }
            return step;
        }

        /**
         * 计算以x为起点，下一跳的点；
         * 这个点必须落在 [x, x+nums[x]] 范围内，且使得跳出的距离最远。
         *
         * @param x
         * @param nums
         * @return
         */
        private int nextJump(int x, int[] nums) {
            int n = nums.length;
            int jump = x;
            int maxPos = 0;
            // 从[x, nums[x]]中找到t,使得跳出距离最远;
            for (int t = x; t <= x + nums[x]; t++) {
                if (t >= n - 1) {
                    return t;
                }
                // 这里要注意t的越界判断，为前面的判断逻辑;
                int reachable = t + nums[t];
                if (reachable > maxPos) {
                    maxPos = reachable;
                    jump = t;
                }
            }
            return jump;
        }
    }


}
