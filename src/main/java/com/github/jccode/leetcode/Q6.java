package com.github.jccode.leetcode;

import scala.Char;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Q6 {

    public static void main(String[] args) {
        String s = "PAYPALISHIRING";
        int numRows = 3;
        Solution2 q = new Solution2();
        System.out.println(q.convert(s, numRows));
    }

    /**
     * 使用多个数组来存
     */
    static class Solution2 {
        public String convert(String s, int numRows) {
            if (numRows <= 1) return s;
            List<StringBuilder> result = new ArrayList<>(numRows);
            for (int i = 0; i < numRows; i++) {
                result.add(new StringBuilder());
            }

            char[] nums = s.toCharArray();
            int n = nums.length;

            /*
            int idx = 0, x = 0;
            while (idx < n) {
                while (x < numRows && idx < n) {
                    result.get(x++).append(nums[idx++]);
                }
                x -= 2;
                while (x >= 0 && idx < n) {
                    result.get(x--).append(nums[idx++]);
                }
                x += 2;
            }
             */

            int i = 0, x = 0, flag = 1;
            while (i < n) {
                result.get(x+flag).append(nums[i++]);
                if (x == 0) {
                    flag = 1;
                } else if (x == numRows-1) {
                    flag = -1;
                }
            }

            StringBuilder sb = new StringBuilder();
            for (StringBuilder item : result) {
                sb.append(item);
            }
            return sb.toString();
        }
    }

    /**
     * 使用二维数组存临时结果;
     */
    static class Solution1 {
        public String convert(String s, int numRows) {
            if (numRows <= 1) return s;
            char[] nums = s.toCharArray();
            int n = nums.length;
            char[][] m = new char[numRows][n];
            int i = 0, x = 0, y = 0;
            while (i < n) {
                while (x < numRows && i < n) {
                    m[x++][y] = nums[i++];
                }
                x-=2;
                y++;
                while (x >= 0 && i < n) {
                    m[x--][y++] = nums[i++];
                }
                x+=2;
                y--;
            }

            for (char[] m0 : m) {
                System.out.println(Arrays.toString(m0));
            }
            StringBuilder sb = new StringBuilder();
            for (char[] m0 : m) {
                for (char j : m0) {
                    if (j != 0) {
                        sb.append(j);
                    }
                }
            }
            return sb.toString();
        }
    }


}
