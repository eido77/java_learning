package chapter14;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class MapTest {
    /*
    一、HashMap
        1. HashMap中元素的特点
        > HashMap中的所有的key彼此之间是不可重复的、无序的。所有的key就构成一个Set集合。---> key所在的类要重写hashCode()和equals()
        > HashMap中的所有的value彼此之间是可重复的、无序的。所有的value就构成一个Collection集合。---> value所在的类要重写equals()
        > HashMap中的一个key-value,就构成了一个entry。
        > HashMap中的所有的entry彼此之间是不可重复的、无序的。所有的entry就构成了一个Set集合。
    2. HashMap源码解析
    2.1 jdk7中创建对象和添加数据过程(以JDK1.7.0_07为例说明)：
    // 创建对象的过程中，底层会初始化数组Entry[] table = new Entry[16];
    HashMap<String,Integer> map = new HashMap<>();
    ...
    map.put("AA",78); // "AA"和78封装到一个Entry对象中，考虑将此对象添加到table数组中。
    ...
    添加/修改的过程：
    将(key1,value1)添加到当前的map中：
    首先，需要调用key1所在类的hashCode()方法，计算key1对应的哈希值1，此哈希值1经过某种算法(hash())之后，得到哈希值2。
    哈希值2再经过某种算法(indexFor())之后，就确定了(key1,value1)在数组table中的索引位置i。
      1.1 如果此索引位置i的数组上没有元素，则(key1,value1)添加成功。 ----> 情况1
      1.2 如果此索引位置i的数组上有元素(key2,value2),则需要继续比较key1和key2的哈希值2 ---> 哈希冲突
          2.1 如果key1的哈希值2与key2的哈希值2不相同，则(key1,value1)添加成功。 ----> 情况2
          2.2 如果key1的哈希值2与key2的哈希值2相同，则需要继续比较key1和key2的equals()。要调用key1所在类的equals(),将key2作为参数传递进去。
              3.1 调用equals()，返回false: 则(key1,value1)添加成功。 ----> 情况3
              3.2 调用equals()，返回true: 则认为key1和key2是相同的。默认情况下，value1替换原有的value2。
     * (1)hash 不同 → 一定不是同一个 key，直接当新元素挂到链表上（情况2）。
     * (2)hash 相同但 equals 为 false → 只是哈希碰撞，key 其实不同，也当新元素加入（情况3）。
     * (3)hash 相同且 equals 为 true → 认定是同一个 key，于是用新 value 覆盖旧 value（3.2）。
     * 之所以要“先比 hash 再比 equals”，是因为 hash 是 int 比较，速度快，能先排除掉绝大多数不相等的情况，
     * 只有 hash 相同时才调用相对更慢的 equals()，这样效率最高。
    说明：情况1：将(key1,value1)存放到数组的索引i的位置
         情况2,情况3：(key1,value1)元素与现有的(key2,value2)构成单向链表结构，(key1,value1)指向(key2,value2)
    随着不断的添加元素，在满足如下的条件的情况下，会考虑扩容：
     * threshold 是“扩容临界值”，即字段 int threshold（= 数组长度 * 加载因子，默认 16*0.75=12），
     * 当有效键值对个数 size 达到它时就该扩容。table[i] 指的是数组第 i 个“桶”（bucket）当前存放的元素。
     * jdk7 的扩容条件是 (size >= threshold) && (null != table[i])：
     * 只有当“元素已达临界值”且“本次要插入的这个位置 table[i] 上已经有元素（即将发生碰撞、要挂链表）”时才扩容。
     * jdk8 已改为在 size 超过 threshold 时就扩容（判断逻辑放在 putVal 中），不再要求 table[i]!=null。
    (size >= threshold) && (null != table[i])
    当元素的个数达到临界值(-> 数组的长度 * 加载因子)时，就考虑扩容。默认的临界值 = 16 * 0.75 --> 12.
    默认扩容为原来的2倍。
    2.2 jdk8与jdk7的不同之处(以jdk1.8.0_271为例)：
    ① 在jdk8中，当我们创建了HashMap实例以后，底层并没有初始化table数组。当首次添加(key,value)时，进行判断，
    如果发现table尚未初始化，则对数组进行初始化。
    ② 在jdk8中，HashMap底层定义了Node内部类，替换jdk7中的Entry内部类。意味着，我们创建的数组是Node[]
    ③ 在jdk8中，如果当前的(key,value)经过一系列判断之后，可以添加到当前的数组角标i中。如果此时角标i位置上有
       元素。在jdk7中是将新的(key,value)指向已有的旧的元素（头插法），而在jdk8中是旧的元素指向新的
       (key,value)元素（尾插法）。 "七上八下"
    ④ jdk7:数组 + 单向链表
       jdk8:数组 + 单向链表 + 红黑树
       什么时候会使用单向链表变为红黑树：如果数组索引i位置上的元素的个数达到8，并且数组的长度达到64时，我们就将此索引i位置上
                                   的多个元素改为使用红黑树的结构进行存储。（为什么修改呢？红黑树进行put()/get()/remove()
                                   操作的时间复杂度为O(logn)，比单向链表的时间复杂度O(n)的好。性能更高。
       什么时候会使用红黑树变为单向链表：当使用红黑树的索引i位置上的元素的个数低于6的时候，就会将红黑树结构退化为单向链表。
    2.3 属性/字段：
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 默认的初始容量 16
    static final int MAXIMUM_CAPACITY = 1 << 30; // 最大容量  1 << 30
    static final float DEFAULT_LOAD_FACTOR = 0.75f; // 默认加载因子
    static final int TREEIFY_THRESHOLD = 8; // 默认树化阈值8，当链表的长度达到这个值后，要考虑树化
    static final int UNTREEIFY_THRESHOLD = 6; // 默认反树化阈值6，当树中结点的个数达到此阈值后，要考虑变为链表

    // 当单个的链表的结点个数达到8，并且table的长度达到64，才会树化。
    // 当单个的链表的结点个数达到8，但是table的长度未达到64，会先扩容
    static final int MIN_TREEIFY_CAPACITY = 64; // 最小树化容量64

    transient Node<K,V>[] table; // 数组
    transient int size;  // 记录有效映射关系的对数，也是Entry对象的个数
    int threshold; // 阈值，当size达到阈值时，考虑扩容
    final float loadFactor; // 加载因子，影响扩容的频率
    二、LinkedHashMap
    1. LinkedHashMap 与 HashMap 的关系:
    > LinkedHashMap 是 HashMap的子类。
    > LinkedHashMap在HashMap使用的数组+单向链表+红黑树的基础上，又增加了一对双向链表，记录添加的(key,value)的
    先后顺序。便于我们遍历所有的key-value。
    LinkedHashMap重写了HashMap的如下方法：
    Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
        LinkedHashMap.Entry<K,V> p = new LinkedHashMap.Entry<K,V>(hash, key, value, e);
        linkNodeLast(p);
        return p;
    }
     * 这里体现了“子类通过重写创建节点的方法，把新节点额外挂到双向链表尾部(linkNodeLast)”，
     * 所以 LinkedHashMap 遍历时能保持插入顺序，而 HashMap 不保证顺序。
    2. 底层结构：LinkedHashMap内部定义了一个Entry
    static class Entry<K,V> extends HashMap.Node<K,V> {
        Entry<K,V> before, after; //增加的一对双向链表
        Entry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }
     * before/after 用来串起“上一个/下一个插入的 Entry”，这就是那对双向链表
    三、HashSet和LinkedHashSet的源码分析
    > HashSet底层使用的是HashMap
    > LinkedHashSet底层使用的是LinkedHashMap
     */
    @Test
    public void test1() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("AA", 123);
    }

    @Test
    public void test2() {
        HashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("AA", 123);
    }
}

/**
 * 分析此map的内存结构
 */
class MapExer {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();
        // 首次添加时才创建table（jdk8 延迟初始化）。
        // 上面 new HashMap<>() 只设了默认参数，table 仍是 null；执行到下面第一个 put 才真正 new 出数组。
        map.put(31, "张三");
        map.put(31, "李四");
        map.put(47, "王五");
        map.put(45, "赵六");
        System.out.println(map); // {45=赵六, 31=李四, 47=王五}
    }
}

/**
 * 说明：
 * key-value存储到HashMap中会存储key的hash值，这样就不用在每次查找时重新计算每一个Entry或
 * Node（TreeNode）的hash值了，因此如果已经put到Map中的key-value，再修改key的属性，而这个属性
 * 又参与hashcode值的计算，那么会导致匹配不上。
 * 这个规则也同样适用于LinkedHashMap、HashSet、LinkedHashSet、Hashtable等所有散列存储结构的集合。
 * 所以实际开发中，经常选用String，Integer等作为key，因为它们都是不可变对象。
 */
class HashSetDemo {
    public static void main(String[] args) {
        HashSet set = new HashSet();
        Person1 p1 = new Person1(1001, "AA");
        Person1 p2 = new Person1(1002, "BB");

        set.add(p1);
        set.add(p2);
        System.out.println(set);
        // 输出：{Person1{1001,AA}, Person1{1002,BB}}（具体顺序看 hash，无序）。p1 按 (1001,"AA") 的 hash 存进某个桶。

        p1.name = "CC";
        set.remove(p1);
        System.out.println(set);
        /*
         * 关键坑点：p1.name 改成 "CC" 后，p1 的 hashCode 也随之改变，但它仍物理存放在按 "AA" 算出的旧桶里。
         * remove(p1) 会用“新 hashCode(即按 CC 算)”去定位桶，跑到了另一个桶，找不到 p1，所以删除失败。
         * 因此输出仍是两个元素：{Person1{1001,CC}, Person1{1002,BB}}（p1 内容已变成 CC 但没被删掉）。
         * 这正是上面类注释所说“修改参与 hashCode 计算的属性会导致匹配不上”。
         */

        set.add(new Person1(1001, "CC"));
        System.out.println(set);
        /*
         * 新对象 (1001,"CC") 的 hashCode 与“当前 p1(已变成CC)”相同，会定位到 p1 应该在的“新桶”。
         * 但 p1 实际存在“旧桶(AA桶)”里，新桶其实是空的，没发生 equals 比较，于是添加成功。
         * 结果集合里出现了两个内容都是 {1001,CC} 的对象：{Person1{1001,CC}, Person1{1001,CC}, Person1{1002,BB}}。
         */

        set.add(new Person1(1001, "AA"));
        System.out.println(set);
        /*
         * 新对象 (1001,"AA") 的 hashCode 与 p1 当初存入时的旧桶(AA桶)一致，定位到该桶找到 p1。
         * 但 p1 现在 name 已是 "CC"，equals 比较 (1001,"AA") 与 (1001,"CC") 返回 false，判定不重复，添加成功。
         * 最终集合有 4 个元素。这个例子集中演示了“可变对象作 Set 元素”的各种诡异现象。
         */
    }
}

class Person1 {
    int id;
    String name;

    public Person1(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person1)) {
            return false;
        }

        Person1 person = (Person1) o;

        if (id != person.id) {
            return false;
        }
        return name != null ? name.equals(person.name) : person.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
