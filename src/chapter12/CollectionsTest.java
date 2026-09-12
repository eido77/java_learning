package chapter12;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionsTest {
    /*
    1. Collections概述
    Collections 是一个操作 Set、List 和 Map 等集合的工具类。
    2. 常用方法
    排序操作：
    - reverse(List)：反转 List 中元素的顺序
    - shuffle(List)：对 List 集合元素进行随机排序
    - sort(List)：根据元素的自然顺序对指定 List 集合元素按升序排序
    - sort(List，Comparator)：根据指定的 Comparator 产生的顺序对 List 集合元素进行排序
    - swap(List，int， int)：将指定 list 集合中的 i 处元素和 j 处元素进行交换
    查找
    - Object max(Collection)：根据元素的自然顺序，返回给定集合中的最大元素
    - Object max(Collection，Comparator)：根据 Comparator 指定的顺序，返回给定集合中的最大元素
    - Object min(Collection)：根据元素的自然顺序，返回给定集合中的最小元素
    - Object min(Collection，Comparator)：根据 Comparator 指定的顺序，返回给定集合中的最小元素
    - int binarySearch(List list,T key)在List集合中查找某个元素的下标，但是List的元素必须是T或T的子类对象，而且必须是可比较大小的，即支持自然排序的。而且集合也事先必须是有序的，否则结果不确定。
    - int binarySearch(List list,T key,Comparator c)在List集合中查找某个元素的下标，但是List的元素必须是T或T的子类对象，而且集合也事先必须是按照c比较器规则进行排序过的，否则结果不确定。
    - int frequency(Collection c，Object o)：返回指定集合中指定元素的出现次数
    复制、替换
    - void copy(List dest,List src)：将src中的内容复制到dest中
    - boolean replaceAll(List list，Object oldVal，Object newVal)：使用新值替换 List 对象的所有旧值
    - 提供了多个unmodifiableXxx()方法，该方法返回指定 Xxx的不可修改的视图。
    添加
    - boolean addAll(Collection  c,T... elements)将所有指定元素添加到指定 collection 中。
    同步
    - Collections 类中提供了多个 synchronizedXxx() 方法，该方法可使将指定集合包装成线程同步的集合，从而可以解决多线程并发访问集合时的线程安全问题
    3. 面试题：区分Collection 和 Collections
    Collection：集合框架中的用于存储一个一个元素的接口，又分为List和Set等子接口。
    Collections：用于操作集合框架的一个工具类。此时的集合框架包括：Set、List、Map
     */
    @Test
    public void test1() {
        /*
         * asList 是 java.util.Arrays 的静态方法。
         * 作用：把“数组 / 一串可变参数”包装成一个 List 返回。
         * 关键机制（易错点）：
         *   1) 它返回的是一个“固定长度”的 List（底层就是传入的那个数组）。
         *   2) 不能 add / remove（会抛 UnsupportedOperationException），
         *      但可以 set（改元素）、可以 sort、reverse（因为这些只改元素不改长度）。
         *   3) 所以下面的 Collections.reverse / sort 能正常工作，
         *      因为它们只是“原地交换元素”，没有增删长度。
         */
        List list = Arrays.asList(45, 43, 65, 6, 43, 2, 32, 45, 56, 34, 23);
        System.out.println(list); // [45, 43, 65, 6, 43, 2, 32, 45, 56, 34, 23]

        /**
         * reverse(List)：反转 List 中元素的顺序
         */
        Collections.reverse(list);
        System.out.println(list); // [23, 34, 56, 45, 32, 2, 43, 6, 65, 43, 45]

        /**
         * shuffle(List)：对 List 集合元素进行随机排序
         */
//        Collections.shuffle(list);
//        System.out.println(list);

        /**
         * sort(List)：根据元素的自然顺序对指定 List 集合元素按升序排序
         */
        Collections.sort(list);
        System.out.println(list); // [2, 6, 23, 32, 34, 43, 43, 45, 45, 56, 65]

        /**
         * sort(List，Comparator)：根据指定的 Comparator 产生的顺序对 List 集合元素进行排序
         */
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Integer && o2 instanceof Integer) {
                    Integer i1 = (Integer) o1;
                    Integer i2 = (Integer) o2;

                    return -(i1.intValue() - i2.intValue());
                }

                throw new RuntimeException("类型不匹配");
            }
        });

        System.out.println(list); // [65, 56, 45, 45, 43, 43, 34, 32, 23, 6, 2]
    }

    @Test
    public void test2() {
        List list = Arrays.asList(45, 43, 65, 6, 43, 2, 32, 45, 56, 34, 23);
        System.out.println(list); // [45, 43, 65, 6, 43, 2, 32, 45, 56, 34, 23]

        /**
         * Object max(Collection)：根据元素的自然顺序，返回给定集合中的最大元素
         */
        Comparable max = Collections.max(list);
        System.out.println(max); // 65

        /**
         * Object max(Collection，Comparator)：根据 Comparator 指定的顺序，返回给定集合中的最大元素
         */
        Object max1 = Collections.max(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Integer && o2 instanceof Integer) {
                    Integer i1 = (Integer) o1;
                    Integer i2 = (Integer) o2;

                    return -(i1.intValue() - i2.intValue());
                }

                throw new RuntimeException("类型不匹配");
            }
        });

        System.out.println(max); // 65
        System.out.println(max1); // 2

        /**
         * int frequency(Collection c，Object o)：返回指定集合中指定元素的出现次数
         */
        int count = Collections.frequency(list, 45);
        System.out.println(count); // 2
    }

    @Test
    public void test3() {
        List src = Arrays.asList(45, 43, 65, 6, 43, 2, 32, 45, 56, 34, 23);
        System.out.println(src); // [45, 43, 65, 6, 43, 2, 32, 45, 56, 34, 23]

        /**
         * void copy(List dest,List src)：将src中的内容复制到dest中
         */
        /*
         * 容量：底层数组预留的空间大小（能装多少而不扩容），是性能优化用的。
         * 长度：实际已经放进去的元素个数，size() 返回的是它。
         * new ArrayList(20) 只是预留了空间，里面一个元素都没放，所以 size() 仍是 0。
         */
        // 错误写法
//        ArrayList dest = new ArrayList(20);
//        ArrayList dest = new ArrayList();
//        System.out.println(dest.size()); // 0

        // 正确写法
        /*
         * 它的圆括号里接收的是“可变参数 T...”，本质是一个数组。
         * 这里传入的是 new Object[src.size()]，即一个长度为 11、元素全是 null 的 Object 数组。
         * 于是得到一个 size() == 11（有 11 个 null 占位）的 List。
         *
         * Collections.copy 要求 dest.size() >= src.size()，
         * 必须先让 dest 真正“有 11 个位置”，才能把 src 的 11 个元素逐个覆盖进去。
         */
        List dest = Arrays.asList(new Object[src.size()]);

        Collections.copy(dest, src);
        /*
         * IndexOutOfBoundsException: Source does not fit in dest
         * 这个异常是“凡是 size() 为 0 的 dest”都会触发，
         * (new ArrayList())和(new ArrayList(20))两种都会报同样的错。
         */
        System.out.println(dest); // [45, 43, 65, 6, 43, 2, 32, 45, 56, 34, 23]
    }

    @Test
    public void test4() {
        /**
         * 提供了多个unmodifiableXxx()方法，该方法返回指定 Xxx的不可修改的视图。
         */
        List list1 = new ArrayList();
        // list1可以写入数据
        list1.add(34);
        list1.add(12);
        list1.add(45);
        System.out.println(list1); // [34, 12, 45]

        List list2 = Collections.unmodifiableList(list1);
        /*
         * unmodifiableList 返回的是一个“只读视图”包装对象（内部类 UnmodifiableList）。
         *   它把 add / remove / set 等所有“修改类”方法都重写成直接抛
         *   UnsupportedOperationException，而 get / size 等“读取类”方法则转发给原 list1。
         *   所以你能读(get)、不能写(add)。
         */
//        list2.add("AA");

        System.out.println(list2.get(0)); // 34
    }

    @Test
    public void test5() {
        /**
         * Collections 类中提供了多个 synchronizedXxx() 方法，该方法可使将指定集合包装成线程同步的集合，从而可以解决多线程并发访问集合时的线程安全问题
         */
        /*
         * list2/map2 只是对 list1/map1 的“同步包装(视图)”，底层是同一份数据。
         * 改 list1 -> list2 能看到；改 list2 -> list1 也能看到。Map 同理。
         */
        List list1 = new ArrayList();

        // 返回的list2就是线程安全的
        List list2 = Collections.synchronizedList(list1);

        HashMap map1 = new HashMap();

        // 返回的map2就是线程安全的
        /*
         * - Collections.synchronizedMap 的返回类型是 Map（接口），不是 HashMap。
         *   它实际返回的是内部类 SynchronizedMap，并不是 HashMap 的子类。
         * - 所以注释掉那行 (HashMap) 的强转是“危险写法”：编译能过，但一旦运行会抛
         *   ClassCastException（SynchronizedMap 无法转成 HashMap）。
         *   它现在没报错只是因为被注释掉、没执行。
         */
//        HashMap map2 = (HashMap) Collections.synchronizedMap(map1);
        Map map2 = Collections.synchronizedMap(map1);
    }
}

/**
 * 模拟斗地主洗牌和发牌，牌没有排序
 * 提示：不要忘了大王、小王
 */
/*
 * 整体思路：斗地主可拆成 4 步——“造牌 -> 洗牌 -> 发牌 -> 展示”。
 *   1) 造牌：花色(4种) × 点数(13种) = 52 张，再手动加大小王 = 54 张，用双重循环拼出所有组合。
 *   2) 洗牌：用 Collections.shuffle 打乱顺序。
 *   3) 发牌：3 名玩家 + 3 张底牌。用下标 i 对 3 取模来轮流发给 3 个人，最后 3 张留作底牌。
 *   4) 展示：分别打印 4 个 List。
 * 牌是“有序、可重复、要按下标轮流发”的一串元素，List 最合适；
 * 要按下标/顺序处理 -> List；要去重 -> Set；要键值映射 -> Map。
 */
class PokerTest {
    public static void main(String[] args) {
        // 1. 组成一副扑克牌
        String[] num = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] color = {"方片♦", "梅花♣", "红桃♥", "黑桃♠"};
        ArrayList poker = new ArrayList();

        for (int i = 0; i < color.length; i++) {
            for (int j = 0; j < num.length; j++) {
                poker.add(color[i] + " " + num[j]);
            }
        }

        // 添加大小王
        poker.add("小王");
        poker.add("大王");

        // 2. 洗牌
        Collections.shuffle(poker);

        // 3. 发牌
        // 3.1 创建3个角色和1个底牌对应的4个ArrayList
        ArrayList tom = new ArrayList();
        ArrayList jerry = new ArrayList();
        ArrayList me = new ArrayList();
        ArrayList lastCards = new ArrayList();

        for (int i = 0; i < poker.size(); i++) {
            if (i >= poker.size() - 3) {
                lastCards.add(poker.get(i));
            } else if (i % 3 == 0) {
                tom.add(poker.get(i));
            } else if (i % 3 == 1) {
                jerry.add(poker.get(i));
            } else if (i % 3 == 2) {
                me.add(poker.get(i));
            }
        }

        // 3.2 遍历显示4个ArrayList
        System.out.println("Tom：");
        System.out.println(tom);
        System.out.println("Jerry：");
        System.out.println(jerry);
        System.out.println("Me：");
        System.out.println(me);
        System.out.println("lastCards：");
        System.out.println(lastCards);
    }
}
