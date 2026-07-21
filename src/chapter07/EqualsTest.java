package chapter07;

import java.io.File;
import java.util.Objects;

public class EqualsTest {
    /*
    equals()的使用
    适用性：
    任何引用数据类型都可以使用。
    java.lang.Object类中equals()的定义：
    public boolean equals(Object obj) {
        return (this == obj);
    }
    子类使用说明：
    >自定义的类在没有重写Object中equals()方法的情況下，调用的就是Object类中声明的equals()，
        比较两个对象的引用地址是否相同。（或比较两个对象是否指向了堆空间中的同一个对象实体）
    > 对于像String、File、Date和包装类等，它们都重写了Object类中的equals()方法，用于比较两个对象的实体内容是否相等。
    开发中使用说明：
    > 实际开发中，针对于自定义的类，常常会判断两个对象是否equals()，而此时主要是判断两个对象的属性值是否相等。
      所以：我们要重写Object类的equals()方法。
    > 如何重写：
        ＞手动自己实现
        > 调用IDEA自动实现（推荐）

     > Object类中equals()的默认实现就是 ==，比较的是两个对象的地址值
      源码：public boolean equals(Object obj) { return (this == obj); }
    > 所以：要想比较两个对象的"内容"是否相同，必须重写equals()
    > String、Date、File、包装类等都重写了equals()，比较的是实体内容
    > 重写equals()时需要遵循的5个约定（针对非null的x、y、z）：
        > 自反性：x.equals(x) 必须返回true
        > 对称性：x.equals(y) 为true，则 y.equals(x) 也必须为true
        > 传递性：x.equals(y) 且 y.equals(z) 为true，则 x.equals(z) 必须为true
        > 一致性：只要对象没被修改，多次调用结果必须一致
        > 非空性：x.equals(null) 必须返回false
    > 重要：重写equals()时必须同时重写hashCode()
      否则把对象放入HashMap、HashSet时会出现"内容相同却被当作两个元素"的问题
      （虽然hashCode()暂时不深入，但这条规则要先记住）
     */
    public static void main(String[] args) {
        User u1 = new User("Tom", 12);
        User u2 = new User("Tom", 12);
        // 重写equals()前：false（Object的equals()比较地址值）
        // 重写equals()后：true（比较name和age的内容）
        System.out.println(u1.equals(u2));

        String str1 = new String("hello");
        String str2 = new String("hello");
        System.out.println(str1 == str2); // false，两个不同的堆对象
        System.out.println(str1.equals(str2)); // true，String重写了equals()，比较内容

        File file1 = new File("hello.txt");
        File file2 = new File("hello.txt");
        System.out.println(file1 == file2); // false，两个不同的堆对象
        System.out.println(file1.equals(file2)); // true，File重写了equals()，比较路径
    }
}

class User {
    String name;
    int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 重写equals()：手动实现
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//
//        if (obj instanceof User) {
//            User user = (User) obj;
//            // 方式1
//            if (this.name == user.name && this.age == user.age) {
//                return true;
//            }
//            return false;
//
//            // 方式2
//            return this.name == user.name && this.age == user.age;
//        }
//        return false;
//    }

    // IDEA自动实现 cmd + N → equals() and hashCode()
    // 新版 IDEA 默认不再生成 if (this == o) return true;
    // 这一行是"自反性短路优化"：如果两个引用指向同一个对象，直接返回 true，
    // 省去后面的 getClass() 比较、强转和逐字段比较。
    // 它只影响性能，不影响正确性——因为 this == o 时，后面的字段比较必然也全部相等。
    // 生成时可以勾选相关选项加回来，或者手动补上；面试/规范写法一般都保留它。
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        // 1. 排除 null，并保证运行时类型完全一致（getClass() 比较不允许子类与父类相等）
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // 2. 类型已确认，安全向下转型
        User user = (User) o;
        // 3. 逐个比较关键属性：
        //    基本数据类型 age 用 ==；
        //    引用类型 name 用 Objects.equals()，它内部做了 null 判断，避免 NPE。
        return age == user.age && Objects.equals(name, user.name);
    }
}
