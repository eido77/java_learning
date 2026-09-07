package chapter12;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CollectionFrameworkTest {
    /*
    1. 内存层面需要针对于多个数据进行存储。此时，可以考虑的容器有：数组、集合类
    2. 数组存储多个数据方面的特点：
       > 数组一旦初始化，其长度就是确定的。
       > 数组中的多个元素是依次紧密排列的，有序的，可重复的
       > (优点) 数组一旦初始化完成，其元素的类型就是确定的。不是此类型的元素，就不能添加到此数组中。
          int[] arr = new int[10];
          arr[0] = 1;
          arr[1] = "AA"; // 编译报错
       > (优点)元素的类型既可以是基本数据类型，也可以是引用数据类型。
          Object[] arr1 = new Object[10];
          arr1[0] = new String();
          arr1[1] = new Date();

       数组存储多个数据方面的弊端：
       > 数组一旦初始化，其长度就不可变了。
       > 数组中存储数据特点的单一性。对于无序的、不可重复的场景的多个数据就无能为力了。
       > 数组中可用的方法、属性都极少。具体的需求，都需要自己来组织相关的代码逻辑。
       > 针对于数组中元素的删除、插入操作，性能较差。
    3. Java集合框架体系（java.util包下）
    java.util.Collection:存储一个一个的数据
        |-----子接口：List:存储有序的、可重复的数据 ("动态"数组)
               |---- ArrayList(主要实现类)、LinkedList、Vector

        |-----子接口：Set:存储无序的、不可重复的数据(高中学习的集合)
               |---- HashSet(主要实现类)、LinkedHashSet、TreeSet

    java.util.Map:存储一对一对的数据(key-value键值对，(x1,y1)、(x2,y2) --> y=f(x),类似于高中的函数)
        |---- HashMap(主要实现类)、LinkedHashMap、TreeMap、Hashtable、Properties
     */

    /**
     * 测试Collection中方法的使用
     */
    /*
    1. 常用方法：（Collection中定义了15个抽象方法）
    add(Object obj)
    addAll(Collection coll)
    clear()
    isEmpty()
    size()
    contains(Object obj)
    containsAll(Collection coll)
    retainAll(Collection coll)
    remove(Object obj)
    removeAll(Collection coll)
    hashCode()
    equals()
    toArray()
    <T> T[] toArray(T[] a)：toArray 的重载版本，能返回指定类型的数组（如 String[]），能让集合只装某一种类型、取出时不用强转
    iterator()
    2. 集合与数组的相互转换：
    集合 ---> 数组：toArray()
    数组 ---> 集合：调用Arrays的静态方法asList(Object ... objs)
    3. 向Collection中添加元素的要求：
      要求元素所属的类一定要重写equals()!
    原因：
    因为Collection中的相关方法（比如：contains() / remove()）在使用时，要调用元素所在类的equals()。
     */
    @Test
    public void test1() {
        /**
         * （1）add(Object obj)：添加元素对象到当前集合中
         */
        /*
         *  Collection 是接口，接口不能实例化(不能 new)，必须 new 一个它的实现类，ArrayList 就是最常用的实现类。
         *  多态：编译期看左边(Collection)，能调用的方法就是 Collection 声明过的；
         *       运行期执行右边(ArrayList)里真正的实现代码。
         * ArrayList 实现了 Collection 接口，Collection 声明的方法它必须全部实现，左边能调的方法右边一定有。
         */
        Collection collection = new ArrayList();

        /*
         * new ArrayList() 创建的是一个"空的动态数组"对象(初始容量 10)。
         * 每调用一次 add，就往集合里多存"一个独立的元素"，不是拼接连在一起，
         * 而是一个一个分开存放的，最终集合里有多少个元素就是调了多少次 add。
         */
        collection.add("AA");

        // 自动装箱：集合只能存对象，int 的 123 会被自动装箱成 Integer 对象再存入。
        collection.add(123);

        collection.add("你好");
        collection.add(new Object());
        collection.add(new Person("Tom", 12));

        System.out.println(collection); // [AA, 123, 你好, java.lang.Object@7a4ccb53, Person{name='Tom', age=12}]

        /**
         * （2）addAll(Collection other)：添加other集合中的所有元素对象到当前集合中，即this = this ∪ other
         */
        Collection collection2 = new ArrayList();
        collection2.add("BB");
        collection2.add(456);

        System.out.println(collection.size()); // 5

        /*
         * addAll(coll)：把 coll 里的元素"逐个拆开"加进来，加了 2 个，size 5->7。
         * add(coll)   ：把整个 coll 当成"一个元素"整体塞进来，只加 1 个，size 5->6，
         *               打印时会出现嵌套的 [BB, 456]。
         */
        collection.addAll(collection2); // [AA, 123, 你好, java.lang.Object@7a4ccb53, Person{name='Tom', age=12}, BB, 456]
//        collection.add(collection2); // [AA, 123, 你好, java.lang.Object@7a4ccb53, Person{name='Tom', age=12}, [BB, 456]]

        /*
         * addAll 是把 collection2 的元素"复制引用"到 collection 里，
         * collection2 本身不会被清空或改变，所以它仍然是 [BB, 456]。
         * (只有 collection 这边变多了，源集合不受影响。)
         */
        System.out.println(collection2); // [BB, 456]
        System.out.println(collection); // [AA, 123, 你好, java.lang.Object@33afa13b, Person{name='Tom', age=12}, BB, 456]

        /**
         * size()：返回集合中实际存储的元素个数。
         */
        System.out.println(collection.size()); // 7
    }

    @Test
    public void test2() {
        Collection collection = new ArrayList();

        collection.add("AA");
        collection.add(123);
        collection.add(128);
        collection.add(new String("你好"));
        collection.add(new Person("Tom", 12));

        /**
         * （4）boolean isEmpty()：判断当前集合是否为空集合
         */
        System.out.println(collection.isEmpty()); // false

        /**
         * （5）boolean contains(Object obj)：判断当前集合中是否存在一个与obj对象equals返回true的元素
         *
         * contains(obj) 会拿 obj 去和集合里每个元素调 equals() 比较，
         * 只要有一个返回 true 就整体返回 true。所以关键全在"元素类的 equals 怎么写"。
         */
        // "AA" 用 String 的 equals(比内容)，集合里有内容相同的 "AA"，故 true。
        System.out.println(collection.contains("AA")); // true

        // "A" 和集合里任何元素内容都不相等，equals 全 false，故 false。
        System.out.println(collection.contains("A")); // false

        // 123 自动装箱成 Integer，Integer 的 equals 比数值，集合里有 123，故 true。
        System.out.println(collection.contains(123)); // true

        // "Integer 缓存(-128~127)"只影响 == 的结果，跟 equals/contains 无关，
        System.out.println(collection.contains(128)); // true

        /*
         * 这里用的是 String 重写后的 equals()
         * new String("你好") 虽然是新对象、地址不同，但 String 的 equals 比"内容"，
         * 内容都是"你好"，所以 true。
         */
        System.out.println(collection.contains(new String("你好"))); // true

        /*
         * Person 重写了 equals(比 name+age)，所以只要 name、age 都相同就 true；
         *   如果不重写，用的是 Object 的 equals(比地址)，new 出来的新对象地址不同 -> false。
         * 调用几次 equals：contains 从头往后逐个比，"比中了就停"。
         *    集合顺序是 [AA, 123, 128, 你好, Person]，Person 排第 5 个：
         *    和 AA/123/128/你好 比都不相等(类型不同直接 false)，第 5 个才 true，
         *    所以一共调 5 次 equals。
         */
        System.out.println(collection.contains(new Person("Tom", 12))); // true

        /**
         * （6）boolean containsAll(Collection coll)：判断coll集合中的元素是否在当前集合中都存在。即coll集合是否是当前集合的“子集”
         */
        Collection collection2 = new ArrayList();

        collection2.add("AA");
        collection2.add("BB");

        /*
         * containsAll 判断的是"普通子集"(⊆)，不是"真子集"，
         * 即"coll 里每个元素在当前集合中都能找到"就算 true，两集合完全相同也返回 true。
         * 这里 coll2=[AA, BB]，当前集合里有 AA 但没有 BB，不是子集，所以 false。
         */
        System.out.println(collection.containsAll(collection2)); // false

        /**
         * （7）boolean equals(Object obj)：判断当前集合与obj是否相等
         * 比较两个 List 内容是否一致时会用到。
         */
        Collection listA = new ArrayList();

        listA.add("AA");
        listA.add("BB");

        Collection listB = new ArrayList();

        listB.add("AA");
        listB.add("BB");

        System.out.println(listA.equals(listB)); // true(元素与顺序都相同)
    }

    @Test
    public void test3() {
        Collection collection = new ArrayList();

        collection.add("AA");
        collection.add(123);
        collection.add(new String("你好"));
        collection.add(new Person("Tom", 12));

        /**
         * （8）void clear()：清空集合元素
         */
        /*
         * 只把 size 改成 0 是不够的：底层数组里那些槽位仍然引用着旧对象，这些对象无法被垃圾回收(GC)，就会"内存泄漏"(该释放的没释放)。
         * 所以 clear 的正确做法是把底层数组每个槽位都置为 null，切断引用后 GC 才能回收，同时把 size 记为 0。
         */
        collection.clear();
        System.out.println(collection.isEmpty()); // true
        System.out.println(collection); // []
        System.out.println(collection.size()); // 0
    }

    @Test
    public void test4() {
        Collection collection = new ArrayList();

        collection.add("AA");
        collection.add("AA");
        collection.add(123);
        collection.add(new String("你好"));
        collection.add(new Person("Tom", 12));

        /**
         * （9） boolean remove(Object obj) ：从当前集合中删除第一个找到的与obj对象equals返回true的元素。
         */
        // remove 靠 equals 判断"删哪个"，不需要是同一个变量/同一个对象。
        collection.remove(new Person("Tom", 12));
        System.out.println(collection); // [AA, AA, 123, 你好]

        /*
         * remove 只删"第一个"匹配的(从索引 0 往后找，删中一个就停)。
         * 两个 AA 内容一样、无法区分谁是谁，删的是"下标更靠前"的那个；
         * 从结果上看不出删的具体是哪一个(因为它俩完全相同)，只知道剩下一个 AA。
         * 想删全部相同元素，要用 removeAll(下面演示)或循环删除。
         */
        collection.remove("AA");
        System.out.println(collection); // [AA, 123, 你好]

        /**
         * （10）boolean removeAll(Collection coll)：从当前集合中删除所有与coll集合中相同的元素。即this = this - this ∩ coll
         * 判断相同同样走 equals，不看地址值
         */
        Collection c1 = new ArrayList(Arrays.asList("AA", "BB", "CC"));
        Collection sub1 = new ArrayList(Arrays.asList("BB", "CC"));

        c1.removeAll(sub1);
        System.out.println(c1); // [AA]

        /**
         * （11）boolean retainAll(Collection coll)：从当前集合中删除两个集合中不同的元素，使得当前集合仅保留与coll集合中的元素相同的元素，
         * 即当前集合中仅保留两个集合的交集，即this  = this ∩ coll；
         */
        Collection c2 = new ArrayList(Arrays.asList("AA", "BB", "CC"));
        Collection sub2 = new ArrayList(Arrays.asList("BB", "CC"));

        c2.retainAll(sub2);
        // 只保留共同的 BB、CC
        System.out.println(c2); // [BB, CC]
    }

    @Test
    public void test5() {
        /**
         * （12）Object[] toArray()：返回包含当前集合中所有元素的数组
         */
        Collection collection = new ArrayList();

        collection.add("AA");
        collection.add("AA");
        collection.add(123);
        collection.add(new String("你好"));
        collection.add(new Person("Tom", 12));

        /**
         * 集合 ---> 数组：toArray()
         */
        Object[] arr = collection.toArray();
        System.out.println(Arrays.toString(arr)); // [AA, AA, 123, 你好, Person{name='Tom', age=12}]

        /**
         * （13）hashCode()：获取集合对象的哈希值
         */
        /*
         * 它不是"一个地址一个值"。List 的 hashCode 是由"里面所有元素的 hashCode 一起算出来的"，
         *   元素内容不变，算出来就不变；一旦增删元素，hashCode 就会变。
         */
        System.out.println(collection.hashCode()); // -296842714
    }

    /**
     * 数组 ---> 集合：调用Arrays的静态方法asList(Object ... objs)
     */
    @Test
    public void test6() {
        String[] arr = new String[]{"AA", "BB", "CC"};

        /*
         * asList 的返回类型是 List，所以用 List 接收更规范、更贴合返回值；
         * 用 Collection 接收也能编译(List 是 Collection 子接口)，但会"丢掉" List 特有的方法，一般不推荐。
         * Arrays.asList 返回的是"固定大小"的特殊 List，不能 add/remove，否则报UnsupportedOperationException。
         */
        List list = Arrays.asList(arr);
        System.out.println(list); // [AA, BB, CC]

        Collection list2 = Arrays.asList("AA", "BB", "CC", "DD");
        System.out.println(list2); // [AA, BB, CC, DD]
    }

    @Test
    public void test7() {
        /*
         * asList 的签名是 asList(T... a)，可变参数 T 只能是"对象类型"，不能是基本类型。
         *
         * Integer[] 里装的是对象，会被当成"3 个元素"摊开 -> size = 3，打印 [1, 2, 3]。
         * int[] 是基本类型数组，它本身是"一个对象"，无法被摊成 3 个 Integer，
         *   于是整个 int[] 被当作"唯一的一个元素" -> size = 1，
         *    元素就是这个数组对象，打印出它的地址风格字符串 [I@6193932a「[I 表示"int 数组"类型」
         */
        Integer[] arr = new Integer[]{1, 2, 3};
        List list = Arrays.asList(arr);
        System.out.println(list.size()); // 3
        System.out.println(list); // [1, 2, 3]

        int[] arr2 = new int[]{1, 2, 3};
        List list2 = Arrays.asList(arr2);
        System.out.println(list2.size()); // 1
        System.out.println(list2); // [[I@6193932a]
    }
}

/*
1. 迭代器(Iterator)的作用？
   用来遍历集合元素的。
2. 如何获取迭代器(Iterator)对象？
Iterator iterator = coll.iterator();
3. 如何实现遍历(代码实现)
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}
 */
class IteratorTest {
    @Test
    public void test1() {
        Collection collection = new ArrayList();

        collection.add("AA");
        collection.add("AA");
        collection.add(123);
        collection.add(new String("你好"));
        collection.add(new Person("Tom", 12));

        // 获取迭代器对象
        /*
         * Iterator 是接口。collection.iterator() 返回的真实对象是
         * ArrayList 内部类 Itr(它实现了 Iterator 接口)，所以又是"编译看左(Iterator)、
         * 运行看右(Itr)"的多态。
         */
        Iterator iterator = collection.iterator();
        System.out.println(iterator.getClass()); // class java.util.ArrayList$Itr

        /**
         * next()：① 指针下移 ② 将下移以后集合位置上的元素返回
         */
        // 方式1
        /*
         * 这是"手动一次次点名"的遍历——每调一次 next() 就取下一个元素。
         * 集合有 5 个元素，正好手写 5 次 next() 全部取出。
         */
//        System.out.println(iterator.next());
//        System.out.println(iterator.next());
//        System.out.println(iterator.next());
//        System.out.println(iterator.next());
//        System.out.println(iterator.next());

        /*
         * 上面 5 次已把元素取完，指针到了末尾，再 next() 后面没有元素了，
         * 就抛 NoSuchElementException(没有下一个了)。
         */
//        System.out.println(iterator.next()); // NoSuchElementException

        // 方式2
        // size() 取集合元素个数，用它当循环次数，个数对得上时能正常跑。
//        for (int i = 0; i < collection.size(); i++) {
//            System.out.println(iterator.next());
//        }

        // 方式3：推荐
        // cmd + option + T：Surround With（包围代码）
        /**
         * hasNext() 是"探路"——它只判断"后面还有没有下一个元素"，返回 true/false，
         * 但不移动指针、不取元素。配合 next() 就能"有就取、没了就停"，天然避免越界
         */
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    public void test2() {
        Collection collection = new ArrayList();

        collection.add("AA");
        collection.add("AA");
        collection.add(123);
        collection.add(new String("你好"));
        collection.add(new Person("Tom", 12));


        // 方式1：错误的遍历
        /*
         * 错在"一次循环里调了两次 next()"——while 条件里一次、循环体里又一次，
         * 相当于每轮跳着取、还多取。当指针已到末尾时再 next() 就抛 NoSuchElementException；
         * 而且这样会漏掉一半元素。切记：一轮循环只应调一次 next()。
         */
//        Iterator iterator = collection.iterator();

//        while (iterator.next() != null) {
//            System.out.println(iterator.next());
//        }

        // 方式2：错误的遍历
        /*
         * 每次写 collection.iterator() 都新建一个"全新迭代器、指针停在最前"。
         *   条件里 new 一个判断 hasNext(永远 true)，循环体里又 new 一个取 next(永远第 1 个)，
         *   两个迭代器都刚出生，于是永远在取第 1 个元素 AA -> 死循环一直打印 AA。
         */
//        while (collection.iterator().hasNext()) {
//            System.out.println(collection.iterator().next());
//        }
    }
}

/*
4. 增强for循环(foreach循环)的使用（jdk5.0新特性）
4.1 作用
用来遍历数组、集合。
4.2 格式：
for (要遍历的集合或数组元素的类型 临时变量 : 要遍历的集合或数组变量) {
    操作临时变量的输出
}
4.3 说明：
> 针对于集合来讲，增强for循环的底层仍然使用的是迭代器。
> 增强for循环的执行过程中，是将集合或数组中的元素依次赋值给临时变量，注意，循环体中对临时变量的修改，可能
  不会导致原有集合或数组中元素的修改。
 */
class ForTest {
    @Test
    public void test1() {
        Collection collection = new ArrayList();

        collection.add("AA");
        collection.add("AA");
        collection.add(123);
        collection.add(new String("你好"));
        collection.add(new Person("Tom", 12));

        // 每轮循环，临时变量 o 自动被赋值为下一个元素，直到取完。
        for (Object o : collection) {
            System.out.println(o);
        }
    }

    @Test
    public void test2() {
        String[] arr = new String[]{"GG", "JJ", "DD", "MM", "SS"};

        for (String s : arr) {
            System.out.println(s);
        }
    }

    @Test
    public void test3() {
        int[] arr = new int[]{1, 2, 3, 4, 5};

        for (int i : arr) {
            System.out.println(i);
        }
    }
}

/**
 * 笔试题：写出如下程序的输出结果
 */
/*
 * foreach 里的临时变量是"元素的一份拷贝副本"。
 * - 赋值操作1(普通 for + 下标)：arr1[i]="MM" 是直接改数组本身的槽位，改得动 -> MM MM MM。
 * - 赋值操作2(foreach)：s="MM" 只是把临时变量 s 指向新字符串，
 *   根本没碰数组里的元素，原数组不变 -> AA CC DD。
 * 一句话：想通过 foreach 的临时变量改原数组/集合，改不动；要改就得用下标或迭代器。
 */
class InterviewTest {
    @Test
    public void testFor() {
        String[] arr1 = new String[]{"AA", "CC", "DD"};

        // 赋值操作1
//        for (int i = 0; i < arr1.length; i++) {
//            arr1[i] = "MM";
//        }
        // MM MM MM

        // 赋值操作2
        for (String s : arr1) {
            s = "MM";
        }
        // AA CC DD

        // 遍历
        for (String s : arr1) {
            System.out.println(s);
        }
    }
}


class Person {
    String name;
    int age;

    public Person() {
    }

    public Person(String name, int age) {
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
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    /*
     * hashCode 的作用是：给对象算一个"整数编号"，供 HashSet / HashMap 这类
     * "哈希结构"快速定位元素用。规范要求"重写 equals 就应同时重写 hashCode"
     * (两个 equals 相等的对象，hashCode 必须相等)，否则以后往 HashSet/HashMap 里
     * 放 Person 会出现"明明相等却判定不重复"的 bug。
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
