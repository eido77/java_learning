package chapter17;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {
    /*
    通过使用反射前后的例子的对比，回答：
    1. 面向对象中创建对象，调用指定结构（属性、方法）等功能，可以不使用反射，也可以使用反射。请问有什么区别？
        不使用反射，我们需要考虑封装性。比如：出了Person类之后，就不能调用Person类中私有的结构
        使用反射，我们可以调用运行时类中任意的构造器、属性、方法。包括了私有的属性、方法、构造器。
    2. 以前创建对象并调用方法的方式，与现在通过反射创建对象并调用方法的方式对比的话，哪种用的多？场景是什么？
        > 开发中主要是完成业务代码，对于相关的对象、方法的调用都是确定的。所以，我们使用非反射的方式多一些。
        > 因为反射体现了动态性（可以在运行时动态的获取对象所属的类，动态的调用相关的方法），所以在设计框架的时候，
          会大量的使用反射。意味着，如果需要学习框架源码，那么就需要学习反射。
        框架 = 注解 + 反射 + 设计模式
    3. 单例模式的饿汉式和懒汉式中，私有化类的构造器了！ 此时通过反射，可以创建单例模式中类的多个对象吗？
    是的！
    4. 通过反射，可以调用类中私有的结构，是否与面向对象的封装性有冲突？是不是Java语言设计存在Bug？
        不存在bug!
        封装性：体现的是是否建议我们调用内部api的问题。比如，private声明的结构，意味着不建议调用。
        反射：体现的是我们能否调用的问题。因为类的完整结构都加载到了内存中，所有我们就有能力进行调用。
     */

    /**
     * 使用反射之前可以执行的操作
     */
    @Test
    public void test1() {
        // 1.创建Person类的实例
        // public Person()
        Person p1 = new Person(); // Person()...
        System.out.println(p1); // Person{name='null', age=0}

        // 2.调用属性
        // public int age
        p1.age = 10;
        System.out.println(p1.age); // 10

        // 3.调用方法
        // public void show()
        p1.show(); // 你好，我是一个Person
    }

    /**
     * 使用反射完成上述的操作
     */
    /*
     * 反射（Reflection）是指程序在“运行时”能够动态地获取一个类的完整结构
     * （构造器、属性、方法），并且能动态地创建对象、访问属性、调用方法的一套 API。
     *
     * 反射带来了“动态性” —— 运行时才决定操作哪个类/方法。
     * 框架（Spring、MyBatis、JUnit 等）事先并不知道你会写哪些类，只能靠反射在运行时
     * 加载并调用你的类。所以：框架 = 注解 + 反射 + 设计模式。
     */
    @Test
    public void test2() throws InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        // 1.创建Person类的实例
        // public Person()
        /*
         * 获取 Person 这个类对应的“Class 对象”（也叫运行时类），
         * 它是反射的入口 —— 拿到它才能进一步获取构造器/属性/方法。
         *
         * Person.class 是“类字面量”写法。任何类名后面加 .class，
         * 都会得到该类的 Class 对象。这里的 .class 不是调用属性或方法，
         * 而是一种固定语法，专门用来取 Class 对象。
         */
        Class clazz = Person.class;
        /*
         * newInstance()：调用该类的“无参构造器”创建一个对象，
         * 等价于 test1 里的 new Person()，所以同样会打印 Person()...。
         *
         * clazz 声明成了没有泛型的 Class，newInstance() 返回的是 Object 类型，
         * Object 不能直接赋给 Person，所以要强转回 Person。
         * 如果把上面写成 Class<Person> clazz = Person.class，
         * newInstance() 就会返回 Person，这里就不用强转了。
         *
         * 推荐的规范写法（现在实际开发更常用，避免过时 API）：
         * Person p1 = clazz.getDeclaredConstructor().newInstance();
         * 即先拿无参构造器，再用 Constructor.newInstance() 创建对象。
         */
        Person p1 = (Person) clazz.newInstance(); // Person()...
        System.out.println(p1); // Person{name='null', age=0}

        // 2.调用属性
        // public int age
        /*
         * 用反射去“找到 age 这个属性 → 给它赋值 → 再把值读出来”。
         * Field 是 java.lang.reflect.Field 类，它的每个实例代表“某个类的一个属性(字段)”。
         * getField("age")：按名字查找该类中【public】的属性，
         *   参数 "age" 就是要找的属性名（字符串形式，区分大小写）。
         */
        Field ageField = clazz.getField("age");
        /*
         * set(p1, 10)：给“p1 这个对象”的 age 属性赋值为 10，
         * 等价于 test1 里的 p1.age = 10。
         * p1 = 要给哪个对象赋值（哪个实例的属性）
         * 10 = 要赋的值
         * 因为属性属于“某个具体对象”，所以必须告诉它
         * “给哪个对象、赋什么值”，因此需要这两个参数。
         */
        ageField.set(p1, 10);
        /*
         * get(p1)：读取“p1 这个对象”的 age 属性的当前值并返回，
         * 等价于 test1 里的 p1.age。
         */
        System.out.println(ageField.get(p1)); // 10

        // 3.调用方法
        // public void show()
        /*
         * getMethod("show")：按方法名查找该类中【public 的】方法，
         * 返回一个 Method 对象来代表这个方法。（注意方法名区分大小写）
         */
        Method showMethod = clazz.getMethod("show");
        /*
         * invoke：真正地“执行/调用”这个方法，等价于 test1 里的 p1.show()。
         * invoke(p1) 里的 p1：表示“在哪个对象上调用这个方法”（调用者对象）。
         *   因为 show() 是实例方法，必须指定是哪个 Person 实例来执行。
         * invoke 执行了 show() 方法体，而 show() 里就是 sout
         */
        showMethod.invoke(p1); // 你好，我是一个Person
    }

    /**
     * 出了Person类之后，就不能直接调用Person类中声明的private权限修饰的结构（属性、方法、构造器）
     * 但是，我们可以通过反射的方式，调用Person类中私有的结构 ---> 暴力反射
     */
    /*
     * 核心区别在于“权限修饰符 + 用的方法不同”：
     * test2 用的是 getField/getMethod/newInstance(无参)，它们只能访问【public】的结构。
     *   Person 里 name 属性、带参构造器 Person(String,int)、showNation 方法都是 private，
     *   用 test2 那套 public 方法根本【找不到】它们，会抛 NoSuchField/NoSuchMethodException。
     * test3 用的是 getDeclaredField/getDeclaredMethod/getDeclaredConstructor，
     *   带 Declared 的版本能获取“本类中声明的所有成员，包括 private”。
     * 再配合 setAccessible(true) 关闭访问检查，就能真正读写/调用私有成员。这就是所谓“暴力反射”
     */
    @Test
    public void test3() throws Exception {
        // 1. 调用私有的构造器，创建Person的实例
        // private Person(String name, int age)
        // 获取 Person 的 Class 对象（反射入口）
        Class clazz = Person.class;
        /*
         * getDeclaredConstructor：获取本类中声明的某个构造器（含 private），返回一个 Constructor 对象。
         * String.class, int.class：在“指定构造器的参数类型列表”，用来精确定位是哪一个构造器。
         */
        Constructor cons = clazz.getDeclaredConstructor(String.class, int.class);
        /*
         * setAccessible(true)：临时关闭 Java 的访问权限检查，
         * 让我们可以访问 private（私有）的构造器/属性/方法。
         */
        cons.setAccessible(true);
        /*
         * "Tom", 12：这是传给构造器的实参，对应private Person(String name, int age)
         * 强转成 Person：Constructor.newInstance 返回的是 Object 类型，
         *   Object 不能直接赋给 Person，所以要强转。
         * 实际创建出来的本来就是 Person 对象，只是编译期声明为 Object，
         * 运行期真实类型是 Person，所以向下转型是安全的。
         */
        Person p1 = (Person) cons.newInstance("Tom", 12);
        System.out.println(p1); // Person{name='Tom', age=12}

        // 2. 调用私有的属性
        // private String name
        /*
         * getDeclaredField("name")：获取本类中声明的名为 name 的属性（含 private）。
         *   getField 只能拿 public 属性，
         *   getDeclaredField 能拿到包括 private 在内的本类属性。
         */
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        /*
         * set(p1, "Jeremy")：把 p1 对象的 name 属性改成 "Jeremy"。
         * p1 = 要修改哪个对象的属性；"Jeremy" = 要设置的新值。
         */
        nameField.set(p1, "Jeremy");
        // get(p1) 读取 p1 的 name 属性值（此时是 "Jeremy"）并打印；get 只需传对象，故只写 p1。
        System.out.println(nameField.get(p1)); // Jeremy

        // 3. 调用私有的方法
        // private String showNation(String nation)
        /*
         * getDeclaredMethod：在本类里按“方法名 + 参数类型”查找一个方法（包括 private 方法），
         *   找到后返回一个 Method 对象。
         *   getMethod 只能找 public 方法（包括从父类继承来的 public 方法）；
         *   getDeclaredMethod 能找本类自己声明的所有方法（包括 private），但找不到父类的方法。
         * "showNation"：要找的方法名，写成字符串，区分大小写。
         *   这不是在给方法起名，而是去查找 Person 里已经写好的 showNation 方法。
         * String.class：这个方法的参数类型列表。
         *   Java 允许方法重载（同名但参数不同），只写方法名可能对应好几个方法，
         *   所以要连参数类型一起写，才能唯一确定是哪一个。
         *   showNation(String nation) 只有一个 String 参数，所以写 String.class。
         *   方法有几个参数，就依次写几个 类型.class；没有参数就只写方法名（比如 getMethod("show")）。
         */
        Method showNation = clazz.getDeclaredMethod("showNation", String.class);
        // showNation 是 private 方法，调用前要先 setAccessible(true) 关掉访问检查
        showNation.setAccessible(true);
        /*
         * invoke：真正执行这个方法，效果相当于 p1.showNation("CHN")
         *   p1：在哪个对象上调用这个方法（调用者）；
         *   "CHN"：传给 showNation 的实参，对应形参 String nation。
         * 调用返回值是 void 的方法（比如 test2 的 show）时，invoke 返回 null；
         *   调用静态方法时，第一个参数可以传 null，因为静态方法不需要调用者对象。
         */
        String info = (String) showNation.invoke(p1, "CHN");
        System.out.println(info); // 我的国籍是：CHN
    }
}

class Person {
    // 属性
    private String name;
    public int age;

    // 构造器
    public Person(){
        System.out.println("Person()...");
    }

    public Person(int age){
        this.age = age;
    }

    private Person(String name, int age){
        this.name = name;
        this.age = age;
    }

    // 方法
    public void show(){
        System.out.println("你好，我是一个Person");
    }

    private String showNation(String nation){
        return "我的国籍是：" + nation;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
