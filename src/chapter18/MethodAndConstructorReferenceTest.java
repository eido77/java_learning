package chapter18;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodAndConstructorReferenceTest {
}

/*
1. 举例：
Comparator<Integer> com = Integer::compare;
 * “目标类型推断（target typing）”，没有目标类型编译器就不知道它是什么。
2. 方法引用的理解
> 方法引用，可以看做是基于lambda表达式的进一步刻画。
> 当需要提供一个函数式接口的实例时，我们可以使用lambda表达式提供此实例。
> 当满足一定的条件的情况下，我们还可以使用方法引用或构造器引用替换lambda表达式。
 * 条件：lambda 的方法体“只是在调用一个已经存在的方法/构造器，并且把参数原封不动地传进去”。
 * 能简化：s -> System.out.println(s)      方法体只是调 println，参数直接透传 → System.out::println
 * 不能简化：s -> System.out.println("a" + s)   参数被加工过了
 * 不能简化：s -> { log(s); print(s); }         方法体不止一次调用
 * 不能简化：() -> new Employee(1001)          参数不是从形参来的，而是写死的常量
3. 方法引用的本质：
方法引用作为了函数式接口的实例。  ---> “万事万物皆对象”
 * 编译期：编译器看到 Consumer<String> c = System.out::println;
 *        会检查 println(String) 的形参/返回值能否“适配”抽象方法 accept(String)，
 *        这个检查叫“签名兼容”，而不是要求名字相同 —— 所以方法名叫什么完全无所谓。
 * 运行期：JVM 通过 invokedynamic 指令 + LambdaMetafactory 动态生成一个实现了
 *        Consumer 接口的实例，它的 accept 内部就是去调 println。
 * 结论：方法引用最终确实变成了“一个对象”，所以说“万事万物皆对象”没错；
 *      但要注意它不是匿名内部类，编译后不会生成 Xxx\$1.class 这种文件。
4. 格式：
类(或对象)::方法名
 * 右边只写方法名，绝对不能写括号和参数。写成 Integer::compare(a, b) 是错的，
 * 因为方法引用表示“这个方法本身”，不是“调用这个方法的结果”。
 * 构造器引用的右边不是方法名，而是关键字 new，即 类名::new。
5. 具体使用情况说明：
情况1：对象 :: 实例方法
要求：函数式接口中的抽象方法a与其内部实现时调用的对象的某个方法b的形参列表和返回值类型都相同(或一致)。
此时，可以考虑使用方法b实现对方法a的替换、覆盖。此替换或覆盖即为方法引用。
注意：此方法b是非静态的方法，需要对象调用。
 * 抽象方法返回 void 时，被引用的方法可以有返回值（返回值会被丢弃），这叫“void 兼容”。
 * 例如 Consumer<List<String>> c = list::add;  add 返回 boolean，但照样能赋给返回 void 的 accept。
 * 反过来不行：抽象方法要求有返回值时，被引用方法绝不能是 void。
情况2：类 :: 静态方法
要求：函数式接口中的抽象方法a与其内部实现时调用的类的某个静态方法b的形参列表和返回值类型都相同(或一致)。
此时，可以考虑使用方法b实现对方法a的替换、覆盖。此替换或覆盖即为方法引用。
注意：此方法b是静态的方法，需要类调用。
 * 对照：Integer::compare 相当于 (a, b) -> Integer.compare(a, b)，参数个数一一对应。
情况3：类 :: 实例方法
要求：函数式接口中的抽象方法a与其内部实现时调用的对象的某个方法b的返回值类型相同。
    同时，抽象方法a中有n个参数，方法b中有n-1个参数，且抽象方法a的第1个参数作为方法b的调用者，且抽象方法a
    的后n-1个参数与方法b的n-1个参数的类型相同(或一致)。则可以考虑使用方法b实现对方法a的替换、覆盖。此替换或覆盖即为方法引用。
注意：此方法b是非静态的方法，需要对象调用。但是形式上，写成对象a所属的类
 * 情况1 和情况3 都是引用“实例方法”，区别只有一个：调用者是“写死的”还是“当参数传进来的”。
 * 情况1：对象在写方法引用时就已经存在（ps、emp），调用者固定。
 * 情况3：对象在调用抽象方法那一刻才传进来，所以要把第 1 个参数“升级”成调用者。
 * 这就是 n 个参数变 n-1 个参数的原因 —— 第 1 个参数被“吃掉”当了调用者：
 * String::compareTo  →  (s1, s2) -> s1.compareTo(s2)
 *                            ↑调用者      ↑剩下的参数
 * 所以 :: 左边写类名 String，并不表示调静态方法，而是表示
 * “这个类的任意一个实例都可以当调用者，具体是哪个实例要等参数传进来才知道”。
 * 极端情况：如果一个类里同时有 static f(A,B) 和 实例方法 f(B)，那么 A::f 会产生歧义，编译器直接报错，
 * 这也说明两种情况在语法上长得一模一样，只能靠方法的 static 修饰符区分。
 */
class MethodRefTest {
    // 情况一：对象 :: 实例方法
    // Consumer中的void accept(T t)
    // PrintStream中的void println(String x)
    @Test
    public void test1() {
        // 1.
        Consumer<String> con1 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        con1.accept("hello!");

        // 2. lambda表达式
        Consumer<String> con2 = s -> System.out.println(s);
        con2.accept("hello!");

        // 3. 方法引用
//        PrintStream ps = System.out;
//        Consumer<String> con3 = ps::println;
        Consumer<String> con3 = System.out::println;
        con3.accept("hello!");
    }

    // Supplier中的T get()
    // Employee中的String getName()
    @Test
    public void test2() {
        Employee emp = new Employee(1001, "马化腾", 34, 6000.38);
        // 1.
        Supplier<String> sup1 = new Supplier<String>() {
            @Override
            public String get() {
                return emp.getName();
            }
        };

        System.out.println(sup1.get());

        // 2. lambda表达式
        Supplier<String> sup2 = () -> emp.getName();
        System.out.println(sup2.get());

        // 3. 方法引用
        /*
         * emp::getName 在创建时就把 emp 当前指向的那个对象“抓住”了；
         * 之后即使把 emp 变量指向别的对象，sup3 调用的仍然是最初抓住的那个对象。
         */
        Supplier<String> sup3 = emp::getName;
        System.out.println(sup3.get());
    }

    // 情况二：类 :: 静态方法
    /*
     * Integer.compare 的实现思路是 (x < y) ? -1 : ((x == y) ? 0 : 1)，
     * 它故意不写成 x - y，因为 x - y 在大数相减时会溢出，导致比较结果错误。
     * 写 Comparator 时也要避免 return o1 - o2; 这种写法。
     */
    @Test
    public void test3() {
        // 1.
        Comparator<Integer> com1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        System.out.println(com1.compare(12, 21));

        // 2.
        Comparator<Integer> com2 = (o1, o2) -> Integer.compare(o1, o2);
        System.out.println(com2.compare(21, 34));

        // 3. 方法引用
        /*
         * Comparator<Integer> 的抽象方法：int compare(Integer, Integer)  → 2 个参数
         * Integer 的静态方法：            static int compare(int, int)   → 2 个参数
         * 静态方法不需要调用者，所以抽象方法的 2 个参数正好原封不动地喂给它，个数一一对应
         */
        Comparator<Integer> com3 = Integer::compare;
        System.out.println(com3.compare(34, 34));
    }

    @Test
    public void test4() {
        // 1.
        Function<Double, Long> fun1 = new Function<Double, Long>() {
            @Override
            public Long apply(Double aDouble) {
                return Math.round(aDouble);
            }
        };

        // 2.
        Function<Double, Long> fun2 = aDouble -> Math.round(aDouble);

        // 3.方法引用
        /*
         * Function<Double, Long> 的抽象方法：Long apply(Double)  → 1 个参数
         * Math 的静态方法：                 static long round(double) → 1 个参数
         * 等价：Math::round  ≡  (d) -> Math.round(d)
         * Math.round 有 round(double) 和 round(float) 两个重载，单看 Math::round 是分不清用哪个的；
         * 正是因为左边声明了 Function<Double, Long>，编译器才能反推出“参数 Double、返回 Long”，
         * 从而唯一确定 round(double)
         * 如果换成 Function<Float, Integer> fun = Math::round; 选中的就是 round(float)。
         * 方法引用的类型不是自己带的，而是由“赋值目标”决定的（目标类型推断）。
         */
        Function<Double, Long> fun3 = Math::round;
    }

    // 情况三：类 :: 实例方法
    @Test
    public void test5() {
        // 1.
        Comparator<String> com1 = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        System.out.println(com1.compare("abc", "abd")); // -1

        // 2.
        Comparator<String> com2 = (s1, s2) -> s1.compareTo(s2);
        System.out.println(com2.compare("abc", "abb")); // 1

        // 3.
        /*
         * Comparator中的int compare(T t1,T t2)
         * String中的int compareTo(String anotherString)
         *   抽象方法 compare(String, String) → 2 个参数
         *   实例方法 compareTo(String)       → 1 个参数
         * 少掉的那 1 个被当成“调用者”了：第1个参数 s1 变成 s1.compareTo(...)。
         * 【左边写类名却不是调静态方法】
         * compareTo 没有 static 修饰符。编译器先去 String 里找 compareTo，
         * 发现它是实例方法，就自动按“第1个参数当调用者”的规则去匹配，这叫“非绑定方法引用”(unbound)；
         * 而 emp::getName（情况1）里对象已经确定，叫“绑定方法引用”(bound)。
         * :: 左边是对象 → 绑定，调用者已定，参数个数相同；
         * :: 左边是类名且方法非 static → 非绑定，调用者待定，参数个数多 1。
         */
        Comparator<String> com3 = String::compareTo;
        System.out.println(com3.compare("abc", "abb")); // 1
        /*
         * "abc".compareTo("abd") 前两位相同，第3位 'c'(99) - 'd'(100) = -1，所以上面输出 -1。
         * "abc".compareTo("abb") 第3位 'c'(99) - 'b'(98) = 1，所以这里输出 1。
         * 规则：先逐字符比较第一个不同位的字符差；若一方是另一方的前缀，则返回长度差。
         * compareTo 只保证符号，具体数值由实现决定。
         */
    }

    @Test
    public void test6() {
        // 1.
        BiPredicate<String, String> biPre1 = new BiPredicate<String, String>() {
            @Override
            public boolean test(String s1, String s2) {
                return s1.equals(s2);
            }
        };

        // 2.
        BiPredicate<String, String> biPre2 = (s1, s2) -> s1.equals(s2);

        // 3. 方法引用
        /*
         * BiPredicate<String,String> 的抽象方法：boolean test(String, String) → 2 个参数
         * String 的实例方法：                    boolean equals(Object)       → 1 个参数
         * 第1个参数当调用者，第2个参数当实参：String::equals ≡ (s1, s2) -> s1.equals(s2)
         * 【对比】
         * 当 s1 为 null 时，s1.equals(s2) 会抛 NullPointerException
         * Lambda 避免异常：(s1, s2) -> Objects.equals(s1, s2)
         * 方法引用避免异常：Objects::equals（情况2：静态方法）
         * 注意：Objects.equals(null, null) 返回 true
         */
        BiPredicate<String, String> biPre3 = String::equals;
    }

    @Test
    public void test7() {
        Employee emp = new Employee(1001, "马化腾", 34, 6000.38);
        // 1.
        Function<Employee, String> fun1 = new Function<Employee, String>() {
            @Override
            public String apply(Employee emp) {
                return emp.getName();
            }
        };
        System.out.println(fun1.apply(emp));

        // 2.
        /*
         * 【不能叫 emp】
         * 因为方法体里第 1 行已经有局部变量 Employee emp = ...，
         * lambda 不像匿名内部类那样有自己独立的作用域，它和外层方法“共享同一个作用域”，
         * 再声明一个 emp 就等于在同一作用域里重复声明，编译器报错。
         * 这叫 lambda 的 “no shadowing（不允许遮蔽外层局部变量）” 规则。
         */
        Function<Employee, String> fun2 = employee -> employee.getName();
        System.out.println(fun2.apply(emp));

        // 3.方法引用
        /*
         * test2：Supplier<String> sup3 = emp::getName;        → 0 个参数，对象写死（情况1，绑定）
         * test7：Function<Employee,String> fun3 = Employee::getName; → 1 个参数，对象当参数传入（情况3，非绑定）
         * 同一个 getName 方法，因为“调用者从哪来”不同，就落到了两种不同的情况里。
         *
         * Function<Employee,String> 的抽象方法：String apply(Employee) → 1 个参数
         * Employee 的实例方法：                 String getName()       → 0 个参数
         * Employee::getName ≡ (e) -> e.getName()，传进来的 e 自己当调用者。
         */
        Function<Employee, String> fun3 = Employee::getName;
        System.out.println(fun3.apply(emp));
    }
}

/*
1. 构造器引用
1.1 格式：
类名 :: new
1.2 说明:
> 调用了类名对应的类中的某一个确定的构造器
> 具体调用的是类中的哪一个构造器，取决于函数式接口的抽象方法的形参列表
 * Xxx::new 本身没写参数，编译器只能反过来看“要把它赋给哪个接口”
2. 数组引用
格式：元素类型[] :: new
 * Employee[]::new ≡ (length) -> new Employee[length]
 * 【为什么要有这个语法】
 * 因为泛型在运行时被擦除，泛型代码里无法直接写 new T[n]，
 * 只能让调用方传一个“数组构造器”进来，最典型的就是 Stream.toArray：
 * Employee[] arr = list.stream().toArray(Employee[]::new);
 * 如果不传，toArray() 只能返回 Object[]，用起来要强转。
 */
class ConstructorRefTest {
    /**
     * 构造器引用
     */
    // Supplier中的T get()
    @Test
    public void test1() {
        // 1.
        Supplier<Employee> sup1 = new Supplier<Employee>() {
            @Override
            public Employee get() {
                return new Employee();
            }
        };
        System.out.println(sup1.get());

        // 2.构造器引用
        // 调用的是Employee类中空参的构造器
        /*
         * Supplier<Employee> 的 Employee get() → 0 参数、返回 Employee；
         * Employee() 这个空参构造器              → 0 参数、产出 Employee。
         * 形状一致，所以能替换。等价：Employee::new ≡ () -> new Employee()
         */
        Supplier<Employee> sup2 = Employee::new;
        System.out.println(sup2.get());
    }

    // Function中的R apply(T t)
    @Test
    public void test2() {
        // 1.
        Function<Integer, Employee> func1 = new Function<Integer, Employee>() {
            @Override
            public Employee apply(Integer id) {
                return new Employee(id);
            }
        };
        System.out.println(func1.apply(12));

        // 2.构造器引用
        // 调用的是Employee类中参数是int类型的构造器
        /*
         * Function<Integer, Employee>，抽象方法 Employee apply(Integer)，1 个参数
         *   → 编译器去 Employee 里找“能接收 1 个 Integer（可拆箱为 int）的构造器”
         *   → 找到 Employee(int id)
         * 等价：Employee::new ≡ (id) -> new Employee(id)
         * 构造器引用自己不带任何参数信息，含义由左边的接口类型决定。
         */
        Function<Integer, Employee> func2 = Employee::new;
        System.out.println(func2.apply(11));
    }

    // BiFunction中的R apply(T t,U u)
    @Test
    public void test3() {
        // 1.
        BiFunction<Integer, String, Employee> func1 = new BiFunction<Integer, String, Employee>() {
            @Override
            public Employee apply(Integer id, String name) {
                return new Employee(id, name);
            }
        };
        System.out.println(func1.apply(10, "Tom"));

        // 2.构造器引用
        // 调用的是Employee类中参数是int、String类型的构造器
        /*
         * BiFunction<Integer,String,Employee> 的抽象方法：Employee apply(Integer, String) → 2 个参数
         * Employee 的构造器：                             Employee(int, String)           → 2 个参数
         * 等价：Employee::new ≡ (id, name) -> new Employee(id, name)
         * java.util.function 包按“参数个数 + 返回值有无”设计了一组接口，选接口的思路是：
         *   无参有返回 → Supplier    1 参有返回 → Function    2 参有返回 → BiFunction
         *   1 参无返回 → Consumer    2 参无返回 → BiConsumer
         *   返回 boolean → Predicate / BiPredicate
         *   参数与返回同类型 → UnaryOperator / BinaryOperator
         */
        BiFunction<Integer, String, Employee> func2 = Employee::new;
        System.out.println(func2.apply(11, "Tony"));
    }

    /**
     * 数组构造器引用
     */
    // Function中的R apply(T t)
    @Test
    public void test4() {
        // 1.
        Function<Integer, Employee[]> func1 = new Function<Integer, Employee[]>() {
            @Override
            public Employee[] apply(Integer length) {
                return new Employee[length];
            }
        };
        System.out.println(func1.apply(10).length);

        // 2.
        /*
         * Function<Integer, Employee[]> 的抽象方法：Employee[] apply(Integer) → 1 个参数，返回数组
         * “创建数组”这个动作：                       给一个长度 → 返回该长度的数组
         * 等价：Employee[]::new ≡ (length) -> new Employee[length]
         * 【存在的理由】
         * 泛型擦除导致泛型代码里不能写 new T[n]，必须由外部传入“怎么造数组”，典型场景：
         * Employee[] arr = EmployeeData.getEmployees().stream().toArray(Employee[]::new);
         * 不传的话 toArray() 只能给你 Object[]，还得强转且不安全。
         */
        Function<Integer, Employee[]> func2 = Employee[]::new;
        System.out.println(func2.apply(20).length);
    }
}

class Employee {
    private int id;
    private String name;
    private int age;
    private double salary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Employee() {
        System.out.println("Employee().....");
    }

    public Employee(int id) {
        this.id = id;
    }

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Employee(int id, String name, int age, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + ", salary=" + salary + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Employee employee = (Employee) o;

        if (id != employee.id) {
            return false;
        }
        if (age != employee.age) {
            return false;
        }
        if (Double.compare(employee.salary, salary) != 0) {
            return false;
        }
        return name != null ? name.equals(employee.name) : employee.name == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        temp = Double.doubleToLongBits(salary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

/**
 * 提供用于测试的数据
 */
class EmployeeData {
    public static List<Employee> getEmployees() {
        List<Employee> list = new ArrayList<>();

        list.add(new Employee(1001, "马化腾", 34, 6000.38));
        list.add(new Employee(1002, "马云", 2, 19876.12));
        list.add(new Employee(1003, "刘强东", 33, 3000.82));
        list.add(new Employee(1004, "雷军", 26, 7657.37));
        list.add(new Employee(1005, "李彦宏", 65, 5555.32));
        list.add(new Employee(1006, "比尔盖茨", 42, 9500.43));
        list.add(new Employee(1007, "任正非", 26, 4333.32));
        list.add(new Employee(1008, "扎克伯格", 35, 2500.32));

        return list;
    }
}
