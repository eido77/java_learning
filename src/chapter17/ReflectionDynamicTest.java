package chapter17;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;

public class ReflectionDynamicTest {
    /**
     * 体会：静态性
     * new Person2() 把要创建的类型在源码里写死了，编译期就定下来了。
     * 想换成 Date、Apple，只能改源码 + 重新编译 —— 静态性。
     */
    public Person2 getInstance() {
        return new Person2();
    }

    /**
     * 体会：反射的动态性
     */
    // 举例1：
    /*
     * 修饰符 <类型形参列表> 返回值类型 方法名(...)
     * Java 是从左往右解析的，必须先"声明" T 这个类型变量，
     * 后面（返回值、参数、方法体）才认识 T。
     */
    public <T> T getInstance(String className) throws Exception {
        /*
         * Class.forName(全类名) 不创建对象，它做的是：让类加载器去加载这个类，
         * 并完成"加载→链接→初始化"（会执行 static 代码块），然后返回代表这个类的
         * Class 对象（运行时类）。创建对象是后面 newInstance() 干的。
         */
        Class<?> clazz = Class.forName(className);

        Constructor<?> con = clazz.getDeclaredConstructor();
        con.setAccessible(true);
        return (T) con.newInstance();
    }

    @Test
    public void test1() throws Exception {
        // 完整写法是：Person2 p1 = this.getInstance();
        Person2 p1 = getInstance();
        System.out.println(p1);

        String className = "chapter17.Person2";
        Person2 per1 = getInstance(className);
        System.out.println(per1);

        String className1 = "java.util.Date";
        Date date1 = getInstance(className1);
        System.out.println(date1);
    }

    // 举例2：
    public Object invoke(String className, String methodName) throws Exception {
        // 1. 创建全类名对应的运行时类的对象
        Class<?> clazz = Class.forName(className);
        Constructor<?> con = clazz.getDeclaredConstructor();
        con.setAccessible(true);
        Object obj = con.newInstance();

        // 2. 获取运行时类中指定的方法，并调用
        /*
         * getDeclaredMethod(name, 参数类型...)：
         * 如果要调 showNation(String)，必须写成 clazz.getDeclaredMethod("showNation", String.class)
         * 并且 invoke 时传实参：method.invoke(obj, "中国")。
         */
        Method method = clazz.getDeclaredMethod(methodName);
        method.setAccessible(true);
        // invoke() 是 java.lang.reflect.Method 类里的实例方法，不是 Object 类的方法。
        return method.invoke(obj);
    }

    @Test
    public void test2() throws Exception {
        String className = "chapter17.Person2";
        String methodName = "show";

        Object returnValue = invoke(className, methodName);
        System.out.println(returnValue); // null
    }
}

class Person2 {
    private String name;
    public int age;

    public Person2() {
        System.out.println("Person()...");
    }

    public Person2(int age) {
        this.age = age;
    }

    private Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void show() {
        System.out.println("你好，我是一个Person");
    }

    private String showNation(String nation) {
        return "我的国籍是：" + nation;
    }

    @Override
    public String toString() {
        return "Person2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

/**
 * 案例：榨汁机榨水果汁，水果分别有苹果(Apple)、香蕉(Banana)、桔子(Orange)等。
 */
// 1、声明(Fruit)水果接口，包含榨汁抽象方法：void squeeze();
interface Fruit {
    // 榨汁的方法
    // 接口里的方法默认 public abstract，不写修饰符也一样；实现类重写时必须是 public。
    void squeeze();
}

// 2、声明榨汁机(Juicer)，包含运行方法：public void run(Fruit f)，方法体中，调用f的榨汁方法squeeze()
class Juicer {
    public void run(Fruit f) {
        /*
         * 多态调用：编译期只知道 f 是 Fruit，
         * 运行期才根据 f 实际指向的对象（Apple/Banana/Orange）决定执行谁的 squeeze()。
         */
        f.squeeze();
    }
}

// 3、声明各种水果类，实现水果接口，并重写squeeze();
class Apple implements Fruit {
    @Override
    public void squeeze() {
        System.out.println("apple");
    }
}

class Banana implements Fruit {
    @Override
    public void squeeze() {
        System.out.println("banana");
    }
}

class Orange implements Fruit {
    @Override
    public void squeeze() {
        System.out.println("orange");
    }
}

// 4、在src下，建立配置文件：config.properties，并在配置文件中配上fruitName=xxx（其中xx为某种水果的全类名）
/*
 * 等号两边不要加空格，properties 会把空格算进 value（" chapter17.Apple" 会导致 ClassNotFoundException）
 * 行尾不要加分号，value 里不要加引号
 */
// fruitName=chapter17.Apple

/*
5、在FruitTest测试类中，
（1）读取配置文件，获取水果类名，并用反射创建水果对象，
（2）创建榨汁机对象，并调用run()方法
 */
class FruitTest {
    @Test
    public void test1() throws Exception {
        // 1. 读取配置文件中的信息，获取全类名
        /*
         * java.util.Properties 是 Hashtable<Object, Object> 的子类，专门用来处理 .properties 配置文件。
         * 文件里每行是 key=value（如 name=Tom），读进来后键和值都按 String 处理，
         * 常用于存放数据库账号、密码等配置，改配置时不用改代码、不用重新编译。
         */
        Properties pros = new Properties();

        File file = new File("config.properties");
        FileInputStream fis = new FileInputStream(file);

        // load()：Properties 的方法：逐行解析 key=value（也支持 key:value，# 开头的行是注释），把每一对存进 pros。
        // 把输入流里的配置内容一次性读进 pros 这个集合，之后就能按 key 取 value。
        pros.load(fis);

        // getProperty(key)：根据 key 取对应的 value。它的返回值类型声明是 String
        String fruitName = pros.getProperty("fruitName");

        // 2. 通过反射，创建指定全类名对应的类的实例
        Class<?> clazz = Class.forName(fruitName);
        Constructor<?> con = clazz.getDeclaredConstructor();
        con.setAccessible(true);
        Fruit fruit = (Fruit) con.newInstance();

        // 3. 通过榨汁机的对象调用run()
        Juicer juicer = new Juicer();
        juicer.run(fruit);
    }
}

class Order1 {
    /*
     * 静态变量的赋值（=1）和下面 static 代码块（=2）都属于"类初始化"阶段，
     * 按源码出现顺序执行，所以初始化完成后 orderDesc 的值是 2，不是 1。
     */
    static int orderDesc = 1;

    static {
        orderDesc = 2;
        System.out.println("Order static block...");
    }
}

/*
 * Class.forName(name)           → 会初始化（执行 static 块）
 * ClassLoader.loadClass(name)   → 只加载，不初始化（不执行 static 块）
 *
 * Class.forName(String name, boolean initialize, ClassLoader loader)
 * 参数说明：
 * name        → 类的全类名
 * initialize  → 是否初始化类：
 *               true  = 初始化，会执行 static 块
 *               false = 只加载，不初始化，不执行 static 块
 * loader      → 指定使用哪个类加载器加载该类，可以为 null
 *
 * forName(String) 等价于 forName(name, true, 当前类的加载器)
 */
class Order1Test {
    @Test
    public void test1() throws ClassNotFoundException {
        String className = "chapter17.Order1";
        Class.forName(className); // Order static block...
    }

    @Test
    public void test2() throws ClassNotFoundException {
        String className = "chapter17.Order1";
        /*
         * 运行这个测试"不会"打印 static 块内容：loadClass 只负责加载，
         * 初始化被推迟到"首次主动使用"时（比如 new、访问静态成员）。
         */
        ClassLoader.getSystemClassLoader().loadClass(className);
    }
}

/**
 * 演示 JDK9+ 模块化（JPMS）对反射的限制。
 *
 * setAccessible(true) 能绕过普通的 private 访问权限，
 * 但不一定能绕过 JPMS 的模块封装。
 *
 * 自己写的普通类中的 private，通常可以通过 setAccessible(true) 访问；
 * JDK 核心模块中的某些 private，即使调用 setAccessible(true)，也可能不允许访问。
 *
 * String 位于 java.base 模块的 java.lang 包中，
 * java.base 默认没有将 java.lang open 给当前程序，
 * 所以对 String 的私有字段 value 调用 setAccessible(true) 会失败。
 */
class StringTest {
    /**
     * 对于 JDK 核心模块中未开放的包，其内部私有结构在 JDK17 中
     * 不能仅靠 setAccessible(true) 进行反射访问。
     */
    @Test
    public void test1() throws Exception {
        Class clazz = Class.forName("java.lang.String");
        String obj = (String) clazz.newInstance();

        // 获取value属性，并获取其值
        /*
         * getDeclaredField("value")：能成功，它只是"查元数据"，不涉及访问权限。
         * 真正失败的是下一行 setAccessible(true)：
         * JDK17 下抛 InaccessibleObjectException，提示 module java.base does not "opens java.lang"。
         */
        Field valueField = clazz.getDeclaredField("value");
        valueField.setAccessible(true);
        System.out.println(valueField.get(obj));
    }
}
