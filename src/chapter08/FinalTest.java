package chapter08;


public class FinalTest {
    /*
    final关键字的使用
    1. final的理解：最终的
    2. final可以用来修饰的结构：类、方法、变量
    3. 具体说明：
    3.1 final修饰类：表示此类不能被继承。
        比如：String、StringBuffer、StringBuilder类
    3.2 final修饰方法：表示此方法不能被重写
        比如：Object类中的getClass()
    3.3 final修饰变量：既可以修饰成员变量，也可以修饰局部变量。
        此时的"变量"其实就变成了"常量"，意味着一旦赋值，就不可更改。
        （命名规范：常量名建议全大写，多个单词用下划线连接，如 MAX_VALUE）
        3.3.1 final修饰成员变量: 有哪些位置可以给成员变量赋值？
            > 显式赋值（声明的同时赋值）
            > 代码块中赋值
            > 构造器中赋值
            注意：这三个位置至少要选一个赋值，且只能赋值一次。
            如果不赋值，编译报错：not initialized。
       3.3.2 final修饰局部变量：一旦赋值就不能修改
            > 方法内声明的局部变量：在调用局部变量前，一定需要赋值。而且一旦赋值，就不可更改
            > 方法的形参：在调用此方法时，给形参进行赋值。而且一旦赋值，就不可更改
    4. final与static搭配：修饰成员变量时，此成员变量称为：全局常量。
       比如：Math的PI
    5. 特别注意（易错点）：
    final修饰引用数据类型的变量时，"不可变"指的是这个引用（地址）不能再指向
    别的对象，但对象内部的属性值是可以修改的。（见下方题目2）
     */
    public static void main(String[] args) {
        E e = new E();
        // cannot assign a value to final variable MIN_SCORE
        // 原因：MIN_SCORE 被 final 修饰，已赋值0，不能再改
//        e.MIN_SCORE = 1;

        E e1 = new E(10);
        // cannot assign a value to final variable LEFT
        // LEFT 已经在构造器中完成赋值，不能再次赋值。
//        e1.LEFT = 11;
    }
}

// final修饰类：A 不能被继承
final class A {
}

// 编译错误：cannot inherit from final A，因为 A 是 final 的，B 试图继承它 → 报错
//class B extends A {
//}

// final修饰方法：method() 不能被子类重写
class C {
    public final void method() {
    }
}

class B extends C {
    // java: method() in chapter08.B cannot override method() in chapter08.C
    //  overridden method is final
    // 原因：父类 C 的 method() 是 final，子类不能重写
//    @Override
//    public void method() {
//    }
}

// final修饰成员变量：演示三种赋值位置
class E {
    // 成员变量
    // variable MIN_SCORE1 not initialized in the default constructor
    // 原因：final成员变量必须赋值，没赋值，所以报错
//    final int MIN_SCORE1;
    // 显式赋值
    final int MIN_SCORE = 0;

    // 代码块赋值
    // 提示：这里的 MAX_SCORE 是代码块里的"局部变量"，不是成员变量
    // 如果想演示"代码块给成员变量赋值"，应该在类里先声明
    {
        final int MAX_SCORE = 100;

    }

    // 构造器中赋值
    // 注意：如果在构造器赋值，那么每个构造器都必须给它赋值
    final int LEFT;

    public E() {
        LEFT = 2;
    }

    public E(int left) {
        LEFT = left;
    }
}

// final修饰局部变量
class F {
    public void method() {
        // 局部变量可以先声明后赋值，但赋值后不能再改
//        final int num = 10;
        final int num;
        num = 10;
        System.out.println(num);
    }

    public void method2(final int num) {
        // final parameter num may not be assigned
        // 原因：形参被 final 修饰，方法内不能再改变它的值
//        num++;
        System.out.println(num);
    }
}


// 题目1：排错
class Something {
    public int addOne(final int x) {
        // final parameter x may not be assigned
        // ++x 会修改 x 的值，而 x 是 final 的，所以报错
//        return ++x;
        // x值没变
        // x + 1 只是用 x 计算出一个新值返回，并没有修改 x 本身，所以正确
        return x + 1;
    }
}

// 题目2：排错（核心考点：final 修饰引用类型）
class Something1 {
    public static void main(String[] args) {
        Other o = new Other();
        new Something1().addOne(o);
    }

    public void addOne(final Other o) {
        // o = new Other();
        // 上面这行会报错：o 是 final，不能再指向新对象
        o.i++;
        // 但这行正确：o 指向的对象没变，只是改了对象内部的属性 i
    }
}

class Other {
    public int i;
}
