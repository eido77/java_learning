package chapter18;

import org.junit.jupiter.api.Test;

public class InstanceOfTest {
    /*
     * instanceof 是 Java 的类型比较运算符：判断"左边引用指向的对象"是不是
     * "右边类型"（类、接口、数组、枚举）或它的子类型的实例，结果是一个 boolean。
     *
     * null instanceof 任何类型 都返回 false，不抛异常，所以它顺带完成了非空判断；
     *
     * instanceof 模式匹配的版本历程：JDK 14 首次预览、JDK 15 第二次预览、JDK 16 转正。

     * 旧写法：if (obj instanceof String) { String s = (String) obj; ... } 判断、强转、使用三步走。
     * 新写法：if (obj instanceof String s) { ... } 一步到位，s 已经是对应的类型，不用再强转。

    JDK14之前：
    // 先判断类型
    if (obj instanceof String) {
        // 然后转换
        String s = (String) obj;
        // 然后才能使用
    }

    JDK14:（自动匹配模式）
    if (obj instanceof String s) {
        // 如果类型匹配 直接使用
    } else {
        // 如果类型不匹配则不能直接使用
    }
     */

    /**
     * 举例1：用同一个功能对比"JDK 14 之前"和"JDK 14 之后"两种写法，
     */
    // JDK14之前
    @Test
    public void test1() {
        Object obj = new String("hello,Java14");

        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println(str.contains("Java"));
        } else {
            System.out.println("非String类型");
        }
    }

    // JDK14中
    @Test
    public void test2() {
        Object obj = new String("hello,Java14");

        if (obj instanceof String str) {
            System.out.println(str.contains("Java"));
        } else {
            System.out.println("非String类型");
        }
    }
}

/**
 * 举例2：定义一个 Computer 类，用三种写法实现 equals，
 * 对比"旧式强转"和"instanceof 模式匹配"的差别。
 */
class Computer {
    private String model; // 型号
    private double price; // 价格
    // 方式1
    /*
     * 方式1（JDK 14 之前的老写法）：
     * 外层 if (o instanceof Computer) 判断类型，然后 (Computer) o 强转成 other，
     * 再在内层 if 里比较字段，两个条件都满足才 return true，否则落到最后 return false。
     * 那时没别的选择，所以强转、嵌套、多出口都是写 equals 的标配。
     */
//    public boolean equals(Object o) {
//        if (o instanceof Computer) {
//            Computer other = (Computer) o;
//            if (this.model.equals(other.model) && this.price == other.price) {
//                return true;
//            }
//        }
//        return false;
//    }

    // 方式2
    /*
     * 方式2：把 o instanceof Computer other 写成一个条件，
     * 一步就得到"已经转好型的变量 other"，于是 (Computer) o 那句强转消失了。
     */
//    public boolean equals(Object o) {
//        if (o instanceof Computer other) {
//            return this.model.equals(other.model) && this.price == other.price;
//        }
//        return false;
//    }

    // 方式3
    /*
     * 把方式2 的 if + return 再合并成一条 return 表达式。
     * 原理是 && 的短路求值：左边 o instanceof Computer other 为 false 时，
     * 右边根本不会执行，所以 other 一定是在"转型成功"之后才被使用的，安全。
     * 如果 o 是 null 或不是 Computer，整个表达式直接得到 false，不会抛异常。
     */
    public boolean equals(Object o) {
        return o instanceof Computer other && this.model.equals(other.model)
                && this.price == other.price;
    }
}
