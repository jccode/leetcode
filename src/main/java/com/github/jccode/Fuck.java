package com.github.jccode;

import java.util.Arrays;

public class Fuck {
    public static void main(String[] args) {
        Fuck f = new Fuck();
        System.out.println(f.isStraight(new int[]{0, 0, 2, 2, 5}));
    }

    public boolean isStraight(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int zeroCount = 0;
        int prev = -1;
        for(int i=0; i<n; i++) {
            if (nums[i] == 0) {
                zeroCount++;
                continue;
            }
            if (prev == -1) {
                prev = nums[i];
                continue;
            }
            int curr = nums[i];
            if (curr == prev+1) {
                continue;
            } else if (zeroCount-- > 0) {
                prev = prev+1;
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
