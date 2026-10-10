package chapter18;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalTest {
    /*
    Optional的使用
    Optional<T>：JDK8 引入的「值容器」，用于显式表达“这个值可能不存在”。
    JDK9 新增 or()/ifPresentOrElse()/stream()，JDK10 新增 orElseThrow()（无参），JDK11 新增 isEmpty()。
    1. 为什么需要Optional类？
    为了避免代码中出现空指针异常。
    把“可能为 null”这件事写进返回值类型里，让编译期/阅读者一眼看到，
    并通过 orElse / ifPresent / map 等 API 强迫调用方正面处理“空”的分支，
    从而把隐式的 null 判断变成显式的、不易漏掉的代码。
    2. 如何实例化？（3 个静态工厂方法，构造器是私有的，不能 new）
    Optional.of(T value)         —— value 必须非 null，传 null 立刻抛 NullPointerException（用于“我断定它不为空”）
    Optional.ofNullable(T value) —— value 可空；为 null 时返回空容器 Optional.empty()（最常用）
    Optional.empty()             —— 直接得到一个空容器
    3. 常用方法（按“是否脱离容器”分两类）
    终端取值（返回 T）：
        orElse(T other)              —— 有值返回值，无值返回 other（备胎）
        orElseGet(Supplier<T> s)     —— 同上，但 other 惰性生成，代价大时用这个
        orElseThrow()                —— 无值抛 NoSuchElementException
        get()                        —— 无值抛 NoSuchElementException，不推荐裸用
    中间操作（仍返回 Optional，可链式）：
        map / flatMap / filter / or
    判断/消费：
        isPresent() / isEmpty() / ifPresent(Consumer) / ifPresentOrElse(...)

    * ifPresent()   ：有值时才执行操作，为空时不执行。
    * orElseThrow() ：有值时返回该值，为空时抛出 NoSuchElementException（JDK10+）。
    * orElse()      ：有值时返回该值，为空时返回指定的默认值。
    4. 易错点
    orElse 的参数“无论是否为空都会先被求值”，所以 orElse(new Xxx()) 会白白创建对象，惰性场景用 orElseGet。
    Optional 没有实现 Serializable，官方定位是「方法返回值」，不建议用作字段、方法参数或集合元素。
    不要写 opt.isPresent() ? opt.get() : def，这等价于 orElse(def)，属于“把 Optional 用回了 null 判断”。
     */
    /*
     * Optional 最典型的用法组合：ofNullable(可能为 null 的值) + orElse(备胎)。
     * 场景模拟：从数据库/接口拿到的 star 可能是 null，如果直接 star.toString() 就会 NPE；
     * 用 Optional 包一层后，空值会被“备胎”杨幂顶替，后续代码就永远拿到一个非 null 的 String。
     */
    @Test
    public void test() {
        String star = "迪丽热巴";
        star = null;

        // 使用Optional避免空指针的问题
        // ofNullable(T value)：用来创建一个Optional实例，value可能是空，也可能非空
        /*
         * Optional.ofNullable(T value)：静态工厂方法，用于创建 Optional 容器。
         * - value 不为 null：返回包含该值的 Optional。
         * - value 为 null：返回空的 Optional（Optional.empty()）。
         *
         * Optional<T> filter(Predicate<? super T> predicate)：实例方法，用于筛选容器中的值。
         * - 容器为空：直接返回空的 Optional，不执行条件判断。
         * - 容器有值且满足条件：保留原值，返回包含该值的 Optional。
         * - 容器有值但不满足条件：返回空的 Optional。
         *
         * 【区别】
         * ofNullable()：根据值是否为 null 创建 Optional，避免直接处理 null。
         * filter()：对已有 Optional 中的值进行条件筛选，不负责创建初始容器。
         *
         * 【示例】
         * Optional.ofNullable(star)            // star 为 null → 空容器
         *         .filter(s -> s.length() > 3) // 长度不大于 3 → 空容器
         *         .orElse("杨幂");              // 容器为空 → 返回默认值 "杨幂"
         */
        Optional<String> optional = Optional.ofNullable(star);

        // orElse(T other):如果Optional实例内部的value属性不为null，则返回value。
        // 如果value为null，则返回other。
        /*
         * orElse 的参数是“先求值后判断”的。写成 orElse(buildDefault()) 时，
         * 即使容器里有值，buildDefault() 也一定会被执行（只是结果被丢弃）。
         * 需要惰性求值就换 orElseGet(() -> buildDefault())。
         */
        String otherStar = "杨幂";
        String finalStar = optional.orElse(otherStar);

        System.out.println(finalStar); // 杨幂
    }

    /*
     * get()：当 Optional 容器里确实有值时，把值原样取回来。
     * 这里 star = "迪丽热巴" 非 null，所以 ofNullable 得到的是非空容器，get() 能正常返回并打印“迪丽热巴”。
     * test() 走“空 -> 备胎”分支，test2() 走“非空 -> 直接取值”分支。
     */
    @Test
    public void test2() {
        String star = "迪丽热巴";
        Optional<String> optional = Optional.ofNullable(star);
        // get()：获取 Optional 中的值，为空时抛出 NoSuchElementException
        /*
         * 【注意】不推荐优先使用 get()，因为它不会提供默认值或自动处理空值。
         * 如果 star 为 null，ofNullable() 会创建空 Optional，此时调用 get() 将抛出异常。
         *
         * 建议根据实际需求选择合适的方法：
         * optional.ifPresent(System.out::println);  // 有值才打印，为空时不执行
         * String s = optional.orElseThrow();        // 有值则返回，为空则抛出异常（JDK10+）
         * String s2 = optional.orElse("杨幂");       // 有值则返回原值，为空则返回默认值
         *
         * 注意：无参 orElseThrow() 与 get() 的空值行为相同，
         * 只是语义更明确，适用于确定应该有值、否则就应抛出异常的场景。
         */
        System.out.println(optional.get()); // 迪丽热巴
    }
}
