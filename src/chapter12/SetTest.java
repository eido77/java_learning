package chapter12;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class SetTest {
    /*
     * 红黑树是一种“自平衡的二叉查找树”。二叉查找树的规则是：左子节点 < 父节点 < 右子节点，
     * 这样查找一个元素时可以像“猜数字”一样每次排除一半，效率很高（O(log n)）。
     * 但普通二叉树在极端情况下（比如按顺序插入 1,2,3,4...）会退化成一条链表，效率降到 O(n)。
     * 红黑树通过给节点染“红/黑”两种颜色并遵守一套规则（如根黑、红节点的孩子必黑、任一路径黑节点数相同等），
     * 在每次增删后自动“旋转+变色”来保持大致平衡，从而始终保证 O(log n) 的查找效率。
     */
    /*
    1. Set及其实现类特点
    java.util.Collection:存储一个一个的数据
        |-----子接口：Set:存储无序的、不可重复的数据(高中学习的集合)
               |---- HashSet：主要实现类；底层使用的是HashMap，即使用数组+单向链表+红黑树结构进行存储。（jdk8中）
                    |---- LinkedHashSet：是HashSet的子类；在现有的数组+单向链表+红黑树结构的基础上，又添加了
                                         一组双向链表，用于记录添加元素的先后顺序。即：我们可以按照添加元素的顺序
                                         实现遍历。便于频繁的查询操作。
               |---- TreeSet：底层使用红黑树存储。可以按照添加的元素的指定的属性的大小顺序进行遍历。
    2. 开发中的使用频率及场景：
    > 较List、Map来说，Set使用的频率比较少。
    > 用来过滤重复数据
    3. Set中常用方法：即为Collection中声明的15个抽象方法。没有新增的方法。
    4. Set中无序性、不可重复性的理解（以HashSet及其子类为例说明）
    >无序性： != 随机性。
             添加元素的顺序和遍历元素的顺序不一致,是不是就是无序性呢？ No!
             到底什么是无序性？与添加的元素的位置有关，不像ArrayList一样是依次紧密排列的。
             这里是根据添加的元素的哈希值，计算的其在数组中的存储位置。此位置不是依次排列的，表现为无序性。
    >不可重复性：添加到Set中的元素是不能相同的。
              比较的标准，需要判断hashCode()得到的哈希值以及equals()得到的boolean型的结果。
              哈希值相同且equals()返回true，则认为元素是相同的。
    5. 添加到HashSet/LinkedHashSet中元素的要求:
      要求元素所在的类要重写两个方法：equals() 和 hashCode()。
      同时，要求equals() 和 hashCode()要保持一致性！我们只需要在IDEA中自动生成两个方法的重写即可，即能保证两个方法的一致性。
    6. TreeSet的使用
    6.1 底层的数据结构：红黑树
    6.2 添加数据后的特点：可以按照添加的元素的指定的属性的大小顺序进行遍历。
    6.3 向TreeSet中添加的元素的要求：
    > 要求添加到TreeSet中的元素必须是同一个类型的对象，否则会报ClassCastException.

     * 【回答：TreeSet 里“同一个类型”指什么？】
     * 指“同一个具体的类”，比如 String 是一个类型、Integer 是另一个类型，二者不是同一类型。
     * 原因：TreeSet 要排序，就必须两两比较大小。它会把元素当作 Comparable 互相调用 compareTo()。
     * 而 String 的 compareTo() 只认识 String，你把一个 Integer 塞进去，
     * String 无法与 Integer 比较，就会抛 ClassCastException。
     * 另外：基本数据类型（int、double 等）根本不能直接放进集合，
     * 放进去的其实是它们的包装类（Integer、Double），会自动装箱。所以集合里永远是“引用类型对象”。

    > 添加的元素需要考虑排序：① 自然排序 ② 定制排序
    6.4 判断数据是否相同的标准
    > 不再是考虑hashCode()和equals()方法了，也就意味着添加到TreeSet中的元素所在的类不需要重写hashCode()和equals()方法了
    > 比较元素大小的或比较元素是否相等的标准就是考虑自然排序或定制排序中，compareTo()或compare()的返回值。
      如果compareTo()或compare()的返回值为0，则认为两个对象是相等的。由于TreeSet中不能存放相同的元素，则
      后一个相等的元素就不能添加到TreeSet中。
     */
    @Test
    public void test1() {
        // 用父接口 Set 声明变量，是“面向接口编程”的习惯。好处是：后面代码只依赖 Set 接口的方法
        Set set = new HashSet();

        set.add("AA");
        set.add(123);
        set.add("BB");
        set.add(new Person3("Tom", 12));

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        /*
         * 为什么是 true？
         * 因为 Person3 重写了 hashCode() 和 equals()：新 new 的 Tom/12
         * 与集合里的 Tom/12 算出相同哈希值、equals() 又返回 true，所以 contains 判定“存在”。
         */
        System.out.println(set.contains(new Person3("Tom", 12))); // true
    }

    /*
     * LinkedHashSet 的遍历顺序 = 添加顺序（不是大小顺序）。
     * 为什么它能保证顺序、HashSet 不能？
     *    HashSet 只按哈希值决定元素落在数组哪个桶，桶的排列和你添加先后没关系，所以遍历顺序“乱”。
     *    LinkedHashSet 在此基础上额外维护了一条“双向链表”，把每个元素按添加先后串起来，
     *    遍历时就沿着这条链表走，于是恢复了添加顺序。
     */
    @Test
    public void test2() {
        Set set = new LinkedHashSet();

        set.add("AA");
        set.add(123);
        set.add("BB");
        set.add(new Person3("Tom", 12));

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}

class Person3 {
    String name;
    int age;

    public Person3() {
    }

    public Person3(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person3{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Person3 equals()...");
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person3 person3 = (Person3) o;
        return age == person3.age && Objects.equals(name, person3.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

class TreeSetTest {
    /**
     * 自然排序
     */
    @Test
    public void test1() {
        TreeSet set = new TreeSet();

        set.add("CC");
        set.add("AA");
        set.add("DD");
        set.add("MM");
        set.add("GG");
        set.add("SS");

        // ClassCastException
        /*
         * 为什么报“String cannot be cast to Integer”？
         * TreeSet 添加新元素时，会拿“新元素”去和“已有元素”比较大小。
         * 具体是让其中一方当作 Comparable 调用 compareTo(另一方)。
         * 这里集合里已经全是 String，再 add(123)（Integer）时，会发生 Integer 与 String 互比，
         * 报错信息里到底谁转谁取决于 JDK 内部比较方向，但本质就是“两种不同类型无法比较”。
         * 所以正是因为 Integer 和 String 不是同一类型。
         *
         * 如果把前面全改成 int（实际存的是 Integer），这里再 add(123)，就都是 Integer，能正常比较、不报错。
         */
//        set.add(123);

        Iterator iterator = set.iterator();

        // 为什么遍历成了从小到大？
        // TreeSet 底层红黑树在“添加时”就已按大小把元素排好了，遍历只是按顺序把它们取出来。
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     * 自然排序
     */
    /*
     * 1) 不实现 Comparable 为什么报错？
     *    TreeSet 靠比较大小来排序和判重，若元素类没实现 Comparable、又没传入 Comparator，
     *    它添加第二个元素时无法比较，运行时抛 ClassCastException（不是编译错误，是运行报错）。
     *
     * 2) 为什么没打印 equals() 里的 sout？
     *    因为 TreeSet 判重“根本不调用 equals()/hashCode()”，只看 compareTo()/compare() 的返回值。
     *    所以在 equals() 里写的打印永远不会触发——这也印证了 TreeSet 判重与 equals 无关。
     *
     * 3) 为什么“都23岁”时有时只留一个（Tom 留、Rose 不进）？
     *    - 若 compareTo() 只比 age（被注释掉的那版）：Tom(23) 和 Rose(23) 比较返回 0，
     *      TreeSet 认为“相等”，后来的 Rose 就被当成重复，不会被添加 —— 这就是“只出现先写的那个”。
     *    - 若 compareTo() 先比 age、age 相同再比 name（下面启用的这版）：Tom 和 Rose 虽同龄，
     *      但名字不同，返回值不为 0，被视为不同元素，两个都能进。所以不会丢 Rose。
     *
     * 4) u6("Tom",23) 与 u1 完全相同，一定不出现吗？
     *    在“下面这版 compareTo”下：u6 和 u1 age 相同、name 也相同，compareTo 返回 0 -> 判为重复 -> u6 一定进不去。
     *    只要 compareTo 对某个已有元素返回 0，新元素就一定被丢弃。所以名字年龄都相同的 u6 必然不出现。
     */
    @Test
    public void test2() {
        TreeSet set = new TreeSet();

        User u1 = new User("Tom", 23);
        User u2 = new User("Jerry", 24);
        User u3 = new User("Jack", 25);
        User u4 = new User("Rose", 23);
        User u5 = new User("Tony", 27);

        User u6 = new User("Tom", 23);

        set.add(u1);
        set.add(u2);
        set.add(u3);
        set.add(u4);
        set.add(u5);

        set.add(u6);

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     * 定制排序
     */
    @Test
    public void test3() {
        Comparator comparator = new Comparator() {
            /**
             * 按照姓名从小到大排列，如果姓名相同，继续比较age，按照从大到小排列
             */
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof User && o2 instanceof User) {
                    User u1 = (User) o1;
                    User u2 = (User) o2;

                    /*
                     * u1.compareTo(u2) 和 u1.getName().compareTo(u2.getName()) 的区别
                     * - u1.compareTo(u2)：调用的是 User 类里定义的“自然排序”规则（先比 age 再比 name）。
                     * - u1.getName().compareTo(u2.getName())：调用的是 String 的比较，只按“姓名”比。
                     * 既然这里在写“定制排序”，本意就是抛开 User 自带的规则、自定义按姓名比，
                     * 所以用下面这行（按 name 比）才符合本方法注释“按姓名排序”的意图。
                     */
//                    int value = u1.compareTo(u2);
                    int value = u1.getName().compareTo(u2.getName());
                    if (value != 0) {
                        return value;
                    }
                    return -(u1.getAge() - u2.getAge());
                }

                // 当传进来的 o1 或 o2 不是 User 类型时（比如误把 String 放进这个 TreeSet），
                // 上面的 instanceof 判断为 false，就会走到这里抛异常，提示“类型不匹配”。
                throw new RuntimeException("类型不匹配");
            }
        };

        // new TreeSet(comparator) 里放 comparator 干什么？
        // 把上面写好的“比较器”交给 TreeSet。之后 TreeSet 每次比较两个元素时，就用这个 comparator 的规则，
        // 而不再用元素自身的 compareTo()。这就是“定制排序”：排序规则由外部传入，灵活可换。
        TreeSet set = new TreeSet(comparator);

        User u1 = new User("Tom", 23);
        User u2 = new User("Jerry", 24);
        User u3 = new User("Jack", 25);
        User u4 = new User("Rose", 23);
        User u5 = new User("Tony", 27);

        set.add(u1);
        set.add(u2);
        set.add(u3);
        set.add(u4);
        set.add(u5);

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}


class User implements Comparable {
    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
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
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    /*
     * 为什么把 equals/hashCode 注释掉？
     * 因为本类是给 TreeSet 用的，而 TreeSet 判重只看 compareTo()/compare()，完全不调用 equals()/hashCode()。
     * 所以对 TreeSet 而言这两个方法“没用到”，注释掉不影响结果。
     */
//    @Override
//    public boolean equals(Object o) {
//        System.out.println("User equals()....");
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        User user = (User) o;
//        return age == user.age && Objects.equals(name, user.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, age);
//    }

    /*
     * 比如：按照年龄从小到大排序
     * */
//    @Override
//    public int compareTo(Object o) {
//        if (this == o) {
//            return 0;
//        }
//        if (o instanceof User) {
//            User u = (User) o;
//            return this.age - u.age;
//        }
//        throw new RuntimeException("类型不匹配");
//    }

    /**
     * 比如：先比较年龄从小到大排列，如果年龄相同，则继续比较姓名，从大到小
     */
    @Override
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        }

        if (o instanceof User) {
            User u = (User) o;

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
 * 案例：
 * 定义方法如下：public static List duplicateList(List list)
 * 要求：① 参数List中只存放Integer的对象
 * ② 在List内去除重复数字值，尽量简单
 */
class Exer1 {
    public static void main(String[] args) {
        ArrayList list = new ArrayList();

        list.add(34);
        list.add(34);
        list.add(34);
        list.add(22);
        list.add(22);
        list.add(22);
        list.add(45);
        list.add(45);
        list.add(45);

        List newList = duplicateList(list);
        System.out.println(newList); // [34, 22, 45]
    }

    public static List duplicateList(List list) {
        /*
         * 方式1 的思路：先把 list 里所有元素塞进 HashSet —— 利用“Set 不允许重复”的特性自动去重；
         * 再把去重后的 Set 元素倒回一个新 ArrayList 返回。set 在这里就是“去重工具”。
         * 缺点：HashSet 不保证顺序，所以结果顺序可能和原来不同（本题恰好凑巧对上）。
         */
        // 方式1
//        HashSet set = new HashSet();
//        for (Object o : list) {
//            set.add(o);
//        }
//
//        List list1 = new ArrayList();
//        for (Object o : set) {
//            list1.add(o);
//        }
//
//        return list1;

        // 方式2
        /*
         * 1) 效果和方式1一样（都是去重），但更简洁。方式2 是“优化/推荐”写法，实际开发一般用它。
         * 2) 效率：两者时间复杂度都是 O(n)，方式2 少写几行、少建中间容器，可读性更好，性能上无明显差异。
         * 3) new HashSet(list)：把 list 里的元素“一次性”全部装进 HashSet，装的过程自动去重。
         *    —— 这里 list 是“数据来源”，作用等同于方式1里那段 for 循环 add。
         */
        HashSet set = new HashSet(list);
        // new ArrayList(set) 把去重后的 set 里的元素一次性倒进一个新的 ArrayList（因为题目要求返回类型是 List）。
        // set 作为“数据来源”传进去，作用等同于方式1里那第二段 for 循环。
        List list1 = new ArrayList(set);

        return list1;
    }
}

/**
 * 案例：
 * 编写一个程序，获取10个1至20的随机数，要求随机数不能重复。并把最终的随机数输出到控制台。
 */
class Exer2 {
    public static void main(String[] args) {
        /*
         * 本题关键词是“不能重复”，而 Set 天生不允许重复，所以选 Set 最省事 —— 每次 add 重复值会自动被丢弃。
         * 判断口诀：
         *   - 要“去重/不重复” -> 用 Set（HashSet 够用；要有序遍历用 LinkedHashSet；要排序用 TreeSet）。
         *   - 要“可重复、按下标存取、保留顺序” -> 用 List（一般 ArrayList）。
         *   - 要“键值对映射” -> 用 Map（HashMap 等）。
         */
//        HashSet set = new HashSet();

        // Set set = new HashSet() 与 HashSet set = new HashSet() 哪个好？
        // 功能完全一样。下面这行用父接口 Set 声明（面向接口编程），更推荐——将来想换实现类只改右边即可。
        // 区别仅在于：用 Set 声明时，只能调用 Set 接口里的方法；用 HashSet 声明才能调用 HashSet 特有方法。
        Set set = new HashSet();

        while (set.size() < 10) {
            /*
             * random 变量该放循环里还是外面？
             * 这里的 random 只是一个 int（基本类型），不是对象，不存在“创建很多对象、内存卸载”的问题。
             * 每次循环它只是被重新赋值，用完即弃，非常轻量，放循环里完全没问题、也更规范
             * （变量“就近声明、最小作用域”是好习惯：只在需要它的地方存在，避免污染外部）。
             */
            int random = (int) (Math.random() * (20 - 1 + 1) + 1);
            set.add(random);
        }

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}


/**
 * 笔试题
 */
class HashSetDemo {
    public static void main(String[] args) {
        // 修改对象字段后，对象的 hashCode 变了，但它在数组里的存储位置没变
        HashSet set = new HashSet();
        Person4 p1 = new Person4(1001, "AA");
        Person4 p2 = new Person4(1002, "BB");

        set.add(p1);
        set.add(p2);

        // 为什么 1001 反而排在后面？
        // HashSet 无序！打印顺序由“哈希值算出的桶位置”决定，跟你 add 的先后无关。
        // 1002 的哈希值恰好让它落在更靠前的桶，所以先被打印，这和声明顺序无关。
        System.out.println(set);
        // [Person4{id=1002, name='BB'}, Person4{id=1001, name='AA'}]

        // 把 p1 的 name 从 "AA" 改成 "CC"，p1 的 hashCode() 随之改变，
        // 但 p1 早已存放在“按旧哈希值(AA)算出的旧桶”里，它的物理位置不会自动挪走 —— 埋下下面 remove 失败的伏笔。
        p1.name = "CC";

        // 为什么 remove(p1) 删不掉？
        // remove 时先用 p1 现在的 hashCode()（基于"CC"）去算桶位置，
        // 却跑到了一个“新桶”，而 p1 实际待在“旧桶(AA算出的)”里，两处对不上，自然找不到、删不掉。
        // 所以打印结果里 1001 仍在，只是 name 已显示为 CC。
        set.remove(p1);
        System.out.println(set);
        /*
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='AA'}]
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='CC'}]
         */

        // 为什么又能加进一个 1001-CC，出现两个 CC？
        // new Person4(1001,"CC") 的哈希值基于"CC"，算出的是“新桶”。
        // 那个新桶里此刻没人（p1 虽然 name 也是 CC，但它蹲在旧桶），于是新对象顺利加入。
        // 结果集合里出现两个看起来一样的 1001-CC：一个在旧桶、一个在新桶。
        set.add(new Person4(1001, "CC"));
        System.out.println(set);
        /*
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='AA'}]
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='CC'}]
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='CC'}, Person4{id=1001, name='CC'}]
         */

        // 为什么这里触发了 equals()（打印出 "Person4 equals()...."）？
        // new Person4(1001,"AA") 的哈希值基于"AA"，算出的正是“旧桶”，而旧桶里蹲着被改名成 CC 的 p1。
        // 哈希值相同（撞进同一个桶）就要再调 equals() 比对：新对象是 1001-AA，桶里那个现在是 1001-CC，
        // equals() 返回 false（name 不同），于是判为不同元素，成功加入。这就是唯一一次触发 equals 的原因。
        set.add(new Person4(1001, "AA"));
        System.out.println(set);
        /*
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='AA'}]
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='CC'}]
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='CC'}, Person4{id=1001, name='CC'}]
        Person4 equals()....
        [Person4{id=1002, name='BB'}, Person4{id=1001, name='CC'}, Person4{id=1001, name='CC'}, Person4{id=1001, name='AA'}]
         */
    }
}

class Person4 {
    int id;
    String name;

    public Person4(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Person4 equals()....");
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person4)) {
            return false;
        }

        Person4 person4 = (Person4) o;

        if (id != person4.id) {
            return false;
        }
        return name != null ? name.equals(person4.name) : person4.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person4{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
