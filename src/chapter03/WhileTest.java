package chapter03;
import java.util.Scanner;
public class WhileTest {
    public static void main(String[] args) {
        /*
        while循环
        凡事循环结构一定有四要素：
            一.初始化条件
            二.循环条件(boolean类型)
            三.循环体
            四.迭代部分

        while格式：
        一
        while (二) {
            三
            四
        }

        执行过程：一 --> 二 --> 三 --> 四 --> 二 --> 三 --> 四 ... --> 二
        for循环和while循环可以相互转换
         */

        //遍历50次HelloWorld
        int i = 1;
        while (i <= 50) {
            System.out.println("Hello World");
            i++;
        }

        // 遍历1-100以内的偶数，并获取偶数的个数，获取所有的偶数的和
        int count = 0; // 记录偶数的个数
        int sum = 0; // 记录所以偶数的和

        // 不能用i，上面用过了，此处和for不同
        int i1 =1;
        while (i1 <= 100) {
            if (i1 % 2 == 0) {
                System.out.println(i1);
                count++;
                sum += i1;
            }
            i1++;
        }
        System.out.println("偶数的个数：" + count);
        System.out.println("所有偶数的和：" + sum);

        //随机生成一个100以内的数，猜这个随机数是多少？
        //从键盘输入数，如果大了，提示大了；如果小了，提示小了；如果对了，就不再猜了，并统计一共猜了多少次。
        //提示：生成一个［a，b］范围的随机数的方式：(int) (Math.random() * (b - a + 1) + a)

        // 1.生成[1,100]范围的随机整数
        int random = (int) (Math.random() * 100 + 1);

        // 2.从键盘获取数据，Scanner
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个[1,100]的整数：");
        int guess = scanner.nextInt();

        //3.声明一个变量，记录猜的次数
        int guessCount = 1;

        // 4.使用循环结构，进行多次循环的对比和获取数据
        while (random != guess) {
            if (guess > random) {
                System.out.println("猜大了");
            } else if (guess < random) {
                System.out.println("猜小了");
            }
            System.out.println("请输入一个[1,100]的整数：");
            guess = scanner.nextInt();
            guessCount++;

        }
        System.out.println("猜对了");
        System.out.println("共猜了" + guessCount + "次");

        scanner.close();

        //世界最高山峰是珠穆朗玛峰，它的高度是8848.86米，假如我有一张足够大的纸，它的厚度是0.1毫米。
        //请问，我折叠多少次，可以折成珠穆朗玛峰的高度？
        //8848.86m = 8848860mm
        double high = 0.1;
        int highCount = 0;
        while (high <= 8848860) {
            high *= 2;
            highCount++;
        }
        System.out.println("折叠次数：" + highCount); // 27


    }
}
