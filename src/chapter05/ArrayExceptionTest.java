package chapter05;

public class ArrayExceptionTest {
    public static void main(String[] args) {
        /*
        1. 数组的使用中常见的异常小结
        > 数组角标越界的异常：ArrayIndexOutOfBoundsException
        > 空指针的异常：NullPointerException
        2. 出现异常会怎样？如何处理？
        > 一旦程序执行中出现了异常，程序就会终止执行。
        > 针对异常提供的信息，修改对应的代码，避免异常再次出现。
         */

        // 1. 数组角标越界的异常：
        int[] arr = new int[10];
        //角标有效范围：0, 1, 2, ... 9
        //System.out.println(arr[10]); // ArrayIndexOutOfBoundsException
        //System.out.println(arr[-1]); // ArrayIndexOutOfBoundsException

        // 2. 空指针异常：
        //情况1
        int[] arr1 = new int[10];
        System.out.println(arr1[0]); // 0
        arr1 = null;
        //System.out.println(arr1[0]); // NullPointerException
        //情况2
        int[][] arr2 = new int[3][2];
        System.out.println(arr2[0][1]); // 0

        int[][] arr3 = new int[3][];
        //System.out.println(arr3[0][1]); // NullPointerException
        arr3[0] = new int[10];
        System.out.println(arr3[0][1]); // 0

        //情况3
        String[] arr4 = new String[4];
        System.out.println(arr4[0]); // null
        //System.out.println(arr4[0].toString()); // NullPointerException


    }
}
