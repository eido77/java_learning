package chapter11;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

public class CompareTest {
    /*
    1.实现对象的排序，可以考虑两种方法：自然排序、定制排序
     * - 自然排序 = 实现 Comparable 接口，重写 compareTo(Object)。
     *   它是"类天生自带的、唯一的、默认的"排序规则，写死在类内部。
     *   适用：这个类有一个最主要、最常用的排序标准（如 String 按字典序、Integer 按数值）。
     * - 定制排序 = 实现 Comparator 接口，重写 compare(Object, Object)。
     *   它是"外部临时提供的"排序规则，可以随时换一套。
     *   适用：①这个类没有实现 Comparable，又想排序；②这个类虽然有自然排序，
     *   但想按别的标准排（比如临时按名字、临时按价格倒序）。
     *
     * - 现在（JDK8+）实际开发几乎不再手写匿名内部类，而是用 Lambda / 方法引用 + Comparator 的静态方法，
     *   例如：Comparator.comparingDouble(Product::getPrice).reversed()。
    2. 方式一：实现Comparable接口的方式
    实现步骤：
    ① 具体的类A实现Comparable接口
    ② 重写Comparable接口中的compareTo(Object obj)方法，在此方法中指明比较类A的对象的大小的标准
    ③ 创建类A的多个实例，进行大小的比较或排序。
    3. 方式二：实现Comparator接口的方式
    实现步骤：
    ① 创建一个实现了Comparator接口的实现类A
    ② 实现类A要求重写Comparator接口中的抽象方法compare(Object o1,Object o2)，在此方法中指明要
       比较大小的对象的大小关系。（比如，String类、Product类）
    ③ 创建此实现类A的对象，并将此对象传入到相关方法的参数位置即可。（比如：Arrays.sort(..,类A的实例)）
    4. 对比两种方式：
    角度一：
        自然排序：单一的，唯一的
        定制排序：灵活的，多样的
    角度二：
        自然排序：一劳永逸的
        定制排序：临时的
    角度三：细节
        自然排序：对应的接口是Comparable，对应的抽象方法compareTo(Object obj)
        定制排序：对应的接口是Comparator，对应的抽象方法compare(Object obj1,Object obj2)
     */
}

class ComparableTest {
    @Test
    public void test1() {
        String[] arr = new String[]{"Tom", "Jerry", "Rose", "Jack", "Lucy"};

        /*
         * Arrays.sort 对"对象数组"用的是 TimSort
         * （归并排序 + 插入排序的优化混合体，JDK 内部实现，稳定排序）；
         * 对"基本类型数组"用的是双轴快速排序（Dual-Pivot QuickSort）。
         */
        Arrays.sort(arr);

        // 遍历
        for (int i = 0; i < arr.length; i++) {
            /*
             * String 能直接排序，是因为 String 类本身已经实现了 Comparable 接口，
             * 内置了 compareTo()，规则是"按字典序"：逐个字符比较其 Unicode 编码值大小。
             */
            System.out.println(arr[i]);
        }
    }

    @Test
    public void test2() {
        Product[] arr = new Product[5];

        arr[0] = new Product("iPhone", 9999);
        arr[1] = new Product("小米", 4999);
        arr[2] = new Product("vivo", 5999);
        arr[3] = new Product("OPPO", 2999);
        arr[4] = new Product("三星", 8999);

        /*
         * 这里能直接 sort，是因为下面 Product 实现了 Comparable 并重写了 compareTo，
         * 即"用的是 Product 的自然排序规则"（价格从高到低，价格相同再按名字）。
         */
        Arrays.sort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}

/*
 * Arrays.sort(数组) 这种单参数版本，要求元素必须是 Comparable 类型，
 * 否则运行时会抛 ClassCastException。所以自定义类想用自然排序，
 * 就必须 implements Comparable 并重写 compareTo。
 */
class Product implements Comparable { // 商品类
    private String name; // 商品名称
    private double price; // 价格

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    /*
     * 当前的类需要实现Comparable中的抽象方法：compareTo(Object o)
     * 在此方法中，指明如何判断当前类的对象的大小。比如：按照价格的高低进行大小的比较。（或从低到高排序）
     *
     * 如果返回值是正数：当前对象大。
     * 如果返回值是负数：当前对象小。
     * 如果返回值是0，一样大。
     */
//    @Override
//    public int compareTo(Object o) {
//        if (o == this) {
//            return 0;
//        }
//
    /*
     * 此时形参 o 的"编译期类型"是 Object，而不是 Product。
     * instanceof 只是"运行期检查"了它实际指向 Product，但编译器看的是声明类型 Object，
     * 不强转就没法调用 Product 特有的方法/字段（如 getPrice、price）。
     * 所以必须 (Product) o 把它的编译期类型也"告诉"编译器，才能访问 Product 的成员。
     */
//        if (o instanceof Product) {
//            Product p = (Product) o;
//
//            return Double.compare(this.price, p.price);
//        }
//
//        // 手动抛出一个异常类的对象
//        throw new RuntimeException("类型不匹配");
//    }

    // 比较的标准：先比较价格（从大到小），价格相同，进行名字的比较 （从小到大）
    @Override
    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        }

        if (o instanceof Product) {
            Product p = (Product) o;

            int value = Double.compare(this.price, p.price);

            if (value != 0) {
                /*
                 * 负号（取相反数）。
                 * Double.compare(this.price, p.price) 默认是"升序"：this 小返回负、this 大返回正。
                 * 加上负号相当于把大小关系反过来，从而实现"价格从高到低（降序）"。
                 */
                return -value;
            }

            // 价格相同时，退而按名字比较：String 自带 compareTo，天然升序（名字从小到大）
            return this.name.compareTo(p.name);
        }

        // 手动抛出一个异常类的对象
        throw new RuntimeException("类型不匹配");
    }
}

class ComparatorTest {
    @Test
    public void test1() {
        Product[] arr = new Product[5];

        arr[0] = new Product("iPhone", 9999);
        arr[1] = new Product("小米", 4999);
        arr[2] = new Product("vivo", 5999);
        arr[3] = new Product("OPPO", 2999);
        arr[4] = new Product("三星", 8999);

        // 创建一个实现了Comparator接口的实现类的对象
        /*
         * 这里用的是"匿名内部类"——没有单独写一个 class 去 implements Comparator，
         * 而是 new 接口的同时就地把方法体写出来。用完即弃，正是"定制排序=临时的"体现。
         */
        Comparator comparator = new Comparator() {
            // 如果判断两个对象o1,o2的大小，其标准就是此方法的方法体要编写的逻辑。
            // 比如：按照价格从高到低排序
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Product && o2 instanceof Product) {
                    Product p1 = (Product) o1;
                    Product p2 = (Product) o2;

                    return -Double.compare(p1.getPrice(), p2.getPrice());
                }

                /*
                 * 一旦传进来的不是 Product（类型不匹配），这个 compare 就没有合理的比较标准，
                 * 与其返回一个错误的大小结果导致排序结果诡异，不如主动抛异常"快速失败"，
                 * 让问题立刻暴露出来，方便定位。这是一种防御性编程。
                 */
                throw new RuntimeException("类型不匹配");
            }
        };

        /*
         * 建议（JDK8 更简洁的等价写法，实际开发常用）：
         *     Comparator<Product> comparator =
         *             Comparator.comparingDouble(Product::getPrice).reversed();
         * 一行搞定"按价格从高到低"，无需匿名内部类、无需强转。
         */
        Arrays.sort(arr, comparator);

        Comparator comparator1 = new Comparator() {
            // 如果判断两个对象o1,o2的大小，其标准就是此方法的方法体要编写的逻辑。
            // 比如：按照name从低到高排序
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Product && o2 instanceof Product) {
                    Product p1 = (Product) o1;
                    Product p2 = (Product) o2;

                    return p1.getName().compareTo(p2.getName());
                }

                throw new RuntimeException("类型不匹配");
            }
        };

        /*
         * 建议：等价 Lambda 写法 ->
         *     Comparator<Product> comparator1 = Comparator.comparing(Product::getName);
         */
        Arrays.sort(arr, comparator1);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    @Test
    public void test2() {
        String[] arr = new String[]{"Tom", "Jerry", "Rose", "Jack", "Lucy"};

        /*
         * 实际开发中，这段等价的现代写法是（推荐，最简洁）：
         *     Arrays.sort(arr, Comparator.reverseOrder());   // 逆字典序
         * 或
         *     Arrays.sort(arr, Comparator.<String>naturalOrder().reversed());
         */
        Arrays.sort(arr, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof String && o2 instanceof String) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;

                    return -s1.compareTo(s2);
                }

                throw new RuntimeException("类型不匹配");
            }
        });

        // 遍历
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
