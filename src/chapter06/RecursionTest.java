package chapter06;

public class RecursionTest {
    public static void main(String[] args) {
        RecursionTest rt = new RecursionTest();
//        rt.method1();
        System.out.println(rt.getSum(100)); // 5050
        System.out.println(rt.getSum1(100)); // 5050

        System.out.println(rt.getMultiply(5)); // 120

        System.out.println(rt.f(10)); // 55

        rt.hanoi(3, 'A', 'C', 'B');
        /*
        把第 1 个盘子从 A 移到 C
        把第 2 个盘子从 A 移到 B
        把第 1 个盘子从 C 移到 B
        把第 3 个盘子从 A 移到 C
        把第 1 个盘子从 B 移到 A
        把第 2 个盘子从 B 移到 C
        把第 1 个盘子从 A 移到 C
         */
    }

    /**
     * 如下递归方法的调用会导致 StackOverflowError
     */
    public void method1() {
        System.out.println("method1()...");
        method1();
    }

    /**
     * 举例1:计算1 - 100内自然数的总和
     * 思路：getSum1(num) = getSum1(num-1) + num
     *      即"前 num-1 个数的和" 再加上 "num 自己"
     */
    public int getSum(int num) {
        int sum = 0;
        for (int i = 1; i <= num; i++) {
            sum += i;
        }
        return sum;
    }

    public int getSum1(int num) {
        // 递归出口：当 num 减到 1（或更小）时，直接返回 num，不再继续调用自己
        // 用 <= 1 而不是 == 1，是为了兜住传入 0 或负数的情况，避免无限递归
//        if (num == 1) {
//            return 1;
        if (num <= 1) {
            return num;
        } else {
            // 递归调用：先算出 (num-1) 的总和，再加上当前的 num
            // 这一行在 getSum1(num-1) 算出结果之前会一直"卡住"等待
            return getSum1(num - 1) + num;
        }
    }

    /**
     * 举例2:计算n!
     * 阶乘 n! = 1 × 2 × 3 × … × n
     * 思路：getMultiply(n) = n × getMultiply(n-1)
     *      即"n 自己" 乘以 "(n-1) 的阶乘"
     */
    public int getMultiply(int n) {
        // 递归出口：当 n == 1 时，1! = 1，直接返回 1，不再继续调用自己
        if (n == 1) {
            return 1;
        } else {
            // 递归调用：当前的 n，乘以 (n-1) 的阶乘
            // 一层层往下拆到 n==1 触底，再把结果一层层乘回来
            return n * getMultiply(n - 1);
        }
    }

    /**
     * 举例3:快速排序
     * 核心思想（分而治之）：
     *   ① 挑一个"基准值"（这里取最左边的元素）
     *   ② 一趟扫描，把比基准小的甩到左边、比基准大的甩到右边，
     *      使基准落到它最终该在的位置
     *   ③ 对基准左边、右边两段，各自再递归快排一次，直到每段只剩 1 个元素
     *
     *
     * @param arr   要排序的数组
     * @param left  当前排序范围的左边界（起始下标）
     * @param right 当前排序范围的右边界（结束下标）
     */
    public void quickSort(int[] arr, int left, int right) {
        // 递归出口：左边界 >= 右边界，说明这段只剩 0 或 1 个元素，无需再排
        if (left >= right) {
            return;
        }

        int pivot = arr[left];  // 基准值，取最左边的元素
        int i = left;           // 左指针，从左往右扫
        int j = right;          // 右指针，从右往左扫

        // 一趟"分区"：把小的换到左边，大的换到右边
        while (i < j) {
            // 右指针从右往左找，找到第一个比基准小的元素才停
            while (i < j && arr[j] >= pivot) {
                j--;
            }
            // 左指针从左往右找，找到第一个比基准大的元素才停
            while (i < j && arr[i] <= pivot) {
                i++;
            }
            // 交换这两个元素，让小的去左边、大的去右边
            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // i 和 j 相遇，把基准值放到这个分界点，此时基准左边都比它小、右边都比它大
        arr[left] = arr[i];
        arr[i] = pivot;

        // 对基准左半段、右半段分别递归（分而治之）
        quickSort(arr, left, i - 1);
        quickSort(arr, i + 1, right);
    }

    /**
     * 举例4:汉诺塔游戏
     * 规则：有 A、B、C 三根柱子，A 上有 n 个盘子（下大上小），
     *      要全部搬到 C 上。每次只能搬一个，且任何时候大盘不能压在小盘上。
     *
     * 递归思路（关键在于"化繁为简"）：
     * 不管有多少个盘子，搬运都能归结为下面三步：
     *   ① 先把上面的 (n-1) 个盘子，借助 C，从 A 搬到 B
     *   ② 再把最底下最大的第 n 个盘子，从 A 直接搬到 C
     *   ③ 最后把那 (n-1) 个盘子，借助 A，从 B 搬到 C
     * 而第 ① ③ 步"搬 n-1 个"又是同样的问题，于是递归调用自己。
     *
     * @param n    当前要搬的盘子数量
     * @param from 起始柱（盘子现在在哪根柱子上）
     * @param to   目标柱（要搬到哪根柱子上）
     * @param via  中转柱（借用来周转的那根柱子）
     */
    public void hanoi(int n, char from, char to, char via) {
        // 递归出口：只剩 1 个盘子时，直接从 from 搬到 to，不用中转
        if (n == 1) {
            System.out.println("把第 1 个盘子从 " + from + " 移到 " + to);
            return;
        }

        // ① 把上面 n-1 个盘子，从 from 借助 to，挪到 via 上
        hanoi(n - 1, from, via, to);

        // ② 把最底下的第 n 个盘子，从 from 直接搬到 to
        System.out.println("把第 " + n + " 个盘子从 " + from + " 移到 " + to);

        // ③ 把那 n-1 个盘子，从 via 借助 from，挪到 to 上
        hanoi(n - 1, via, to, from);
    }

    /**
     * 举例5:斐波那契数列
     * 1 1 2 3 5 8 13 21 34 55 ...
     * 规律：从第 3 项起，每一项都等于前两项之和
     * f(n) = f(n - 1) + f(n - 2)
     *
     * 【重要缺点：会重复计算相同的项】
     *    因为每层都调用两次自己（f(n-1) 和 f(n-2)），展开后是一棵分叉的树，
     *    不同的支路会算到同一个数，而程序并不会"记住"算过的结果，每次都从头再算一遍。
     *    以 f(5) 为例：
     *                  f(5)
     *                /      \
     *            f(4)        f(3)    ← f(3) 在这里被算了一次
     *           /    \       /    \
     *        f(3)   f(2)  f(2)   f(1)   ← f(3) 又被算了一次，f(2) 出现了好几次
     *        /   \
     *     f(2)  f(1)
     *    可以看到 f(3) 被算了 2 遍、f(2) 被算了 3 遍，全是重复劳动。
     *    n 越大，重复得越夸张，时间复杂度高达 O(2^n)，算 f(50) 就会慢到卡死。
     *    （实际开发会改用循环，或用"缓存数组记住算过的值"来避免重复，这里先了解。）
     */
    public int f(int n) {
        // 递归出口1：第 1 项规定为 1
        if (n == 1) {
            return 1;
        } else if (n == 2) {
            // 递归出口2：第 2 项也规定为 1
            // 斐波那契需要"两个"出口，因为公式要回看前两项，缺了任何一个，递归都会停不下来
            return 1;
        } else {
            // 递归调用
            // 这里调用了"两次"自己，每一层都会分叉成两条支路，展开后是一棵二叉树，而不是一条直线，
            // 不同支路会重复算到相同的项（见上方说明）
            return f(n - 1) + f(n - 2);
        }
    }

    /**
     * 举例6:
     * File类的对象表示一个文件目录。
     * 计算指定的文件目录的大小，遍历指定的文件目录中的所有的文件，删除指定的文件目录。
     */


}
