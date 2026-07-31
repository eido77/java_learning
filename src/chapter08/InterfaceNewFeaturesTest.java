package chapter08;

public class InterfaceNewFeaturesTest {
    public static void main(String[] args) {
        /*
         * ======================================================================
         * 【本类主题】JDK8/JDK9 接口新特性演示
         *   - JDK8 : 接口中可以定义【静态方法】和【默认方法】
         *   - JDK9 : 接口中可以定义【私有方法】
         * ======================================================================
         */

        // 知识点1：接口中的【静态方法】只能通过“接口名”调用，不能通过实现类（对象或类名）调用。
        CompareA.method1(); // CompareA:1
        // 编译报错，静态方法不会被实现类继承，所以不能用实现类调用
//        SubClass.method1();

        // 知识点2：接口中的【默认方法】可以被实现类继承。
        //   - 实现类没重写：调用接口中的默认方法
        //   - 实现类重写了：调用实现类自己重写后的方法
        SubClass s1 = new SubClass();
        s1.method2(); // SubClass:method2（因为 SubClass 重写了 method2）

        // 知识点3：接口冲突问题。
        //   当实现类实现了两个接口，且这两个接口中有【同名同参数】的默认方法时，
        //   实现类【必须重写】该方法，否则编译报错。
        // class SubClass implements CompareA, CompareB {
        s1.method3(); // SubClass:method3（SubClass 重写了 method3 解决了冲突）

        // 知识点4：类优先原则。
        //   子类（实现类）既继承了父类，又实现了接口，且父类的方法与接口的默认方法【同名同参数】时：
        //   若子类没重写，则优先调用【父类】中的方法（“类优先原则”）。
        //   注意：本例中 SubClass 重写了 method4，所以最终调用的是子类自己的方法。
        // System.out.println("SubClass:method4");写之前是 SuperClass:4
        s1.method4(); // SubClass:method4
    }
}

interface CompareA {
    /*
    属性：默认就是 public static final（常量）
    方法：
             JDK8 之前：只能是【抽象方法】(public abstract)
             JDK8     ：新增【静态方法】、【默认方法】
             JDK9     ：新增【私有方法】
     */

    // JDK8 静态方法：只能用接口名调用（对应知识点1）
    public static void method1() {
        System.out.println("CompareA:1");
    }

    // JDK8 默认方法：可被实现类继承或重写（对应知识点2）
    public default void method2() {
        System.out.println("CompareA:2");
    }

    // JDK8 默认方法：与 CompareB 中同名同参数，会产生接口冲突（对应知识点3）
    public default void method3() {
        System.out.println("CompareA:3");
    }

    // JDK8 默认方法：与父类 SuperClass 中同名同参数，涉及类优先原则（对应知识点4）
    public default void method4() {
        System.out.println("CompareA:4");
    }

    // JDK9 私有方法：仅供接口内部使用，外部无法访问
    //说明：私有方法的作用——只能在接口【内部】被其他默认/静态方法调用，用于抽取默认方法中的公共代码，对外不可见
    private void method6() {
        System.out.println("接口中定义的私有方法");
    }
}

// 接口 CompareB：与 CompareA 存在同名同参数的默认方法 method3（用于演示接口冲突）
interface CompareB {
    public default void method3() {
        System.out.println("CompareB:3");
    }
}

// 这是最初的写法，后来改成下面同时继承父类+实现两个接口
//class SubClass implements CompareA {
class SubClass extends SuperClass implements CompareA, CompareB {
    @Override // 重写接口的默认方法（知识点2）
    public void method2() {
        System.out.println("SubClass:method2");
    }

    @Override // 必须重写以解决 CompareA、CompareB 的接口冲突（知识点3）
    public void method3() {
        System.out.println("SubClass:method3");
    }

    @Override // 重写方法（知识点4，覆盖掉父类/接口的同名方法）
    public void method4() {
        System.out.println("SubClass:method4");
    }

    public void method5() {
        // 知识点5：在子类（实现类）中，如何分别调用【自己】/【父类】/【各接口】中被重写的方法
        // 调用【本类】重写的方法         --> SubClass:method4
        method4();
        // 调用【父类 SuperClass】的方法  --> SuperClass:4
        super.method4();
        // 调用【本类】重写的方法          --> SubClass:method3
        method3();
//        CompareA.method3(); // 错误写法，接口名直接调用只能用于“静态方法”，默认方法不行
        // 调用【接口 CompareA】的默认方法 --> CompareA:3
        CompareA.super.method3();
        // 调用【接口 CompareB】的默认方法 --> CompareB:3
        CompareB.super.method3();
        // 注意调用接口默认方法的固定语法格式是 “接口名.super.方法名()”

    }
}

// 父类：其 method4 与接口 CompareA 的默认方法同名同参数（用于演示知识点4：类优先原则）
class SuperClass {
    public void method4() {
        System.out.println("SuperClass:4");
    }
}
