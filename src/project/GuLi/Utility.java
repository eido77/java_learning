package project.GuLi;

import java.util.Scanner;
/**
 Utility工具类：
 将不同的功能封装为方法，就是可以直接通过调用方法使用它的功能，而无需考虑具体的功能实现细节。
 */
public class Utility {
    /*
    同一个类=同一对 { } 大括号里面。 public class -- { ... } 这中间所有东西都算"同一个类内部"。
    凡是 public class 名字，那个名字都是一个类、都能当类型。只是有的类（--、Utility）真会被当类型用，有的类（各种 Test）只是拿来跑 main 练手。
    public class -- {}， --就升级成一个可用的类型，能像 int 一样声明变量。
    格式都是「类型 变量名 = new 类型(...)」造一个 Scanner 时,得告诉它"从哪读"——System.in 就是告诉它"从键盘读"。Dog 不需要额外信息
    int     x       = 123;                       // int 是自带的类型，直接给值
    Dog     d       = new Dog();                 // 别的地方用，要加new，Dog是定义的类（public class Dog）
    Scanner scanner = new Scanner(System.in);    // Scanner 库自带的类，要 new

    static —— 管"怎么调"，用类名调，还是用对象调。工具方法 static，对象方法不 static。
    你想调用的方法（xxx），必须先在那个类里定义过；调用时写的名字，必须和定义时一模一样。
    有 static：用 【类名.方法名()】，不用 new【有 static → xxx属于类 → 用类名点 → Dog.xxx()】
    public static void xxx() { ... }【写在【方法所属的那个类】里，比如 Dog.java，只写在 Dog.java 里一次】
    Dog.xxx();      // 写在【要用它的地方】，可以是别的类/别的文件
    无 static：先 new 造对象，再用 【对象.方法()】【没 static → xxx属于某个对象 → 用对象名点 → d.xxx()】
    public void xxx() { ... }  // 只写在 Dog.java 里一次
    Dog d = new Dog();           // 先 new 一条狗，new Dog() 就是"造一条狗"，造出来的这条狗存在变量 d 里。
    d.xxx();                    // 用对象 d 点

    public static void main 里的 void，意思就是"main 这个方法不返回东西"，跟 int、char、String 是同一格的"竞争对手"，这一格只能填一个，要么填 void（不交货），要么填具体类型（交货）。
    private，别的类不能调用。public，别的类可以调用
     */
    private static Scanner scanner = new Scanner(System.in);
    /**
     用于界面菜单的选择。该方法读取键盘，如果用户键入’1’-’4’中的任意字符，则方法返回。返回值为用户键入字符。
     */
    public static char readMenuSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1);
            c = str.charAt(0);
            if (c != '1' && c != '2' && c != '3' && c != '4') {
                System.out.print("选择错误，请重新输入：");
            }
            else
                break;
        }
        return c;
    }
    /**
     用于收入和支出金额的输入。该方法从键盘读取一个不超过4位长度的整数，并将其作为方法的返回值。
     */
    public static int readNumber() {
        int n;
        for (; ; ) {
            String str = readKeyBoard(4);
            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("数字输入错误，请重新输入：");
            }
        }
        return n;
    }
    /**
     用于收入和支出说明的输入。该方法从键盘读取一个不超过8位长度的字符串，并将其作为方法的返回值。
     */
    public static String readString() {
        String str = readKeyBoard(8);
        return str;
    }

    /**
     用于确认选择的输入。该方法从键盘读取‘Y’或’N’，并将其作为方法的返回值。
     */
    public static char readConfirmSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            } else {
                System.out.print("选择错误，请重新输入：");
            }
        }
        return c;
    }


    private static String readKeyBoard(int limit) {
        String line = "";

        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (line.length() < 1 || line.length() > limit) {
                System.out.print("输入长度（不大于" + limit + "）错误，请重新输入：");
                continue;
            }
            break;
        }

        return line;
    }
}
