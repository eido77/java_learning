package chapter12;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class MapTest {
    /*
    1. Map及其实现类对比
    java.util.Map:存储一对一对的数据(key-value键值对，(x1,y1)、(x2,y2))
        |---- HashMap:主要实现类;线程不安全的，效率高;可以添加null的key和value值;底层使用数组+单向链表+红黑树结构存储（jdk8）
            |---- LinkedHashMap:是HashMap的子类；在HashMap使用的数据结构的基础上，增加了一对双向链表，用于记录添加的元素的先后顺序，
                                进而我们在遍历元素时，就可以按照添加的顺序显示。
                                开发中，对于频繁的遍历操作，建议使用此类。
        |---- TreeMap:底层使用红黑树存储;可以按照添加的key-value中的key元素的指定的属性的大小顺序进行遍历。需要考虑使用①自然排序 ②定制排序。
        |---- Hashtable:古老实现类;线程安全的，效率低;不可以添加null的key或value值;底层使用数组+单向链表结构存储（jdk8）
            |---- Properties:其key和value都是String类型。常用来处理属性文件。
    [面试题] 区别HashMap和Hashtable、区别HashMap和LinkedHashMap、HashMap的底层实现（① new HashMap() ② put(key,value)）
     * 1) HashMap vs Hashtable：
     *    - 线程安全：HashMap 不安全（效率高）；Hashtable 用 synchronized 修饰方法，安全但效率低。
     *    - null：HashMap 允许 1 个 null 键和多个 null 值；Hashtable 键、值都不允许 null。
     *    - 出身：Hashtable 是 JDK1.0 的古老类；HashMap 是 JDK1.2 的新类。
     *    - 结论：并发场景不要用 Hashtable，改用 ConcurrentHashMap。
     *
     * 2) HashMap vs LinkedHashMap：
     *    - LinkedHashMap 是 HashMap 的子类。
     *    - 在 HashMap 的数组+链表/红黑树基础上，额外维护一条“双向链表”记录插入顺序，
     *      所以遍历时能按插入顺序输出；HashMap 遍历顺序无保证。
     *    - 频繁遍历建议用 LinkedHashMap。
     *
     * 3) HashMap 底层实现（JDK8）：
     *    - new HashMap()：并不立即创建长度16的数组，而是在第一次 put 时才初始化（懒加载）。
     *    - put(key,value)：先算 key 的 hash（对 hashCode 做扰动），定位到数组下标；
     *      该位置为空则直接放；不为空则遍历链表/红黑树，用 equals 判断 key 是否已存在，
     *      存在则覆盖 value，不存在则追加。
     *    - 当链表长度 > 8 且数组长度 >= 64 时，链表转为红黑树；元素数超过 容量*0.75 时扩容为 2 倍。

    2. HashMap中元素的特点
    > HashMap中的所有的key彼此之间是不可重复的、无序的。所有的key就构成一个Set集合。---> key所在的类要重写hashCode()和equals()
    > HashMap中的所有的value彼此之间是可重复的、无序的。所有的value就构成一个Collection集合。---> value所在的类要重写equals()
    > HashMap中的一个key-value,就构成了一个entry。
    > HashMap中的所有的entry彼此之间是不可重复的、无序的。所有的entry就构成了一个Set集合。
    3. Map中的常用方法
    - 添加、修改操作：
      - Object put(Object key,Object value)：将指定key-value添加到(或修改)当前map对象中
      - void putAll(Map m):将m中的所有key-value对存放到当前map中
    - 删除操作：
      - Object remove(Object key)：移除指定key的key-value对，并返回value
      - void clear()：清空当前map中的所有数据
    - 元素查询的操作：
      - Object get(Object key)：获取指定key对应的value
      - boolean containsKey(Object key)：是否包含指定的key
      - boolean containsValue(Object value)：是否包含指定的value
      - int size()：返回map中key-value对的个数
      - boolean isEmpty()：判断当前map是否为空
      - boolean equals(Object obj)：判断当前map和参数对象obj是否相等
    - 元视图操作的方法：
      - Set keySet()：返回所有key构成的Set集合
      - Collection values()：返回所有value构成的Collection集合
      - Set entrySet()：返回所有key-value对构成的Set集合
    小结：
        增：
            put(Object key,Object value)
            putAll(Map m)
        删：
            Object remove(Object key)
        改：
            put(Object key,Object value)
            putAll(Map m)
        查：
            Object get(Object key)
        长度：
            size()
        遍历：
           遍历key集：Set keySet()
           遍历value集：Collection values()
           遍历entry集：Set entrySet()
    4. TreeMap的使用
    > 底层使用红黑树存储;
    > 可以按照添加的key-value中的key元素的指定的属性的大小顺序进行遍历。
    > 需要考虑使用 ① 自然排序 ② 定制排序。
    > 要求:向TreeMap中添加的key必须是同一个类型的对象。
    5. Hashtable与Properties的使用
    Properties:是Hashtable的子类，其key和value都是String类型的，常用来处理属性文件。
     */

    @Test
    public void test1() {
        Map map = new HashMap();

        map.put(null, null);

        map.put("Tom", 23);
        map.put(34, "AA");
        map.put("CC", "11");

        /*
         * HashMap 是【无序】的，它不按你 put 的先后顺序存，而是按 key 的哈希值决定存到数组哪个下标。
         *  - null 键的 hash 被规定为 0，所以永远落在数组下标 0，因此常常出现在最前面。
         *  - "CC"、34、"Tom" 各自的 hashCode 算出不同下标，遍历时按下标从小到大输出，
         *    所以看起来“乱序”“像反的”，其实只是哈希分布的结果，和插入顺序无关。
         */
        System.out.println(map); // {null=null, CC=11, 34=AA, Tom=23}
    }

    @Test
    public void test2() {
        Map map = new Hashtable();

        // NullPointerException
//        map.put(null, null);
//        map.put(null, 123);
//        map.put(123, null);
        /*
         * Hashtable 键(key)、值(value)都不能为 null，只要有一个是 null 就抛 NullPointerException。
         * 上面三行只要解开任意一行都会崩
         * Hashtable.put 内部会直接调用 value.hashCode()，value 为 null 就报空指针；
         * key 为 null 同理。而 HashMap 对 null 键做了特殊处理（hash 记为 0），所以 HashMap 允许 null。
         */
    }

    @Test
    public void test3() {
        LinkedHashMap map = new LinkedHashMap();
        /*
         *  - put 的第一个参数是 key，第二个参数是 value。
         *  - 这里 key/value 类型五花八门（String、Integer 混着放），是因为用的是 raw type，
         *    key、value 都被当成 Object，所以“随便写”都能编译通过。
         *  - 正式项目应加泛型限定类型，例如 LinkedHashMap<String, Integer>，就不能乱放了。
         */
        map.put("Tom", 23);
        map.put(34, "AA");
        map.put("CC", "11");

        /*
         * LinkedHashMap 底层 = HashMap 的“数组+链表/红黑树” + 一条额外的“双向链表”。
         * 这条双向链表专门记录元素的插入先后顺序，所以遍历时能按 put 的顺序输出，
         */
        System.out.println(map); // {Tom=23, 34=AA, CC=11}
    }

    @Test
    public void test4() {
        HashMap map = new HashMap();

        /**
         * 添加
         * Object put(Object key,Object value)：将指定key-value添加到(或修改)当前map对象中
         */
        map.put("AA", 56);
        map.put(67, "Tom");
        map.put("BB", 78);
        map.put(new Person5("Jerry", 12), 56);

        System.out.println(map); // {AA=56, BB=78, 67=Tom, Person5{name='Jerry', age=12}=56}

        /**
         * int size()：返回map中key-value对的个数
         */
        System.out.println(map.size()); // 4

        /**
         * Object remove(Object key)：移除指定key的key-value对，并返回value
         */
        /*
         * remove 本身会把这个 key-value 从 map 中删掉，删除动作和“要不要接收返回值”无关。
         * 返回值是“被删掉的那个 value”，这是方法额外提供的信息，方便知道删了什么。
         */
        Object value = map.remove("AA");
        System.out.println(value); // 56
        System.out.println(map); // {BB=78, 67=Tom, Person5{name='Jerry', age=12}=56}

        /**
         * 修改
         * Object put(Object key,Object value)：将指定key-value添加到(或修改)当前map对象中
         */
        // put 的返回值约定是：如果 key 已存在，返回“被覆盖掉的旧 value”；如果是新增，返回 null。
        Object oldValue = map.put("BB", 99);
        System.out.println(oldValue); // 78
        System.out.println(map); // {BB=99, 67=Tom, Person5{name='Jerry', age=12}=56}

        /**
         * Object get(Object key)：获取指定key对应的value
         */
        /*
         * 自动装箱：put(67, "Tom") 里的 67 是 int 基本类型，但 Map 的 key 只能是对象(Object)，
         * 所以编译器自动把 int 67 装箱成 Integer.valueOf(67)。get(67) 同理，67 也被装箱成 Integer 再去比较。
         */
        Object value2 = map.get(67);
        System.out.println(value2); // Tom
    }

    @Test
    public void test5() {
        HashMap map = new HashMap();

        map.put("AA", 56);
        map.put(67, "Tom");
        map.put("BB", 78);
        map.put(new Person5("Jerry", 12), 56);

        System.out.println(map); // {AA=56, BB=78, 67=Tom, Person5{name='Jerry', age=12}=56}

        /**
         * Set keySet()：返回所有key构成的Set集合
         */
        Set keySet = map.keySet();

        Iterator iterator = keySet.iterator();
//        while (iterator.hasNext()) {
        /*
         * map.get(iterator.next()) —— next() 取到的是 key，再用 get(key) 换成 value，打印的是 value。
         * iterator.next()          —— 直接打印取到的 key 本身。
         */
//            System.out.println(map.get(iterator.next()));
            /*
            56
            78
            Tom
            56
             */

//            System.out.println(iterator.next());
            /*
            AA
            BB
            67
            Person5{name='Jerry', age=12}
             */
//        }

        /**
         * Collection values()：返回所有value构成的Collection集合
         */
        /*
         * 方式1（推荐）：只想要“所有 value”时，直接用 values() 最直观、语义最清晰。
         * values() 返回的是 Collection（不是 Set），因为 value 允许重复。
         */
//        Collection values = map.values();

//        for (Object value : values) {
//            System.out.println(value);
            /*
            56
            78
            Tom
            56
             */
//        }

        /*
         * 方式2：先拿 key 集合，再对每个 key 调用 get(key) 换出 value。
         * 能得到相同结果，但多了一步“按 key 查 value”，效率略低，不如直接用 values()。
         */
//        Set keySet1 = map.keySet();
//        for (Object key : keySet1) {
//            System.out.println(map.get(key));
            /*
            56
            78
            Tom
            56
             */
//        }

        /**
         * Set entrySet()：返回所有key-value对构成的Set集合
         */
        // 方式1：遍历entry集：Set entrySet()
        Set entrySet = map.entrySet();

        Iterator iterator2 = entrySet.iterator();
        while (iterator2.hasNext()) {
            // 方法1
            /*
             * 把迭代器取出的一个“键值对”还原成 Map.Entry，再分别取 key 和 value。
             * (1) 为什么写 Map.Entry：Entry 是接口 Map 内部的“静态嵌套接口”，
             *     它不是顶层类型，必须通过外部类名限定，写成 Map.Entry 才能引用到它。
             * (2) 为什么要强转：因为没用泛型，entrySet 是 Set（元素被当作 Object），
             *     iterator2.next() 返回 Object，要调用 getKey()/getValue() 就必须先强转成 Map.Entry。
             *     若写成泛型 Map<K,V>，entrySet() 返回 Set<Map.Entry<K,V>>，就不用强转了。
             */
//            Map.Entry entry = (Map.Entry) iterator2.next();
//            System.out.println(entry.getKey() + "--->" + entry.getValue());
            /*
            AA--->56
            BB--->78
            67--->Tom
            Person5{name='Jerry', age=12}--->56
             */

            // 方法2
            /*
             * 直接打印 entry 对象，走的是 Entry 的 toString()，格式固定是 “key=value”。
             * 方法1 是自己分别取出 key、value 再拼字符串，格式可以自定义（这里用了 --->）。
             * 只想快速看内容用方法2；要单独使用 key/value 或自定义输出用方法1。
             */
            System.out.println(iterator2.next());
            /*
            AA=56
            BB=78
            67=Tom
            Person5{name='Jerry', age=12}=56
             */
        }

        // 方式2：遍历entry集：keySet() 、get(key)
        Set keySet1 = map.keySet();

        for (Object key : keySet1) {
            System.out.println(key + "--->" + map.get(key));
            /*
            AA--->56
            BB--->78
            67--->Tom
            Person5{name='Jerry', age=12}--->56
             */
        }
    }
}

class TreeMapTest {
    /**
     * 自然排序
     */
    @Test
    public void test1() {
        TreeMap map = new TreeMap();

        map.put("CC", 89);
        map.put("BB", 78);
        map.put("JJ", 82);
        map.put("WW", 78);

        /*
         * TreeMap 要按 key 大小排序存储，就必须“比较 key”。
         * 前面的 key 都是 String，String 的 compareTo 只能和 String 比。
         * 现在放入 Integer(67)，TreeMap 试图把 Integer 当成 String 去比较，
         * 无法转换，于是抛 ClassCastException。
         * 向 TreeMap 添加的所有 key 必须是同一种、且可互相比较的类型。
         */
//        map.put(67, 78); // ClassCastException

        Set entrySet = map.entrySet();

        for (Object entry : entrySet) {
            System.out.println(entry);
            /*
            BB=78
            CC=89
            JJ=82
            WW=78
             */
        }
    }

    @Test
    public void test2() {
        TreeMap map = new TreeMap();

        User2 u1 = new User2("Tom", 23);
        User2 u2 = new User2("Jerry", 43);
        User2 u3 = new User2("Rose", 13);
        User2 u4 = new User2("Jack", 23);
        User2 u5 = new User2("Tony", 33);

        /*
         * put(key, value) 需要两个参数：u1 是 key（User2 对象），后面的 78 是 value。
         * value 不能省略：put 要求必须传两个参数，只写 put(u1) 会编译报错。
         * 如果确实不需要 value，可以放个占位值（如 null 或 0），或改用 Set 而不是 Map。
         */
        map.put(u1, 78);
        map.put(u2, 76);
        map.put(u3, 88);
        map.put(u4, 45);
        map.put(u5, 99);

        /*
         * TreeMap 的排序/查找靠的是 compareTo（自然排序）或 Comparator（定制排序），
         * 它“不使用” equals()/hashCode()。所以 User2 里注释掉的 equals/hashCode
         * 对 TreeMap 没有影响，解开也不会改变 TreeMap 的行为。
         * （equals/hashCode 主要影响的是 HashMap/HashSet 的去重与查找。）
         */
        Set entrySet = map.entrySet();
        for (Object entry : entrySet) {
            System.out.println(entry);
        }
        /*
        User2{name='Rose', age=13}=88
        User2{name='Tom', age=23}=78
        User2{name='Jack', age=23}=45
        User2{name='Tony', age=33}=99
        User2{name='Jerry', age=43}=76
         */

        /*
         * containsKey 在 TreeMap 里是靠 compareTo 找的：只有当某个已存在 key 与参数
         * compareTo 结果为 0 时才算“包含”，返回 true；否则 false。
         * User2 的 compareTo 是“先比 age，再比 name”，Maria/33 和已有的任何 key 都不完全相等，
         * 所以返回 false。若构造一个 age 和 name 都与某个已存在 key 相同的对象，则返回 true。
         */
        System.out.println(map.containsKey(new User2("Maria", 33))); // false
    }

    /**
     * 定制排序
     */
    @Test
    public void test3() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof User2 && o2 instanceof User2) {
                    User2 u1 = (User2) o1;
                    User2 u2 = (User2) o2;

                    int value = u1.getName().compareTo(u2.getName());
                    if (value != 0) {
                        return value;
                    }
                    return u1.getAge() - u2.getAge();
                }

                /*
                 *   return 0   ：告诉 TreeMap“这两个对象相等”，会导致本该不同的元素被当作重复而丢失，
                 *                掩盖了类型错误，属于埋雷，不推荐。
                 *   throw 异常 ：直接暴露“类型不匹配”的错误，符合“早失败”原则，便于定位问题。
                 */
//                return 0;
                throw new RuntimeException("类型不匹配");
            }
        };

        TreeMap map = new TreeMap(comparator);

        User2 u1 = new User2("Tom", 23);
        User2 u2 = new User2("Jerry", 43);
        User2 u3 = new User2("Rose", 13);
        User2 u4 = new User2("Jack", 23);
        User2 u5 = new User2("Tony", 33);

        map.put(u1, 78);
        map.put(u2, 76);
        map.put(u3, 88);
        map.put(u4, 45);
        map.put(u5, 99);

        Set entrySet = map.entrySet();
        for (Object entry : entrySet) {
            System.out.println(entry);
            /*
            User2{name='Jack', age=23}=45
            User2{name='Jerry', age=43}=76
            User2{name='Rose', age=13}=88
            User2{name='Tom', age=23}=78
            User2{name='Tony', age=33}=99
             */
        }
    }
}

class PropertiesTest {
    @Test
    public void test1() throws IOException {
        // 方式1：数据和代码耦合度高；如果修改的话，需要重写的编译代码、打包发布，繁琐
        // 数据
//        String name = "Tom";
//        String password = "abc123";

        // 代码：用于操作name、password代码
        // ...

        // 方式2：将数据封装到具体的配置文件中，在程序中读取配置文件中的信息。实现了
        // 数据和代码的解耦；由于我们没有修改代码，就省去了重新编译和打包的过程。
        /*
         * (1) new File("info.properties") 只是创建了一个“文件路径对象”，并不会真的去创建磁盘文件
         * (2) 因为这里只“读取”(FileInputStream + load)，所以文件必须提前手动建好，否则会抛
         *     FileNotFoundException。
         * (3) 只要文件名、相对位置和这里写的一致，就会被当成同一个文件。
         */
        File file = new File("info.properties");
        System.out.println(file.getAbsoluteFile()); // javacode/info.properties

        // 创建文件输入流，用来读取指定文件
        FileInputStream fileInputStream = new FileInputStream(file);

        // 创建 Properties 对象，用来存储和读取“键=值”形式的配置数据
        /*
         * new Properties() 创建一个 Properties 容器对象（它是 Hashtable 的子类）。
         * 它负责“承接”从流里加载进来的键值对，之后用 getProperty(key) 取值。
         * 没有这个对象，就没地方存放读到的配置数据。
         */
        Properties properties = new Properties();
        // 将文件中的配置数据加载到 properties 中
        properties.load(fileInputStream);

        // 读取数据
        String name = properties.getProperty("name");
        String password = properties.getProperty("password");

        System.out.println(name + ":" + password);

        /*
         * 推荐写法（示意）：
         *     try (FileInputStream in = new FileInputStream(file)) {
         *         properties.load(in);
         *     }
         * try-with-resources 会自动关闭流，更安全简洁。
         */
        fileInputStream.close();
    }
}

class Person5 {
    String name;
    int age;

    public Person5() {
    }

    public Person5(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person5{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Person5 equals()...");
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person5 person5 = (Person5) o;
        return age == person5.age && Objects.equals(name, person5.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

class User2 implements Comparable {
    private String name;
    private int age;

    public User2() {
    }

    public User2(String name, int age) {
        this.name = name;
        this.age = age;
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

    @Override
    public String toString() {
        return "User2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        System.out.println("User2 equals()....");
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        User2 user2 = (User2) o;
//        return age == user2.age && Objects.equals(name, user2.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, age);
//    }

    // 比如：按照年龄从小到大排序
//    @Override
//    public int compareTo(Object o) {
//        if (this == o) {
//            return 0;
//        }
//
//        if (o instanceof User2) {
//            User2 u = (User2) o;
//            return this.age - u.age;
//        }
//
//        throw new RuntimeException("类型不匹配");
//    }

    // 比如：先比较年龄从小到大排列，如果年龄相同，则继续比较姓名，从大到小
    /*
     * 这就是“自然排序”：让 User2 实现 Comparable 并重写 compareTo，
     * TreeMap 就会按这个规则排列 key。
     * 返回值含义：负数表示 this 排在前，正数表示 this 排在后，0 表示相等（会被视为同一个 key）。
     * 这里逻辑：先按 age 升序；age 相同再按 name 降序（-this.name.compareTo(u.name) 取反实现降序）。
     */
    @Override
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        }

        if (o instanceof User2) {
            User2 u = (User2) o;
            int value = this.age - u.age;
            if (value != 0) {
                return value;
            }
            return -this.name.compareTo(u.name);
        }

        throw new RuntimeException("类型不匹配");
    }
}

/**
 * 添加你喜欢的歌手以及你喜欢他唱过的歌曲,并遍历
 */
/*
 * 解题思路（关键是分清“一对多”的结构）：
 *   一个歌手 -> 多首歌，这是典型的“键(1个) 对 值(多个)”，所以用 Map。
 *   key   = 歌手名(String)；
 *   value = 该歌手的多首歌，多首歌是一组有序可重复的数据，用 List(ArrayList)。
 *   于是整体结构就是 Map<String, List<String>>（这里没用泛型而已）。
 * 什么时候用什么（简单判断）：
 *   - 只是一堆元素、要有序、可重复、按下标访问   -> List(ArrayList)
 *   - 要去重、不关心顺序                       -> Set(HashSet)
 *   - 成对的映射关系“谁对应谁”                 -> Map(HashMap)
 *   - 映射里“一个键对多个值”                   -> Map 的 value 再套 List/Set
 */
class SingerTest {
    public static void main(String[] args) {
        HashMap singers = new HashMap();

        // 添加1个歌手和其歌曲
        String singer1 = "周杰伦";

        ArrayList songs1 = new ArrayList();
        songs1.add("夜曲");
        songs1.add("晴天");
        songs1.add("七里香");
        songs1.add("发如雪");
        songs1.add("屋顶");
        songs1.add("青花瓷");

        /*
         * singers 是那个总的 Map 容器（存所有歌手）；singer1 只是一个歌手名(key)。
         * put 是往 Map 里放数据，要用容器 singers 来调用：
         * singers.put(singer1, songs1) 表示“把 周杰伦 -> 他的歌单 这一对放进 Map”。
         */
        singers.put(singer1, songs1);

        // 再添加1个歌手和其歌曲
        String singer2 = "林俊杰";

        ArrayList songs2 = new ArrayList();
        songs2.add("江南");
        songs2.add("曹操");
        songs2.add("小酒窝");
        songs2.add("可惜没如果");

        singers.put(singer2, songs2);

        /*
         * 这里是在“遍历 Map”：
         *   entrySet() 拿到所有“歌手->歌单”键值对组成的 Set；
         *   iterator() 得到迭代器，while(hasNext) 逐个往后取；
         *   next() 取出的每一项就是一个 Map.Entry（一对 key-value）。
         */
        Set entrySet = singers.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            /*
             * iterator.next() 返回一个 Object（因为没用泛型），实际上是一个键值对；
             * (Map.Entry) 把它强转成 Map.Entry 接口类型，这样才能调用 getKey()/getValue()；
             * entry.getKey()   -> 歌手名(周杰伦/林俊杰)；
             * entry.getValue() -> 该歌手的歌单 List。
             */
            Map.Entry entry = (Map.Entry) iterator.next();

            System.out.println("歌手：" + entry.getKey());
            System.out.println("歌曲有：" + entry.getValue());
            /*
            歌手：林俊杰
            歌曲有：[江南, 曹操, 小酒窝, 可惜没如果]
            歌手：周杰伦
            歌曲有：[夜曲, 晴天, 七里香, 发如雪, 屋顶, 青花瓷]
             */
        }
    }
}

/**
 * 案例：二级联动
 * 将省份和城市的名称保存在集合中，当用户选择省份以后，二级联动，显示对应省份的地级市供用户选择。
 * 提示：如果输入的省份不正确，需要重新输入。 如果输入的城市不正确，需要重新输入。
 * <p>
 * “二级联动”指两个存在依赖关系的选择项：第一级(省份)选定后，第二级(城市)的可选内容
 * 会随之动态变化，只显示该省份下的城市。常见于网页上省/市/区的下拉框联动。
 */
class CityMap {
    public static Map model = new HashMap();

    static {
        model.put("北京", new String[]{"北京"});
        model.put("辽宁", new String[]{"沈阳", "盘锦", "铁岭", "丹东", "大连", "锦州", "营口"});
        model.put("吉林", new String[]{"长春", "延边", "吉林", "白山", "白城", "四平", "松原"});
        model.put("河北", new String[]{"承德", "沧州", "邯郸", "邢台", "唐山", "保定", "石家庄"});
        model.put("河南", new String[]{"郑州", "许昌", "开封", "洛阳", "商丘", "南阳", "新乡"});
        model.put("山东", new String[]{"济南", "青岛", "日照", "临沂", "泰安", "聊城", "德州"});
    }
}

class CitymapTest {
    public static void main(String[] args) {
        // 1. 获取Map，并遍历map中的所有的key
        Map map = CityMap.model;

        Set provinces = map.keySet();

        for (Object province : provinces) {
            System.out.print(province + "\t\t");
        }

        // 2. 根据提示，从键盘获取省份值，判断此省份是否存在
        //    如果存在遍历其value中的各个城市。如果不存在，提示用户重新输入
        Scanner scanner = new Scanner(System.in);

        String[] cities;

        while (true) {
            System.out.println("\n请选择你所在的省份：");
            String province = scanner.next();

            // 获取省份对应的各个城市构成的String[]
            cities = (String[]) map.get(province);

            /*
             * cities == null（省份不存在，get 返回 null）
             *  或 cities.length == 0（存在但没有城市），任一成立就提示重输。
             * “短路”特性：|| 左边为 true 就不再判断右边——这里很关键：
             *   先判 cities == null 放在前面，能避免对 null 调用 .length 造成空指针。
             */
            if (cities == null || cities.length == 0) {
                System.out.println("你输入的省份有误，请重新输入");
            } else {
                // 意味着用户输入的省份是存在的，则跳出当前循环
                break;
            }
        }

        for (int i = 0; i < cities.length; i++) {
            System.out.println(cities[i] + "\t\t");
        }

        // 3. 根据提示，从键盘获取城市，遍历各个城市构成的String[],判断输入的城市是否存在于此数组中
        //    如果存在，信息登记完毕。如果不存在，提示用户重新输入。
        l:
        while (true) {
            System.out.println("请选择你所在的城市：");
            String city = scanner.next();

            // 方式1：
            for (int i = 0; i < cities.length; i++) {
                if (city.equals(cities[i])) {
                    System.out.println("信息登记完毕");
                    /*
                     * l: 是“标签(label)”，给外层 while 起个名字。
                     * break l; 表示“跳出被标记为 l 的那层循环”，而不是只跳出当前 for。
                     * 这里 for 在 while 里面，普通 break 只能跳出 for；要一次跳出外层 while，就用 break 标签。
                     * 使用场景：需要从“多层嵌套循环”里一次性跳出（break 标签）或跳到外层继续
                     *          （continue 标签）时才用。能不用尽量不用，容易降低可读性。
                     */
                    break l;
                }
            }

            System.out.println("输入的城市有误，请重新输入");

            // 方式2（推荐）：把“查找”抽成方法 containsCity(...)，逻辑清晰、可复用，普通 break 即可跳出 while
            if (containsCity(cities, city)) {
                System.out.println("信息登记完毕");
                break;
            }

            System.out.println("输入的城市有误，请重新输入");
        }

        scanner.close();
    }

    // 第2种方式处理城市是否存在
    public static boolean containsCity(String[] cities, String city) {
        for (int i = 0; i < cities.length; i++) {
            if (city.equals(cities[i])) {
                return true;
            }
        }

        return false;
    }
}
