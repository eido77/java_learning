package chapter15;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectStreamTest {
    /*
    1. 数据流及其作用（了解）
    DataOutputStream:可以将内存中的基本数据类型的变量、String类型的变量写出到具体的文件中。
    DataInputStream:将文件中保存的数据还原为内存中的基本数据类型的变量、String类型的变量。
    2. 对象流及其作用
    2.1 API:
    ObjectInputSteam
    ObjectOutputStream
    2.2 作用:
    可以读写基本数据类型的变量、引用数据类型的变量。
    3. 对象的序列化机制是什么
    对象序列化机制允许把内存中的Java对象转换成平台无关的二进制流，从而允许把这种二进制流持久地保存在磁盘上，
    或通过网络将这种二进制流传输到另一个网络节点。// 当其它程序获取了这种二进制流，就可以恢复成原来的Java对象。
    4.如下两个过程使用的流：
    序列化过程：使用ObjectOutputStream流实现。将内存中的Java对象保存在文件中或通过网络传输出去
    反序列化过程：使用ObjectInputSteam流实现。将文件中的数据或网络传输过来的数据还原为内存中的Java对象
     * ObjectOutputStream(oos) = Output = 写出，把内存对象写到文件/网络 → 所以序列化用它。
     * ObjectInputStream(ois)  = Input  = 读入，把文件/网络的字节读回内存还原对象 → 所以反序列化用它。
     * 站在"内存"的角度看，往外写(Out)=序列化，往里读(In)=反序列化。
    5.自定义类要想实现序列化机制，需要满足：
    ① 自定义类需要实现接口：Serializable
    ② 要求自定义类声明一个全局常量： static final long serialVersionUID = 42234234L;
       用来唯一的标识当前的类。
    ③ 要求自定义类的各个属性也必须是可序列化的。
       > 对于基本数据类型的属性：默认就是可以序列化的
       > 对于引用数据类型的属性：要求实现Serializable接口
    6.注意点：
    ① 如果不声明全局常量serialVersionUID，系统会自动声明生成一个针对于当前类的serialVersionUID。
    如果修改此类的话，会导致serialVersionUID变化，进而导致反序列化时，出现InvalidClassException异常。
    ② 类中的属性如果声明为transient或static，则不会实现序列化。
     */
    /**
     * 序列化过程：使用ObjectOutputStream流实现。将内存中的Java对象保存在文件中或通过网络传输出去
     */
    @Test
    public void test1() throws IOException {
        // 1.创建File对象和流的对象
        File file = new File("/Users/Shared/java_learning/io/object.txt");
        /*
         * ObjectOutputStream 是"处理流/包装流"，它没有接收 File 的构造器，
         * 只有接收 OutputStream 的构造器，本身不会直接连接文件。所以要先用
         * FileOutputStream(节点流)接上文件，再让 ObjectOutputStream 去"包装"它：
         * 文件读写交给 FileOutputStream，把对象转成字节交给 ObjectOutputStream。
         * 这就是 Java IO 典型的"装饰器模式"分层设计。
         */
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

        // 2.写出数据即为序列化的过程
        oos.writeUTF("你好，世界");
        oos.flush();

        oos.writeObject("走，来");
        oos.flush();

        oos.close();
    }

    /**
     * 反序列化过程：使用ObjectInputSteam流实现。将文件中的数据或网络传输过来的数据还原为内存中的Java对象
     */
    @Test
    public void test2() throws IOException, ClassNotFoundException {
        // 1.
        File file = new File("/Users/Shared/java_learning/io/object.txt");

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        // 2. 读取文件中的对象（或反序列化的过程）
        String str1 = ois.readUTF();
        System.out.println(str1); // 你好，世界

        /*
         * readObject() 的返回类型固定是 Object
         * 1) 用 String 接收：必须强转 → String s = (String) ois.readObject();
         *    因为编译器只知道返回 Object，不会自动当成 String。
         * 2) 用 Object 接收：不需要强转 → Object o = ois.readObject();
         *    但之后想调用 String 的方法(如 length())时还是要强转。
         */
//        Object str2 = (String) ois.readObject();
        String str2 = (String) ois.readObject();
        System.out.println(str2); // 走，来

        ois.close();
    }

    /**
     * 演示自定义类的对象的序列化和反序列化过程
     * 序列化过程：
     */
    @Test
    public void test3() throws IOException, ClassNotFoundException {
        // 1.
        File file = new File("/Users/Shared/java_learning/io/object1.dat");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

        // 2.写出数据即为序列化的过程
        Person p1 = new Person("Tom", 12);
        oos.writeObject(p1);
        oos.flush();

        /*
         * 引用类型字段的类也必须实现 Serializable。Person 里的 Account 字段，
         * 而 Account 已经 implements Serializable，所以 p2 能被正常序列化。
         */
        Person p2 = new Person("Jerry", 23, 1001, new Account(2000));
        oos.writeObject(p2);
        oos.flush();

        // 3.
        oos.close();
    }

    /**
     * 反序列化过程：
     */
    @Test
    public void test4() throws IOException, ClassNotFoundException {
        // 1.
        File file = new File("/Users/Shared/java_learning/io/object1.dat");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        // 2. 读取文件中的对象（或反序列化的过程）
        /*
         * readObject() 声明的返回类型是 Object
         * 从"可读性/可用性"看，直接用 Person 接更规范：Person person = (Person) ois.readObject();
         * 这样拿到手就是 Person，能直接调 getName() 等方法，不用再多转一次。
         *
         * 强转：虽然运行时对象确实是 Person，但编译器只看readObject() 的声明返回类型 Object，
         * 但不知道具体是谁，所以必须你手动 (Person)
         *
         * ObjectInputStream 不知道原来的变量名 p1、p2，只按对象写入的顺序读取。
         * test3() 中先写 p1、再写 p2，所以这里第一次 readObject() 得到 p1 的对象，
         * 第二次得到 p2 的对象；如果写入顺序交换，读取结果也会跟着交换。
         */
        Person person = (Person) ois.readObject();
        System.out.println(person); // Person{name='Tom', age=12, id=0, acct=null}

        Person person1 = (Person) ois.readObject();
        System.out.println(person1); // Person{name='Jerry', age=23, id=1001, acct=Account{balance=2000.0}}

        ois.close();
    }
}

// Serializable:属于一个标识接口
class Person implements Serializable {
    /*
     * transient："修饰字段"的关键字，加在字段类型前面，如：transient int age;
     * 作用：被 transient 修饰的字段"不参与序列化"。序列化时它的值不会写进文件，
     * 反序列化后该字段会变成默认值（int --> 0、对象 --> null）。
     * 用途：给"不想/不能保存的敏感或临时字段"用，比如密码、缓存、线程等。
     *
     * transient 和 static 的共同点：都不会被序列化。
     * - static 字段属于"类"而不属于"对象"，本来就不随对象写出，反序列化后它显示的是
     *   "当前JVM里该静态变量的值"，而不是文件里的值。
     * - transient 字段属于对象但被你主动排除，反序列化后是默认值。
     *
     * 若把 int age 改成 transient int age，反序列化后 age 会是 0；
     * 若把它改成 static，序列化也不会保存它，读回来是类当前的静态值。
     */
    String name;
    int age;
    int id;
    Account acct;
    static final long serialVersionUID = 422334254234L;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, int id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public Person(String name, int age, int id, Account acct) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.acct = acct;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @Override
//    public String toString() {
//        return "Person{" +
//                "name='" + name + '\'' +
//                ", age=" + age +
//                '}';
//    }


//    @Override
//    public String toString() {
//        return "Person{" +
//                "name='" + name + '\'' +
//                ", age=" + age +
//                ", id=" + id +
//                '}';
//    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", id=" + id +
                ", acct=" + acct +
                '}';
    }
}

class Account implements Serializable {
    double balance;
    static final long serialVersionUID = 422234L;

    public Account(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                '}';
    }
}
