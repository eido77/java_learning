package chapter05;

public class ArraySumExer2 {
    public static void main(String[] args) {
        /*
        案例1：数组的扩容:
        现有数组 int[] arr = new int[]{1,2,3,4,5};
        现将数组长度扩容1倍，并将10,20,30三个数据添加到arr数组中，如何操作？
         */
        //没强调就是放到末尾
        int[] arr = new int[]{1,2,3,4,5};
        //扩容1倍容量
        //一旦初始化，数组长度就确定了，扩容的话新造一个
        int[] newArr = new int[arr.length * 2];
        //或 int[] newArr = new int[arr.length << 1];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        newArr[arr.length] = 10;
        newArr[arr.length + 1] = 20;
        newArr[arr.length + 2] = 30;
        // 让 arr 指向新数组，此后都指向同一个数组，两者效果一样；旧的5长度数组没人引用了会被GC回收
        arr = newArr;

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " "); // 1 2 3 4 5 10 20 30 0 0
        }

        /*
        案例：数组的缩容：
        现有数组 int[] arr={1,2,3,4,5,6,7}。现需删除数组中索引为4的元素。
         */
        int[] arr1 ={1,2,3,4,5,6,7};
        int deleteIndex = 4;
        //方式1：不新建数组，把 deleteIndex 后面的元素整体往前挪一位
        /*
        报错：ArrayIndexOutOfBoundsException: Index 7 out of bounds for length 7
        当 i = 6（最后一个索引）时，arr1[i + 1] 即 arr1[7] 超出范围
        for (int i = deleteIndex; i < arr1.length; i++) {
            arr1[i] = arr1[i + 1];
        }
         */
        for (int i = deleteIndex; i < arr1.length - 1; i++) {
            arr1[i] = arr1[i + 1];
        }

        //修改最后一个元素，设置默认值
        //如果不加，1 2 3 4 6 7 7
        // arr1.length 始终是 7，不是 6。
        arr1[arr1.length - 1] = 0;
        // 1 2 3 4 6 7 0
        //方式2：新建数组，新的数组长度比原有数组少一个
        int[] newArr1 = new int[arr1.length - 1];
        for (int i = 0; i < deleteIndex; i++) {
            newArr1[i] = arr1[i];
        }
        for (int i = deleteIndex; i < arr1.length - 1; i++) {
            newArr1[i] = arr1[i + 1];
        }
        arr1 = newArr1;
        // 1 2 3 4 6 7

        System.out.println();
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + " ");
        }

    }
}
