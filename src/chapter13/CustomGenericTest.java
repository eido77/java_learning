package chapter13;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CustomGenericTest {
    /*
    1. 自定义泛型类\接口
    1.1 格式
    class A<T> {
    }

    interface B<T1, T2> {
    }
    1.2 使用说明
    ① 我们在声明完自定义泛型类以后，可以在类的内部（比如：属性、方法、构造器中）使用类的泛型。
    ② 我们在创建自定义泛型类的对象时，可以指明泛型参数类型。一旦指明，内部凡是使用类的泛型参数的位置，都具体化为指定的类的泛型类型。
    ③ 如果在创建自定义泛型类的对象时，没有指明泛型参数类型，那么泛型将被擦除，泛型对应的类型均按照Object处理，但不等价于Object。
        * 擦除后取出的是 Object，但编译器仍按声明处的规则做检查，且带泛型的引用与裸类型在编译期行为不同。
    - 经验：泛型要使用一路都用。要不用，一路都不要用。
    ④ 泛型的指定中必须使用引用数据类型。不能使用基本数据类型，此时只能使用包装类替换。
    ⑤
    除创建泛型类对象外，子类继承泛型类时、实现类实现泛型接口时，也可以确定泛型结构中的泛型参数。比如：SubOrder2
    如果我们在给泛型类提供子类时，子类也不确定泛型的类型，则可以继续使用泛型参数。比如：SubOrder3
    我们还可以在现有的父类的泛型参数的基础上，新增泛型参数。比如：比如：SubOrder4,SubOrder5
     * 说明⑤补充：这里罗列了子类继承泛型父类的四种情况，建议对照理解——
     *  情况一（SubOrder1）：子类继承时不写泛型（用 extends Order 这种“裸类型”），子类不是泛型类，父类泛型按 Object 处理；
     *  情况二（SubOrder2）：子类继承时把父类泛型定死（extends Order<Integer>），子类不是泛型类；
     *  情况三（SubOrder3）：子类继承时把自己的泛型传给父类（extends Order<T>），子类是泛型类；
     *  情况四（SubOrder4/SubOrder5）：子类在父类泛型基础上再新增自己的泛型参数，子类是泛型类。
    1.3 注意点
    ① 泛型类可能有多个参数，此时应将多个参数一起放在尖括号内。比如：<E1,E2,E3>
    ② JDK7.0 开始，泛型的简化操作：ArrayList<Fruit> flist = new ArrayList<>();
     * 注意点②补充：new ArrayList<>() 后面的空尖括号 <> 叫“菱形运算符（diamond operator）”，
     * 作用是让编译器根据左边声明的类型自动推断右边的泛型，省去重复书写，这属于“类型推断”机制。
    ③ 如果泛型结构是一个接口或抽象类，则不可创建泛型类的对象。
    ④ 不能使用new E[]。但是可以：E[] elements = (E[])new Object[capacity];
    参考：ArrayList源码中声明：Object[] elementData，而非泛型参数类型数组。
     * 注意点④补充：为什么不能 new E[]？因为泛型在编译后会“类型擦除”，运行时 E 的真实类型已丢失，
     * JVM 无法确定要创建哪种类型的数组，所以只能创建 Object[] 再强转，这一步强转会有未检查警告。
    ⑤ 在类/接口上声明的泛型，在本类或本接口中即代表某种类型，但不可以在静态方法中使用类的泛型。
    ⑥ 异常类不能是带泛型的。
    2. 自定义泛型方法
    2.1 问题：在泛型类的方法中，使用了类的泛型参数。那么此方法是泛型方法吗？
     * 2.1 的回答：不是。判断是不是“泛型方法”的唯一标准是——方法自己是否在返回值前声明了 <T>。
     * 仅仅是在方法里用了“类上声明的泛型”（比如 getT() 用了类的 T），那只是普通方法用了类的泛型，不算泛型方法。
    2.2 格式
    权限修饰符 <T> 返回值类型 方法名(形参列表){ // 通常在形参列表或返回值类型的位置会出现泛型参数T
    }
    2.3 举例
    public <E> E method(E e){
    }
    2.4 说明
    > 声明泛型方法时，一定要添加泛型参数<T>
    > 泛型参数在方法调用时，指明其具体的类型
    > 泛型方法可以根据需要声明为static的
    > 泛型方法所属的类是否是一个泛型类，都可以。
     */
    @Test
    public void test1() {
        // p1 演示“非泛型类不能加尖括号”
        Person p1 = new Person();
        // Person 只是普通类，类名后没有声明 <T>，所以不能用 Person<String> 这种带尖括号的写法。
//        Person<String> p2 = new Person<>();

        // list 演示“带泛型的集合”只能加声明类型
        ArrayList<String> list = new ArrayList<>();
        list.add("AA");
//        list.add(123);

        // List1 演示“裸类型（不写泛型）”出于向下兼容仍可用，但会丢失类型检查。
        // jdk5.0之前集合是没有声明为泛型的。JDK5.0 才引入泛型，为兼容旧代码，允许使用不带泛型的“裸类型”，但编译器会给未检查警告，
        // 且失去了编译期类型检查，运行时更易出错，实际开发不推荐这样写。
        ArrayList list1 = new ArrayList();
        list1.add(123);
        list1.add("AA");
    }

    /**
     * 测试自定义的泛型类
     */
    @Test
    public void test2() {
        // 实例化时，就可以指明类的泛型参数的类型
        // Order<T> 是泛型类，创建对象时不写 <..>（即裸类型）语法上允许，
        // 但会丢失类型检查，getT() 返回的会被当作 Object。
        Order order = new Order();
        // 不完全等价。Order order（裸类型）会关闭泛型检查，行为退化，编译器给未检查警告；
        // Order<Object> order3 是明确指定为 Object，仍受泛型检查约束。取值时都得到 Object，但编译期语义不同。
        // Order<Object> order3 = new Order<>();

        Object obj = order.getT();

        // 泛型参数在指明时，是不可以使用基本数据类型的！但是可以使用包装类替代基本数据类型。
//        Object<int> order1 = new Order<>();
        Order<Integer> order2 = new Order<>();
        Integer t = order2.getT();

        Order<String> order3 = new Order<>();
        order3.setT("AA");
    }

    // 测试Order的子类
    @Test
    public void test3() {
        // 实例化SubOrder1
        SubOrder1 sub1 = new SubOrder1();
        Object t = sub1.getT();


        // SubOrder1不是泛型类，此处编译错误
        //   “不是泛型类就一定不能用泛型吗？” —— 对象层面确实不能加尖括号；
        //   “那能用泛型的都是泛型类？” —— 用“类上声明的泛型”的必须是泛型类，但泛型方法是例外（方法自己声明 <T>，所属类不必是泛型类）。
//        SubOrder1<Iterator> sub2 = new SubOrder1<>();
    }

    @Test
    public void test4() {
        SubOrder2 sub2 = new SubOrder2();
        // SubOrder2 继承时把父类泛型定死为 Order<Integer>，所以从父类继承来的 getT() 返回 Integer，
        // 这是“继承时确定父类泛型”，与“SubOrder2 自己是不是泛型类”是两回事——它自己没声明 <T>，所以不是泛型类。
        // 作用：当子类只服务于某一具体类型时，这样写能固定类型、对外更简洁，调用方不用再指定泛型。
        Integer t = sub2.getT();

        SubOrder3<String> sub3 = new SubOrder3<>();
        String t1 = sub3.getT();
        sub3.show("AA");

        // SubOrder4<E> extends Order<Integer> —— 父类泛型被定死成 Integer，所以 getT() 是 Integer；
        // 而 E 是 SubOrder4 自己新增的泛型，实例化时被指定为 String，所以 getE() 是 String。两者来源不同，故类型不同。
        SubOrder4<String> sub4 = new SubOrder4<>();
        Integer t2 = sub4.getT();
        String e = sub4.getE();

        // 尖括号里泛型参数的名字和个数可以自定义，SubOrder5<T,E> 声明了两个，
        //     使用时按声明顺序一一对应：<String,Integer> 里 T=String、E=Integer，顺序不能乱。
        // T、E 是“类型占位符/参数”，本身不是具体类型，好比“形参”，实例化时才被具体类型“实参”替换，
        //     所以不是“从 T 里面找东西”，而是“外部传进来什么类型，T 就代表什么类型”。
        SubOrder5<String, Integer> sub5 = new SubOrder5<>();
        String t3 = sub5.getT();
        Integer e1 = sub5.getE();
    }

    // 测试泛型方法的使用
    @Test
    public void test5() {
        // 这里的 <String> 对本例无所谓 —— 因为下面调用的 copyFromArrayToList 是“泛型方法”，
        // 它的类型由传入的实参（Integer[]）单独推断，跟 order1 自身的类泛型 <String> 无关。
        // Order 确实是泛型类，规范上创建对象应指明泛型；此处写 <String> 是规范的，只是它不参与后面这次调用而已。
        Order<String> order1 = new Order<>();

        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        // 因为 copyFromArrayToList 是泛型方法，它的泛型 E 会根据实参 arr（Integer[]）被推断为 Integer，
        // 所以返回值就是 ArrayList<Integer>。这就是“泛型方法在调用时确定类型”。
        ArrayList<Integer> integers = order1.copyFromArrayToList(arr);

        for (Integer integer:integers) {
            System.out.println(integer);
        }

    }
}

class Person {
}

/**
 * 泛型类
 */
// 尖括号里可以是任意个、任意命名的泛型参数，如 <T>、<K,V>、<E1,E2,E3>，<T> 只是最常见的单参数写法。
class Order<T> {
    // 声明了类的泛型参数以后，就可以在类的内部使用此泛型参数。
    // T 是“类型参数（占位符）”，代表一种“待定类型”，实例化时才被具体类型替换。
    // 用单个大写字母（如 T/E/K/V）只是命名惯例，并非强制，但强烈建议遵守，是规范写法。
    T t;
    int orderId;

    public Order() {
    }

    public Order(T t, int orderId) {
        this.t = t;
        this.orderId = orderId;
    }

    // getT、setT 只是“用了类上声明的泛型 T”的普通方法，
    // 方法自己没有在返回值前声明 <..>，所以不是泛型方法。
    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "t=" + t +
                ", orderId=" + orderId +
                '}';
    }

    // 不可以在静态方法中使用类的泛型
    // 类上声明的泛型 T 是随“对象”确定的（创建对象时才指定类型），
    // 而静态方法属于类、不依赖对象、在没有对象时就可调用，此时 T 还没确定，所以静态方法里不能用类的泛型 T。
//    public static void method1() {
//        System.out.println("t : " + t);
//    }

    // 自定义泛型方法
    // 泛型 <E> 放在返回值前面，是为了“先声明这个方法自己拥有的类型参数”，编译器才知道后面出现的 E 是类型参数而非某个类。
    // 不能放到方法后面。它的作用是让方法在“调用时”根据实参推断类型。
    // 与普通方法的区别：泛型方法有自己独立的类型参数，类型在“调用时”确定，且不依赖类是否为泛型类。
    public <E> E method(E e) {
        return null;
    }

    // 定义泛型方法，将E[]数组元素添加到对应类型的ArrayList中，并返回
    // 泛型方法可以是 static。“不能用 static”指的是“类上声明的泛型 T 不能用在静态方法里”；
    // 而泛型方法的 <E> 是方法自己声明的、调用时才确定，与对象无关，所以可以加 static
    public <E> ArrayList<E> copyFromArrayToList(E[] arr) {
        ArrayList<E> list = new ArrayList<>();
        for (E e : arr) {
            list.add(e);
        }

        return list;
    }
}

// “异常类不能带泛型”。加上 <T> 就报错，是因为异常需要被 catch 捕获，
// 而泛型在运行时会被擦除，JVM 无法区分 MyException<String> 与 MyException<Integer>，catch 就无法正确匹配，
// 所以 Java 直接禁止“可抛出类（Throwable 的子类）”使用泛型。
//class MyException<T> extends Exception {
//}

// SubOrder1不是泛型类
// 继承时如果不给父类指定泛型（用裸类型 extends Order），子类就不是泛型类，父类的 T 按 Object 处理。
// 若想让子类保留泛型能力，应写成 extends Order<T> 并在子类名后声明 <T>（见 SubOrder3）。
class SubOrder1 extends Order {
}

// SubOrder2不是泛型类
// extends Order<Integer> 是“把父类的泛型定死为 Integer”，这属于“继承时确定父类泛型”，
// 但 SubOrder2 类名后自己没有声明 <T>，所以它自身不是泛型类。
// “继承了泛型类”和“自己是不是泛型类”是两码事：前者说父类，后者看自己类名后有没有 <..>。
class SubOrder2 extends Order<Integer> {
    // 泛型方法所属的类不一定是泛型类（普通类里也能写泛型方法）。
    // 泛型类里的方法也不一定要是泛型方法 —— 只用类上泛型 T 的方法（如 getT）就是普通方法。
//    public <E> ArrayList<E> copyFromArrayToList(E[] arr) {
//        ArrayList<E> list = new ArrayList<>();
//        // 数组不能用迭代器？为什么呢？这里可以用迭代器吗？
//        for (E e : arr) {
//            list.add(e);
//        }
//
//        return list;
//    }
}

// SubOrder3是泛型类
// 类名后声明了 <T>（class SubOrder3<T>），只要类名后带 <..> 就是泛型类。
// 这个 T 是“待定类型”，实例化时才确定（如 SubOrder3<String>）；子类不想定死类型时，
// 就把自己的 T 继续传给父类 extends Order<T>，父类也用这个 T。不能“空着不写”，要么传具体类型，要么传泛型参数 T。
class SubOrder3<T> extends Order<T> {
    // T t 放在圆括号里是“方法的形参声明” —— T 是参数类型、t 是参数名。
    // 这里 T 是类上声明的泛型，show 方法接收一个 T 类型的参数并打印它。
    public void show(T t) {
        System.out.println(t);
    }
}

// SubOrder4是泛型类
class SubOrder4<E> extends Order<Integer> {
    E e;

    public SubOrder4() {
    }

    public SubOrder4(E e) {
        this.e = e;
    }

    // Integer integer 对应父类 Order(T t, int orderId) 中的 T
    // 因为本类 extends Order<Integer>，父类的 T 已被定死为 Integer，所以 super(integer, orderId) 传的第一个参数必须是 Integer。
    public SubOrder4(Integer integer, int orderId, E e) {
        super(integer, orderId);
        this.e = e;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }
}

// SubOrder5是泛型类
// 类名后声明了 <T, E>，带 <..> 即为泛型类。
// T, E 不要求一起用，用哪个都行：这里 T 传给父类 Order<T>，E 用作本类字段类型
class SubOrder5<T, E> extends Order<T> {
    E e;

    public SubOrder5() {
    }

    public SubOrder5(T t, int orderId, E e) {
        super(t, orderId);
        this.e = e;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }
}
