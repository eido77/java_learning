package chapter18;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamAPITest {
    /*
     * Optional<T> 是 JDK8 提供的“容器类”，它内部要么装着一个非 null 的值，要么是空（Optional.empty()）。
     * 用类型本身强制你考虑“可能没有值”的情况，从而避免 NullPointerException。
     *
     *   get()                —— 有值返回值；空的直接抛 NoSuchElementException
     *   orElse(默认值)        —— 空时返回默认值，推荐
     *   orElseGet(供给型函数) —— 空时才去计算默认值，默认值构造开销大时用它
     *   orElseThrow(异常供给) —— 空时抛你指定的业务异常
     *   ifPresent(消费型函数) —— 有值才执行某段逻辑，不用自己判空
     *   isPresent()/isEmpty()—— 先判断再取
     * 应优先用 orElse / ifPresent。
     */
    /*
    1. Stream API  vs 集合框架
     * 集合里的元素是“已经存在”的；流里的元素是“按需流过去”的，流本身不存数据、用完即废。
    > Stream API 关注的是多个数据的计算（排序、查找、过滤、映射、遍历等），面向CPU的。
      集合关注的数据的存储，面向内存的。
    2. 使用说明
    ① Stream 自己不会存储元素。
    ② Stream 不会改变源对象。相反，他们会返回一个持有结果的新Stream。
    ③ Stream 操作是延迟执行的。这意味着他们会等到需要结果的时候才执行。即一旦执行终止操作，就执行中间操作链，并产生结果。
    ④ Stream 一旦执行了终止操作，就不能再调用其它中间操作或终止操作了。
    * 终止操作后面不能再接任何操作（流已被消费，再用抛 IllegalStateException）；
    * 没有终止操作，中间操作一行都不会真正执行（延迟执行/惰性求值）。
    3. Stream 执行流程
    步骤1：Stream的实例化
    步骤2：一系列的中间操作
    步骤3：执行终止操作
     */

    /**
     * 测试Stream的实例化
     */
    // 创建 Stream方式一：通过集合
    @Test
    public void test1() {
        List<Employee> list = EmployeeData.getEmployees();
        // default Stream<E> stream() : 返回一个顺序流
        /*
         * stream()：把集合中已有的元素作为数据源，返回一个顺序 Stream 对象。
         * 它本身不遍历集合，也不复制数据，只是建立一条数据处理流水线。
         * 真正的遍历和计算通常要等到执行终止操作时才发生。
         */
        Stream<Employee> stream = list.stream();

        // default Stream<E> parallelStream() : 返回一个并行流
        // parallelStream() 也是 Collection 的 default 方法，任何 Collection 实现类都能调用
        /*
         * parallelStream()：把集合中已有的元素作为数据源，返回一个并行 Stream 对象。
         * 它本身不遍历集合，也不复制数据，只是建立一条支持并行处理的流水线。
         * 真正的遍历和计算通常要等到执行终止操作时才发生。
         * 并行流可以利用多个线程处理元素，但不一定比顺序流更快，也不保证处理顺序。
         */
        Stream<Employee> stream1 = list.parallelStream();

        /*
         * stream()         —— 顺序流，单线程按顺序一个个处理元素；
         * parallelStream() —— 并行流，底层把数据切片丢给 ForkJoinPool.commonPool() 多线程同时处理。
         */
        System.out.println(stream); // java.util.stream.ReferencePipeline$Head@1677d1
        System.out.println(stream1); // java.util.stream.ReferencePipeline$Head@48fa0f47
    }

    // 创建 Stream方式二：通过数组
    @Test
    public void test2() {
        // 调用Arrays类的static <T> Stream<T> stream(T[] array): 返回一个流
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        Stream<Integer> stream = Arrays.stream(arr);

        int[] arr1 = new int[]{1, 2, 3, 4, 5};
        IntStream stream1 = Arrays.stream(arr1);
    }

    // 创建 Stream方式三：通过Stream的of()
    @Test
    public void test3() {
        /*
         * Stream.of(T... values)：把若干个已知的、零散的元素直接拼成一条流，
         * 省去先 new 一个数组或集合的步骤，最适合写测试/demo 数据。
         *
         * Stream.of(new int[]{1,2,3})      -> Stream<int[]>，只有 1 个元素（基本类型数组被当成一个整体对象）
         * Stream.of(new Integer[]{1,2,3})  -> Stream<Integer>，3 个元素（引用类型数组会被当成可变参数展开）
         * 想从 int[] 得到 3 个元素，必须用 Arrays.stream(arr) 或 IntStream.of(arr)
         */
        Stream<String> stream = Stream.of("AA", "BB", "CC", "SS", "DD");
    }

    /**
     * 测试Stream的中间操作
     */
    // 1-筛选与切片
    @Test
    public void test4() {
        // filter(Predicate p)：接收 Lambda，从流中“保留”满足条件的元素，返回一个新的 Stream（源集合完全不变）
        // 练习：查询员工表中薪资大于7000的员工信息
        List<Employee> list = EmployeeData.getEmployees();
        Stream<Employee> stream = list.stream();
        /*
         * filter() 的作用是：根据指定条件筛选元素，只保留满足条件的元素。
         * forEach() 的作用是：对流中的每个元素执行指定操作。
         * forEach 是终止操作，返回值是 void，不再是 Stream，后面没法再接任何东西，天然只能放末尾。
         *
         * void forEach(Consumer<? super T> action)：Stream 接口的方法
         * 需要的参数是 Consumer<Employee>，唯一抽象方法是 void accept(Employee t) —— “接收一个元素，不返回”。
         * System.out::println 不是“立刻执行打印”，它表示“println 这个动作”，被包装成了一个 Consumer 对象。
         *
         * 中间操作（filter/map/sorted…）如果后面没有任何终止操作，
         * 整条链一行代码都不会执行——这就是“延迟执行”。
         */
        stream.filter(emp -> emp.getSalary() > 7000).forEach(System.out::println);

        // limit(n) —— 截断流，使其元素不超过给定数量
        // 错误的。因为stream已经执行了终止操作，就不可以再调用其它的中间操作或终止操作了。
        // limit 是“短路操作”凑够 2 个满足条件的元素后，流就不再继续遍历后面的数据了，对大数据量很省事。
        /*
         * 因为 Stream 是“一次性消费品”，不是集合那样可以反复遍历的容器。
         * 上一行 stream.filter(...).forEach(...) 已经执行了终止操作，这个 stream 对象内部会被标记为
         * “linkedOrConsumed = true”（已被链接或已被消费）。
         * 此时再调用 stream.limit(2)，JDK 会直接抛运行时异常。
         * 正确做法：每次重新从源头创建一条新流，即再写一次 list.stream()。
         */
//        stream.limit(2).forEach(System.out::println);
        list.stream().filter(emp -> emp.getSalary() > 7000).limit(2).forEach(System.out::println);

        // skip(n) —— 跳过元素，返回一个扔掉了前 n 个元素的流。若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补
        /*
         * skip(n)：丢掉流最前面的 n 个元素，返回剩下元素组成的新流；元素不足 n 个则得到空流（不报错）。
         * skip 返回的仍是 Stream，是“中间操作”，
         * 不加终止操作的话整行代码什么都不会发生。forEach 在这里就是“触发执行 + 逐个打印”。
         */
        list.stream().skip(5).forEach(System.out::println);

        list.add(new Employee(1009, "马斯克", 40, 12500.32));
        list.add(new Employee(1009, "马斯克", 40, 12500.32));
        list.add(new Employee(1009, "马斯克", 40, 12500.32));
        list.add(new Employee(1009, "马斯克", 40, 12500.32));

        // distinct() —— 筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素
        /*
         * distinct()：中间操作，依据 hashCode() + equals() 去掉重复元素，返回新流，源集合不受影响。
         *
         * 【去掉hashCode重写】
         * 去重会失效：4 个“马斯克”会被原样全部打印出来。
         * 原因：distinct() 底层是用 LinkedHashSet（顺序流）来判重的，而 HashSet 的判重分两步：
         *   第 1 步：比 hashCode()，不相等 → 直接认定为不同对象，根本不会去调 equals()；
         *   第 2 步：hashCode 相等时，才调 equals() 做最终确认。
         */
        list.stream().distinct().forEach(System.out::println);
    }

    // 2-映射
    @Test
    public void test5() {
        // map(Function f) —— 接收一个函数作为参数，将元素转换成其他形式或提取信息，该函数会被应用到每个元素上，并将其映射成一个新的元素。
        // 练习：转换为大写
        List<String> list = Arrays.asList("aa", "bb", "cc", "dd");
        // 方式1：
        /*
         * map()：一对一的“变形/提取”。流里有几个元素，出来还是几个元素，但类型或内容变了。
         * 签名：<R> Stream<R> map(Function<? super T, ? extends R> mapper)
         * 圆括号里需要一个 Function<T, R>，它的唯一抽象方法是：R apply(T t) —— “吃进一个 T，吐出一个 R”。
         */
        list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);
        // 方式2：
        list.stream().map(String::toUpperCase).forEach(System.out::println);

        // 练习：获取员工姓名长度大于3的员工。
        List<Employee> employees = EmployeeData.getEmployees();
        /*
         * filter 之后流里仍然是 Employee 对象，所以打印出来是整个员工信息（调用 Employee.toString()），
         * 而不是姓名。想只要姓名，就得加 map。
         */
        employees.stream().filter(emp -> emp.getName().length() > 3).forEach(System.out::println);

        // 练习：获取员工姓名长度大于3的员工的姓名。
        // 方式1：
        employees.stream().filter(emp -> emp.getName().length() > 3).map(emp -> emp.getName()).forEach(System.out::println);
        // 方式2：
        employees.stream().map(emp -> emp.getName()).filter(name -> name.length() > 3).forEach(System.out::println);
        // 方式3：
        employees.stream().map(Employee::getName).filter(name -> name.length() > 3).forEach(System.out::println);
    }

    // 3-排序
    @Test
    public void test6() {
        // sorted() —— 对流中元素排序后返回一个“新流”，不改动数据源
        /*
         * 无参版本叫“自然排序”，Stream 的实例方法
         * sorted 是“有状态 + 非短路”的中间操作 —— 它必须把所有元素都收齐才能排序，
         * 所以不能像 filter 那样一个一个流过去，对无限流（Stream.iterate）直接用 sorted 会卡死。
         */
        Integer[] arr = new Integer[]{345, 3, 64, 3, 46, 7, 3, 34, 65, 68};
        String[] arr1 = new String[]{"GG", "DD", "MM", "SS", "JJ"};

        Arrays.stream(arr).sorted().forEach(System.out::println);
        System.out.println(Arrays.toString(arr)); // arr数组并没有因为升序，做调整

        /*
         * arr  是 Integer[] → Integer.compareTo 按数值大小升序：3,3,3,7,34,46,64,65,68,345
         * arr1 是 String[]  → String.compareTo 按字典序（逐字符比 Unicode）升序：DD,GG,JJ,MM,SS
         *
         * 易混淆的经典坑：如果元素是数字型字符串，字典序 ≠ 数值序。
         * {"10","9","100"} 自然排序结果是 "10","100","9"，不是 9,10,100。
         */
        Arrays.stream(arr1).sorted().forEach(System.out::println);

        // Employee没有实现Comparable接口、重写 int compareTo(Employee o) 方法，运行时报错
//        List<Employee> list = EmployeeData.getEmployees();
//        list.stream().sorted().forEach(System.out::println);

        // sorted(Comparator com) —— 定制排序
        List<Employee> list = EmployeeData.getEmployees();
        /*
         * sorted 的重载版本：Stream<T> sorted(Comparator<? super T> comparator)。
         * Comparator 是函数式接口，唯一抽象方法 int compare(T o1, T o2)，所以可以用 Lambda 实现。
         * 约定返回值：负数表示 o1 排在 o2 前面，0 表示相等，正数表示 o1 排在后面。
         *
         * 【e1.getAge() - e2.getAge() 这种“相减法”有整型溢出隐患】
         * 当两个值一正一负且绝对值很大时，int 相减会溢出，导致比较结果正负颠倒、排序错乱。
         *   list.stream().sorted(Comparator.comparingInt(Employee::getAge))        // 升序
         *   list.stream().sorted(Comparator.comparingInt(Employee::getAge).reversed()) // 降序
         * 多级排序也很方便（先按年龄升序，年龄相同再按工资降序）：
         *   Comparator.comparingInt(Employee::getAge)
         *             .thenComparing(Comparator.comparingDouble(Employee::getSalary).reversed())
         * double 类型永远不要用相减法（小数差会被截断成 0），必须用 Double.compare。
         */
        list.stream().sorted((e1, e2) -> e1.getAge() - e2.getAge()).forEach(System.out::println);

        // 字符串从大到小排列
        /*
         * 没注释的：(s1, s2) -> -s1.compareTo(s2)
         *   把自然比较结果取反，于是“大的排前面”→ 降序：SS, MM, JJ, GG, DD
         * 注释掉的：String::compareTo
         *   等价于 (s1, s2) -> s1.compareTo(s2)，就是自然顺序本身 → 升序：DD, GG, JJ, MM, SS
         *
         * 【手动加负号实现降序是不推荐的写法】
         * 理论上有溢出风险——如果 compareTo 返回 Integer.MIN_VALUE，取负后还是 Integer.MIN_VALUE
         */
        Arrays.stream(arr1).sorted((s1, s2) -> -s1.compareTo(s2)).forEach(System.out::println);
//        Arrays.stream(arr1).sorted(String :: compareTo).forEach(System.out::println);
    }

    /**
     * 测试Stream的终止操作
     */
    // 1-匹配与查找
    @Test
    public void test7() {
        // allMatch(Predicate p) —— 检查是否匹配所有元素。
        // 练习：是否所有的员工的年龄都大于18
        List<Employee> list = EmployeeData.getEmployees();
        /*
         * allMatch() 签名：boolean allMatch(Predicate<? super T> predicate)
         * “是不是流里每一个元素都满足这个条件？”全满足返回 true，只要有一个不满足立刻返回 false。
         * 它是“终止操作”，返回 boolean（不是 Stream），所以链条到此为止，不能再往后接中间操作。
         * 还是“短路操作”：遇到第一个 false 就停止遍历，不会白跑完整个集合。
         *
         * 空流时 allMatch 返回 true（数学上的“空真”/vacuous truth）。
         * 即 Stream.empty().allMatch(x -> false) == true。做业务校验时要先判空集合。
         * 而 anyMatch 对空流恒返回 false。写业务判断时要留意这个反直觉行为。
         * 还有 noneMatch(Predicate)：全都不满足才返回 true，常用来做“不存在非法数据”的校验。
         */
        System.out.println(list.stream().allMatch(emp -> emp.getAge() > 18));

        // anyMatch(Predicate p) —— 检查是否至少匹配一个元素。
        // 练习：是否存在年龄大于18岁的员工
        /*
         * allMatch：全部都满足才 true（“所有人都大于18岁吗”）—— 遇到第一个不满足就 false
         * anyMatch：只要有一个满足就 true（“存在大于18岁的人吗”）—— 遇到第一个满足就 true，立刻短路返回
         *
         * allMatch（全真）、anyMatch（存在）、noneMatch（全假），三者都是短路型终止操作。
         */
        System.out.println(list.stream().anyMatch(emp -> emp.getAge() > 18));
        // 练习：是否存在员工的工资大于 10000
        System.out.println(list.stream().anyMatch(emp -> emp.getSalary() > 10000));

        // findFirst() —— 返回第一个元素
        /*
         * findFirst()签名：Optional<T> findFirst()
         * 返回流中“遇到顺序”的第一个元素。它也是短路操作，拿到第一个就不再往下走。
         * findAny()，串行流下通常也返回第一个，但并行流下返回“最先找到的任意一个”，
         * 不保证是第一个，换来的是更好的并行性能。
         *
         * 【为什么返回 Optional】
         * 因为流有可能是空的（集合为空，或 filter 之后一个不剩），这时“第一个元素”并不存在。
         * 如果直接返回 T，就只能返回 null，调用方一不留神就 NPE。所以 JDK 用 Optional<T> 把
         * “可能没有值”这件事写进类型里，强制你处理。
         */
        System.out.println(list.stream().findFirst().get());
    }

    @Test
    public void test8() {
        List<Employee> list = EmployeeData.getEmployees();
        // count()：终止操作，返回剩余元素个数，类型是 long
        System.out.println(list.stream().filter(emp -> emp.getSalary() > 7000).count());

        // max(Comparator c) —— 返回流中最大值
        // 练习：返回最高工资的员工
        /*
         * 签名：Optional<T> max(Comparator<? super T> comparator)
         *   max 必须知道“按什么标准算大”，所以要传一个 Comparator<Employee>（int compare(T o1, T o2)）。
         *
         * (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())
         * 取两人薪资，用 Double.compare 比较：前者大返回正数、相等返回 0、前者小返回负数。
         */
        System.out.println(list.stream().max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));

        // 练习：返回最高的工资：
        // 方式1：
        System.out.println(list.stream().max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())).get().getSalary());

        // 方式2：
        /*
         * 方式1：先在 Employee 层面比大小，找出“人”，再从人身上取工资
         *       Stream<Employee> --max(按salary比)--> Optional<Employee> --get()--> Employee --getSalary()--> double
         *
         * 方式2：先把每个人映射成工资，流里只剩数字，再直接比数字
         *       Stream<Employee> --map(取salary)--> Stream<Double> --max(比Double)--> Optional<Double> --get()--> Double
         *
         * 想知道“是谁拿最高工资”→ 必须用方式1，因为方式2 一 map 就把人丢了，只剩一个数字。
         * 只想要“最高工资是多少”→ 方式2 更直白。
         */
        System.out.println(list.stream().map(emp -> emp.getSalary()).max((salary1, salary2) -> Double.compare(salary1, salary2)).get());
        System.out.println(list.stream().map(emp -> emp.getSalary()).max(Double::compare).get());

        // min(Comparator c) —— 返回流中最小值
        // 练习：返回最低工资的员工
        /*
         * 签名：Optional<T> min(Comparator<? super T> comparator)
         * Stream 的终止操作，返回值是 Optional<Employee>，所以这行打印出来是 Optional[Employee{...}]
         */
        System.out.println(list.stream().min((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));

        // forEach(Consumer c) —— 内部迭代
        /*
         * forEach 返回 void，是终止操作，后面不能再接任何流操作；
         * 并行流下 forEach 不保证元素顺序，需要保序要用 forEachOrdered。
         */
        list.stream().forEach(System.out::println);

        // 针对于集合，jdk8中增加了一个遍历的方法
        /*
         * 这行的 forEach 和上一行【不是同一个方法】，只是重名
         *   list.stream().forEach(...) → Stream 接口的方法，先建流水线，再终止执行
         *   list.forEach(...)          → java.lang.Iterable 接口在 JDK8 新增的 default 方法，
         *                                内部就是一个普通的增强 for 循环，不创建任何 Stream
         * 只是单纯遍历 → 用 list.forEach(...)，没有创建流的开销，更轻量；
         * 需要先过滤/映射/排序 → 才用 list.stream()....forEach(...)。
         */
        list.forEach(System.out::println);
        // 针对于List来说，遍历的方式：① 使用Iterator ② 增强for ③ 一般for ④ forEach()
    }

    // 2-归约
    @Test
    public void test9() {
        // reduce(T identity, BinaryOperator) —— 可以将流中元素反复结合起来，得到一个值。返回 T
        // 练习1：计算1-10的自然数的和
        /*
         * reduce()
         *   “归约/聚合”：把流中的多个元素按某种规则【两两合并】，最终坍缩成一个值。
         *   求和、求积、求最大值、字符串拼接本质上都是归约。它是终止操作。
         *   执行过程（以求和为例，初始值 0）：
         *       0 + 1 = 1 → 1 + 2 = 3 → 3 + 3 = 6 → … → 45 + 10 = 55
         *   每一步的结果都作为下一步的第一个参数继续参与运算，像滚雪球。
         *
         * 签名：T reduce(T identity, BinaryOperator<T> accumulator)
         *   参数1 identity（初始值 / 同一元素）：
         *       既是运算的起点，也是【空流时的返回值】—— 正因为有它兜底，这个重载版本返回 T 而不是 Optional<T>。
         *       它必须满足“同一律”：identity op x == x。所以求和用 0，求积必须用 1（用 0 的话结果恒为 0）。
         *   参数2 accumulator（累加器）：
         *       类型 BinaryOperator<T>，它是 BiFunction<T,T,T> 的子接口，唯一抽象方法 T apply(T t1, T t2)，
         *       含义是“两个同类型的值进来，合并成一个同类型的值出去”。
         *
         * 下面四行逐行解释
         *   第1行 reduce(0, (x1, x2) -> x1 + x2)
         *         x1 是“到目前为止累计的结果”，x2 是“流中新流过来的元素”，返回两者之和。结果 55。
         *   第2行 reduce(0, (x1, x2) -> Integer.sum(x1, x2))
         *         和第1行完全等价，只是把 + 换成了 JDK 自带的静态方法 Integer.sum，结果仍是 55。
         *   第3行 reduce(0, Integer::sum)
         *         再把上一行的 Lambda 简化为“类名::静态方法”形式的方法引用，三者结果一致，这行最简洁。
         *   第4行 reduce(10, (x1, x2) -> x1 + x2)
         *         初始值改成 10，所以结果是 10 + (1+2+…+10) = 65，用来演示 identity 会实际参与运算。
         *         注意 10 不满足加法的同一律，这在【顺序流】下只是“多加了 10”，结果可预期；
         *         但在【并行流】下，每个线程分片都会各自加一次 10，结果会变得不可控。
         */
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(list.stream().reduce(0, (x1, x2) -> x1 + x2));
        System.out.println(list.stream().reduce(0, (x1, x2) -> Integer.sum(x1, x2)));
        System.out.println(list.stream().reduce(0, Integer::sum));
        System.out.println(list.stream().reduce(10, (x1, x2) -> x1 + x2));

        // reduce(BinaryOperator) —— 可以将流中元素反复结合起来，得到一个值。返回 Optional<T>
        // 练习2：计算公司所有员工工资的总和
        /*
         * 签名：Optional<T> reduce(BinaryOperator<T> accumulator)
         *   没有 identity 兜底，万一流是空的，就确实没有任何值可返回，所以返回类型只能是 Optional<T>。
         *   有值时它的执行方式是：拿第一个元素当起点，再依次和后面的元素合并。
         */
        List<Employee> employeeList = EmployeeData.getEmployees();
        System.out.println(employeeList.stream().map(emp -> emp.getSalary()).reduce((salary1, salary2) -> Double.sum(salary1, salary2)));
        System.out.println(employeeList.stream().map(emp -> emp.getSalary()).reduce(Double::sum));
    }

    // 3-收集
    @Test
    public void test10() {
        List<Employee> list = EmployeeData.getEmployees();
        // collect(Collector c) —— 将流转换为其他形式。接收一个 Collector接口的实现，用于给Stream中元素做汇总的方法
        // 练习1：查找工资大于6000的员工，结果返回为一个List或Set
        /*
         * 签名：<R, A> R collect(Collector<? super T, A, R> collector)
         * 它是终止操作，作用是把“流水线上的结果”重新装回一个实实在在的容器里（List、Set、Map、字符串、统计值…）。
         */
        List<Employee> list1 = list.stream().filter(emp -> emp.getSalary() > 6000).collect(Collectors.toList());
        /*
         * list1 是“加工后的新集合”，list 是“原始数据源”，两者内容不同但互不影响：
         *   list1.forEach(...) → 只打印工资 > 6000 的那几位员工（filter 的结果被 collect 装进了新 List）
         *   list.forEach(...)  → 打印全部员工，一个不少、顺序也没变
         *
         * list1 虽然是新的 List 容器，但里面装的 Employee 是【同一批对象的引用】，
         * 不是深拷贝。也就是说 list1.get(0).setSalary(1) 会连带影响 list 里的那个员工。
         * 集合是新的，元素还是旧的。
         */
        list1.forEach(System.out::println);
        list.forEach(System.out::println);

        // 练习2：按照员工的年龄进行排序，返回到一个新的List中
        List<Employee> list2 = list.stream().sorted((e1, e2) -> e1.getAge() - e2.getAge()).collect(Collectors.toList());
        list2.forEach(System.out::println);
    }
}
