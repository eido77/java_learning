package chapter05;

import java.util.Arrays;

public class ArraysTest {
    public static void main(String[] args) {
        /*
        数组工具类Arrays的使用 (熟悉)
        1. Arrays类所在位置: 处在java.util包下
        2. 作用：
        java.util.Arrays类即为操作数组的工具类，包含了用来操作数组（比如排序和搜索）的各种方法。
         */
        //1. boolean equals(int[] a,int[] b)：比较两个数组的元素是否依次相等
        int[] arr1 = new int[]{1, 2, 3, 4, 5};
        int[] arr2 = new int[]{1, 2, 3, 4, 5};

        // arr1 == arr2 比较的是引用地址，看是不是同一个对象
        System.out.println(arr1 == arr2); // false
        // Arrays.equals(arr1, arr2) 比较的是元素内容，按顺序逐个比元素
        boolean isEquals = Arrays.equals(arr1, arr2);
        System.out.println(isEquals); // true
        arr2 = new int[]{1, 2, 3, 5, 4};

        boolean isEquals1 = Arrays.equals(arr1, arr2);
        System.out.println(isEquals1); // false

        //2. String toString(int[] a):输出数组元素信息。
        System.out.println(arr1); // [I@3fee733d
        // [ ] 是 Arrays.toString 为了好看而加的包裹符号
        System.out.println(Arrays.toString(arr1)); // [1, 2, 3, 4, 5]

        //3.void fill(int[] a,int val):将指定值填充到数组之中。
        //方法名/方法调用与它的左括号之间不留空格
        // a：要填充的数组；val：要填充的值（value 缩写，参数名而已）
        // fill 是在原数组上修改，没有 new 新数组，所以地址不变（还是同一个对象），只是内容变了
        Arrays.fill(arr1, 10);
        System.out.println(arr1); // [I@3fee733d
        System.out.println(Arrays.toString(arr1)); // [10, 10, 10, 10, 10]
        Arrays.fill(arr1, 1, 4, 1);
        System.out.println(Arrays.toString(arr1)); // [10, 1, 1, 1, 10]

        //4. void sort(int[] a):使用快速排序算法实现的排序
        int[] arr = new int[]{34, 54, 3, 2, 65, 7, 34, 5, 76, 34, 67};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr)); // [2, 3, 5, 7, 34, 34, 34, 54, 65, 67, 76]

        //5. int binarySearch(int[] a,int key):二分查找
        //使用前提：当前数组必须是有序的
        //如果是无序的，会给一个具体的错误数字
        int index = Arrays.binarySearch(arr, 5);
        //只要返回的是负数，就是没找到
        System.out.println(index); // 2


    }
}
