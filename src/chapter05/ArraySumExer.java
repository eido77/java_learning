package chapter05;

public class ArraySumExer {
    public static void main(String[] args) {
        /*
        案例：定义一个int型的一维数组，包含10个元素，分别赋一些随机整数，然后求出所有元素的
        最大值，最小值，总和，平均值，并输出出来。
        要求：所有随机数都是两位数：[10,99]
        提示：求[a,b]范围内的随机数： (int)(Math.random() * (b - a + 1)) + a;
         */
        int[] arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 90 + 10);
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        System.out.println("max = " + max);
        System.out.println("min = " + min);

        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        System.out.println("sum = " + sum);

        double avg = (double) sum / arr.length;
        System.out.println("avg = " + avg);

        /*
        案例：评委打分
        分析以下需求，并用代码实现：
        （1）在编程竞赛中，有10位评委为参赛的选手打分，分数分别为：5,4,6,8,9,0,1,2,7,3
        （2）求选手的最后得分（去掉一个最高分和一个最低分后其余8位评委打分的平均值）
         */
        int[] scores = {5, 4, 6, 8, 9, 0, 1, 2, 7, 3};
        int sum1 = 0;
        int max1 = scores[0];
        int min1 = scores[0];
        for (int i = 0; i < arr.length; i++) {
            System.out.println(scores[i]);
            sum1 += scores[i];
            if (scores[i] > max1) {
                max1 = scores[i];
            }
            if (scores[i] < min1) {
                min1 = scores[i];
            }
        }
        int avg1 = (sum1 - max1 - min1) / (scores.length - 2);
        System.out.println("最后得分为：" + avg1); // 4

        /*
        案例：使用二维数组打印一个 10 行杨辉三角。
           提示：
           1. 第一行有 1 个元素, 第 n 行有 n 个元素
           2. 每一行的第一个元素和最后一个元素都是 1
           3. 从第三行开始, 对于非第一个元素和最后一个元素的元素。即：
           yanghui[i][j] = yanghui[i-1][j-1] + yanghui[i-1][j];
         */
        int[][] yh = new int[10][];
        for (int i = 0; i < yh.length; i++) {
            yh[i] = new int[i + 1]; // 第 i 行有 i+1 个元素
            yh[i][0] = 1;
            yh[i][i] = 1;
            for (int j = 1; j < yh[i].length - 1; j++) {
                yh[i][j] = yh[i - 1][j - 1] + yh[i - 1][j];
            }
            for (int j = 0; j < yh[i].length; j++) {
                System.out.print(yh[i][j] + " ");
            }
            System.out.println();
        }

        //举例：创建一个长度为6的int型数组，要求数组元素的值都在1-30之间，且是随机赋值。同时，要求元素的值各不相同。
        int[] arr1 = new int[6];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (int) (Math.random() * 30 + 1);
/*
当 i 走到最后一个下标 5 时，i + 1 就是 6，而数组最大下标只有 5，arr1[6] 不存在，所以越界报错。
你只和相邻的下一个比，没有和前面所有元素比。
arr1[i + 1] 这个位置的值，在循环走到 i + 1 那一轮的开头又会被重新随机赋值，所以你在 i 这轮对它做的修正白做了，会被覆盖掉。
            if (arr1[i] == arr1[i + 1]) {
                arr1[i + 1] = (int)(Math.random() * 30 + 1);
 */
            for (int j = 0; j < i; j++) {
                if (arr1[i] == arr1[j]) {
                    i--;
                    break;
                }
            }
        }
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + " ");
        }

        System.out.println();
        /*
        案例：复制、赋值
        使用简单数组
        (1)创建一个名为ArraySumExer的类，在main()方法中声明array1和array2两个变量，他们是int[]类型的数组。
        (2)使用大括号{}，把array1初始化为8个素数：2,3,5,7,11,13,17,19。
        (3)显示array1的内容。
        (4)赋值array2变量等于array1，修改array2中的偶索引元素，使其等于索引值(如array[0]=0,array[2]=2)。
        (5)打印出array1。
        思考：array1和array2是什么关系？
            array1和array2是两个变量，共同指向了堆空间中的同一个数组结构。即二者的地址值相同。
         */
        int[] array1 = {2,3,5,7,11,13,17,19};
        for (int i = 0; i < array1.length; i++) {
            System.out.print(array1[i] + " "); // 2 3 5 7 11 13 17 19
        }
        //赋值
        int[] array2 = array1;
        System.out.println("1" + array1); // [I@54bedef2
        System.out.println("2" + array2); // [I@54bedef2
        for (int i = 0; i < array2.length; i++) {
            if (i % 2 == 0) {
                array2[i] = i;
            }
        }
        System.out.println();
        for (int i = 0; i < array1.length; i++) {
            System.out.print(array1[i] + " "); // 0 3 2 7 4 13 6 19
        }
        //拓展：修改题目，实现array2对array1数组的复制
        //复制
        array2 = new int[array1.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array1[i];
        }
        System.out.println(array1); // [I@54bedef2
        System.out.println(array2); // [I@27716f4

        /*
        案例：
        定义数组：int[] arr = new int[]{34,54,3,2,65,7,34,5,76,34,67};
        如何实现数组元素的反转存储？你有几种方法。
         */
        int[] ar = new int[]{34,54,3,2,65,7,34,5,76,34,67};
        //方式1
        for (int i = 0; i < ar.length / 2; i++) {
            //交换ar[i]与ar[ar.length - 1 - i]位置的元素
            int temp = ar[i];
            ar[i] = ar[ar.length - 1 - i];
            ar[ar.length - 1 - i] = temp;
        }
        for (int i = 0; i < ar.length; i++) {
            System.out.print(ar[i] + " ");
        }
        //方式2（不推荐）
        int[] newAr = new int[ar.length];
        for (int i = ar.length - 1; i >= 0; i--) {
            newAr[ar.length - 1 - i] = ar[i];
        }
        ar = newAr;
        //方式3
        for (int i = 0, j = ar.length - 1; i < j; i++, j--) {
            int temp = ar[i];
            ar[i] = ar[j];
            ar[j] = temp;
        }

    }
}
