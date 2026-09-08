package chapter12;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ListTest {
    /*
    1. List接口中存储数据的特点：用于存储有序的、可以重复的数据。---> 使用List替代数组，"动态"数组
    2. List中的常用方法：
    第1波：Collection中声明的15个方法。
    第2波：因为List是有序的，进而就有索引，进而就会增加一些针对索引操作的方法。
    - 插入元素
      - `void add(int index, Object ele)`:在index位置插入ele元素
      - boolean addAll(int index, Collection eles):从index位置开始将eles中的所有元素添加进来
    - 获取元素
      - `Object get(int index)`:获取指定index位置的元素
      - List subList(int fromIndex, int toIndex):返回从fromIndex到toIndex位置的子集合
    - 获取元素索引
      - int indexOf(Object obj):返回obj在集合中首次出现的位置
      - int lastIndexOf(Object obj):返回obj在当前集合中末次出现的位置
    - 删除和替换元素
      - `Object remove(int index)`:移除指定index位置的元素，并返回此元素
      - `Object set(int index, Object ele)`:设置指定index位置的元素为ele
    小结：
        增
            add(Object obj)
            addAll(Collection coll)
        删
            remove(Object obj)
            remove(int index)
        改
            set(int index, Object ele)
        查
            get(int index)
        插
            add(int index, Object ele)
            addAll(int index, Collection eles)
        长度
            size()
        遍历
            iterator() ：使用迭代器进行遍历
            增强for循环
            一般的for循环
    3. List及其实现类特点
    java.util.Collection:存储一个一个的数据
        |-----子接口：List:存储有序的、可重复的数据 ("动态"数组)
               |---- ArrayList:List的主要实现类；线程不安全的、效率高；底层使用Object[]数组存储
                               在添加数据、查找数据时，效率较高；在插入、删除数据时，效率较低
               |---- LinkedList:底层使用双向链表的方式进行存储；在对集合中的数据进行频繁的删除、插入操作时，建议使用此类
                               在插入、删除数据时，效率较高；在添加数据、查找数据时，效率较低；
               |---- Vector:List的古老实现类；线程安全的、效率低；底层使用Object[]数组存储
    [面试题] ArrayList、Vector的区别？  ArrayList、LinkedList的区别？
     * 1. ArrayList vs Vector：
     *    - 二者底层都是 Object[] 数组，是"动态数组"。
     *    - Vector 是古老实现类(JDK1.0)，方法多用 synchronized 修饰，线程安全但效率低。
     *    - ArrayList(JDK1.2) 线程不安全、效率高，是开发中的主流选择。
     *    - 扩容策略：ArrayList 默认扩容为原来的 1.5 倍，Vector 默认扩容为原来的 2 倍。
     * 2. ArrayList vs LinkedList：
     *    - ArrayList 底层是数组，随机访问(get/set)快，中间插入/删除慢(要移动元素)。
     *    - LinkedList 底层是双向链表，中间插入/删除快(只改指针)，随机访问慢(要从头遍历)。
     */
    @Test
    public void test1() {
        List list = new ArrayList();

        /**
         * add(Object obj)
         */
        list.add("AA");
        // 补充说明：123 是 int，会被自动装箱成 Integer 存入(List 只能存对象，不能存基本类型)
        list.add(123); // 自动装箱
        list.add("BB");
        list.add(new Person1("Tom", 12));

        System.out.println(list.toString()); // [AA, 123, BB, Person1{name='Tom', age=12}]

        /**
         * add(int index, Object ele)
         */
        /*
         * "插入"到 index 位置(不是覆盖)。
         * 原本 index 位置及之后的元素会整体后移一位，集合长度 +1。
         * 若要"覆盖"某位置，应该用 set(index, ele)。
         */
        list.add(2, "CC");
        System.out.println(list); // [AA, 123, CC, BB, Person1{name='Tom', age=12}]

        /**
         * addAll(int index, Collection eles)
         */
        List list1 = Arrays.asList(1, 2, 3);

        // addAll：把 list1 里的元素"逐个拆开"插入到索引 1 起始的位置
        list.addAll(1, list1);
        System.out.println(list); // [AA, 1, 2, 3, 123, CC, BB, Person1{name='Tom', age=12}]

        /*
         * add(1, list1) 是把整个 list1 当作"一个元素"塞进去，
         * 所以打印出来是 [1, 2, 3] 这样一个嵌套的整体；
         * 而上面的 addAll(1, list1) 是把里面的元素一个个拆开加进去。
         */
        list.add(1, list1);
        System.out.println(list); // [AA, [1, 2, 3], 1, 2, 3, 123, CC, BB, Person1{name='Tom', age=12}]
    }

    @Test
    public void test2() {
        List list = new ArrayList();

        list.add("AA");
        list.add(2); // 自动装箱
        list.add(123); // 自动装箱
        list.add("BB");
        list.add(new Person1("Tom", 12));

        /**
         * remove(int index)
         * 删除索引2的元素
         */
        /*
         * List 有两个重载方法 remove(int index) 和 remove(Object obj)。
         * 这里传入的是字面量 2，编译器优先精确匹配基本类型 int，
         * 所以调用的是 remove(int index)，即按"索引"删除，删掉的是索引 2 上的 123。
         * 若想按"值"删除数字 2，必须手动装箱：remove(Integer.valueOf(2))(见下方)。
         */
        list.remove(2);
        System.out.println(list); // [AA, 2, BB, Person1{name='Tom', age=12}]

        // get(int index) 只接收索引，这里取的是索引 2 的元素 BB
        System.out.println(list.get(2)); // BB

        /**
         * remove(Object obj)
         * 删除数据2的元素
         */
        /*
         * Integer.valueOf(2) 把 int 2 显式变成 Integer 对象，
         * 从而精确匹配到 remove(Object obj) 重载，按"值"删除。
         * 也可以写成 remove((Integer) 2)，效果相同。
         */
        list.remove(Integer.valueOf(2));
        System.out.println(list); // [AA, BB, Person1{name='Tom', age=12}]
    }

    @Test
    public void test3() {
        List list = new ArrayList();

        list.add("AA");
        list.add(2); // 自动装箱
        list.add(123); // 自动装箱
        list.add("BB");
        list.add(new Person1("Tom", 12));

        // 遍历方式1：使用迭代器
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        // 遍历方式2：增强for循环
        // 增强 for 底层其实就是迭代器，写法更简洁，但遍历中不能用 list.remove(推荐用迭代器的 remove)
        for (Object o : list) {
            System.out.println(o);
        }

        // 遍历方式3：一般for循环
        // 普通 for 依赖索引 get(i)，只有 List 这类"有索引"的集合才能用；Set 就用不了
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}


class Person1 {
    String name;
    int age;

    public Person1() {
    }

    public Person1(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person1{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Person1 equals()...");
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person1 person1 = (Person1) o;
        return age == person1.age && Objects.equals(name, person1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

/**
 * 案例：键盘录入学生信息，保存到集合List中。
 * (1) 定义学生类，属性为姓名、年龄，提供必要的getter、setter方法，构造器，toString()，equals()方法。
 */
class Student {
    private String name;
    private int age;

    public Student() {
    }

    public Student(String name, int age) {
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
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return age == student.age && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

/**
 * (2) 使用ArrayList集合，保存录入的多个学生对象。
 * (3) 循环录入的方式，1：继续录入，0：结束录入。
 * (4) 录入结束后，用foreach遍历集合。
 */
class StudentTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 创建一个 ArrayList 集合，用来动态存放录入的多个 Student 对象。
        List list = new ArrayList();

        System.out.println("请录入学生信息：");

        // 通过循环的方式，添加多个学生信息
        while (true) {
            System.out.println("1：继续录入，0：结束录入");

            int selection = sc.nextInt();

            if (selection == 0) {
                break;
            }

            System.out.print("请输入学生的姓名：");
            String name = sc.next();

            System.out.print("请输入学生的年龄：");
            int age = sc.nextInt();

            Student student = new Student(name, age);

            list.add(student);
        }

        // 遍历集合中的学生信息
        System.out.println("遍历学生信息：");

        for (Object o : list) {
            /*
             * 若只是打印，o 会自动调用 toString()，不加强转也能正常输出，所以这里可省。
             * 但想调用 Student 特有的方法(如 o.getName())，就必须强转
             * 规范做法是加泛型从而根本不需要强转。
             */
//            Student student = (Student) o;

            System.out.println(o);
        }

        sc.close();
    }
}

/**
 * 案例：
 * 定义方法public static int listTest(Collection list,String s)统计集合中指定元素出现的次数
 * (1) 创建集合，集合存放随机生成的30个小写字母
 * (2) 用listTest统计，a、b、c、x元素的出现次数
 */
class ListTest2 {
    public static void main(String[] args) {
        // 需求1：随机生成30个字符，存放在ArrayList中
        ArrayList list = new ArrayList();

        for (int i = 0; i < 30; i++) {
            /*
             * 'a' - 'z'  [97,122]
             *  + "" 的作用：把 char 拼接成 String 存进集合。
             *    因为下面 listTest 用 s.equals(o) 且 s 是 String，
             *    集合里也必须存 String 才能比对成功
             */
            list.add((char) (Math.random() * (122 - 97 + 1) + 97) + "");
        }

        System.out.println(list);

        int aCount = listTest(list, "a");
        int bCount = listTest(list, "b");
        int cCount = listTest(list, "c");
        int xCount = listTest(list, "x");

        System.out.println("aCount = " + aCount);
        System.out.println("bCount = " + bCount);
        System.out.println("cCount = " + cCount);
        System.out.println("xCount = " + xCount);
    }

    // 需求2：遍历ArrayList，查找指定的元素出现的次数
    /*
     * 这里用 s.equals(o) 而不是 o.equals(s) 是有意为之
     * s 一定是非 null 的形参，用它调 equals 可避免元素为 null 时的空指针异常。
     */
    public static int listTest(Collection list, String s) {
        int count = 0;

        for (Object o : list) {
            if (s.equals(o)) {
                count++;
            }
        }

        return count;
    }
}
