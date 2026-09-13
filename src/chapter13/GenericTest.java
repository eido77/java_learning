package chapter13;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Predicate;

public class GenericTest {
    /*
    1. 什么是泛型？
    所谓泛型，就是允许在定义类、接口时通过一个`标识`表示类中某个`属性的类型`或者是某个方法的
    `返回值或参数的类型`。这个类型参数将在使用时（例如，继承或实现这个接口、创建对象或调用方法时）
    确定（即传入实际的类型参数，也称为类型实参）。
    2. 在集合中使用泛型之前可能存在的问题
    问题1：类型不安全。因为add()的参数是Object类型，意味着任何类型的对象都可以添加成功
    问题2：需要使用强转操作，繁琐。还有可能导致ClassCastException异常。
    3. 在集合、比较器中使用泛型 (重点)
    见代码。
    4.使用说明
    > 集合框架在声明接口和其实现类时，使用了泛型（jdk5.0），在实例化集合对象时，
      如果没有使用泛型，则认为操作的是Object类型的数据。
      如果使用了泛型，则需要指明泛型的具体类型。一旦指明了泛型的具体类型，则在集合的相关的方法中，凡是使用类的泛型的位置，都替换为具体的泛型类型。

     * E、T、K、V 只是类型参数的常见名称，不是 Java 关键字：
     *   E 通常表示 Element，T 表示 Type，K 表示 Key，V 表示 Value。
     * E “等待确定的类型参数”
     * 如果 E 没有显式上界，例如 <E>，它隐含的上界确实是 Object，但实际使用时可以指定为 Integer、String、Employee 等引用类型。
     * <K, V>表示声明两个类型参数：
     * K 通常代表 Key，即键的类型；V 通常代表 Value，即值的类型。
     * HashMap<String, Integer> 表示键按照 String 检查，值按照 Integer 检查。
     * K 和 V 只是常见命名，也可以使用其他合法标识符，但使用惯例名称更容易阅读。
     */

    /**
     * 集合中使用泛型前的场景
     */
    @Test
    public void test1() {
        /*
         * 这里没有指定泛型，因此 List 和 ArrayList 都是原始类型。
         * 原始类型会绕过一部分编译期类型检查，不推荐在新代码中使用。
         */
        List list = new ArrayList();
        list.add(67);
        list.add(78);
        list.add(76);
        list.add(99);

        // 问题1：类型不安全。因为add()的参数是Object类型，意味着任何类型的对象都可以添加成功
//        list.add("AA");

        /*
         * 原始类型的集合允许同时保存 Integer、String 等不同类型的对象。
         * 真正的风险通常不会在 add() 时出现，而是在后续把元素强制转换为某个具体类型时出现。
         */
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            // 问题2：需要使用强转操作，繁琐。还有可能导致ClassCastException异常
            Integer i = (Integer) iterator.next();
            /*
             * 自动拆箱。变量 i 的类型是 Integer，而 score 的类型是 int，
             * 编译器会自动把这次赋值处理为类似 i.intValue() 的操作。
             * 如果 i 为 null，自动拆箱会抛出 NullPointerException。
             */
            int score = i;
            System.out.println(score);
        }
    }

    /**
     * 集合中使用泛型的例子
     */
    @Test
    public void test2() {
        List<Integer> list = new ArrayList<Integer>();

        list.add(67);
        list.add(78);
        list.add(76);
        list.add(99);

//        list.add("AA");

        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            // 因为添加的都是Integer类型，避免了强转操作
            Integer i = iterator.next();
            /*
             * int 不能作为泛型类型参数，不能写 List<int> 或 Iterator<int>。
             * 泛型类型参数必须是引用类型，应使用对应的包装类 Integer。
             * 从 Integer 赋值给 int 时，Java 会自动拆箱，所以这里可以声明为 int score。
             */
            int score = i;
            System.out.println(score);
        }
    }

    /**
     * 泛型在Map中使用的例子
     */
    @Test
    public void test3() {
//        HashMap<String, Integer> map = new HashMap<String, Integer>();

        // 类型推断（jdk7的新特性）
        /*
         * 编译器根据左侧 HashMap<String, Integer> 推断右侧类型，因此它与显式写出
         * new HashMap<String, Integer>() 在这里具有相同的类型检查效果。
         */
        HashMap<String, Integer> map = new HashMap<>();

        map.put("Tom", 67);
        map.put("Jerry", 87);
        map.put("Rose", 99);

        /*
         * map.entrySet() 会把 Map 中的一组“键值对”视为一个 Set。
         * 每个键值对的类型是 Map.Entry<String, Integer>：
         * String 是键的类型，Integer 是值的类型。
         * 实际上可以从内向外理解为“由多个 String-Integer 键值对组成的 Set”。
         */
//        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
//        Iterator<Map.Entry<String, Integer>> iterator = entrySet.iterator();

        /*
         * var 是 Java 10 引入的局部变量类型推断语法，只能用于满足条件的局部变量。
         * 编译器会根据等号右侧表达式推断出静态类型，它不是 JavaScript 中那种动态类型。
         */
        var entrySet = map.entrySet();
        var iterator = entrySet.iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + "-->" + value);
            /*
            Tom-->67
            Rose-->99
            Jerry-->87
             */
        }
    }
}

/**
 * 案例1
 */
/*
1. 定义一个Employee类。
   该类包含：private成员变量name,age,birthday，其中 birthday 为 MyDate 类的对象；
   并为每一个属性定义 getter, setter 方法；
   并重写 toString 方法输出 name, age, birthday
 */
class Employee implements Comparable<Employee> {
    private String name;
    private int age;
    private MyDate birthday;

    public Employee() {
    }

    public Employee(String name, int age, MyDate birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
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

    public MyDate getBirthday() {
        return birthday;
    }

    public void setBirthday(MyDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }

    // 按照name从低到高排序
    @Override
    public int compareTo(Employee o) {
        /*
         * this 表示当前调用 compareTo() 的 Employee 对象，this.name 是当前对象的姓名。
         * o 是传入的另一个 Employee 对象，o.name 是另一个对象的姓名。
         * 例如 e1.compareTo(e2) 中，this 是 e1，o 是 e2。
         */
        return this.name.compareTo(o.name);
    }
}

/*
2. MyDate类包含:
   private成员变量year,month,day；并为每一个属性定义 getter, setter 方法；
 */
class MyDate implements Comparable<MyDate> {
    private int year;
    private int month;
    private int day;

    public MyDate() {
    }

    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return year + "年" + month + "月" + day + "日";
    }

    // 按照生日从小到大排列
    @Override
    public int compareTo(MyDate o) {
        /*
         * 当前写法通过减法判断大小，在普通合法年份范围内通常能正常工作。
         * 但如果两个 int 相减发生整数溢出，比较结果可能错误。更稳妥的写法是：
         * int yearComparison = Integer.compare(this.getYear(), o.getYear());
         */
        int yearDistance = this.getYear() - o.getYear();
        if (yearDistance != 0) {
            return yearDistance;
        }

        int monthDistance = this.getMonth() - o.getMonth();
        if (monthDistance != 0) {
            return monthDistance;
        }

        return this.getDay() - o.getDay();
    }
}

/*
3. 创建该类的 5 个对象，并把这些对象放入 TreeSet 集合中（TreeSet 需使用泛型来定义）
4. 分别按以下两种方式对集合中的元素进行排序，并遍历输出：
   1). 使Employee 实现 Comparable 接口，并按 name 排序
   2). 创建 TreeSet 时传入 Comparator对象，按生日日期的先后排序。
 */
class EmployeeTest {
    // 需求1（自然排序）：使Employee 实现 Comparable 接口，并按 name 排序
    @Test
    public void test1() {
        /*
         * TreeSet 的特点是元素不重复，并会按照 Comparable 或 Comparator 维护排序结果。
         * 选择集合时可以先考虑需求：
         * 1. 只要求有序存放、允许重复：通常使用 List。
         * 2. 不允许重复、不关心顺序：通常使用 HashSet。
         * 3. 不允许重复并要求排序：通常使用 TreeSet。
         * 4. 按键值对保存数据：通常使用 Map。
         */
        TreeSet<Employee> set = new TreeSet<>();

        Employee e1 = new Employee("HanMeimei", 18, new MyDate(1998, 12, 21));
        Employee e2 = new Employee("LiLei", 20, new MyDate(1996, 11, 21));
        Employee e3 = new Employee("LiHua", 21, new MyDate(2000, 9, 12));
        Employee e4 = new Employee("ZhangLei", 19, new MyDate(1997, 5, 31));
        Employee e5 = new Employee("ZhangYi", 23, new MyDate(2001, 11, 2));

        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);

        // 遍历
        Iterator<Employee> iterator = set.iterator();
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            System.out.println(employee);
            /*
            Employee{name='HanMeimei', age=18, birthday=1998年12月21日}
            Employee{name='LiHua', age=21, birthday=2000年9月12日}
            Employee{name='LiLei', age=20, birthday=1996年11月21日}
            Employee{name='ZhangLei', age=19, birthday=1997年5月31日}
            Employee{name='ZhangYi', age=23, birthday=2001年11月2日}
             */
        }
    }

    // 需求2（定制排序）：创建 TreeSet 时传入 Comparator对象，按生日日期的先后排序。
    @Test
    public void test2() {
        /*
         * 现代代码中更常使用 Lambda 简化为：
         * Comparator<Employee> comparator =
         *         (o1, o2) -> o1.getBirthday().compareTo(o2.getBirthday());
         * 或使用比较器组合：
         * Comparator<Employee> comparator =
         *         Comparator.comparing(Employee::getBirthday);
         */
        Comparator<Employee> comparator = new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                /*
                 * 例如同一年份的“5月”和“12月”会先比较月份部分的字符 '5' 和 '1'，
                 * 因而可能错误地认为“12月”排在“5月”之前。除非字符串使用固定宽度且格式为
                 * yyyy-MM-dd，否则不应依赖日期的显示文本进行时间先后比较。
                 */
//                return o1.getBirthday().toString().compareTo(o2.getBirthday().toString());

                // 写法1
//                int yearDistance = o1.getBirthday().getYear() - o2.getBirthday().getYear();
//                if (yearDistance != 0) {
//                    return yearDistance;
//                }
//
//                int monthDistance = o1.getBirthday().getMonth() - o2.getBirthday().getMonth();
//                if (monthDistance != 0) {
//                    return monthDistance;
//                }
//
//                return o1.getBirthday().getDay() - o2.getBirthday().getDay();

                // 写法2
                /*
                 * 写法1由 Employee 的比较器重复实现日期比较规则，代码较长，并且日期规则变化时需要多处修改。
                 * 写法2复用 MyDate.compareTo()，职责更清楚，也更容易维护，因此这里更推荐写法2。
                 */
                return o1.getBirthday().compareTo(o2.getBirthday());
            }
        };

        TreeSet<Employee> set = new TreeSet<>(comparator);

        Employee e1 = new Employee("HanMeimei", 18, new MyDate(1998, 12, 21));
        Employee e2 = new Employee("LiLei", 20, new MyDate(1996, 11, 21));
        Employee e3 = new Employee("LiHua", 21, new MyDate(2000, 9, 12));
        Employee e4 = new Employee("ZhangLei", 19, new MyDate(1996, 5, 31));
        Employee e5 = new Employee("ZhangYi", 23, new MyDate(2000, 9, 2));

        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);

        // 遍历
        Iterator<Employee> iterator = set.iterator();
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            System.out.println(employee);
            /*
            Employee{name='ZhangLei', age=19, birthday=1996年5月31日}
            Employee{name='LiLei', age=20, birthday=1996年11月21日}
            Employee{name='HanMeimei', age=18, birthday=1998年12月21日}
            Employee{name='ZhangYi', age=23, birthday=2000年9月2日}
            Employee{name='LiHua', age=21, birthday=2000年9月12日}
             */
        }
    }
}

/**
 * 案例2
 */
class Exer02 {
    public static void main(String[] args) {
        // （1）创建一个ArrayList集合对象，并指定泛型为<Integer>
        ArrayList<Integer> list = new ArrayList<>();

        // （2）添加5个[0,100)以内的随机整数到集合中
        /*
         * Math.random() 写法可以实现目标。如果需要更明确地生成指定范围内的整数，
         * 可以考虑使用 Random.nextInt(100) 或 ThreadLocalRandom.current().nextInt(100)。
         */
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * 100);
            list.add(random);
        }

        // （3）使用foreach遍历输出5个整数
        for (Integer i : list) {
            System.out.println(i);
        }

        // （4）使用集合的removeIf方法删除偶数，为Predicate接口指定泛型<Integer>
        /**
         * Predicate<Integer> 表示一个接收 Integer 并返回 boolean 的判断规则。
         * removeIf() 会删除所有让 Predicate.test() 返回 true 的元素。
         */
        /*
         * Predicate 匿名内部类可以使用 Lambda 简化为：
         * list.removeIf(integer -> integer % 2 == 0);
         * 也可以进一步使用更简短的参数名，但应保证含义仍然清楚。
         */
        list.removeIf(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                /*
                 * test() 需要返回 boolean，表达当前元素是否满足删除条件。
                 * integer % 2 == 0 在元素为偶数时得到 true，因此 removeIf() 会删除该元素；
                 * 在元素为奇数时得到 false，因此保留该元素。
                 *
                 * 这里 Integer 会自动拆箱为 int，再执行取余运算。当前列表没有 null；
                 * 如果列表中存在 null，这个表达式会因自动拆箱而抛出 NullPointerException。
                 */
                return integer % 2 == 0;
            }
        });

        // （5）再使用Iterator迭代器输出剩下的元素，为Iterator接口指定泛型<Integer>
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            System.out.println(i);
        }
    }
}
