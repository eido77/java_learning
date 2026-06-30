package chapter06;

public class OrderTest {
    public static void main(String[] args) {
        Order2 order = new Order2();

        // ========== 同包内（当前真实情况，能编译通过）==========
        // 调用属性（同包内）
        order.orderDefault = 1; // 缺省：同包可访问 ✓
        order.orderPublic = 2; // public：可访问 ✓
        // orderPrivate has private access in chapter06.Order2
//        order.orderPrivate = 3; // private：类外不可访问 ✗

        // 调用方法（同包内）
        order.methodDefault(); // 缺省：✓
        order.methodPublic(); // public：✓
        // methodPrivate() has private access in chapter06.Order2
//        order.methodPrivate(); // private：类外不可访问 ✗


        /*
         * ========== 跨包（其他包）的访问情况 ==========
         * （宋老师课件用 test1 / test2 两个包真实演示这个）
         *
         * 【第一道坎 · 类级别】
         *   要跨包用 Order2，它必须是 public class，否则别的包根本看不到这个类：
         *       Order2 order1 = new Order2();
         *       → 报错：Order2 is not public, cannot be accessed from outside package
         *   即使 import 导包也没用——类不是 public，包外就是不可见。
         *   （课件里 test1 的 Order 就是 public class，所以 test2 能 import 并 new 出来。）
         *
         * 【第二步 · 成员级别】假设 Order2 已是 public class，能进到类里之后：
         *   order.orderPublic    = 1;   // public：✓ 跨包唯一畅通的
         *   order.methodPublic();       // public 方法：✓
         *
         *   order.orderDefault   = 2;   // 缺省：✗ 跨包就失效（关键区别！同包能用，跨包不行）
         *   order.methodDefault();      // 缺省方法：✗
         *   order.orderPrivate   = 3;   // private：✗ 任何包外都不行
         *   order.methodPrivate();      // private 方法：✗
         *   order.orderProtected = 4;   // protected：✗ 非子类不可访问（只有子类才行）
         *   order.methodProtected();    // protected 方法：✗（同上，要靠继承）
         *   // protected 课件 test2 没演示，因为它跨包要靠「子类继承」才能用，
         *   // 属于后面继承章节的内容，这里先跳过。
         */

        /*
         * ========== 总结（结合同包 + 跨包两种测试）==========
         *   同包内       → 除 private 外，缺省 / protected / public 都能用
         *   跨包非子类   → 只剩 public 能用；缺省 / private / protected 都被挡
         *   跨包子类     → public ✓、protected ✓（等学继承再验证）
         *
         *   一句话：缺省(default) 是「本包限定」，跨包就失效，
         *           这是它和 public 最大的区别。
         */


    }
}
