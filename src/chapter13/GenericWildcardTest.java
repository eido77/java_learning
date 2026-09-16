package chapter13;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericWildcardTest {
    /*
    1. 通配符: ?
    2. 使用说明：
    > 举例：ArrayList<?>
    > G<?> 可以看做是G<A>类型的父类，即可以将G<A>的对象赋值给G<?>类型的引用（或变量）
    3. 读写数据的特点(以集合ArrayList<?>为例说明)
    > 读取数据：允许的，读取的值的类型为Object类型
    > 写入数据：不允许的。特例：写入null值。
    4. 有限制条件的通配符
       List<? extends A> : 可以将List<A>或List<B>赋值给List<? extends A>。其中B类是A类的子类。
       List<? super A> :可以将List<A>或List<B>赋值给List<? extends A>。其中B类是A类的父类。

     * 前提：所有讨论都建立在“泛型没有多态”这个前提上。即 ArrayList<Object> 不是 ArrayList<String> 的父类。
     * 1) List<?>（无限制通配符）
     *    - ? 的范围：任意类型，从 负无穷（任意子类） 到 正无穷（任意父类，最顶是 Object）。
     *    - List<任意类型> 都能赋给 List<?>。
     *    - 读：只能当作 Object 读（因为编译器不知道具体是什么，只能保证“至少是 Object”）。
     *    - 写：除 null 外都不能写（编译器不知道你写的东西符不符合那个未知类型）。
     * 2) List<? extends A>（上界通配符，upper bound）
     *    - ? 的范围：A 以及 A 的所有子类。用数轴表示就是【负无穷 ~ A】，A 是“上限/天花板”。
     *    - 能赋值的：List<A> 和 List<A的子类>。List<A的父类> 不行。
     *    - 读：可以读，读出来最“大”能保证是 A，所以能用 A 类型接（get 返回 A）。
     *    - 写：除 null 外都不能写。因为 ? 可能是 A 的任意某个子类，你写的对象无法保证匹配。
     *    - extends → 适合“取数据/读”（生产者 Producer）。
     * 3) List<? super A>（下界通配符，lower bound）
     *    - ? 的范围：A 以及 A 的所有父类，直到 Object。用数轴表示就是【A ~ 正无穷】，A 是“下限/地板”。
     *    - 能赋值的：List<A> 和 List<A的父类>。List<A的子类> 不行。
     *    - 读：只能当作 Object 读（因为 ? 可能是 A 的任意父类，只有 Object 是所有父类的共同上界）。
     *    - 写：可以写 A 以及 A 的子类（因为它们一定 “is-a” 那个未知的父类）。
     *    - super → 适合“存数据/写”（消费者 Consumer）。
     * PECS 口诀：Producer-Extends, Consumer-Super。
     *    要从集合里读取 → 用 extends；要往集合里写入 → 用 super。
     */
    @Test
    public void test1() {
        // 1. 演示“普通引用”的多态：子类对象可以赋给父类引用（String 是 Object 的子类）。
        Object obj = null;
        String str = "AA";

        // 基于继承性的多态的使用
        /*
         * String 是 Object 的子类，子类对象天然“is-a”父类，可以直接赋给父类引用。
         * 要求：右边的类型必须是左边类型的子类（或同类型）。这就是普通对象的多态
         * 注意：这只对“普通对象”成立，泛型参数之间没有这种多态（见 test2）。
         */
        obj = str;

        // 2.
        Object[] arr = null;
        String[] arr1 = null;

        /*
         * “数组的协变(covariance)”：因为 String 是 Object 的子类，Java 规定 String[] 也是 Object[] 的子类。
         * 所以 String[] 能赋给 Object[]。这一点和泛型正好相反（数组协变，泛型不协变）。
         * 数组协变其实不安全，如 Object[] o = new String[3]; o[0]=123; 编译能过但运行时会 ArrayStoreException。
         * Java 引入泛型通配符，正是为了在编译期就堵住这类问题。
         */
        arr = arr1;
    }

    /**
     * 类SuperA是类A的父类，则G<SuperA> 与 G<A>的关系：G<SuperA> 和 G<A>是并列的两个类，没有任何子父类的关系。
     * 比如：ArrayList<Object> 、ArrayList<String>没有关系
     */
    @Test
    public void test2() {
        /*
         * 虽然两者“容器”都是 ArrayList，但泛型不同就是完全不同的类型。
         * ArrayList<Object> 和 ArrayList<String> 之间没有父子关系（泛型不具备多态）。
         */
        ArrayList<Object> list1 = null;
        ArrayList<String> list2 = null;

        /*
         * 比较的是“泛型参数”，即使 String 是 Object 子类，ArrayList<String> 也不是 ArrayList<Object> 的子类。
         * 泛型故意设计成“不协变”，就是为了防止 test1 里数组那种运行期崩溃。下面的反证法解释了为什么不能这么设计。
         */
//        list1 = list2;

        /*
         * 反证法：
         * 第一步：假设 list1 = list2 合法，那么 list1 和 list2 现在指向同一个 ArrayList 对象。
         * 第二步：list2 声明为 <String>，可以 add("AA")；list1 声明为 <Object>，可以 add(123)。
         * 第三步：于是同一个底层集合里既有 String 又有 Integer(123)。
         * 第四步：list2.get(...) 编译器认为一定返回 String，却取到了 Integer 123，赋给 String 会类型错误。
         */
//        list2.add("AA");
//        list1.add(123);
        // 相当于取出的123赋值给了str，错误的
//        String str = list2.get(1); // NullPointerException

        /*
         * 下面的 method(ArrayList<Object> list) 形参写死了是 ArrayList<Object>。
         * 传参本质也是赋值：list1 是 ArrayList<Object>，类型完全一致，能传；
         * list2 是 ArrayList<String>，和 ArrayList<Object> 不是父子关系，所以不能传（和上面 list1=list2 是同一个道理）
         */
        method(list1);
//        method(list2);
    }

    @Test
    public void test3() {
        Person2<Object> per = null;
        Person2<String> per1 = null;

        /*
         * 泛型参数不同 = 不同类型，且泛型不具备多态。
         * Person2<Object> 和 Person2<String> 是并列的两个类型，互不为父子，所以 per = per1 报错。
         * 说明：这个规则不只对集合成立，对“任何自定义泛型类”都成立。test2 用的是 ArrayList，这里用的是自定义 Person2，本质一致。
         */
//        per = per1;
    }

    public void method(ArrayList<Object> list) {
    }

    /**
     * 类SuperA是类A的父类或接口，SuperA<G> 与 A<G>的关系：SuperA<G> 与A<G> 有继承或实现的关系。
     * 即A<G>的实例可以赋值给SuperA<G>类型的引用（或变量）
     * 比如：List<String> 与 ArrayList<String>
     */
    @Test
    public void test4() {
        List<String> list1 = null;
        ArrayList<String> list2 = new ArrayList<>();

        /*
         * 泛型参数相同(<String> 都一样)，只是“容器类型”不同：ArrayList 实现了 List 接口，所以 ArrayList<String> 是 List<String> 的子类型。
         * 这属于“容器本身的继承关系”，与泛型多态无关，是允许的。
         * 泛型参数一致时，容器的父子关系照样成立（ArrayList<String> → List<String> 可赋值）。
         */
        list1 = list2;

        list1.add("AA");

        // 传参给下面的 method2(List<String>)。
        // list2 是 ArrayList<String>，是 List<String> 的子类型，所以能传。
        method2(list2);
    }

    public void method2(List<String> list) {
        /*
         * 这个方法的形参是 List<String>，并不是通配符 ?，元素类型是确定的 String。
         * 所以这里完全可以写 for (String s : list)，用 Object 只是“能用但不精确”。
         * 用 Object 能过，是因为任何元素都是 Object 的子类（向上兼容）。
         */
        for (Object obj : list) {
            System.out.println(obj);
        }

        list.add("AA");

        // List<String> 里 add("AA") 和 add(null) 都合法；只有在 List<?> 里才“只能 add(null)”。
        list.add(null);
    }

    /**
     * 测试：通配符?的使用
     */
    @Test
    public void test5() {
        List<?> list = null;
        List<Object> list1 = null;
        List<String> list2 = null;

        /*
         * List<?> 可以看成“所有 List<具体类型> 的公共父类”。
         * 所以不管是 List<Object> 还是 List<String>，都能赋给 List<?>。这正是通配符 ? 的用途：兼容各种泛型的 List。
         */
        list = list1;
        list = list2;

        /*
         * 传参本质是赋值。下面 method1 的形参是 List<?>，能接收任意 List<具体类型>。
         * 所以 List<Object>、List<String> 都能传进去。
         */
        method1(list1);
        method1(list2);
    }

    /*
     * 用 List<?> 作形参，可以接收任意元素类型的 List（List<String>、List<Integer>...都行）。
     * 适用场景：方法只需要遍历/读取，不关心也不需要写入具体元素类型时，就用 List<?>。
     */
    public void method1(List<?> list) {
    }

    @Test
    public void test6() {
        List<?> list = null;
        List<String> list1 = new ArrayList<>();

        list1.add("AA");
        // List<?> 是所有 List<具体类型> 的公共父类，List<String> 可以赋给 List<?>。
        list = list1;

        // 读取数据（以集合为例说明）
        /*
         * 通过 list（List<?>）读取时，编译器已经“忘记”了具体类型是什么，只知道“至少是 Object”。
         * 所以 get 的返回类型被当作 Object，不能直接赋给 String（除非强制转换）。
         */
//        String str = list.get(0);
        // 只能用 Object 接。因为 ? 是未知类型，Object 是所有类型的共同父类，是唯一安全的接收类型。
        Object obj = list.get(0);
        System.out.println(obj); // AA

        // 写入数据（以集合为例说明），操作失败。
        /*
         * ? 用在“声明/形参”里（如 List<?> list、method1(List<?> list)），表示“元素类型未知但确定存在”。
         * ? 不能出现在 add 的实参位置，list.add(?) 这种写法本身就是语法错误。
         */
//        list.add(?);
//        list.add("BB");

        // 特例：可以将null写入集合中
        /*
         * List<?> 写入时只能 add(null)。
         * null 可以赋给任何引用类型，无论 ? 实际是什么类型，写 null 都安全，所以是唯一例外。
         */
        list.add(null);
    }

    /**
     * 测试：有限制条件的通配符的使用
     */
    /*
     * 带限制的通配符就分 extends A（上界）和 super A（下界）两种。
     *   - test7    ：extends —— 赋值规则（哪些能赋给 List<? extends Father>）
     *   - test7_1  ：extends —— 读写规则（能读不能写）
     *   - test8    ：super   —— 赋值规则（哪些能赋给 List<? super Father>）
     *   - test9_1  ：super   —— 读写规则（能写受限、读只能 Object）
     */
    // 【test7｜extends 上界通配符——赋值规则演示】
    @Test
    public void test7() {
        /*
         * List<? extends Father> 里的 ? 只能是 “Father 或 Father 的子类”，即 ? ≤ Father。
         * 所以能赋给它的，必须是 List<Father> 或 List<Father的子类>。
         * 下面：list1 是 List<Object>，Object 是 Father 的“父类”，超出上界(比 Father 还大)，所以【不可以】。
         *      list2 是 List<Father>，正好等于上界，可以；
         *      list3 是 List<Son>，Son 是 Father 子类，在范围内，可以。
         */
        List<? extends Father> list = null;
        List<Object> list1 = null;
        List<Father> list2 = null;
        List<Son> list3 = null;

//        list = list1;
        list = list2;
        list = list3;
    }

    // 【test7_1｜extends 上界通配符——读写规则演示（能读、不能写）】
    @Test
    public void test7_1() {
        List<? extends Father> list = null;
        List<Father> list1 = new ArrayList<>();

        list1.add(new Father());
        // list1 是 List<Father>，Father 在 “≤Father” 的上界范围内，所以能赋给 List<? extends Father>。
        list = list1;

        // 读取数据
        /*
         * 因为 ? 无论是 Father 还是 Father 的哪个子类，取出来的元素“一定 is-a Father”。
         * 所以编译器能保证 get 的返回值至少是 Father，可以安全地用 Father 接。这就是 extends 适合“读”的原因。
         */
        Father father = list.get(0);

        // 写入数据
        list.add(null);
        /*
         * ? 的具体类型未知（可能是 Father，也可能是 Son 或别的子类）。
         * Order 和 Father 没有继承关系，肯定不匹配那个未知类型，所以不能写。
         */
//        list.add(new Order1());

        /*
         * extends 下，编译器不知道 ? 到底是哪个子类，写入任何“具体对象”都不安全。
         * 如果 list 实际指向 List<Son>，你却 add(new Father())，就会把父类塞进子类集合，破坏类型安全。
         * 所以哪怕写 Father、Son 都不允许 —— 因为编译器无法确认它们等于那个未知的 ?。唯一能写的只有 null。
         */
//        list.add(new Father());
//        list.add(new Son());
    }

    // 【test8｜super 下界通配符——赋值规则演示】
    @Test
    public void test8() {
        /*
         * List<? super Father> 里的 ? 只能是 “Father 或 Father 的父类”，即 ? ≥ Father。
         * 能赋给它的必须是 List<Father> 或 List<Father的父类>。
         * 下面：list1 是 List<Object>，Object 是 Father 的父类，在 “≥Father” 范围内，可以；
         *      list2 是 List<Father>，正好等于下界，可以；
         *      list3 是 List<Son>，Son 是 Father 的“子类”，比下界 Father 还小，超出范围，所以【不可以】。
         */
        List<? super Father> list = null;
        List<Object> list1 = null;
        List<Father> list2 = null;
        List<Son> list3 = null;

        list = list1;
        list = list2;
//        list = list3;
    }

    // 【test9_1｜super 下界通配符——读写规则演示（能写受限、读只能 Object）】
    // 读写规律：能写（可写 Father 及其子类），读只能用 Object 接。
    @Test
    public void test9_1() {
        List<? super Father> list = null;
        List<Father> list1 = new ArrayList<>();

        list1.add(new Father());

        /*
         * list1 是 List<Father>，Father 等于下界 Father
         * 在 “≥Father” 范围内，所以能赋给 List<? super Father>。
         */
        list = list1;

        // 读取数据
        /*
         * super（? ≥ Father）：? 可能是 Father，也可能是它的任意父类（如 Object）。
         * 取出来的元素只能保证“至少是 Object”（Object 是所有父类的共同上界），所以只能用 Object 接。
         */
        Object o = list.get(0);

        // 写入数据
        list.add(null);
//        list.add(new Order1());
        list.add(new Father());
        list.add(new Son());
    }
}

class Person2<T> {
    String name;
    T t;
}

class Father {
}

class Son extends Father {
}

class Order1 {
}
