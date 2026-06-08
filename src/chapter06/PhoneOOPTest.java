package chapter06;

//是Phone的测试类
public class PhoneOOPTest {
    public static void main(String[] args) {
        /*
        面向过程编程(POP) ：
            以`函数`为组织单位。
            是一种“`执行者思维`”，适合解决简单问题。扩展能力差、后期维护难度较大。
        面向对象编程(OOP)：
            以`类`为组织单位。每种事物都具备自己的`属性`和`行为/功能`。
            是一种“`设计者思维`”，适合解决复杂问题。代码扩展性强、可维护性高。
        他们是相辅相成的。面向对象离不开面向过程

        面向对象编程的两个核心概念：类（Class）、对象（Object）
        谈谈对这两个概念的理解？
           类:具有相同特征的事物的抽象描述，是`抽象的`、概念上的定义。
           对象：实际存在的该类事物的`每个个体`，是`具体的`，因而也称为`实例(instance)`。

        类的声明与使用
        体会：设计类，其实就是设计类的成员
        class Person{

        }
        类的内部成员一、二：
        一：属性、成员变量、field（字段、域）
        二：（成员）方法、函数、method

        面向对象完成具体功能的操作的三步流程（非常重要）
        步骤1：创建类，并设计类的内部成员（属性、方法）
        步骤2：创建类的对象。比如：Phone p1 = new Phone();
        步骤3：通过对象，调用其内部声明的属性或方法，完成相关的功能

        类的实例化
        等价描述：类的实例化 <=> 创建类的对象  <=> 创建类的实例
        格式：类类型 对象名 = 通过new创建的对象实体
        举例：
        Phone p1 = new Phone();
        Scanner scan = new Scanner(System.in);
        String str = new String();
         */

        //创建Phone的对象
        //数据类型 变量名 = 变量值;
        //Scanner sc = new Scanner(System.in);
        Phone p1 = new Phone();

        //通过Phone的对象，调用其内部声明的属性或方法
        //格式："对象.属性"或者"对象.方法"
        p1.name = "17 pro max";
        p1.price = 9999;

        System.out.println(p1.name); // 17 pro max
        System.out.println(p1.price); // 9999.0

        //调用方法
        p1.call(); // 手机能拨打电话
        p1.sendMessage("123"); // 发送信息123
        p1.playGame();


    }
}
