package chapter05;

public class BubbleSortTest {
    public static void main(String[] args) {
        /*
        排序算法的衡量标准：1.时间复杂度（最重要）2.空间复杂度3.稳定性
        Ο(1)＜Ο(log2n)＜Ο(n)＜Ο(nlog2n)＜Ο(n<sup>2</sup>)＜Ο(n<sup>3</sup>)＜…＜Ο(2<sup>n</sup>)＜Ο(n!)<O(n<sup>n</sup>)
        排序的分类：内部排序（内存中排序）；外部排序（外部存储设备+内存）
        排序算法：
        > 冒泡排序：时间复杂度：O(n^2)
        > 快速排序：开发中默认选择的排序方式；时间复杂度：O(nlogn)
         */


        /*
        案例：使用冒泡排序，实现整型数组元素的排序操作
        比如：int[] arr = new int[]{34,54,3,2,65,7,34,5,76,34,67};
         */
        int[] arr = new int[]{34, 54, 3, 2, 65, 7, 34, 5, 76, 34, 67};
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "\t");
        }

        //冒泡排序，实现数组元素从小到大排列
        for (int j = 0; j < arr.length; j++) {
            for (int i = 0; i < arr.length - 1 - j; i++) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                }
            }
        }

        System.out.println();
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "\t");
        }


    }
}
