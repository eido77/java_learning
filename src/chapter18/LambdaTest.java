package chapter18;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Consumer;

public class LambdaTest {
    /*
    1. Lambda表达式的使用举例：
    Comparator<Integer> com = (o1, o2) -> Integer.compare(o1, o2);
    2. Lambda表达式的格式举例:
     Lambda = "抽象方法的形参列表 -> 抽象方法的方法体"，接口名、方法名都不用写。
        * 第 1 步：看目标类型是哪个函数式接口（等号左边的类型，或者方法参数要求的类型），例如 Consumer<String>。
        * 第 2 步：找到这个接口里那个唯一的抽象方法签名，例如 void accept(String s)。
        * 第 3 步：把这个方法的形参列表抄到 -> 左边（类型可以省），把方法体写到 -> 右边。
        *         方法有返回值就 return；只有一行语句时，{} 和 return 必须一起省略。
    3. Lambda表达式的格式
    -> : lambda操作符或箭头操作符
    -> 的左边: lambda形参列表，对应着要重写的接口中的抽象方法的形参列表。
    -> 的右边: lambda体，对应着接口的实现类要重写的方法的方法体。
    4. Lambda表达式的本质：
    > 一方面，lambda表达式作为接口的实现类的对象。  ---> "万事万物皆对象"
    > 另一方面，lambda表达式是一个匿名函数。
     * Lambda 和匿名内部类"像"但不等价。匿名内部类编译后会生成 XxxTest\$1.class 这种独立文件，
     * 而 Lambda 由 invokedynamic + 运行时生成实现，不产生 \$1 文件；
     * 另外 Lambda 里的 this 指的是外部类对象，匿名内部类里的 this 指匿名对象自己
    5. 函数式接口：
    5.1 什么是函数式接口？为什么需要函数式接口？
    > 如果接口中只声明有一个抽象方法，则此接口就称为函数式接口。
     * "有且只有一个抽象方法"。接口里可以同时有 default 方法、static 方法、private 方法，
     * 以及重写 Object 的 public 方法（如 equals/toString），这些都不参与计数，接口依然是函数式接口。
    > 因为只有给函数式接口提供实现类的对象时，我们才可以使用lambda表达式。
    5.2 api中函数式接口所在的包
    jdk8中声明的函数式接口都在java.util.function包下。
    5.3 4个基本的函数式接口
              接口            对应的抽象方法
    消费型接口：Consumer<T>     void accept(T t)   有参数、无返回值 → "只进不出"，像个消费者
    供给型接口：Supplier<T>     T get()            无参数、有返回值 → "只出不进"，像个供货方
    函数型接口：Function<T,R>   R apply(T t)       有参数、有返回值 → 输入 T 输出 R，就是数学上的函数 f(x)
    判断型接口：Predicate<T>    boolean test(T t)  有参数、返回 boolean → "断言/判断"，用于条件筛选
    6. Lambda表达式的语法规则总结
    -> 的左边：lambda形参列表，参数的类型都可以省略。如果形参只有一个，则一对()也可以省略。
     * 零参数时，() 绝对不能省，必须写成 () -> ...
    -> 的右边：lambda体，对应着重写的方法的方法体。如果方法体中只有一行执行语句，则一对{}可以省略。
              如果有return关键字，则必须一并省略。
     * 合法：(o1, o2) -> o1.compareTo(o2)                  （省了 {} 也省了 return）
     * 合法：(o1, o2) -> { return o1.compareTo(o2); }      （{} 和 return 都保留）
     * 非法：(o1, o2) -> return o1.compareTo(o2);          （去了 {} 却留着 return）
     * 非法：(o1, o2) -> { o1.compareTo(o2); }             （保留 {} 却漏了 return，返回值类型不匹配）
     */

    /**
     * Lambda表达式的使用举例
     */
    @Test
    public void test1() {
        Comparator<Integer> com1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };

        int compare1 = com1.compare(12, 21);
        System.out.println(compare1); // -1

        // Lambda表达式的写法
        Comparator<Integer> com2 = (Integer o1, Integer o2) -> Integer.compare(o1, o2);
        int compare2 = com2.compare(23, 21);
        System.out.println(compare2); // 1


        // 方法引用
        /*
         * 方法引用 = Lambda 的进一步简写。当 Lambda 体"只是调用一个已存在的方法，
         * 且参数原封不动地传过去"时，就可以用 类名/对象名::方法名 代替，连参数都不用写。
         * 本行等价于 (o1, o2) -> Integer.compare(o1, o2)，因为参数顺序、个数都对得上。
         * 四种常见格式：
         * (1) 类名::静态方法名        Integer::compare   ← 本行就是这种
         * (2) 对象名::实例方法名      System.out::println 等价于 s -> System.out.println(s)
         * (3) 类名::实例方法名        String::compareTo  等价于 (a, b) -> a.compareTo(b)
         *                            （第一个参数当调用者，其余参数当实参）
         * (4) 类名::new（构造器引用） ArrayList::new     等价于 () -> new ArrayList<>()
         * 注意 :: 中间不能有空格，而且方法引用不写括号、不写参数 —— 写了就变成"调用方法"了。
         */
        Comparator<Integer> com3 = Integer::compare;
        int compare3 = com3.compare(23, 21);
        System.out.println(compare3); // 1
    }

    /**
     * Lambda表达式的使用
     */
    // 语法格式一：无参，无返回值
    @Test
    public void test2() {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("我爱北京天安门");
            }
        };

        r1.run();

        // Lambda表达式的写法
        /*
         * () 是 run() 的空形参列表 —— Lambda 左边永远对应"抽象方法的形参列表"，
         * run 没有参数，所以形参列表就是一对空的 ()，必须原样写出来占位。
         */
        Runnable r2 = () -> {
            System.out.println("我爱上海东方明珠");
        };

        r2.run();
    }

    // 语法格式二：Lambda 需要一个参数，但是没有返回值。
    @Test
    public void test3() {
        Consumer<String> con = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        con.accept("谎言和誓言的区别是什么？");

        Consumer<String> con1 = (String s) -> {
            System.out.println(s);
        };
        con1.accept("一个是说的人当真了，一个是听的人当真了。");
    }

    // 语法格式三：数据类型可以省略，因为可由编译器推断得出，称为“类型推断”
    @Test
    public void test4() {
        Consumer<String> con1 = (String s) -> {
            System.out.println(s);
        };
        con1.accept("如果大学可以重来，你最想重来的事是啥？");

        Consumer<String> con2 = (s) -> {
            System.out.println(s);
        };
        con2.accept("谈一场轰轰烈烈的爱情");
    }

    @Test
    public void test4_1() {
        /*
         * 推断"数组元素的类型和数组长度"。完整写法是：
         * int[] arr = new int[]{1, 2, 3, 4};
         * 因为左边已经声明了 int[]，编译器能确定 {1,2,3,4} 是 int 数组、长度为 4，
         * 所以 new int[] 可以省掉 —— 省掉的信息由上下文推断出来，这就是类型推断。
         * 这种简写只能用在"声明并初始化"的同一行；
         * 先 int[] arr; 再 arr = {1,2,3,4}; 是编译不过的
         */
        int[] arr = {1, 2, 3, 4}; // 类型推断

        HashMap<String, Integer> map = new HashMap<>(); // 类型推断

        /*
         * "局部变量类型推断"（var，JDK 10 引入）：把左边的类型也交给编译器从右边推断。
         * 这里 var 实际被推断为 Set<Map.Entry<String, Integer>>，写 var 只是省去这一长串。
         * var 不是 JavaScript 那种动态类型！它仍是静态类型，编译期就定死了，
         * 推断出 Set 之后就不能再把别的类型赋给 entrySet。
         * 限制：var 只能用于有初始值的局部变量，不能用于成员变量、方法形参、方法返回值，
         *     也不能写 var x = null;（无法推断）。
         */
        var entrySet = map.entrySet(); // 类型推断，在jdk10及之后可以用。
    }

    // 语法格式四：Lambda 若只需要一个参数时，参数的小括号可以省略
    @Test
    public void test5() {
        Consumer<String> con1 = (s) -> {
            System.out.println(s);
        };
        con1.accept("世界那么大，我想去看看");

        Consumer<String> con2 = s -> {
            System.out.println(s);
        };
        con2.accept("世界那么大，我想去看看");
    }

    // 语法格式五：Lambda 需要两个或以上的参数，多条执行语句，并且可以有返回值
    @Test
    public void test6() {
        Comparator<Integer> com1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                System.out.println(o1);
                System.out.println(o2);
                return o1.compareTo(o2);
            }
        };

        System.out.println(com1.compare(12, 21));

        Comparator<Integer> com2 = (o1, o2) -> {
            System.out.println(o1);
            System.out.println(o2);
            return o1.compareTo(o2);
        };

        System.out.println(com2.compare(12, 21));
    }

    // 语法格式六：当 Lambda 体只有一条语句时，return 与大括号若有，都可以省略
    @Test
    public void test7() {
        Comparator<Integer> com1 = (o1, o2) -> {
            return o1.compareTo(o2);
        };
        System.out.println(com1.compare(12, 6));

        Comparator<Integer> com2 = (o1, o2) -> o1.compareTo(o2);
        System.out.println(com2.compare(12, 16));
    }

    @Test
    public void test8() {
        Consumer<String> con1 = s -> {
            System.out.println(s);
        };
        con1.accept("怀才就像怀孕，时间久了总会让人看出来");
    }
}

// @FunctionalInterface：标记该接口为函数式接口，并让编译器检查接口中只能有一个抽象方法
@FunctionalInterface
interface MyFunctionalInterface {
    void method();
//    void method1();
}

/*
 * 自定义函数式接口 MyFunctionalInterface 的使用示例
 * 说明：只要接口满足"只有一个抽象方法"，就不必是 JDK 自带的接口，
 * 自定义接口同样可以用 Lambda 来创建实现类对象。
 */
class MyFunctionalInterfaceTest {
    @Test
    public void test1(){
        MyFunctionalInterface m = () -> System.out.println("hello");
        m.method();
    }
}
