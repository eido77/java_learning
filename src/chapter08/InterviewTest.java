package chapter08;

public class InterviewTest {
    public static void main(String[] args) {
        // new Integer() 每次都在堆中创建新对象，两个引用地址不同
        Integer i = new Integer(1);
        Integer j = new Integer(1);
        System.out.println(i == j); // false ：比较的是对象地址，两个 new 出来的对象地址不同

        // 自动装箱与 IntegerCache（缓存范围 [-128, 127]），底层调用 Integer.valueOf()
        // valueOf() 中：值在 [-128, 127] 内会直接复用 IntegerCache.cache 数组中的对象
        Integer m = 1; // 自动装箱，值 1 在缓存范围内 → 复用缓存对象
        Integer n = 1; // 同样复用同一个缓存对象
        System.out.println(m == n); // true：m、n 指向缓存中同一个对象，地址相同

        // 128 超出了缓存范围 [-128, 127]，valueOf() 会 new 一个新对象
        Integer x = 128;
        Integer y = 128;
        System.out.println(x == y); // false ：超出缓存范围，各自 new 新对象，地址不同

        // 基本类型之间比较：会先自动类型提升（int → double），比较的是数值
        int i1 = 10;
        double d1 = 10.2;
//        System.out.println(i1 == d1); // false：i 提升为 10.0，与 10.2 数值不相等

        // 两个不同的包装类之间不能用 == 比较（Integer 和 Double 没有继承/转换关系）
        Integer i11 = 10;
        Double d11 = 10.2;
//		System.out.println(i11 == d11); // 编译报错：Integer 与 Double 类型不兼容，无法用 == 比较

        // 包装类 与 基本类型 比较：包装类会自动拆箱为基本类型，再按数值比较
        Integer m1 = 1000;
        double n1 = 1000;
        System.out.println(m1 == n1); // true ：m1 拆箱为 int，再提升为 double，数值相等

        // 同理：Integer 与 int 比较，Integer 自动拆箱后按数值比较（与缓存无关）
        Integer x2 = 1000;
        int y2 = 1000;
        System.out.println(x2 == y2); // true ：x2 拆箱为 int，数值相等（注意：这里不看 128 缓存）

        // 三元运算符就是一种简写的 if-else
        // 条件 ? 表达式1 : 表达式2;
        // 如果条件为 true，执行（返回）表达式1，如果条件为 false，执行（返回）表达式2

        // 三元运算符的类型统一：会将两个分支的结果提升为同一类型
        // 三元表达式两个分支类型不同（Integer / Double）时，
        // 编译器会做数值提升，把 Integer 也提升为 Double，所以结果是 1.0 而不是 1
        Object o1 = true ? new Integer(1) : new Double(2.0);
        System.out.println(o1); // 1.0 ：三元运算符统一为 double，Integer(1) 被拆箱并提升为 1.0

        // if-else 与三元运算符的区别：if-else 不会做类型统一
        // if-else 是普通语句，两个分支各自独立赋值，不存在类型提升
        Object o2;
        if (true)
            o2 = new Integer(1);
        else
            o2 = new Double(2.0);
        System.out.println(o2); // 1 ：直接就是 Integer(1)，不会被提升为 double
    }
}
