package chapter18;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class VarTest {
    /*
     * var = 局部变量类型推断（Local-Variable Type Inference）
     * 写法：var 变量名 = 初始化表达式;  编译器根据【右边表达式的静态类型】推断出左边变量的类型，
     * 并把推断结果直接写进 .class 文件。也就是说：源码里省略了类型，字节码里类型还是写死的。
     *
     * 类型一旦推断出来就固定，之后不能再赋值为别的类型。
     * 例：var i = 10; i = "abc";  // 编译错误，i 已经是 int
     *
     * var 不是关键字，而是"保留类型名"(reserved type name)。所以老代码里
     * int var = 1;  仍然合法；但不能再有 class var {} 这样的类名。
     *
     * 必须有初始化表达式，而且不能是 null（null 没有具体类型，推不出来）。
     *
     * 只能用于【局部变量】：方法体内的变量、for 的索引、增强 for 的迭代变量、
     * try-with-resources 的资源变量。
     */

    /**
     * 局部变量类型推断（JDK10的新特性）
     */
    // 可以使用的场景
    @Test
    public void test1() {
        // 1.局部变量的实例化
        /*
         * list 被推断为 ArrayList<String>，set 被推断为 LinkedHashSet<Integer>。
         *
         * var 和菱形语法 <> 不能一起省。
         * var bad = new ArrayList<>(); // 推断成 ArrayList<Object>
         * 用 var 时，右边的泛型实参必须显式写出来。
         */
        var list = new ArrayList<String>();
        var set = new LinkedHashSet<Integer>();

        // 2.增强for循环中的索引
        //  v 被推断为 String
        for (var v : list) {
            System.out.println(v);
        }

        // 3.传统for循环中
        // i 被推断为 int
        for (var i = 0; i < 100; i++) {
            System.out.println(i);
        }

        // 4. 返回值类型含复杂泛型结构
        // iterator 被推断为 Iterator<Integer>
        var iterator = set.iterator();

        HashMap<String, Integer> map = new HashMap<>();
        // entrySet 的推断结果是 Set<Map.Entry<String, Integer>>
        // map 是 HashMap<String,Integer>，entrySet() 返回 Set<Map.Entry<K,V>>
        // ->Set<Map.Entry<String,Integer>>。
        var entrySet = map.entrySet();
    }

    /**
     * var "不能用"的场景
     */
    /*
     * 声明一个成员变量（var i / var j = 0）
     * 没有初始化的方法内的局部变量声明（var i）
     * var i;   -> 两处违规：① 它在类体里，是成员变量；② 没有初始化表达式。
     * var j=0; -> 有初始化值，但仍在类体里，是成员变量，照样不允许。
     */
//    var i;
//    var j = 0;

    @Test
    public void test2() {
        // 右边 new int[]{...} 是完整表达式，类型明确是 int[]，arr 被推断为 int[]。
        var arr = new int[]{1, 2, 3};

        // 声明一个数组变量，并为数组静态初始化（省略new的情况下）
        // {1,2,3} 只是数组初始化器，不是表达式，它的元素类型要由左边声明决定，var 没法提供。
//        var arr1 = {1,2,3};

        // var 后面不能再跟 []，因为维度信息也必须由推断得出，不能手写。
//        var[] arr2 = new int[]{1,2,3};
    }

    /*
     * 方法的返回值类型（public var method）
     * 方法的参数类型（var m）
     * ① public var method(...)  返回值类型用 var -> 非法，签名必须类型明确。
     * ② var m 作为形参          -> 非法，形参没有初始化表达式可推断。
     * ③ var i = 0; 在方法体内    -> 这一行本身合法，但它与形参 int i 同名，
     *                             局部变量重复定义，同样编译错误。
     */
//    public var method(int i,var m){
//        var i = 0;
//    }

    @Test
    public void test3() {
//        try{
//            System.out.println(10 / 0);
        // 作为catch块中异常类型（catch (var e)）
        /*
         * catch 的参数不是"赋值初始化"，而是由 JVM 按类型匹配分派异常，
         * 类型是"输入条件"而非"推断结果"；多 catch (A | B e) 更没法推。
         */
//        } catch (var e) {
//            e.printStackTrace();
//        }

        // 不能用 var 接收 Lambda 表达式
        /*
         * Lambda 是 poly expression（多态表达式），它自己没有独立类型，
         * 必须靠左边的目标类型（Comparator<String>？BiFunction？）才能确定，
         * 而 var 恰好把左边类型抹掉了，于是互相等待、无法推断。
         */
//        var com = (s1,s2)-> s1.compareTo(s2);

        // 不能用 var 接收方法引用
//        var f = String::compareTo;
    }
}
