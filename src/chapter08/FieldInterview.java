package chapter08;

public class FieldInterview {
    public static void main(String[] args) {
        // ========== Test1：静态初始化块 + 实例初始化块 + 构造器的执行顺序 ==========
        new HelloB();
         /*
        执行顺序规则：
        1. 先加载父类，执行父类的 static 静态块（只执行一次，类加载时）
        2. 再加载子类，执行子类的 static 静态块（只执行一次，类加载时）
        3. 创建对象时，先执行父类的【实例初始化块】，再执行父类的【构造方法】
        4. 然后执行子类的【实例初始化块】，再执行子类的【构造方法】

        输出结果：
        static A     -> 父类静态块（类加载，只执行一次）
        static B     -> 子类静态块（类加载，只执行一次）
        I'm A Class  -> 父类实例初始化块
        HelloA       -> 父类构造方法
        I'm B Class  -> 子类实例初始化块
        HelloB       -> 子类构造方法
        */
        System.out.println();

        // ========== Test2：静态变量默认值 + 静态代码块 + 自增自减运算 ==========
        System.out.println("x=" + x);
        // x, y, z 是 static int，默认初始值为 0
        // 类加载时会先执行两个 static 块：
        //   第一个静态块里定义的是局部变量 x（和成员变量x无关，作用域仅在块内），
        //     int x = 5; x--; 结果只是局部变量x变成4，不影响成员变量x
        //   第二个静态块： x--; 这里的x就是成员变量x，此时成员变量x（默认0）自减1，变成 -1
        // 所以打印 x=-1

        // z 默认是 0，自减后 z = -1
        z--;

        // method() 方法内容：y = z++ + ++z;
        // 运算顺序（从左到右）：
        //   1. z++  取当前z的值参与运算（此时z=-1），然后z自增为0   → 该项的值是 -1
        //   2. ++z  z先自增为1，然后取值参与运算                 → 该项的值是 1
        //   3. y = -1 + 1 = 0
        // 执行完 method() 后：z = 1，y = 0
        method();
        System.out.println("result:" + (z + y + ++z));
        // 此时 z=1, y=0
        // 表达式 z + y + ++z 运算顺序（从左到右逐步取值）：
        //   1. 取 z 的值 = 1   （第一个z，此时还没变化）
        //   2. 取 y 的值 = 0
        //   3. ++z ：z 先自增为2，取值 = 2   （第二个z，即++z）
        //   4. 相加：1 + 0 + 2 = 3
        // 所以输出 result:3

        /*
        最终输出：
        x=-1
        result:3
        */
        System.out.println();

        // ========== Test3：构造方法中调用被子类重写的方法（多态陷阱） ==========
        Sub1 s = new Sub1();
        /*
        创建 Sub1 对象时的执行流程：

        1. 因为 Sub1 继承 Base，所以先执行 Base 的构造过程：
           a. 先执行 Base 的【实例初始化块】 -> 打印 "base"
           b. 再执行 Base 的【构造方法】Base()，构造方法内部调用了 method(100)
              注意：这里调用的 method 是【非静态方法】，Java 中非静态方法调用遵循"动态绑定"（多态），
              也就是说：虽然是在 Base 的构造方法里调用，但实际调用的是 【子类 Sub1 重写后的 method】
              （因为此时对象已经确定是 Sub1 类型，虚拟机按实际对象类型去找方法）
              所以打印 "sub : 100"   而不是 "base : 100"

        2. Base 构造完成后，继续执行 Sub1 自己的部分：
           a. 先执行 Sub1 的【实例初始化块】 -> 打印 "sub"
           b. 再执行 Sub1 的【构造方法】Sub1()，构造方法内部显式调用 super.method(70)
              super.method(70) 明确指定调用父类 Base 的 method 方法（不走多态）
              所以打印 "base : 70"

        最终输出：
        base
        sub : 100
        sub
        base : 70
        */
    }
    // Test2
    static int x, y, z;

    static {
        int x = 5;
        x--;
    }

    static {
        x--;
    }

    public static void method() {
        y = z++ + ++z;
    }
}

// Test1
class HelloA {
    public HelloA() {
        System.out.println("HelloA");
    }

    {
        System.out.println("I'm A Class");
    }

    static {
        System.out.println("static A");
    }
}

class HelloB extends HelloA {
    public HelloB() {
        System.out.println("HelloB");
    }

    {
        System.out.println("I'm B Class");
    }

    static {
        System.out.println("static B");
    }
}

// Test3
class Base{
    Base(){
        method(100);
    }
    {
        System.out.println("base");
    }
    public void method(int i){
        System.out.println("base : " + i);
    }
}
class Sub1 extends Base{
    Sub1(){
        super.method(70);
    }
    {
        System.out.println("sub");
    }
    public void method(int j){
        System.out.println("sub : " + j);
    }
}
