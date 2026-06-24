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
     *
     * @param arr 获取最大值的数组
     * @return 数组的最大值
     */
    public int getMax(int[] arr) {
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
     *
     * @param arr 获取最小值的数组
     * @return 数组的最小值
     */
    public int getMin(int[] arr) {
        int min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    public int getSum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    public int getAvg(int[] arr) {
        //类内部调用不用 new和 .
        return getSum(arr) / arr.length;
    }

    //数组的遍历
    public void print(int[] arr) {
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
    public int[] copy(int[] arr) {
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }

    //数组的反转
    public void reverse(int[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    //数组的排序
    public void sort(int[] arr) {
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

    /**
     * 针对于数组进行排序操作
     *
     * @param arr        待排序的数组
     * @param sortMethod asc:升序（ascend） desc:降序
     */
    public void sort(int[] arr, String sortMethod) {
        // 数组排序，可以指明排序的方式（从小到大、从大到小）

        //这种写法如果sortMethod是null的话会报错
//        if (sortMethod.equals("asc")) {
        // 用字符串常量 "asc" 调用 equals，而不是 sortMethod.equals("asc")
        // 因为 sortMethod 可能为 null，null.equals(...) 会抛 NullPointerException
        // 而 "asc" 是常量，永远不为 null，调用 equals 一定安全
        if ("asc".equals(sortMethod)) { // 安全的写法
            for (int j = 0; j < arr.length; j++) {
                for (int i = 0; i < arr.length - 1 - j; i++) {
                    if (arr[i] > arr[i + 1]) {
//                        int temp = arr[i];
//                        arr[i] = arr[i + 1];
//                        arr[i + 1] = temp;

                        //错误的
//                        swap(arr[i], arr[i + 1]);
                        swap(arr, i, i + 1);
                    }
                }
            }
            //这种写法如果sortMethod是null的话会报错
//        } else if (sortMethod.equals("desc")) {
            // 同理，常量在前可避免 sortMethod 为 null 时报错
        } else if ("desc".equals(sortMethod)) { // 安全的写法
            for (int j = 0; j < arr.length; j++) {
                for (int i = 0; i < arr.length - 1 - j; i++) {
                    if (arr[i] < arr[i + 1]) {
//                        int temp = arr[i];
//                        arr[i] = arr[i + 1];
//                        arr[i + 1] = temp;

                        //错误的
                        // 错误:swap(arr[i], arr[i+1]) 传的是两个 int 值的副本,
                        // 方法内交换的是局部参数,影响不到原数组
//                        swap(arr[i], arr[i + 1]);

                        // 正确:传数组引用 + 下标,方法内通过引用改的就是同一个数组
                        swap(arr, i, i + 1);
                    }
                }
            }
        } else {
            System.out.println("您输入的排序方式有误");
        }
    }

    //错误的
    // 错误写法:int 是基本类型,值传递。i、j 只是实参的副本,
    // 交换副本对调用方没有任何影响,排序失效。
//    public void swap(int i, int j) {
//        int temp = i;
//        i = j;
//        j = temp;
//    }
    // 正确写法:数组是引用类型,传进来的是引用(地址)的副本
    // 但它和原引用指向同一个数组对象,所以通过下标修改 arr[i]、arr[j] 会真正改到原数组。
    public void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //查找
    //方法1
    public int binarySearch(int[] arr, int target) {
        // return Arrays.binarySearch(arr, target);
        int index = Arrays.binarySearch(arr, target);
        //如果没找到，返回的是-(插入点) - 1
        return index;
    }

    //方法2
    public int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }


}
