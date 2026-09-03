package chapter11;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

public class StringTest {
    /*
    1. String类的理解(以JDK8为例说明)
    1.1 类的声明
    public final class String
        implements java.io.Serializable, Comparable<String>, CharSequence
    > final:String是不可被继承的
    > Serializable:可序列化的接口。凡是实现此接口的类的对象就可以通过网络或本地流进行数据的传输。
    > Comparable:凡是实现此接口的类，其对象都可以比较大小。
    1.2 内部声明的属性：
    jdk8中：
    private final char value[]; // 存储字符串数据的容器
        > final : 指明此value数组一旦初始化，其地址就不可变。

     * byte 本质就是 8 位二进制(-128~127)，它并不"知道"自己存的是数字还是字符。
     * 字符最终在计算机里也是用数字(编码值)表示的：比如 'a' 的编码是 97。
     * jdk9 的 String 底层改成 byte[]，是因为大量字符串其实只用到 Latin-1(每字符1字节)，
     * 用 byte[] 存能省一半内存；遇到中文等字符时会用 UTF-16 编码，一个字符占 2 字节，
     * 用两个 byte 拼起来表示。所以"byte 只能存数字"没错，只是字符本来就是靠数字编码的。
     * 另外配套多了一个 coder 字段，用来记录当前是 Latin-1 还是 UTF-16 编码。
    jdk9开始：为了节省内存空间，做了优化
    private final byte[] value; // 存储字符串数据的容器
    2. 字符串常量的存储位置
    > 字符串常量都存储在字符串常量池(StringTable)中
    > 字符串常量池不允许存放两个相同的字符串常量。
    > 字符串常量池，在不同的jdk版本中，存放位置不同。
      jdk7之前：字符串常量池存放在方法区
      jdk7及之后：字符串常量池存放在堆空间。
    3. String的不可变性的理解
    ① 当对字符串变量重新赋值时，需要重新指定一个字符串常量的位置进行赋值，不能在原有的位置修改
    ② 当对现有的字符串进行拼接操作时，需要重新开辟空间保存拼接以后的字符串，不能在原有的位置修改
    ③ 当调用字符串的replace()替换现有的某个字符时，需要重新开辟空间保存修改以后的字符串，不能在原有的位置修改
    4. String实例化的两种方式
    第1种方式：String s1 = "hello";
    第2种方式：String s2 = new String("hello");
    5. String的连接操作:+
    情况1：常量 + 常量: 结果仍然存储在字符串常量池中，返回此字面量的地址。注：此时的常量可能是字面量，也可能是final修饰的常量
    情况2：常量 + 变量  或  变量 + 变量 ：都会通过new的方式创建一个新的字符串，返回堆空间中此字符串对象的地址
    情况3：调用字符串的intern():返回的是字符串常量池中字面量的地址
    (了解)情况4：concat(xxx):不管是常量调用此方法，还是变量调用，同样不管参数是常量还是变量，总之，调用完concat()方法
    都返回一个新new的对象。
    6. String的构造器和常用方法
    6.1 构造器
    * `public String() ` ：初始化新创建的 String对象，以使其表示空字符序列。
    * `public String(String original)`： 初始化一个新创建的 `String` 对象，使其表示一个与参数相同的字符序列；换句话说，新创建的字符串是该参数字符串的副本。
    * `public String(char[] value) ` ：通过当前参数中的字符数组来构造新的String。
    * `public String(char[] value,int offset, int count) ` ：通过字符数组的一部分来构造新的String。
    * `public String(byte[] bytes) ` ：通过使用平台的【默认字符集】解码当前参数中的字节数组来构造新的String。
    * `public String(byte[] bytes,String charsetName) ` ：通过使用指定的字符集解码当前参数中的字节数组来构造新的String。
    7. String的算法练习
     */

    /**
     * 2. 字符串常量的存储位置
     */
    @Test
    public void test1() {
        // 字符串常量池不允许存放两个相同的字符串常量
        String s1 = "hello"; // 字面量的定义方式
        String s2 = "hello"; // 字面量的定义方式

        /*
         * 为什么是 true：两次都是用字面量"hello"赋值。
         * 第一次时常量池里没有"hello"，就在池中创建一个；
         * 第二次发现池中已有"hello"，直接复用同一个地址，不再新建。
         * 所以 s1、s2 指向常量池里同一个对象，== 比地址，结果为 true。
         */
        System.out.println(s1 == s2); // true
    }

    /**
     * 3. String的不可变性的理解
     * ① 当对字符串变量重新赋值时，需要重新指定一个字符串常量的位置进行赋值，不能在原有的位置修改
     */
    @Test
    public void test2() {
        String s1 = "hello";
        String s2 = "hello";

        /*
         * s2 = "hi" 并不是把原来"hello"那块内存改成"hi"，
         * 而是在常量池另找/新建一个"hi"，让 s2 改指向它。
         * 原来的"hello"没被动过，s1 还指着它，所以 s1 仍是 hello。
         * 这就是"不可变性"：变的是引用的指向，不是原字符串内容本身。
         */
        s2 = "hi";

        System.out.println(s1); // hello
    }

    /**
     * ② 当对现有的字符串进行拼接操作时，需要重新开辟空间保存拼接以后的字符串，不能在原有的位置修改
     */
    @Test
    public void test3() {
        String s1 = "hello";
        String s2 = "hello";

        /*
         * s2 += "world" 等价于 s2 = s2 + "world"。
         * 因为含变量 s2，会 new 出一个新字符串"helloworld"放在堆里，s2 改指向它。
         * 原来的"hello"没变，s1 仍然是 hello。
         */
        s2 += "world";

        System.out.println(s1); // hello
        System.out.println(s2); // helloworld
    }

    /**
     * ③ 当调用字符串的replace()替换现有的某个字符时，需要重新开辟空间保存修改以后的字符串，不能在原有的位置修改
     */
    @Test
    public void test4() {
        String s1 = "hello";
        String s2 = "hello";

        /*
         * 调用 replace(char, char) 这个重载，
         * 参数是 char（字符），char 字面量就必须用单引号 'l'。
         * String 还有另一个重载 replace(CharSequence, CharSequence)，那个用双引号，
         * 可以一次替换一串，比如 s2.replace("ll", "ww")。见下面 test10 的例子。
         *
         * String 不可变，replace 不会改原串，
         * 而是返回一个"替换后的新字符串"，用返回值 s3 接住。
         */
        String s3 = s2.replace('l', 'w');

        System.out.println(s1); // hello
        System.out.println(s2); // hello
        System.out.println(s3); // hewwo
    }

    @Test
    public void test5() {
        String s1 = "hello";
        String s2 = "hello";

        /*
         * String 是一个类，new 一个类对象是最普通的写法。它有个构造器 String(String original)，
         * 传入"hello"，就会在【堆】里 new 出一个新的 String 对象，内容拷贝自"hello"。
         * 所以每次 new 都会在堆里得到一个全新对象，地址各不相同。
         */
        String s3 = new String("hello");
        String s4 = new String("hello");

        /*
         * == 比的是地址(引用)，equals 比的是内容。
         * s1、s2 是字面量，指向常量池同一个"hello"，所以 s1 == s2 为 true。
         * s3、s4 是 new 出来的，各自在堆里是独立对象，地址都不同，
         * 所以 s1 == s3、s3 == s4 都是 false。
         */
        System.out.println(s1 == s2); // true
        System.out.println(s1 == s3); // false
        System.out.println(s3 == s4); // false

        /*
         * String 重写了 equals，比较的是字符内容而非地址。
         * 四个变量内容都是"hello"，所以 equals 全部为 true。
         */
        System.out.println(s1.equals(s2)); // true
        System.out.println(s1.equals(s3)); // true
        System.out.println(s3.equals(s4)); // true
    }

    /**
     * String s = new String("hello");的内存分析：
     * 1. "hello" 是字符串字面量，会放入字符串常量池。
     * 2. new String() 会在堆中创建一个新的String对象。
     *
     * 如果字符串常量池中不存在"hello"，执行该语句通常会创建两个对象：
     * 一个是字符串常量池中的字面量对象，
     * 一个是堆中新创建的String对象。
     *
     * 如果常量池中已经存在"hello"，则只会创建一个堆中的String对象。
     */
    String s = new String("hello");

    @Test
    public void test6() {
        Person p1 = new Person();
        Person p2 = new Person();
        p1.name = "Tom";
        p2.name = "Tom";

        /*
         * 改的是引用指向，不是原对象。
         * p2.name = "Jerry" 只是让 p2 的 name 改指向"Jerry"，
         * 和 p1.name 是两个独立的引用，p1.name 完全不受影响，仍是 Tom。
         */
        p2.name = "Jerry";
        System.out.println(p1.name); // Tom
    }

    /**
     * 5. String的连接操作:+
     * 情况1：常量 + 常量: 结果仍然存储在字符串常量池中，返回此字面量的地址。注：此时的常量可能是字面量，也可能是final修饰的常量
     * 情况2：常量 + 变量  或  变量 + 变量 ：都会通过new的方式创建一个新的字符串，返回堆空间中此字符串对象的地址
     * 情况3：调用字符串的intern():返回的是字符串常量池中字面量的地址
     * (了解)情况4：concat(xxx):不管是常量调用此方法，还是变量调用，同样不管参数是常量还是变量，总之，调用完concat()方法
     * 都返回一个新new的对象。
     */
    @Test
    public void test7() {
        String s1 = "hello";
        String s2 = "world";

        String s3 = "helloworld";
        String s4 = "hello" + "world";
        String s5 = s1 + "world"; // 通过查看字节码文件发现调用了StringBuilder的toString()---> new String()
        String s6 = "hello" + s2;
        String s7 = s1 + s2;

        /*
         * 【核心规律】关键看"+两边编译期能不能确定值"。
         * s4 = "hello" + "world"：两边都是字面量常量，编译器在编译期就直接算好成"helloworld"，
         * 相当于 s4 = "helloworld"，指向常量池同一个对象，所以 s3 == s4 为 true。
         *
         * s5 = s1 + "world"、s6 = "hello" + s2、s7 = s1 + s2：只要有一个是变量(s1/s2)，
         * 编译期没法确定值，就会在运行时用 StringBuilder 拼接，最后 toString() 得到一个
         * 【new 在堆里】的新 String。堆对象地址和常量池的 s3 不同，也彼此不同，所以全是 false。
         */
        System.out.println(s3 == s4); // true
        System.out.println(s3 == s5); // false
        System.out.println(s3 == s6); // false
        System.out.println(s3 == s7); // false
        System.out.println(s5 == s6); // false
        System.out.println(s5 == s7); // false

        /*
         * intern() 会去常量池里找有没有内容相同的字符串：
         * 有就返回池中那个的地址，没有就把当前内容放进池里再返回池中地址。
         * s5 内容是"helloworld"，s5.intern() 返回的是常量池里"helloworld"的地址，
         * 而 s3 本来就指着常量池的"helloworld"，所以 s3 == s8 为 true。
         */
        String s8 = s5.intern(); // intern():返回的是字符串常量池中字面量的地址
        System.out.println(s3 == s8); // true
    }

    @Test
    public void test8() {
        /*
         * 常量的值在编译期就固定了，不可能再变。
         * 于是编译器把 s1、s2 当成常量代入，s1 + "world" 在编译期就能算成"helloworld"，
         * 等价于常量 + 常量，结果进常量池，指向和 s3 同一个对象。
         * 所以这里 s5、s6、s7 都与 s3 相等（对比 test7 里没 final 时全是 false）。
         */
        final String s1 = "hello";
        final String s2 = "world";

        String s3 = "helloworld";
        String s4 = "hello" + "world";
        String s5 = s1 + "world";
        String s6 = "hello" + s2;
        String s7 = s1 + s2;

        System.out.println(s3 == s5); // true
        System.out.println(s3 == s6); // true
        System.out.println(s3 == s7); // true
    }

    /**
     * (了解)情况4：concat(xxx):不管是常量调用此方法，还是变量调用，同样不管参数是常量还是变量，总之，调用完concat()方法
     * 都返回一个新new的对象。
     */
    @Test
    public void test9() {
        /*
         * concat 是一个普通方法，它内部 new 出一个新的 String 返回，
         * 而且这个 new 是在【运行时】发生的，不会像"常量+常量"那样被编译期优化进常量池。
         * 所以哪怕 "hello".concat("world") 两边都是常量，结果也是运行时 new 的堆对象。
         * s3、s4、s5 是三次独立调用，各得一个新堆对象，地址都不同，两两比较全是 false。
         */
        String s1 = "hello";
        String s2 = "world";

        String s3 = s1.concat(s2);
        String s4 = "hello".concat("world");
        String s5 = s1.concat("world");

        System.out.println(s3); // helloworld
        System.out.println(s4); // helloworld
        System.out.println(s5); // helloworld
        System.out.println(s3 == s4); // false
        System.out.println(s3 == s5); // false
        System.out.println(s4 == s5); // false
    }
}

class Person {
    String name;
}

class StringMethodTest {
    /**
     * String构造器的使用
     */
    @Test
    public void test1() {
        String s1 = new String();
        String s2 = new String("");

        String s3 = new String(new char[]{'a', 'b', 'c'});
        System.out.println(s3); // abc
    }

    /**
     * String与常见的其它结构之间的转换
     * 1. String与基本数据类型、包装类之间的转换（复习）
     */
    @Test
    public void test2() {
        int num = 10;

        // 基本数据类型 ---> String
        // 方式1：num + "" 靠拼接空串把 int 转成 String，写法简单但会产生一次拼接，性能略差
        String s1 = num + "";

        // 方式2：String.valueOf(num)，语义清楚、推荐，内部实际是 Integer.toString(num)
        String s2 = String.valueOf(num);

        // String --> 基本数据类型:调用包装类的parseXxx(String str)
        String s3 = "123";
        int i1 = Integer.parseInt(s3);
    }

    /**
     * 2. String与char[]之间的转换
     */
    /*
     * toCharArray() 的作用就是把字符串里的"每一个字符"都拆出来，
     * 按顺序放进一个 char 数组，长度正好等于字符串长度。所以不会只进第一个、更不会进不去。
     * "hello" -> ['h','e','l','l','o']，arr.length == 5，遍历打印就是竖着的 h e l l o。
     */
    @Test
    public void test3() {
        String str = "hello";

        // String --> char[]:调用String的toCharArray()
        char[] arr = str.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        //char[] ---> String:调用String的构造器
        String str1 = new String(arr);
        System.out.println(str1); // hello
    }

    /**
     * 3. String与byte[]之间的转换（难度）
     * 在utf-8字符集中，一个汉字占用3个字节，一个字母占用1个字节。
     * 在gbk字符集中，一个汉字占用2个字节，一个字母占用1个字节。
     * utf-8或gbk都向下兼容了ascii码。
     * 编码与解码：
     * 编码：String ---> 字节或字节数组
     * 解码：字节或字节数组 ----> String
     * 要求：解码时使用的字符集必须与编码时使用的字符集一致！不一致，就会乱码。
     */
    @Test
    public void test4() throws UnsupportedEncodingException {
        String str = new String("hello你好");

        // String --> byte[]:调用String的getBytes()
        byte[] arr = str.getBytes(); // 使用默认的字符集:utf-8

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        /*
         * 【"为什么会报错/报的什么错"】
         * getBytes(String charsetName) 声明了会抛受检异常 UnsupportedEncodingException
         * (当你写的字符集名字系统不认识时抛)。Java 规定受检异常必须处理，
         * 否则【编译】就不通过——所以这不是运行报错，是必须处理它。
         * "gbk"本身是合法字符集名，正常运行不会真的抛，但编译器仍要求你先处理这个"可能"。
         *
         * 【两种处理方式】
         *  - throws：在方法后面 throws UnsupportedEncodingException，把异常往上抛。
         *  - try-catch：把这行包进 try 里，catch (UnsupportedEncodingException e) 自己处理。
         *  正式代码更推荐用 java.nio 里的StandardCharsets.UTF_8 这种常量，它不抛受检异常，也就不用处理了。
         */

        // getBytes(String charsetName):使用指定的字符集
        byte[] arr1 = str.getBytes("gbk");

        for (int i = 0; i < arr1.length; i++) {
            System.out.println(arr1[i]);
        }

        // byte[] ---> String:
        /*
         * 不乱码的关键：编码和解码用的字符集要一致。
         * arr 是用 UTF-8 编出来的，这里 new String(arr) 也用默认 UTF-8 来解，
         * "编码=解码"，所以完美还原成 hello你好，不乱码。
         */
        String str1 = new String(arr); // 使用默认的字符集：utf-8
        System.out.println(str1); // hello你好

        /*
         * 字符集名字不区分大小写，"utf-8""UTF-8""Utf-8"都行。
         * 这里 arr 是 UTF-8 编的，显式再用 UTF-8 解，一致，所以正常还原。
         */
        String str2 = new String(arr, "utf-8"); // 显式的指明解码的字符集：utf-8
        System.out.println(str2); // hello你好

        // 乱码
        /*
         * arr 是 UTF-8 编的，这里却用 GBK 去解，编码≠解码，汉字部分就按错误规则拆读，
         * 于是变成 hello浣犲ソ 这种乱码。英文部分因为两套字符集都兼容 ASCII，所以 hello 没乱。
         */
        String str3 = new String(arr, "gbk"); // 显式的指明解码的字符集：gbk
        /*
         * 【"每次结果都一样还算乱码吗"】算。乱码 ≠ 随机。
         * 只要输入的字节和解码规则不变，错误的解码结果也是固定可复现的，所以每次都一样。
         * 但已经不是原本想要的"你好"了(变成了别的字符)，无法正确表达原意，所以仍属乱码，不能直接用。
         */
        System.out.println(str3); // hello浣犲ソ

        // arr1 是 GBK 编的，这里也用 GBK 解，一致，所以能正确还原成 hello你好
        String str4 = new String(arr1, "gbk");
        System.out.println(str4); // hello你好
    }

    /*
     * 6.2 常用方法
     * （1）boolean isEmpty()：字符串是否为空
     * （2）int length()：返回字符串的长度
     * （3）String concat(xx)：拼接
     * （4）boolean equals(Object obj)：比较字符串是否相等，区分大小写
     * （5）boolean equalsIgnoreCase(Object obj)：比较字符串是否相等，不区分大小写
     * （6）int compareTo(String other)：比较字符串大小，区分大小写，按照Unicode编码值比较大小
     * （7）int compareToIgnoreCase(String other)：比较字符串大小，不区分大小写
     * （8）String toLowerCase()：将字符串中大写字母转为小写
     * （9）String toUpperCase()：将字符串中小写字母转为大写
     * （10）String trim()：去掉字符串前后空白符
     * （11）public String intern()：结果在常量池中共享
     *
     * 【查找】
     * （12）boolean contains(xx)：是否包含xx
     * （13）int indexOf(xx)：从前往后找当前字符串中xx，即如果有返回第一次出现的下标，要是没有返回-1
     * （14）int indexOf(String str, int fromIndex)：返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始
     * （15）int lastIndexOf(xx)：从后往前找当前字符串中xx，即如果有返回最后一次出现的下标，要是没有返回-1
     * （16）int lastIndexOf(String str, int fromIndex)：返回指定子字符串在此字符串中最后一次出现处的索引，从指定的索引开始反向搜索。
     *
     * 【字符串截取】
     * （17）String substring(int beginIndex) ：返回一个新的字符串，它是此字符串的从beginIndex开始截取到最后的一个子字符串。
     * （18）String substring(int beginIndex, int endIndex) ：返回一个新字符串，它是此字符串从beginIndex开始截取到endIndex(不包含)的一个子字符串。
     *
     * 【和字符/字符数组相关】
     * （19）char charAt(index)：返回[index]位置的字符
     * （20）char[] toCharArray()： 将此字符串转换为一个新的字符数组返回
     * （21）static String valueOf(char[] data)  ：返回指定数组中表示该字符序列的 String
     * （22）static String valueOf(char[] data, int offset, int count) ： 返回指定数组中表示该字符序列的 String
     * （23）static String copyValueOf(char[] data)： 返回指定数组中表示该字符序列的 String
     * （24）static String copyValueOf(char[] data, int offset, int count)：返回指定数组中表示该字符序列的 String
     * （25）boolean startsWith(xx)：测试此字符串是否以指定的前缀开始
     * （26）boolean startsWith(String prefix, int toffset)：测试此字符串从指定索引开始的子字符串是否以指定前缀开始
     * （27）boolean endsWith(xx)：测试此字符串是否以指定的后缀结束
     *
     * 【开头与结尾】
     * （28）String replace(char oldChar, char newChar)：返回一个新的字符串，它是通过用 newChar 替换此字符串中出现的所有 oldChar 得到的。 不支持正则。
     * （29）String replace(CharSequence target, CharSequence replacement)：使用指定的字面值替换序列替换此字符串所有匹配字面值目标序列的子字符串。
     * （30）String replaceAll(String regex, String replacement)：使用给定的 replacement 替换此字符串所有匹配给定的正则表达式的子字符串。
     * （31）String replaceFirst(String regex, String replacement)：使用给定的 replacement 替换此字符串匹配给定的正则表达式的第一个子字符串。
     */
    @Test
    public void test5() {
        /**
         * （1）boolean isEmpty()：字符串是否为空
         */
        String s1 = "";
        String s2 = new String();
        String s3 = new String("");
        String s4 = null;

        /*
         * isEmpty() 判断的是"长度是否为 0"，即 length()==0。
         * s1/s2/s3 内容都是空串(长度0)，所以都是 true。
         */
        System.out.println(s1.isEmpty()); // true
        System.out.println(s2.isEmpty()); // true
        System.out.println(s3.isEmpty()); // true
        /*
         * 报空指针异常：NullPointerException
         * 注意区分"空串"和"null"：s4 是 null，压根没有对象，
         * 调用它的任何方法(.isEmpty())都会 NullPointerException。
         * 所以判空时常先判 s != null 再判 s.isEmpty()。
         */
//        System.out.println(s4.isEmpty());

        /**
         * （2）int length()：返回字符串的长度
         */
        String s5 = "hello";
        String s6 = "hi";
        System.out.println(s5.length()); // 5
        System.out.println(s6.length()); // 2
    }

    @Test
    public void test6() {
        /**
         * （4）boolean equals(Object obj)：比较字符串是否相等，区分大小写
         * （5）boolean equalsIgnoreCase(Object obj)：比较字符串是否相等，不区分大小写
         */
        String s1 = "hello";
        String s2 = "Hello";

        // equals 区分大小写：'h' 和 'H' 不同，所以 false
        System.out.println(s1.equals(s2)); // false

        // equalsIgnoreCase 忽略大小写：内容相同即 true
        System.out.println(s1.equalsIgnoreCase(s2)); // true

        /**
         * （6）int compareTo(String other)：比较字符串大小，区分大小写，按照Unicode编码值比较大小
         * （7）int compareToIgnoreCase(String other)：比较字符串大小，不区分大小写
         */
        String s3 = "abcd";
        String s4 = "adef";

        /*
         * 【compareTo 怎么比】从下标 0 开始逐个字符比编码值，遇到第一个不同的字符就返回
         * "前者字符编码 - 后者字符编码"，后面的不再看；若一路都相同，则返回"两串长度之差"。
         * "abcd" vs "adef"：第 0 位 a==a 相同，第 1 位 'b'(98) vs 'd'(100)，98-100 = -2，
         * 返回 -2 表示 s3 比 s4 小。
         */
        System.out.println(s3.compareTo(s4)); // -2

        String s5 = "abcd";
        String s6 = "abcd";

        // 完全相同，逐位都相等且长度相等，返回 0
        System.out.println(s5.compareTo(s6)); // 0

        /*
         * 'b'(98) vs 'B'(66)，98-66 = 32，返回 32(大写字母编码比小写小)；
         * 而 compareToIgnoreCase 忽略大小写视为相等，返回 0。
         */
        s6 = "aBcd";
        System.out.println(s5.compareTo(s6)); // 32
        System.out.println(s5.compareToIgnoreCase(s6)); // 0

        /*
         * 【"为什么差这么大"】比的还是 Unicode 编码差。汉字的编码值很大(几万级)，
         * "张"和"李"的编码相差就有 2094，所以 '张'-'李' = -2094，数值自然很大。规则没变，只是字符编码大。
         * 【能用 compareToIgnoreCase 吗】能调用，语法上没问题，只是"大小写"这个概念对汉字没意义，
         * 汉字没有大小写，忽略大小写和普通比较结果一样。
         */
        String s7 = "张ab";
        String s8 = "李cd";
        System.out.println(s7.compareTo(s8)); // -2094

        /**
         * （10）String trim()：去掉字符串前后空白符
         */
        /*
         * 【"什么空格都能去吗"】不是。trim() 只去掉字符串【首尾】、且编码 <= 空格(即 <= '\u0020')
         * 的字符，比如半角空格、Tab、换行等。它去不掉"全角空格"(\u3000，中文输入法下打的宽空格)，
         * 因为它的编码大于 0x20。中间的空格也不会动("hel   lo"中间那段保留)。
         * 想连全角空格/各种 Unicode 空白一起去，用 strip()(JDK11+) 更彻底。
         */
        String s9 = "   hel   lo     ";
        System.out.println(s9.trim()); // hel   lo
    }

    @Test
    public void test7() {
        /**
         * 【查找】
         * （12）boolean contains(xx)：是否包含xx
         */
        String s1 = "上午下午你好上午下午";
        /*
         * contains 判断是否包含"这一段连续的子串"。
         * "你"在里面 -> true；"ni"根本没有 -> false；
         * "好你"要求"好"紧跟"你"连续出现，原串里是"你好"不是"好你" -> false。
         */
        System.out.println(s1.contains("你")); // true
        System.out.println(s1.contains("ni")); // false
        System.out.println(s1.contains("好你")); // false

        /**
         * （13）int indexOf(xx)：从前往后找当前字符串中xx，即如果有返回第一次出现的下标，要是没有返回-1
         */
        System.out.println(s1.indexOf("好")); // 5
        System.out.println(s1.indexOf("好1")); // -1
        /*
         * indexOf(1) 里的 1 是 int，被当成"字符编码为1的字符"去找，没有 -> -1。
         * 找 Unicode 编码为 1 的字符
         */
        System.out.println(s1.indexOf(1)); // -1

        /**
         * （14）int indexOf(String str, int fromIndex)：返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始
         */
        /*
         * 带 fromIndex：从指定下标开始往后找。
         * "下午"第一次在下标 2；从 2 开始找到的仍是 2；从 3 开始找，跳过了前一个，
         * 就找到第二个"下午"在下标 8。
         */
        System.out.println(s1.indexOf("下午", 2)); // 2
        System.out.println(s1.indexOf("下午", 3)); // 8

        /**
         * （15）int lastIndexOf(xx)：从后往前找当前字符串中xx，即如果有返回最后一次出现的下标，要是没有返回-1
         */
        // 从后往前找，返回"最后一次出现"的下标：最后那个"下午"起始下标是 8
        System.out.println(s1.lastIndexOf("下午")); // 8

        /**
         * （16）int lastIndexOf(String str, int fromIndex)：返回指定子字符串在此字符串中最后一次出现处的索引，从指定的索引开始反向搜索。
         */
        /*
         * 带 fromIndex 的 lastIndexOf：从该下标开始【往前】找。
         * 判断标准是"子串的起始下标 <= fromIndex"就算候选，取满足条件里最靠右的那个。
         * "下午"出现在下标 2 和 8：
         * fromIndex 为 9、8 时，起始 8 <= 它，返回 8；
         * fromIndex 为 7、3、2 时，8 不满足，只能取起始下标 2，返回 2。
         */
        System.out.println(s1.lastIndexOf("下午", 9)); // 8
        System.out.println(s1.lastIndexOf("下午", 8)); // 8
        System.out.println(s1.lastIndexOf("下午", 7)); // 2
        System.out.println(s1.lastIndexOf("下午", 3)); // 2
        System.out.println(s1.lastIndexOf("下午", 2)); // 2
    }

    /**
     * 【字符串截取】
     * （17）String substring(int beginIndex) ：返回一个新的字符串，它是此字符串的从beginIndex开始截取到最后的一个子字符串。
     * （18）String substring(int beginIndex, int endIndex) ：返回一个新字符串，它是此字符串从beginIndex开始截取到endIndex(不包含)的一个子字符串。
     */
    /*
     * substring(begin, end) 是"左闭右开"[begin, end)：包含 begin，不包含 end。
     * 好处是 end - begin 正好等于截取长度，且上一段的 end 可直接当下一段的 begin，方便连续切割。
     */
    @Test
    public void test8() {
        String s1 = "上午你好上午";
        System.out.println(s1.substring(2)); // 你好上午
        System.out.println(s1.substring(2, 4)); // 你好
        System.out.println(s1.substring(2, 3)); // 你
    }

    @Test
    public void test9() {
        /**
         * 【和字符/字符数组相关】
         * （19）char charAt(index)：返回[index]位置的字符
         */
        String s1 = "上午你好上午";
        // charAt(2)：取下标 2 处的字符，"上(0)午(1)你(2)..."，所以是 '你'
        System.out.println(s1.charAt(2)); // 你

        /**
         * （21）static String valueOf(char[] data)  ：返回指定数组中表示该字符序列的 String
         *
         * static 是"方法本身的修饰符"，写在方法定义处；而你这里是在【调用】方法，调用时从不写 static。
         * 判断依据：静态方法用【类名.方法名】调用(如 String.valueOf)，这正说明它是 static 的；
         * 非静态方法用【对象.方法名】调用(如 s1.charAt)。所以你没漏写，调用处本来就不带 static。
         */
        String s2 = String.valueOf(new char[]{'a', 'b', 'c'});
        System.out.println(s2); // abc

        /**
         * （23）static String copyValueOf(char[] data)： 返回指定数组中表示该字符序列的 String
         */
        String s3 = String.copyValueOf(new char[]{'a', 'b', 'c'});
        System.out.println(s3); // abc

        /*
         * s2、s3 内容都是"abc"但地址不同：valueOf 和 copyValueOf 都各自 new 出新的 String 返回，
         * 是两个独立堆对象，所以 == 为 false。(内容比较请用 equals，会是 true)
         */
        System.out.println(s3 == s2); // false

        /**
         * （25）boolean startsWith(xx)：测试此字符串是否以指定的前缀开始
         * （26）boolean startsWith(String prefix, int toffset)：测试此字符串从指定索引开始的子字符串是否以指定前缀开始
         */
        /*
         * startsWith(prefix)：判断是否以 prefix 开头。
         * startsWith(prefix, toffset)：判断"从 toffset 这个下标起"是否以 prefix 开头。
         * 原串"上午你好上午"：
         *  从 0 起是"上午..." -> "上午"成立 true；
         *  从 1 起是"午你..." -> 不以"上午"开头 false；从 3 起是"好上..." false；
         *  从 4 起是"上午" -> true。
         */
        System.out.println(s1.startsWith("上午")); // true
        System.out.println(s1.startsWith("上午a")); // false
        System.out.println(s1.startsWith("你好")); // false
        System.out.println(s1.startsWith("上午", 0)); // true
        System.out.println(s1.startsWith("上午", 1)); // false
        System.out.println(s1.startsWith("上午", 3)); // false
        System.out.println(s1.startsWith("上午", 4)); // true
    }

    /**
     * 【开头与结尾】
     * （28）String replace(char oldChar, char newChar)：返回一个新的字符串，它是通过用 newChar 替换此字符串中出现的所有 oldChar 得到的。 不支持正则。
     */
    @Test
    public void test10() {
        String s1 = "hello";
        /*
         * replace(char, char)：把所有的 'l' 换成 'w'，返回新串，不改原串。
         * 这里参数是 char，所以两边用单引号
         */
        String s2 = s1.replace('l', 'w');
        System.out.println(s1); // hello
        System.out.println(s2); // hewwo

        /**
         * （29）String replace(CharSequence target, CharSequence replacement)：使用指定的字面值替换序列替换此字符串所有匹配字面值目标序列的子字符串。
         */
        /*
         * 这是另一个重载 replace(CharSequence, CharSequence)：参数是字符串(双引号)，
         * 可以一次替换"一整段"，把所有"ll"换成"abc"，得到 heabco。
         * 它按【字面值】替换，不支持正则(要正则用 replaceAll)。
         */
        String s3 = s1.replace("ll", "abc");
        System.out.println(s3); // heabco
    }
}
