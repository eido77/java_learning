package chapter06;

import java.util.Arrays;

public class MyArraysTest {
    /*
    案例：
    根据上一章数组中的常用算法操作，自定义一个操作int[]的工具类。
    涉及到的方法有：求最大值、最小值、总和、平均数、遍历数组、复制数组、数组反转、
                 数组排序(默认从小到大排序)、查找等
     */
    public static void main(String[] args) {
        MyArrays myArrays = new MyArrays();
        //写法A，不能再用，没起变量
        //myArrays.getMax(new int[]{1,2,3,4});
        //写法B，推荐
        int[] arr = new int[]{1,2,3,4};

        //最大值
        System.out.println("最大值为" + myArrays.getMax(arr)); // 4

        //平均
        System.out.println("平均值为" + myArrays.getAvg(arr)); // 2

        //遍历
        myArrays.print(arr);

        System.out.println();
        //查找
        int index = myArrays.binarySearch(arr, 2);
        if (index >= 0) {
            System.out.println("找到了，位置为" + index); // 1
        } else {
            System.out.println("没找到");
        }
        System.out.println(myArrays.linearSearch(arr, 3)); // 2
        //如果没找到，返回的是-(插入点) - 1
        System.out.println(myArrays.binarySearch(arr, 5)); // -5

        //排序
        int[] arr3 = new int[]{2, 3, 1, 4};
        myArrays.sort(arr3, "asc"); // [1, 2, 3, 4]
        myArrays.sort(arr3, "desc"); // [4, 3, 2, 1]
        //System.out.println(Arrays.toString(arr3));
        //用String类型接收
        String arr4 = Arrays.toString(arr3);
        System.out.println(arr4); // [1, 2, 3, 4]

    }

}
