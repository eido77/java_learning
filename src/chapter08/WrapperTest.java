package chapter08;

import org.junit.jupiter.api.Test;

public class WrapperTest {
    /*
    包装类的使用
    1. 为什么要使用包装类？
    Java中基本数据类型不具备"面向对象"的特性（如封装、继承、多态），也无法作为Object被统一处理。
    为了让基本数据类型也能"当对象用"，Java为8种基本数据类型都提供了对应的包装类。
    2. (掌握)基本数据类型对应的包装类类型
    byte -> Byte
    short -> Short
    int -> Integer
    long -> Long
    float -> Float
    double ->Double

    char -> Character
    boolean -> Boolean
    3. 掌握基本数据类型 与 包装类之间的转换。
       3.1 为什么需要转换
       > 有些场景必须用对象（包装类），不能用基本类型。
             比如集合ArrayList的add(Object obj)、Object类的equals(Object obj)，参数都要求是对象。
             此时就需要把基本数据类型 转换为 包装类对象。
       > 反过来，包装类是对象，对象本身不能直接做 + - * / 等算术运算（jdk5前）。
             想参与运算，就需要把包装类对象 转换回 基本数据类型。
       3.2 如何转换：
            （装箱）基本数据类型 ---> 包装类：① 使用包装类的构造器 ② （建议）调用包装类的valueOf(xxx xx)
            （拆箱）包装类 ---> 基本数据类型：调用包装类的xxxValue()
        注意：原来使用基本数据类型变量的位置，改成包装类以后，对于成员变量来说，其默认值变化了
             基本类型默认值是 0 / 0.0 / false 等；包装类是引用类型，默认值是 null。
        jdk5.0新特性：自动装箱(autoboxing)、自动拆箱(unboxing)，编译器自动帮我们完成上面的转换
    4. String 与 基本数据类型、包装类之间的转换。
       基本数据类型、包装类 ---> String类型：① 调用String的重载的静态方法valueOf(xxx xx) ; ② 基本数据类型的变量 + ""
       String类型 ---> 基本数据类型、包装类: 调用包装类的静态方法：parseXxx()
     */
    public static void main(String[] args) {
        int num = 1;
        Integer obj = 1; // 自动装箱
        System.out.println(obj); // 1
    }

    // （装箱）基本数据类型 ---> 包装类：① 使用包装类的构造器 ② （建议）调用包装类的valueOf(xxx xx)
    // @Test方法必须和main方法"平级"（都是类的成员方法），不能嵌套写在别的方法里。
    @Test
    public void test1() {
        // ① 使用包装类的构造器（jdk9起被标记为@Deprecated，不推荐再用）
        int i1 = 10;
        // cmd + p可以看参数提示
        Integer ii1 = new Integer(i1);
        System.out.println(ii1.toString()); // 10

        float f1 = 12.3F;
        Float ff1 = new Float(f1);
        System.out.println(ff1.toString()); // 12.3

        // 特殊：Float、Integer等的构造器还能直接接收"数字字符串"，把String转成包装类
        String s1 = "32.1";
        Float fs1 = new Float(s1);
        System.out.println(fs1); // 32.1
        // 但字符串内容必须是合法数字，否则抛 NumberFormatException（数字格式化异常）
//        s1 = "abc";
//        Float fs2 = new Float(s1);

        boolean b1 = true;
        Boolean bb1 = new Boolean(b1);
        System.out.println(bb1); // true

        // Boolean(String)的规则：忽略大小写，只有内容等于"true"才为true，其余一律false
        String s2 = "false";
        Boolean bs2 = new Boolean(s2);
        System.out.println(bs2); // false
        s2 = "False123";
        Boolean bs22 = new Boolean(s2);
        System.out.println(bs22); // false
        s2 = "TrUe";
        Boolean bs222 = new Boolean(s2);
        System.out.println(bs222); // true

        // ② （建议）调用包装类的valueOf(xxx xx)
        int i2 = 20;
        // IDEA中写完 Integer.valueOf(i2) 后，按 option + enter 可自动补全左边的 "Integer ii2 ="
        Integer ii2 = Integer.valueOf(i2);
        Boolean b2 = Boolean.valueOf(true);
        Float f2 = Float.valueOf(12.3F);
    }

    // （拆箱）包装类 ---> 基本数据类型：调用包装类的xxxValue()
    @Test
    public void test2() {
        Integer ii1 = new Integer(10);
        int i1 = ii1.intValue(); // 拆箱：Integer -> int
        i1 = i1 + 1;

        Float ff1 = new Float(12.3F);
        float f1 = ff1.floatValue();

        Boolean bb1 = Boolean.valueOf(true);
        boolean b1 = bb1.booleanValue();
        System.out.println(b1);
    }

    // 验证：成员变量是"基本类型"还是"包装类"，默认值不同
    @Test
    public void test3() {
        Account1 account1 = new Account1();
        System.out.println(account1.isFlag1); // false
        System.out.println(account1.isFlag2); // null

        System.out.println(account1.balance1); // 0.0
        System.out.println(account1.balance2); // null
    }

    // jdk5.0新特性：自动装箱、自动拆箱（编译器自动完成，本质仍是valueOf() / xxxValue()）
    @Test
    public void test4() {
        // 自动装箱：基本数据类型 ---> 包装类
        int i1 = 10;
        Integer ii1 = i1; // 自动装箱（底层：Integer.valueOf(i1)）
        System.out.println(ii1.toString()); // 10

        Integer ii2 = i1 + 1; // 先算出int结果，再自动装箱
        Boolean bb1 = true; // 自动装箱

        // 自动拆箱：包装类 ---> 基本数据类型
        int i2 = ii1; // 自动拆箱（底层：ii1.intValue()）
        boolean b1 = bb1; // 自动拆箱
    }

    // 4. String 与 基本数据类型、包装类之间的转换。
    @Test
    public void test5() {
        // 基本数据类型、包装类 ---> String类型：① 调用String的重载的静态方法valueOf(xxx xx); ② 基本数据类型的变量 + ""
        // ① 调用String的重载的静态方法valueOf(xxx xx);
        int i1 = 10;
        String str1 = String.valueOf(i1);
        // 不是带引号的10。引号只是"源代码里"表示字符串的写法，打印时不会把引号打出来，控制台看到的就是纯粹的 10（但它类型是String）
        System.out.println(str1); // 10

        boolean b1 = true;
        Boolean bb1 = b1; // 自动装箱：boolean -> Boolean
        String str2 = String.valueOf(b1); // 传基本类型boolean
        String str3 = String.valueOf(bb1); // 传包装类Boolean，结果一样
        System.out.println(str2); // true
        System.out.println(str3); // true

        // ② 基本数据类型的变量 + ""
        // 原理：任何类型和字符串做 + ，都会被转成字符串再拼接，所以 +"" 就能得到String
        String str4 = i1 + "";
        String str5 = b1 + "";

        // String类型 ---> 基本数据类型、包装类: 调用包装类的静态方法：parseXxx()
        String s1 = "123";
        int ii1 = Integer.parseInt(s1);
        System.out.println(ii1 + 10); // 133
        // 对比：如果不转直接 "123"+10 会得到字符串"12310"
        System.out.println(s1 + 10); // 12310

        String s2 = "true";
        boolean bb2 = Boolean.parseBoolean(s2);
        System.out.println(bb2); // true

        // 特别的：字符串内容必须是合法的对应类型，否则抛 NumberFormatException
        String s3 = "123a";
        // "123a"里带字母a，不是合法数字，parseInt时会抛 NumberFormatException
//        int i3 = Integer.parseInt(s3);
        // 补充：Boolean.parseBoolean 不会抛异常，规则同前——忽略大小写只认"true"，其余都是false
    }
}

class Account1 {
    boolean isFlag1;
    Boolean isFlag2;

    double balance1;
    Double balance2;
}
