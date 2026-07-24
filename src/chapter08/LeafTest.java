package chapter08;

public class LeafTest {
    public static void main(String[] args) {
        /**
         * 类的初始化顺序演示：由父及子，静态先行。
         *
         * 执行规律：
         * 1. 类首次主动使用时触发初始化，静态初始化块按"父类 → 子类"顺序执行，且全程只执行一次；
         * 2. 创建实例时，先执行父类的（普通初始化块 + 构造器），再执行子类的；
         * 3. 普通初始化块总是在同一个类的构造器体之前执行；
         * 4. this(...) 只在同类内跳转，不会重复触发普通初始化块。
         */
        // 第一次 new：触发类初始化（静态块）+ 实例初始化
        new Leaf();

        // 第二次 new：类已初始化完毕，静态块不再执行，只走实例初始化流程
		System.out.println();
		new Leaf();
        /*
        Root的静态初始化块
        Mid的静态初始化块
        Leaf的静态初始化块
        Root的普通初始化块
        Root的无参数的构造器
        Mid的普通初始化块
        Mid的无参数的构造器
        Mid的带参数构造器，其参数值：尚硅谷
        Leaf的普通初始化块
        Leaf的构造器

        Root的普通初始化块
        Root的无参数的构造器
        Mid的普通初始化块
        Mid的无参数的构造器
        Mid的带参数构造器，其参数值：尚硅谷
        Leaf的普通初始化块
        Leaf的构造器
         */
    }
}

class Root{
    static{
        System.out.println("Root的静态初始化块");
    }
    {
        System.out.println("Root的普通初始化块");
    }
    public Root(){
        super(); // 隐式调用 Object 的构造器，写不写效果相同
        System.out.println("Root的无参数的构造器");
    }
}
class Mid extends Root{
    static{
        System.out.println("Mid的静态初始化块");
    }
    {
        System.out.println("Mid的普通初始化块");
    }
    public Mid(){
        // 此处省略的 super() 会调用 Root()
        System.out.println("Mid的无参数的构造器");
    }
    public Mid(String msg){
        // 通过this调用同一类中重载的构造器
        this(); // 调用本类重载构造器；此时不再隐式调用 super()，避免父类被初始化两次
        System.out.println("Mid的带参数构造器，其参数值："
                + msg);
    }
}
class Leaf extends Mid{
    static{
        System.out.println("Leaf的静态初始化块");
    }
    {
        System.out.println("Leaf的普通初始化块");
    }
    public Leaf(){
        // 通过super调用父类中有一个字符串参数的构造器
        super("尚硅谷"); // 显式调用父类带 String 参数的构造器
        System.out.println("Leaf的构造器");
    }
}
