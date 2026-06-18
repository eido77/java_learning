package chapter06;

import java.util.Arrays;

public class MyArrays {
    /*
    案例：
    根据上一章数组中的常用算法操作，自定义一个操作int[]的工具类。
    涉及到的方法有：求最大值、最小值、总和、平均数、遍历数组、复制数组、数组反转、
                 数组排序(默认从小到大排序)、查找等
     */
    /**
     * 获取int[]数组最大值
     * @param arr 获取最大值的数组
     * @return 数组的最大值
     */
    public int getMax(int[] arr){
        int max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     * 获取int[]数组最小值
     * @param arr 获取最小值的数组
     * @return 数组的最小值
     */
    public int getMin(int[] arr){
        int min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min){
                min = arr[i];
            }
        }
        return min;
    }

    public int getSum(int[] arr){
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    public int getAvg(int[] arr){
        //类内部调用不用 new和 .
        return getSum(arr) / arr.length;
    }

    //数组的遍历
    public void print(int[] arr){
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            //如果没有i == 0的话，会多一个“,”
            if (i == 0) {
                System.out.print(arr[i]);
            } else {
                System.out.print(", " + arr[i]);
            }
        }
        System.out.print("]");
    }

    //复制数组
    public int[] copy(int[] arr){
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }

    //数组的反转
    public void reverse(int[] arr){
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    //数组的排序
    public void sort(int[] arr){
        //方式1
        Arrays.sort(arr);
        //方式2：冒泡排序
        /*
        for (int j = 0; j < arr.length; j++) {
            for (int i = 0; i < arr.length - 1 - j; i++) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                }
            }
        }
         */
    }

    //查找
    //方法1
    public int binarySearch(int[] arr, int target){
        // return Arrays.binarySearch(arr, target);
        int index = Arrays.binarySearch(arr, target);
        //如果没找到，返回的是-(插入点) - 1
        return index;
    }
    //方法2
    public int linearSearch(int[] arr, int target){
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }


}
