package chapter05;

public class LinearBinarySearchTest {
    public static void main(String[] args) {
        /*
        案例1：线性查找
        定义数组：int[] arr1 = new int[]{34,54,3,2,65,7,34,5,76,34,67};
        查找元素5是否在上述数组中出现过？如果出现，输出对应的索引值。
         */
        int[] arr1 = new int[]{34, 54, 3, 2, 65, 7, 34, 5, 76, 34, 67};
        int target = 5;
        //查找方式1 ：线性查找，执行时间复杂度0(N)
        /*
        boolean flag = true;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] == target) {
                System.out.println("找到了" + target + "，对应的位置为：" + i); // 7
                flag = false;
                break;
            }
        }
        if (flag) {
            System.out.println("未找到");
        }
         */
        //方式2
        int i = 0;
        for (; i < arr1.length; i++) {
            if (arr1[i] == target) {
                System.out.println("找到了" + target + "，对应的位置为：" + i); // 7
                break;
            }
        }
        if (i == arr1.length) {
            System.out.println("未找到");
        }

        /*
        案例2：二分法查找，执行时间复杂度0(logN)
        数组必须有序
        定义数组：int[] arr2 = new int[]{2,4,5,8,12,15,19,26,37,49,51,66,89,100};
        查找元素5是否在上述数组中出现过？如果出现，输出对应的索引值。
         */
        int[] arr2 = new int[]{2,4,5,8,12,15,19,26,37,49,51,66,89,100};
        int target2 = 5;
        int head = 0; // 默认首索引
        int end = arr2.length - 1; // 默认尾索引
        boolean isFlag = false;
        while (head <= end) {
            int mid = (head + end) / 2;
            if (target2 == arr2[mid]) {
                System.out.println("找到了，对应的位置为" + mid);
                isFlag = true;
                break;
            } else if (target2 > arr2[mid]) {
                head = mid + 1;
            } else { // target2 < arr2[mid]
                end = mid - 1;
            }
        }
        if (!isFlag) {
            System.out.println("未找到");
        }







    }
}
