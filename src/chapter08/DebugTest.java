package chapter08;

public class DebugTest {
    /*
    IDEA中调试程序
    1. 为什么需要Debug?
    编好的程序在执行过程中如果出现错误，该如何查找或定位错误呢？简单的代码直接就可以看出来，
    但如果代码比较复杂，就需要借助程序调试工具（Debug）来查找错误了。
    2. Debug的步骤
        1、添加断点
        2、启动调试
        3、单步执行
        4、观察变量和执行流程，找到并解决问题
     */
    public static void main(String[] args) {
        // Debug1
        // 演示1：行断点 & 测试debug各个常见操作按钮：最常用，点在某一行代码左侧
        int m = 10;
        int n = 20;
        // 断点一般加在"怀疑出错的那行"或它的前一行，从这里开始一步步往下走观察
        System.out.println("m = " + m + ",n = " + n);
        // 重点观察：swap执行完之后，m 和 n 的值有没有变？
        // 结论：没变。因为基本数据类型是"值传递"，传给方法的是m、n的副本，方法内交换的是副本，不影响这里的m、n
        swap(m, n);
        System.out.println("hello");
        System.out.println("m = " + m + ",n = " + n);

        // 2. 数组的打印：直接打印数组名，得到的是"地址值"（哈希形式），不是内容
        int[] arr = new int[]{1, 2, 3, 4, 5};
        System.out.println(arr); // 地址值

        // 特例：char[] 数组直接打印，输出的是内容而不是地址值
        // 原因：println 对 char[] 做了重载处理
        char[] arr1 = new char[]{'a', 'b', 'c'};
        System.out.println(arr1); // abc（唯一一个直接打印内容的数组类型）
    }

    // swap方法：内部交换的是形参m、n（实参的副本），对调用处的变量没有影响
    public static void swap(int m, int n) {
        int temp = m;
        m = n;
        n = temp;
    }
}

// 演示2： 方法断点：点在方法名那一行，进入该方法时会停下（可用于观察多态调用的是哪个方法）
// 场景：在父类方法上打"方法断点"，运行时可以清楚地看到多态到底调用了哪个类的方法
class Debug2 {
    public static void main(String[] args) {
        // 1. 普通调用：子类对象调用子类自己的test()
        Son instance = new Son();
        instance.test();

        // 2. 类的多态性：编译看左边(Father3)，运行看右边(Son)，实际执行Son的test()
        Father3 instance1 = new Son();
        instance1.test();

        // 3. 接口的多态性：接口引用指向实现类对象，实际执行ConsumerImpl的accept()
        Consumer con = new ConsumerImpl();
        con.accept("atguigu");

        // 4. （可选）在HashMap的put上打断点，能进入源码观察底层存储过程
//        HashMap map = new HashMap();
//        map.put("Tom",12);
//        map.put("Jerry",11);
//        map.put("Tony",20);
    }
}

class Father3 {
    public void test() {
        System.out.println("Father3 : test1");
        System.out.println("Father3 : test2");
    }
}

class Son extends Father3 {
    @Override
    public void test() {
        System.out.println("Son : test1");
        System.out.println("Son : test2");
    }
}

interface Consumer {
    void accept(String str);
}

class ConsumerImpl implements Consumer {
    @Override
    public void accept(String str) {
        System.out.println("ConsumerImple:" + str);
    }
}

// 演示3：字段断点：点在成员变量上，该变量被读取/修改时会停下（在成员变量 id 上打断点）
// 作用：id 的值每次被"修改"或"读取"时，程序都会停下来，
// 可以用来追踪 id 值的变化过程：默认值1 -> 代码块改成2 -> 构造器改成3
class Debug3 {
    public static void main(String[] args) {
        // 用带参构造器创建对象，观察 id 最终是多少（结果为3）
        Person4 p1 = new Person4(3);
        System.out.println(p1.toString());
    }
}

class Person4 {
    private int id = 1; // ① 显式赋值：id = 1
    private String name;

    public Person4() {
    }

    {
        id = 2; // ② 代码块：id = 2（在构造器之前执行）
    }

    public Person4(int id) {
        this.id = id; // ③ 构造器：id = 3（最后执行，覆盖前面的值）
    }


    public Person4(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "Person4{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

// 演示4：条件断点：给行断点加条件，只有满足条件时才停下（循环里非常有用）
// 用法：在for循环那行打断点，右键断点填写条件(如 i == 5 或 target == 8)，
// 只有满足条件时才会停下，避免在循环里一次次手动跳过，非常适合调试大循环
class Debug04 {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        for (int i = 0; i < arr.length; i++) {
            int target = arr[i]; // 在此行设条件断点，例如条件写 target == 8
            System.out.println(target);
        }
    }
}

// 演示5：强制结束
// 场景：调试到一半发现不需要往下走了（比如不想真的执行写库操作），可以点 Stop 按钮(cmd+F2)直接终止程序，后面的代码不再执行
class Debug05 {
    public static void main(String[] args) {
        System.out.println("获取请求的数据");
        System.out.println("调用写入数据库的方法");
        insert(); // 可在进入insert()后强制结束，避免执行"写入数据库"逻辑
        System.out.println("程序结束");
    }

    private static void insert() {
        System.out.println("进入insert()方法");
        System.out.println("获取数据库连接");
        System.out.println("将数据写入数据表中");
        System.out.println("写出操作完成");
        System.out.println("断开连接");
    }
}
