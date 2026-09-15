package chapter13;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomGenericExer {
}

/**
 * 案例1
 */
// 定义泛型类 DAO3<T>，在其中定义一个 Map 成员变量，Map 的键为 String 类型，值为 T 类型。
class DAO3<T> {
    /*
     * Map 是"键值对"结构，本身就有两个泛型：Map<K, V>，
     *    第一个是键(key)类型、第二个是值(value)类型。顺序由题目决定（键 String、值 T）。
     *    所以只要用 Map 的泛型，就一定要写两个类型参数，不能只写一个。
     * 声明 ≠ 初始化：Map<String, T> map; 只是"声明"了一个成员变量，
     *    它的默认值是 null（引用类型成员变量默认就是 null），并没有指向任何真实对象。
     *    "初始化"是指给它赋一个实际的对象（例如 new HashMap<>()）。
     * 不初始化的后果：编译能通过（语法没错），但运行时一旦调用 map.put / map.get，
     *    就会对 null 调用方法，抛出 NullPointerException(空指针异常)。
     *    所以 save、DAO3Test 里的 save 都会在运行时报 NPE，而不是编译报错。
     */
//    Map<String, T> map;

    /*
     * 写了 = new HashMap<>() 就完成了初始化。
     * 为什么用 new HashMap：Map 是"接口"，接口不能直接 new，必须 new 一个它的实现类，
     * HashMap 就是最常用的实现类。所以是 new HashMap<>() 而不是 new Map<>()。
     */
    Map<String, T> map = new HashMap<>();

    /**
     * 保存 T 类型的对象到 Map 成员变量中
     */
    public void save(String id, T entity) {
        // 把"键 id → 值 entity"这条数据存进 Map。
//        map.put(id, entity);

        /*
         * ! 是逻辑非，作用于它后面的那个布尔表达式。
         * map.containsKey(id) 整体返回一个 boolean，! 直接对这个结果取反即可，无需加括号。
         * 但如果要对"复合条件"整体取反，就必须加括号：
         *   !(a && b)  // 取反整个 a && b
         *   !a && b    // 只取反了 a，含义完全不同
         */
        if (!map.containsKey(id)) {
            map.put(id, entity);
        }
    }

    /**
     * 从 map 中获取 id 对应的对象
     */
    public T get(String id) {
        /*
         * map.get(id) 会返回该 key 对应的 value（类型正好是 T），直接返回即可；
         * 如果 id 不存在，HashMap 的 get 会返回 null（不会报错）。
         */
        return map.get(id);
    }

    /**
     * 替换 map 中key为id的内容,改为 entity 对象
     */
    public void update(String id, T entity) {
        /*
         * save   → if (!containsKey) 才放：只在"不存在"时新增，已存在则不动；
         * update → if (containsKey)  才放：只在"已存在"时覆盖，不存在则不动。
         */
        if (map.containsKey(id)) {
            map.put(id, entity);
        }
    }

    /**
     * 返回 map 中存放的所有 T 对象
     */
    /*
     * 本方法要返回"所有 value 组成的集合"，而 map.values() 返回的类型是
     * Collection（不是 List），所以下面才需要把它转成 List 再返回。
     */
    public List<T> list() {
        /*
         * map.values() 返回 Map 里所有"值"的一个 Collection 视图（不含 key）。
         * 目的就是把所有 value 取出来，最终整理成 List 返回。
         */
//        Collection<T> values = map.values();
        /*
         * return values 编译报错：方法要求返回 List<T>，但 values 是 Collection<T>，
         *    Collection 是 List 的父接口，父类型不能自动当子类型返回（类型不匹配）。
         * 强转 (List<T>) values：编译能过（语法允许向下转型），但运行时会抛
         *    ClassCastException —— 因为 map.values() 的真实类型是 HashMap 内部的 Values 类，
         *    它并不是 ArrayList，也没实现 List 接口。
         * 强转成功的前提确实是"对象的真实类型是目标类型或其子类型"，
         *    即 values instanceof List 为 true 才安全；这里它为 false，所以崩。
         */
//        return (List<T>) values;

        // 方法1
        /*
         * 强转是"骗编译器把 Collection 当成 List"，对象本身没变，运行时会崩；
         * 而这里是"真正 new 了一个 ArrayList（它确实是 List），再把元素复制进去"，
         * 返回的是货真价实的 List，所以安全可用。
         */
//        Collection<T> values = map.values();
//        ArrayList<T> list = new ArrayList<>();
//        list.addAll(values);
//        return list;

        // 方法2
        Collection<T> values = map.values();
        /*
         * ArrayList 提供了一个构造器 ArrayList(Collection<? extends E> c)，
         * 它会在创建时就把传入集合里的所有元素复制进新列表。
         * 作用等价于"先 new ArrayList，再 addAll(values)"，所以完全可以代替方法1，而且更简洁，推荐用方法2。
         */
        ArrayList<T> list = new ArrayList<>(values);
        return list;
    }

    /**
     * 删除指定 id 对象
     */
    public void delete(String id) {
        map.remove(id);
    }
}

/*
定义一个 User 类：
该类包含：private成员变量（int类型） id，age；（String 类型）name。
 */
class User {
    private int id;
    private int age;
    private String name;

    public User() {
    }

    public User(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id && age == user.age && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, name);
    }
}

/*
定义一个测试类：
创建 DAO3 类的对象， 分别调用其 save、get、update、list、delete 方法来操作 User 对象，
使用 Junit 单元测试类进行测试。
 */
class DAO3Test {
    public static void main(String[] args) {
        // 本次要用 DAO 存取 User 对象，所以把 T 指定为 User，之后所有方法都按 User 类型工作。
        DAO3<User> dao = new DAO3<>();

        dao.save("1001", new User(1, 33, "Tom"));
        dao.save("1002", new User(2, 28, "Jerry"));

        dao.update("1002", new User(3, 27, "Jack"));

        dao.delete("1002");

        List<User> list = dao.list();
        for (User user : list) {
            System.out.println(user);
        }
    }
}

/**
 * 案例2：泛型方法
 */
class Exer2 {
    // 编写一个泛型方法，实现任意引用类型数组指定位置元素交换
    public static <E> void method1(E[] e, int a, int b) {
        // e 是一个数组，e[a] 表示访问数组中下标为 a 的那个元素，方括号里放的是"索引(下标)"。
        E temp = e[a];
        e[a] = e[b];
        e[b] = temp;
    }

    @Test
    public void test1() {
        // 泛型方法可接受任意引用类型数组，这里只是随便挑 String 数组来测试
        String[] arr = new String[]{"AA", "BB", "CC"};
        method1(arr, 0, 2);
        /*
         * 数组直接打印（如 System.out.println(arr)）只会显示地址，形如 [Ljava.lang.String;@1b6d3586。
         * Arrays.toString(arr) 会把数组格式化成 [AA, BB, CC] 这样可读的字符串，所以要用它。
         */
        System.out.println(Arrays.toString(arr));
    }

    // 编写一个泛型方法，接收一个任意引用类型的数组，并反转数组中的所有元素
    public static <E> void method2(E[] e) {
        /*
         * "双指针反转"：i 从头(0)、j 从尾开始，两两交换后同时向中间靠拢。
         * j 要 -1 是因为数组下标从 0 到 length-1，最后一个元素的下标是 length-1，不是 length（否则越界）。
         */
        for (int i = 0, j = e.length - 1; i < j; i++, j--) {
            E temp = e[i];
            e[i] = e[j];
            e[j] = temp;
        }
    }

    @Test
    public void test2() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6};
        method2(arr);
        System.out.println(Arrays.toString(arr));
    }
}

/**
 * 案例3
 */
/*
我们要声明一个学生类，该类包含姓名、成绩。
语文老师希望成绩是“优秀”、“良好”、“及格”、“不及格”，
数学老师希望成绩是89.5, 65.0，
英语老师希望成绩是'A','B','C','D','E'。
 */
class Student<T> {
    private String name;
    // T 只能代表引用类型，基本类型要用包装类；且静态成员/静态方法不能用类的 T
    private T score;

    public Student() {
    }

    public Student(String name, T score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getScore() {
        return score;
    }

    public void setScore(T score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student<?> student = (Student<?>) o;
        return Objects.equals(name, student.name) && Objects.equals(score, student.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }
}

class StudentTest {
    public static void main(String[] args) {
        Student<String> s1 = new Student<>("Tom", "良好");
        Student<Double> s2 = new Student<>("Jerry", 87.5);
        // Character 是基本类型 char 的"包装类"，用来把一个字符包装成对象（泛型里必须用引用类型）
        Student<Character> s3 = new Student<>("Rose", 'A');

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
    }
}
