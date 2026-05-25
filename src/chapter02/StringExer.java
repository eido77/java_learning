package chapter02;

public class StringExer {
    public static void main(String[] args) {

        //案例1：要求填写自己的姓名、年龄、性别、体重、婚姻状况（已婚用true表示，单身用false表示）、联系方式等等。
        String name = "张三";
        int age = 10;
        char gender = '男';
        double weight = 100.5;
        boolean isMarried = false;
        String phoneNumber = "123456789";

        System.out.println("name = " + name);
        System.out.println("age = " + age);
        System.out.println("gender = " + gender);
        System.out.println("weight = " + weight);
        System.out.println("isMarried = " + isMarried);
        System.out.println("phoneNumber = " + phoneNumber);

        //练习1:
        //从左到右算，碰到 String 就拼接，没碰到就按数字算。char 遇数字按 ASCII，char 遇 String 按字符。
        //"Hello" 不是「被当作」String，它本身就是 String，编译器一看到双引号就这么认定了。
        //String str1 = 4;                          //判断对错: no，4是int
        String str2 = 3.5f + "";                  //判断str2对错: yes，String"3.5"
        System.out.println(str2);                 //输出:3.5
        System.out.println(3+4+"Hello!");         //输出:7Hello!
        System.out.println("Hello!"+3+4);         //输出:Hello!34
        System.out.println('a'+1+"Hello!");       //输出:98Hello!，'a' + 1是char + int
        System.out.println("Hello"+'a'+1);        //输出:Helloa1

        //练习2:
        //char按ASCII计算，'*' 的 ASCII 值是 42，'\t' 的 ASCII 值是 9
        //从左到右，一旦变成了String，后面的 + 就全部都是拼接。如果有括号，先把括号当成一个独立的小题目算出来，再代入到大表达式里
        //char见数变数字，char见String变字符
        System.out.println("*	*");               //输出:*   *
        System.out.println("*\t*");                //输出:*   *，直接打印出来，没有任何运算过程。
        System.out.println("*" + "\t" + "*");      //输出:*   *
        System.out.println('*' + "\t" + "*");      //输出:*   *
        System.out.println('*' + '\t' + "*");      //输出:51*
        System.out.println('*' + "\t" + '*');      //输出:*   *
        System.out.println("*" + '\t' + '*');      //输出:*   *
        System.out.println('*' + '\t' + '*');      //输出:93



    }
}
