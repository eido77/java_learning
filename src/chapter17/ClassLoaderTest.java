package chapter17;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.util.Properties;

public class ClassLoaderTest {
    /*
    1. Class类的理解（掌握）
    （如下以Java类的加载为例说明）
     * javac 把 .java 编译成 .class；java 命令启动 JVM 后，类加载器把 .class 读入内存：
     * 类的结构信息（字段、方法、常量池等）放在方法区（JDK8 中由元空间 Metaspace 实现，用的是本地内存），
     * 同时在【堆】中创建一个 java.lang.Class 对象，作为访问这些结构信息的入口，它就是"Class 的一个实例"。
     * Class 对象本身在堆里，不在方法区；"一个类只对应一个 Class 实例"的前提是：由同一个类加载器加载。
    比如：加载到内存中的Person类或String类或User类，都作为Class的一个一个的实例
    Class clazz1 = Person.class; // 运行时类
    Class clazz2 = String.class;
    Class clazz3 = User.class;
    Class clazz4 = Comparable.class;
     * 一个类被某个类加载器加载后会被缓存，之后再用到不会重复加载，
     * 所以它在 JVM 里只有一个 Class 对象（这就是后面 test1 里 == 都为 true 的原因）。
     * 不同的类加载器可以把同名的类各加载一份，得到的是两个不同的 Class 对象。
    2. 体会：Class看做是反射的源头
    3. 获取Class实例的几种方式(掌握前三种)
    见代码
    4. Class的实例都可以指向哪些结构呢？（熟悉）
    简言之，所有Java类型！
    （1）class：外部类，成员(成员内部类，静态内部类)，局部内部类，匿名内部类
    （2）interface：接口
    （3）[]：数组
    （4）enum：枚举
    （5）annotation：注解@interface
    （6）primitive type：基本数据类型
    （7）void
    5. 类的加载过程(了解)
    过程1：类的装载(loading)
    将类的class文件读入内存，并为之创建一个java.lang.Class对象。此过程由类加载器完成
    过程2：链接(linking)
    > 验证(Verify): 确保加载的类信息符合JVM规范，例如：以cafebabe开头，没有安全方面的问题。
    > 准备(Prepare)：为类变量(static)分配内存，并赋"默认零值"（0、false、null），不是代码里写的值。
     * 例：文件末尾的 Order 类，准备阶段 orderDesc = 0，初始化阶段先变成 1，再被静态代码块改成 2。
     * 特例：static final 修饰的编译期常量（如 static final int X = 10;）在准备阶段就直接是 10。
     * JDK7 起静态变量随 Class 对象存放在堆中
    > 解析(Resolve):虚拟机常量池内的符号引用（常量名）替换为直接引用（地址）的过程。
    过程3：初始化(initialization)
    执行类构造器<clinit>()方法的过程。
     * <clinit>() 由【编译器】自动收集所有静态变量的显式赋值语句和 static 代码块，
     * 按源码中出现的顺序合并生成；类里没有这些内容就不会生成 <clinit>()。
     * JVM 保证 <clinit>() 在多线程下只执行一次（会加锁），这也是"静态内部类实现单例"线程安全的原理。
     * 触发初始化的常见时机：new 对象、读写静态变量（编译期常量除外）、调用静态方法、Class.forName()；
     * User.class、ClassLoader.loadClass() 只加载、不初始化，不会执行 static 代码块。
    6. 关于类的加载器(了解、JDK8版本为例)
     * JDK9 起有变化：扩展类加载器被平台类加载器(PlatformClassLoader)取代，rt.jar 被拆成模块（lib/modules 文件），
     * 详见下面 test3 的说明。
    6.1 作用：负责类的加载，并对应于一个Class的实例。
    6.2 分类（分为两种）：
    > BootstrapClassLoader:引导类加载器、启动类加载器
         > 使用C/C++语言编写的，不能通过Java代码获取其实例
         > 负责加载Java的核心库（JAVA_HOME/jre/lib/rt.jar或sun.boot.class.path路径下的内容）
    > 继承于ClassLoader的类加载器
        > ExtensionClassLoader:扩展类加载器
                > 负责加载从java.ext.dirs系统属性所指定的目录中加载类库，或从JDK的安装目录的jre/lib/ext子目录下加载类库
        > SystemClassLoader/ApplicationClassLoader:系统类加载器、应用程序类加载器
                > 我们自定义的类，默认使用的类的加载器。
        > 用户自定义类的加载器
                > 实现应用的隔离（同一个类在一个应用程序中可以加载多份）；数据的加密。
     6.3 这些加载器之间的"父加载器"关系【不是】extends 继承关系！
     * 所谓 parent，是加载器对象里的一个成员变量（组合关系），见下面 MyClassLoader 的演示。
     * （JDK8 中 AppClassLoader 和 ExtClassLoader 在类层次上都继承 URLClassLoader，算是"兄弟"；
     * 但运行时 AppClassLoader 对象的 parent 字段指向 ExtClassLoader 对象。）
     * 加载类时先交给 parent 去加载，parent 加载不了才自己加载，这就是"双亲委派机制"。
     * 好处：java.lang.String 等核心类只会由引导类加载器加载，防止被自己写的同名类冒充、篡改。
     */
    /*
     * 【bug 修复：编译错误】
     * 内部类名叫 ClassLoader，在本类中会遮蔽 java.lang.ClassLoader。下面所有 ClassLoader 都变成指这个内部类，
     * 它没有 getSystemClassLoader()、getParent() 方法，clazz.getClassLoader() 的返回值也赋不给它
     * 修复：改名为 MyClassLoader，避免和 JDK 的类重名；它不需要访问外部类的实例，所以加上 static 写成静态内部类。
     */
    // class ClassLoader {
    static class MyClassLoader {
        MyClassLoader parent;

        public MyClassLoader(MyClassLoader parent) {
            this.parent = parent;
        }
    }

    /*
     * "父加载器"形成：创建 loader1 时把 loader0 传进去，loader1.parent 就指向 loader0。
     * loader0 没有父加载器（parent 为 null），相当于真实情况中的引导类加载器（Java 里也用 null 表示）。
     * 这里只是建立对象之间的关系，并没有真的去加载类。
     */
    // 测试代码：
    /*
     * ClassLoader loader0 = new ClassLoader();
     * ClassLoader loader1 = new ClassLoader(loader0);
     * 【bug 修复：编译错误】类名改成了 MyClassLoader（原因见上）；原来的 new ClassLoader() 没有对应的无参构造器，
     * 这里按"loader0 没有父加载器"的本意，改成传 null。
     */
    MyClassLoader loader0 = new MyClassLoader(null);
    MyClassLoader loader1 = new MyClassLoader(loader0);
    // loader0 叫做 loader1 的"父加载器"(parent)，注意不是"父类"，两者没有 extends 关系，只是对象引用关系。
    // 我们就把loader0叫做loader1的父类加载器

    /**
     * 获取Class实例的几种方式
     */
    @Test
    public void test1() throws ClassNotFoundException {
        // 1. 使用类字面量"类名.class"（注意：.class 是语法，不是静态属性，User 里并没有叫 class 的字段）
        Class clazz1 = User.class;
        System.out.println(clazz1); // class chapter17.User

        // 2. 调用运行时类的对象的getClass()
        User u1 = new User();
        // getClass() 返回对象的"实际运行时类型"，
        // Object o = new User(); o.getClass() 得到的是 User 而不是 Object。
        Class clazz2 = u1.getClass();

        /*
         * clazz1 和 clazz2 指向堆里的同一个 Class 对象。
         * User 类由同一个类加载器（系统类加载器）只加载一次，JVM 中只有唯一一个 User 的 Class 对象，
         * 不管用哪种方式获取，拿到的都是它。
         */
        System.out.println(clazz1 == clazz2); // true

        // 3. 调用Class的静态方法 forName(String className)
        /*
         * 全类名 = 完整包名 + "." + 类名，例如 chapter17.User、java.lang.String，一个字符都不能省，再长也要写全。
         * forName 是在【运行时】根据字符串找类，import 只在编译期起作用，管不到字符串，
         * JVM 只能靠完整名字在 classpath 中定位 chapter17/User.class。
         * 内部类要用 $ 连接，例如 "chapter17.ClassLoaderTest$MyClassLoader"
         */
        String className = "chapter17.User"; // 全类名
        /*
         * <?> 是泛型的"无界通配符"，意思是"某种类型的 Class，但编译时不知道具体是哪种"。
         * forName 的返回值类型本身就是 Class<?>，因为只凭一个字符串，编译器无法知道是哪个类。
         */
        Class<?> clazz3 = Class.forName(className);
        // forName 拿到的也是内存中唯一的那个 User 的 Class 对象，所以还是同一个地址。
        System.out.println(clazz1 == clazz3); // true

        // 4. 使用类的加载器的方式 (了解)
        Class<?> clazz4 = ClassLoader.getSystemClassLoader().loadClass("chapter17.User");
        System.out.println(clazz1 == clazz4); // true
    }

    @Test
    public void test2() {
        // 普通类：java.lang.Object，打印结果为 class java.lang.Object
        Class c1 = Object.class;
        // 接口：Comparable，打印结果为 interface java.lang.Comparable
        Class c2 = Comparable.class;
        // 一维数组：String[]，打印结果为 class [Ljava.lang.String;（[ 表示一维，L...; 表示引用类型）
        Class c3 = String[].class;
        // 二维数组：int[][]，打印结果为 class [[I（两个 [ 表示二维，I 表示 int）
        Class c4 = int[][].class;
        // 枚举：java.lang.annotation.ElementType（就是写 @Target 时用的 TYPE、METHOD 那些值）
        Class c5 = ElementType.class;
        // 注解：@Override 本身是一个 @interface，同样有 Class 实例
        Class c6 = Override.class;
        // 基本数据类型：int，打印为 int。易错：int.class != Integer.class，但 int.class == Integer.TYPE
        Class c7 = int.class;
        // void 也有 Class 实例，打印为 void，用于表示方法"无返回值"（反射获取方法返回类型时会遇到）
        Class c8 = void.class;
        // Class 本身也是一个类，所以 Class.class 也是 Class 的一个实例
        Class c9 = Class.class;

        int[] a = new int[10];
        int[] b = new int[100];
        Class c10 = a.getClass();
        Class c11 = b.getClass();
        // 只要元素类型与维度一样，就是同一个Class
        // 长度不同（10 和 100）不影响，长度属于数组对象，不属于类型；但 int[] 和 int[][]、int[] 和 long[] 不是同一个 Class。
        System.out.println(c10 == c11); // true
    }

    /**
     * 在jdk8中执行如下的代码：
     */
    /*
     * JDK8：系统类加载器 → 扩展类加载器(ExtClassLoader) → 引导类加载器(null)
     * JDK9+：系统类加载器 → 平台类加载器(PlatformClassLoader) → 引导类加载器(null)
     * 变化原因：JDK9 引入模块化，rt.jar 和 ext 扩展目录都被移除；JDK17 设置 java.ext.dirs 会直接启动失败。
     */
    // JDK17 写法
    @Test
    public void test3ForJdk17() {
        // 1. 获取系统(应用)类加载器：负责加载 classpath 上我们自己写的类和第三方 jar
        ClassLoader appLoader = ClassLoader.getSystemClassLoader();
        System.out.println(appLoader);           // jdk.internal.loader.ClassLoaders$AppClassLoader@xxxx
        System.out.println(appLoader.getName()); // app（getName() 是 JDK9 新增的）

        // 2. 父加载器是平台类加载器：取代了扩展类加载器，负责加载 java.sql 等部分平台模块
        ClassLoader platformLoader = appLoader.getParent();
        System.out.println(platformLoader);           // jdk.internal.loader.ClassLoaders$PlatformClassLoader@xxxx
        System.out.println(platformLoader.getName()); // platform
        // JDK9 新增的直接获取方式，拿到的是同一个对象
        System.out.println(platformLoader == ClassLoader.getPlatformClassLoader()); // true

        // 3. 再往上是引导类加载器：仍由 C++ 实现，没有对应的 Java 对象，用 null 表示
        ClassLoader bootLoader = platformLoader.getParent();
        System.out.println(bootLoader); // null
    }

    /*
     * 类的加载器
     * 根据类名找到对应的 .class 字节码（从 jar、目录、网络等位置），读进内存，生成 Class 对象。
     * 除引导类加载器是 JVM 内部的 C++ 代码外，其他加载器都是普通 Java 对象，
     * 都是 java.lang.ClassLoader 的子类实例，JVM 启动时自动创建；每个 Class 对象都记得是谁加载了自己（getClassLoader()）。
     * 分工：引导类加载器加载核心类库；扩展/平台类加载器加载扩展或平台类库；系统类加载器加载我们自己写的类。
     * 配合"双亲委派"机制，保证核心类不会被篡改，同一个类也不会被重复加载。
     */
    @Test
    public void test3() {
        // 获取系统类加载器
        /*
         * java.lang.ClassLoader 是 JDK 中所有类加载器（引导类加载器除外）的抽象父类。
         * getSystemClassLoader()：返回系统类加载器（应用程序类加载器）对象，也就是加载我们自己写的 User 等类的那个加载器。
         */
        ClassLoader classLoader1 = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader1); // sun.misc.Launcher$AppClassLoader@18b4aac2
        // 输出说明：格式是"类名@哈希码的十六进制"（Object.toString() 的格式）
        // JDK17 输出为 jdk.internal.loader.ClassLoaders$AppClassLoader@xxxx。

        // 获取系统类加载器的父加载器：JDK8 中是扩展类加载器，JDK9+ 中是平台类加载器。
        /*
         * getParent() 返回当前加载器的"父加载器"，
         * 注意：它返回的是另一个加载器对象，不是父类（不是 getSuperclass() 的意思）。
         * classLoader2 是扩展类加载器，它是真实存在的 Java 对象，所以打印"类名@哈希码"；
         * classLoader3 应该是引导类加载器，它用 C++ 实现，在 Java 中没有对应的对象，getParent() 只能返回 null，
         */
        ClassLoader classLoader2 = classLoader1.getParent();
        System.out.println(classLoader2); // sun.misc.Launcher$ExtClassLoader@28a418fc

        // 获取引导类加载器：结果为 null，不是"获取失败"，而是引导类加载器在 Java 层就是用 null 表示的。
        ClassLoader classLoader3 = classLoader2.getParent();
        System.out.println(classLoader3); // null
    }

    @Test
    public void test4() throws ClassNotFoundException {
        // 用户自定义的类使用的是系统类加载器加载的
        Class clazz1 = User.class;
        /*
         * getClassLoader()：查询"是哪个类加载器加载了 User 这个类"，打印出来用来验证"自定义类由系统类加载器加载"。
         * ClassLoader 没有重写 toString()，所以输出 Object 默认的"类名@哈希码十六进制"。
         */
        ClassLoader classLoader = clazz1.getClassLoader();
        System.out.println(classLoader); // sun.misc.Launcher$AppClassLoader@18b4aac2

        // Java 核心类库（如 java.lang.String）由引导类加载器加载，所以 getClassLoader() 返回 null。
        // JDK9+ 中 java.sql 等部分模块改由平台类加载器加载，不是所有 java.* 类都返回 null。
        // forName()：Class 的静态方法：根据全类名找到这个类（没加载就先加载），并完成初始化，
        // 然后返回它的 Class 对象；这里拿到的是 String 的 Class 对象。
        Class clazz2 = Class.forName("java.lang.String");
        ClassLoader classLoader1 = clazz2.getClassLoader();
        System.out.println(classLoader1); // null
    }

    // 7. （掌握）用类加载器读取 classpath 下的配置文件
    /**
     * 需求：通过ClassLoader加载指定的配置文件
     */
    @Test
    public void test5() throws IOException {
        /*
         * java.util.Properties 是 Hashtable<Object, Object> 的子类，专门用来处理 .properties 配置文件。
         * 文件里每行是 key=value（如 name=Tom），读进来后键和值都按 String 处理，
         * 常用于存放数据库账号、密码等配置，改配置时不用改代码、不用重新编译。
         */
        Properties pros = new Properties();

        /*
         * InputStream 接收：这是链式调用，先 getSystemClassLoader() 得到加载器对象，再调用它的 getResourceAsStream()；
         * 左边变量的类型取决于【最后一个】方法的返回值类型，getResourceAsStream 返回的就是 InputStream。
         *
         * getResourceAsStream(name)：在 classpath（类路径）下查找名为 name 的资源文件，返回读取它的输入流；
         * 找不到文件时不抛异常，而是返回 null，之后 load(null) 抛出 NullPointerException
         */
        // 通过类的加载器读取的文件的默认的路径为：当前module下的src下
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("info1.properties")) {
            // load()：Properties 的方法：逐行解析 key=value（也支持 key:value，# 开头的行是注释），把每一对存进 pros。
            // 把输入流里的配置内容一次性读进 pros 这个集合，之后就能按 key 取 value。
            pros.load(is);
        }

        // getProperty(key)：根据 key 取对应的 value。它的返回值类型声明是 String
        String name = pros.getProperty("name");
        String pwd = pros.getProperty("password");
        System.out.println(name + ":" + pwd);
    }

    // Properties:处理属性文件
    @Test
    public void test6() throws IOException {
        Properties pros = new Properties();

        // 读取的文件的默认路径为：当前的module
        /*
         * 生效的那行：读取 module 根目录下的 info.properties。
         * 注释掉的那行：读取 module 下 src 目录里的 info1.properties，也就是 test5 用类加载器读的同一个文件，
         * 类加载器方式按 classpath 查找，项目打成 jar 以后依然能用；
         * FileInputStream 方式依赖磁盘目录结构，打包后 src 目录就不存在了。所以项目里读配置一般用类加载器方式。
         */
//        FileInputStream is = new FileInputStream(new File("src/info1.properties"));
        try (FileInputStream is = new FileInputStream(new File("info.properties"))) {
            pros.load(is);
        }

        String name = pros.getProperty("name");
        String pwd = pros.getProperty("password");
        System.out.println(name + ":" + pwd);
    }
}

class User {
    private String name;
    public int age;

    public User() {
//        System.out.println("User()...");
    }

    public User(int age) {
        this.age = age;
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void show() {
        System.out.println("你好，我是一个User");
    }

    private String showNation(String nation) {
        return "我的国籍是：" + nation;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

/*
 * 准备阶段：orderDesc 被赋默认值 0；
 * 初始化阶段：执行 <clinit>()，按源码顺序先执行 orderDesc = 1，再执行 static 块里的 orderDesc = 2，最终为 2。
 */
class Order {
    static int orderDesc = 1;

    static {
        orderDesc = 2;
    }
}
