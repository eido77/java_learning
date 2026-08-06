package chapter08;

import java.util.Scanner;
import java.util.Vector;

public class VectorTest {
    /*
    利用Vector代替数组处理：从键盘读入学生成绩（以负数代表输入结束），找出最高分，并输出学生成绩等级。
        为什么用 Vector 而不用数组？
        数组长度在创建时就固定了，必须提前知道要存多少个元素；
        而 Vector 可以随着元素的增减自动伸缩，适合"事先不知道数量"的场景。
    1、创建Vector对象：Vector v = new Vector();
    2、给向量Vector添加元素：v.addElement(Object obj);   //obj必须是对象
    3、取出向量中的元素：Object  obj = v.elementAt(0);
       注意第一个元素的下标是0，返回值是Object类型的。
    4、计算向量的长度：v.size();
    5、若与最高分相差10分内：A等；20分内：B等；30分内：C等；其它：D等
     */
    public static void main(String[] args) {
        // 1、创建Vector对象：Vector v = new Vector();
        Vector v = new Vector();
        Scanner scanner = new Scanner(System.in);
        // 更通用、严谨的写法是 int maxScore = Integer.MIN_VALUE;
        int maxScore = 0; // 记录最高分

        // 2.从键盘获取多个学生成绩，存放到v中（以负数代表输入结束）
        while (true) {
            System.out.print("请输入学生成绩（以负数代表输入结束）：");
            int intScore = scanner.nextInt();

            // 遇到负数：结束输入
            if (intScore < 0) {
                break;
            }

            // 方式1：手动写法
            // 装箱：将int --> Integer 对象
//            Integer score = Integer.valueOf(intScore);
            // 添加学生成绩到容器v中
            // 方法调用时，只需要传入变量，不需要再写变量类型。创建变量的时候，需要写类型，使用变量的时候，不需要写类型
//            v.addElement(score);

            // 方式2：自动装箱（JDK5.0之后）
            // 下面这行发生了"自动装箱"（JDK5.0 起支持）：int -> Integer 会自动完成
            // 方法调用时，只需要传入变量，不需要再写变量类型。创建变量的时候，需要写类型，使用变量的时候，不需要写类型
            v.addElement(intScore);

            // 3.获取学生成绩的最大值
            if (maxScore < intScore) {
                maxScore = intScore;
            }
        }

        System.out.println("最高分为：" + maxScore);

        // 4.依次获取v中的每个学生成绩，与最高分进行比较，获取学生等级，并输出
        for (int i = 0; i < v.size(); i++) {
            // elementAt 返回的是 Object，需要转回 Integer 才能当数字用
            Object objScore = v.elementAt(i);
            // 方式1：JDK5.0之前（手动写法）
//            Integer integerScore = (Integer) objScore; // 强制类型转换 Object -> Integer
//            int score = integerScore.intValue(); // 手动拆箱：Integer -> int

            // 方式2：自动拆箱：先把 Object 强转为 Integer，再自动 Integer -> int
            int score = (Integer) objScore;

            // grade 在下面的 if-else 中每个分支都会被赋值，逻辑上一定有值，所以这里不用给默认值，编译器也能确定它被初始化了。
            char grade;

            if (maxScore - score <= 10) {
                grade = 'A';
            } else if (maxScore - score <= 20) {
                grade = 'B';
            } else if (maxScore - score <= 30) {
                grade = 'C';
            // 最后一档用 else 更合适：它表示"以上都不满足的所有情况"，不用再重复写条件，也不会漏掉边界。
            } else {
                grade = 'D';
            }

            System.out.println("Student " + i + " score is " + score + " garde is " + grade);
        }

        scanner.close();
    }
}
