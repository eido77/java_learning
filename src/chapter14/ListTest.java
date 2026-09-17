package chapter14;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class ListTest {
    /*
    一、ArrayList
    1. ArrayList的特点：
    > 实现了List接口，存储有序的、可以重复的数据
    > 底层使用Object[]数组存储
    > 线程不安全的
    2. ArrayList源码解析：
    2.1 jdk7版本：(以jdk1.7.0_07为例)
    // 底层会初始化数组，数组的长度为10。Object[] elementData = new Object[10];
    ArrayList<String> list = new ArrayList<>();

    list.add("AA"); // elementData[0] = "AA";
    list.add("BB"); // elementData[1] = "BB";
    ...
    当要添加第11个元素的时候，底层的elementData数组已满，则需要扩容。默认扩容为原来长度的1.5倍。
    并将原有数组中的元素复制到新的数组中。
    2.2 jdk8版本:(以jdk1.8.0_271为例)
    // 底层会初始化数组，即：Object[] elementData = new Object[]{};
    ArrayList<String> list = new ArrayList<>();

    list.add("AA"); // 首次添加元素时，会初始化数组elementData = new Object[10];elementData[0] = "AA";
    list.add("BB"); // elementData[1] = "BB";
    ...
    当要添加第11个元素的时候，底层的elementData数组已满，则需要扩容。默认扩容为原来长度的1.5倍。
    并将原有数组中的元素复制到新的数组中。
    小结：
    jdk1.7.0_07版本中：ArrayList类似于饿汉式
    jdk1.8.0_271版本中：ArrayList类似于懒汉式
     * jdk8 里 new ArrayList() 时 elementData 指向的是一个"共享的空数组"，
     * 长度为 0，并不是立刻就有长度 10 的数组；只有第一次 add 时才真正扩到 10。
     * 这就是"懒汉式(用时才创建)"和 jdk7"饿汉式(一 new 就创建 10)"的区别。
    二、Vector
    1. Vector的特点：
    > 实现了List接口，存储有序的、可以重复的数据
    > 底层使用Object[]数组存储
    > 线程安全的
    2. Vector源码解析：(以jdk1.8.0_271为例)
     * Vector 现在基本被淘汰，实际开发几乎不用它。
     * 如果确实需要线程安全的 List，更推荐 Collections.synchronizedList 或 CopyOnWriteArrayList。
    // 底层初始化数组，长度为10.Object[] elementData = new Object[10];
    Vector v = new Vector();
    v.add("AA"); //elementData[0] = "AA";
    v.add("BB");//elementData[1] = "BB";
    ...
    当添加第11个元素时，需要扩容。默认扩容为原来的2倍。
    三、LinkedList
    1. LinkedList的特点：
    > 实现了List接口，存储有序的、可以重复的数据
    > 底层使用双向链表存储
    > 线程不安全的
    2. LinkedList在jdk8中的源码解析：
      * 理解 Node 的双向链表结构(item + next + prev)以及 first / last 两个指针，
      * 才能明白为什么"中间插入/删除快、随机访问慢"，也不需要扩容(每次 add 只是 new 一个 Node)。
    LinkedList<String> list = new LinkedList<>(); // 底层也没做啥
    list.add("AA"); // 将"AA"封装到一个Node对象1中，list对象的属性first、last都指向此Node对象1。
    list.add("BB"); // 将"BB"封装到一个Node对象2中，对象1和对象2构成一个双向链表，同时last指向此Node对象2
    ...
    因为LinkedList使用的是双向链表，不需要考虑扩容问题。
    LinkedList内部声明：
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
    }
    3. LinkedList是否存在扩容问题？No!
    四、启示与开发建议
    1. Vector基本不使用了。
     * 1) 随机访问：ArrayList 按下标 get 是 O(1)，LinkedList 要从头/尾遍历是 O(n)；
     * 2) 内存与缓存：数组内存连续、CPU 缓存命中率高；LinkedList 每个节点还要多存
     *    prev、next 两个引用，内存开销更大、访问更分散；
     * 3) 真实场景里"查询/遍历"远多于"在中间插入删除"，正好是 ArrayList 的强项；
     * 4) LinkedList 插入删除是 O(1) 有个前提——你已经"定位到"那个节点，而定位本身是 O(n)。
     * 所以除非有大量"头部/中间"的插入删除，否则默认选 ArrayList。
    2. ArrayList底层使用数组结构，查找和添加（尾部添加）操作效率高，时间复杂度为O(1)
                               删除和插入操作效率低，时间复杂度为O(n)
       LinkedList底层使用双向链表结构，删除和插入操作效率高，时间复杂度为O(1)
                                  查找(随机访问)效率低，为 O(n)；尾部添加因为有 last 指针，是 O(1)。
    3. 在选择了ArrayList的前提下，new ArrayList() : 底层创建长度为10的数组。
                              new ArrayList(int capacity):底层创建指定capacity长度的数组。
       如果开发中，大体确认数组的长度，则推荐使用ArrayList(int capacity)这个构造器，避免了底层的扩容、复制数组的操作。
     */
    @Test
    public void test1() {
        ArrayList<String> list = new ArrayList<>();

        list.add("AA"); // elementData[0] = "AA";
        list.add("BB");// elementData[1] = "BB";
    }

    @Test
    public void test2(){
        Vector v = new Vector(); // 底层初始化数组，长度为10.Object[] elementData = new Object[10];
        v.add("AA"); // elementData[0] = "AA";
        v.add("BB"); // elementData[1] = "BB";
    }

    @Test
    public void test3(){
        LinkedList<String> list = new LinkedList<>(); // 底层也没做啥
        list.add("AA"); // 将"AA"封装到一个Node对象1中，list对象的属性first、last都指向此Node对象1。
        list.add("BB"); // 将"BB"封装到一个Node对象2中，对象1和对象2构成一个双向链表，同时last指向此Node对象2
    }
}
