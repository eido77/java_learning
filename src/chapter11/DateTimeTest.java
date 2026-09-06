package chapter11;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

public class DateTimeTest {
    /*
    一、JDK8之前的API:
    1. System类的currentTimeMillis()
        > 获取当前时间对应的毫秒数，long类型，时间戳
        > 当前时间与1970年1月1日0时0分0秒之间的毫秒数
        > 常用来计算时间差
    2. 两个Date类
    |--java.util.Date
          > 两个构造器的使用
          > 两个方法的使用：①toString() ② long getTime()
              |----java.sql.Date: 对应着数据库中的date类型
    3. SimpleDateFormat类：用于日期时间的格式化和解析
        格式化：日期--->字符串
        解析：字符串 ---> 日期
    4. Calendar类（日历类）：抽象类
         ① 实例化：由于Calendar是一个抽象类，所以我们需要创建其子类的实例。这里我们通过Calendar的静态方法
                   getInstance()即可获取
         ② 常用方法：get(int field) / set(int field,xx) / add(int field,xx) / getTime() / setTime()
    二、JDK8中的API:
    1. LocalDate,LocalTime,LocalDateTime --->类似于Calendar
    > 实例化：now() / of(xxx,xx,xx)
    > 方法：get() / withXxx() / plusXxx() / minusXxx() ...
    2. Instant:瞬时 --->类似于Date
    > 实例化：now() / ofEpochMilli()
    > 方法：toEpochMilli()
    3. DateTimeFormatter ---> 类似于SimpleDateFormat
    用于格式化和解析LocalDate,LocalTime,LocalDateTime
     */

    /**
     * 一、JDK8之前的API
     */
    /*
     * Date类的使用
     * |--java.util.Date
     *   > 两个构造器的使用
     *   > 两个方法的使用：① toString() ② long getTime()
     *       |----java.sql.Date: 对应着数据库中的date类型
     */
    @Test
    public void test1() {
        // 创建一个基于当前系统时间的Date的实例
        Date date1 = new Date();
        System.out.println(date1.toString()); // Sat Sep 05 14:39:01 CST 2026
        System.out.println(date1.getTime()); // 1788590341490

        // 创建一个基于指定时间戳的Date的实例
        /*
         * 1788590341490 已经超过了 int 的最大值（约 21 亿），
         * 而 Java 中不带后缀的整数字面量默认是 int 类型，直接写会"整数字面量超出范围"编译报错。
         * 加上 L 后缀表示这是一个 long 类型的字面量，才能容纳这么大的数（毫秒时间戳）。
         */
        Date date2 = new Date(1788590341490L);
        /*
         * 其实上面和这里都不是"必须"加 toString()。
         * System.out.println(Object) 内部会自动调用参数的 toString()，
         * 所以 println(date2) 和 println(date2.toString()) 打印结果完全一样。
         * 加不加只是显式与隐式的区别，效果相同。
         */
        System.out.println(date2.toString()); // Sat Sep 05 14:39:01 CST 2026
    }

    @Test
    public void test2() {
        /*
         *  为什么要写全名 java.sql.Date：因为本文件已经 import java.util.Date，
         *    一个文件里同一个简单类名 Date 只能通过 import 绑定到一个包。
         *    要再用另一个包里的同名类 java.sql.Date，就必须写全限定名来区分。
         *
         *  规范：并非任何时候都要写全。只有在"同名冲突"时才需要写全限定名。
         *    如果没有冲突，正常 import 后直接用简单类名即可。
         *
         *  空参构造器：java.sql.Date 没有推荐使用的空参构造器
         *    （其无参构造已被废弃 @Deprecated），它主要用 long 时间戳构造。
         */
        java.sql.Date date1 = new java.sql.Date(1788590341490L);

        /*
         * println 会自动调用 toString()，加不加结果一样。
         * - 只要是往 println / 字符串拼接 里传对象，都会自动调用 toString()，不用手动加。
         * - 什么时候必须手动调 toString()？当你需要拿到"字符串本身"去做后续处理时，
         *   比如 String s = date1.toString(); 再对 s 做 substring、equals 等操作。
         */
        System.out.println(date1.toString()); // 2026-09-05
        System.out.println(date1.getTime()); // 1788590341490
    }

    /**
     * 3. SimpleDateFormat类：用于日期时间的格式化和解析
     * 格式化：日期--->字符串
     * 解析：字符串 ---> 日期
     */
    @Test
    public void test3() throws ParseException {
        /*
         * 这里用的是 SimpleDateFormat 的空参构造，
         * 它会使用"默认的、依赖当前系统区域(Locale)的格式"，
         * 所以输出的形式（如 2026/9/5 下午2:51）在不同电脑上可能不一样，
         * 实际开发一般不用空参，而是像 test4 那样显式指定 pattern，保证格式固定可控。
         */
        SimpleDateFormat sdf = new SimpleDateFormat();

        // 格式化：日期 ---> 字符串
        Date date1 = new Date();
        String strDate = sdf.format(date1);
        /*
         * format() 的返回值 strDate 本身就已经是 String 类型，
         * 已经是字符串，不需要再 toString()。
         * 而 date1、date2 是 Date 对象，才涉及"要不要 toString"的问题。
         */
        System.out.println(strDate); // 2026/9/5 下午2:51

        // 解析：字符串 ---> 日期
        Date date2 = sdf.parse("2026/9/5 下午2:51");
        System.out.println(date2); // Sat Sep 05 14:51:00 CST 2026
    }

    @Test
    public void test4() throws ParseException {
        /*
         * pattern 里字母的含义（区分大小写）：
         * y=年  M=月(大写)  d=日  H=24小时制的时(大写)  h=12小时制的时(小写)
         * m=分(小写)  s=秒  S=毫秒
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 格式化：日期 ---> 字符串
        Date date1 = new Date();
        String strDate = sdf.format(date1);
        System.out.println(strDate); // 2026-09-05 15:06:25

        // 解析：字符串 ---> 日期
        Date date2 = sdf.parse("2026-09-05 14:59:17");
        System.out.println(date2); // Sat Sep 05 14:59:17 CST 2026

        // 解析失败。因为参数的字符串不满足SimpleDateFormat可以识别的格式。
        /*
         * 解析时，字符串的格式必须和构造 sdf 时指定的 pattern 严格对应，
         * 否则会抛 ParseException。这里 pattern 是 yyyy-MM-dd HH:mm:ss，
         * 而 "26-09-05 下午2:59" 既不是4位年、也不是24小时制、还多了"下午"，故解析失败。
         */
//        Date date3 = sdf.parse("26-09-05 下午2:59");
    }

    /**
     * 4. Calendar类（日历类）：抽象类
     * ① 实例化：由于Calendar是一个抽象类，所以我们需要创建其子类的实例。这里我们通过Calendar的静态方法
     * getInstance()即可获取
     * ② 常用方法：get(int field) / set(int field,xx) / add(int field,xx) / getTime() / setTime()
     */
    @Test
    public void test5() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getClass()); // class java.util.GregorianCalendar

        // 测试方法
        // get(int field)
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH)); // 5
        System.out.println(calendar.get(Calendar.DAY_OF_YEAR)); // 248

        // set(int field,xx)
        // 说明：set 是把该字段"直接设为指定值"（这里把"月中第几天"设成23号）
        calendar.set(Calendar.DAY_OF_MONTH, 23);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH)); // 23

        // add(int field,xx)
        // 说明：add 是在当前值基础上"增减"（负数为减），这里 23-1=22
        System.out.println(calendar.get(Calendar.DAY_OF_YEAR)); // 266

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH)); // 22

        System.out.println(calendar.get(Calendar.DAY_OF_YEAR)); // 265

        // getTime()：Calendar --> Date
        Date date = calendar.getTime();
        System.out.println(date); // Tue Sep 22 15:22:28 CST 2026

        // setTime()：使用指定Date重置Calendar
        Date date2 = new Date();
        calendar.setTime(date2);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH)); // 5
    }

    /**
     * 练习：
     * 如何将一个java.util.Date的实例转换为java.sql.Date的实例
     */
    @Test
    public void test6() {
        Date date1 = new Date();

        /*
         * 为什么下面这行强转会抛 ClassCastException？
         * 因为 java.sql.Date 是 java.util.Date 的子类，
         * date1 实际 new 出来的是"父类 java.util.Date"对象，
         * 父类对象不能强转成子类类型（对象的真实类型并不是 sql.Date）。
         */
//        java.sql.Date date2 = (java.sql.Date) date1;

        java.sql.Date date2 = new java.sql.Date(date1.getTime());
        System.out.println(date2); // 2026-09-05
    }

    /**
     * 拓展：
     * 将控制台获取的年月日（比如：2022-12-13）的字符串数据，保存在数据库中。
     * （简化为得到java.sql.Date的对象，此对象对应的时间为2022-12-13）。
     * 字符串 ---> java.util.Date ---> java.sql.Date
     */
    /*
     * - java.util.Date：表示【日期 + 时间】（精确到毫秒），
     *   toString() 输出：星期 月 日 时:分:秒 时区 年
     *   例如：Tue Dec 13 00:00:00 CST 2022
     *
     * - java.sql.Date：对应数据库中的 DATE 类型，语义上【只表示日期，不含时分秒】，
     *   toString() 输出格式为 yyyy-MM-dd
     *   例如：2022-12-13
     */
    @Test
    public void test7() throws ParseException {
        String pattern = "2022/12/13";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        // 得到java.util.Date
        Date date1 = sdf.parse(pattern);
        System.out.println(date1); // Tue Dec 13 00:00:00 CST 2022

        // 转换为java.sql.Date
        java.sql.Date date2 = new java.sql.Date(date1.getTime());
        System.out.println(date2); // 2022-12-13
    }


    /**
     * 二、JDK8中的API
     */
    /*
     * JDK 8 之前的日期时间 API 存在一些设计问题：
     * 1. 可变性：
     *    Date 和 Calendar 都是可变的，调用相关方法可能直接修改原对象。
     * 2. API 设计不合理：
     *    Date 中年份以 1900 年为基准，月份从 0 开始（0 表示 1 月）。
     * 3. 格式化：
     *    通常使用 SimpleDateFormat 对 Date 进行格式化；
     *    Calendar 一般先通过 getTime() 转换为 Date，再进行格式化。
     * 4. 线程安全：
     *    Date 和 Calendar 本身不是线程安全的；
     *    SimpleDateFormat 也是非线程安全的。
     * 5. 功能有限：
     *    旧 API 的日期时间处理能力和设计不够完善，例如对时区、
     *    日期计算等场景的处理比较繁琐。
     * JDK 8 引入了全新的 java.time 日期时间 API，
     * 推荐使用 LocalDate、LocalTime、LocalDateTime、Instant、
     * ZonedDateTime、DateTimeFormatter 等类。
     */
    @Test
    public void test8() {
        /*
         * String 是不可变的：任何"修改"都会返回新对象，原对象不动（见 s1 依旧是 hello）。
         * Calendar 是可变的：set 直接改变了原对象自身的状态（这是它的缺陷之一）。
         */
        String s1 = "hello";
        String s2 = s1.replace('l', 'w');
        System.out.println(s1); // hello

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 23);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH)); // 23
    }

    @Test
    public void test9() {
        // 偏移性：Date中的年份是从1900开始的，而月份都从0开始。
        /*
         * 具体解释这行"离谱输出"是怎么来的（这就是偏移性的坑）：
         * new Date(year, month, date) 里：
         *   - year 参数是"距离1900的偏移量"，传 2026 实际代表 1900+2026 = 3926 年；
         *   - month 参数从 0 开始计数，传 9 实际代表第10个月即"10月"(October)；
         *   - date（日）才是正常的，传 5 就是 5 号。
         * 所以输出 Tue Oct 05 ... 3926，完全违反直觉。
         * 这个带参构造器已被 @Deprecated 废弃，正因为它太反直觉。
         */
        Date date = new Date(2026, 9, 5);
        System.out.println(date); // Tue Oct 05 00:00:00 CST 3926
    }

    /**
     * JDK8的api:LocalDate \ LocalTime \ LocalDateTime
     */
    @Test
    public void test10() {
        // now():获取当前日期和时间对应的实例
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println(localDate); // 2026-09-05
        System.out.println(localTime); // 20:15:43.509676
        /*
         * T 是 ISO-8601 国际标准日期时间格式里的固定分隔符，
         * 作用是"分隔日期部分和时间部分"，T 就是 Time 的意思。
         * 即 2026-09-05T20:15:43.509683 = 日期(2026-09-05) + T + 时间(20:15:43.509683)。
         * 它只是分隔符，没有别的含义，是标准规定的写法。
         */
        System.out.println(localDateTime); // 2026-09-05T20:15:43.509683

        // of():获取指定的日期、时间对应的实例
        LocalDate localDate1 = LocalDate.of(2021, 5, 23);
        LocalTime localTime1 = LocalTime.of(23, 59, 59);
        LocalDateTime localDateTime1 = LocalDateTime.of(2022, 12, 5, 11, 23, 45);

        System.out.println(localDate1); // 2021-05-23
        System.out.println(localTime1); // 23:59:59
        System.out.println(localDateTime1); // 2022-12-05T11:23:45

        // getXXX()
        LocalDateTime localDateTime2 = LocalDateTime.now();
        System.out.println(localDateTime2.getDayOfMonth()); // 5

        // 体现不可变性
        // withXxx()
        /*
         * withXxx 表示"把某个字段替换成指定值"（这里把日替换成10号），
         * 但它不会改动原对象，而是返回一个新对象——这正是"不可变性"的体现：
         * 打印 localDateTime2 仍是原来的5号，localDateTime3 才是改后的10号。
         */
        LocalDateTime localDateTime3 = localDateTime2.withDayOfMonth(10);
        System.out.println(localDateTime2); // 2026-09-05T20:28:14.042061
        System.out.println(localDateTime3); // 2026-09-10T20:28:14.042061

        // plusXxx()
        // 说明：plusXxx 表示"加上一段时间"（同理有 minusXxx 减），同样返回新对象、不改原对象
        LocalDateTime localDateTime4 = localDateTime2.plusDays(15);
        System.out.println(localDateTime2); // 2026-09-05T20:31:14.131684
        System.out.println(localDateTime4); // 2026-09-20T20:31:14.131684
    }

    /**
     * JDK8的api: Instant
     */
    @Test
    public void test11() {
        // now():
        Instant instant = Instant.now();
        /*
         * - 结尾的 Z 是 ISO-8601 标准里的时区标记，代表"零时区/UTC+0"（Z = Zulu time）。
         *    看到 Z 就说明这个时间是 UTC+0 的时间，没有任何时区偏移。
         * - Instant.now() 得到的是 UTC+0（世界标准时间）的时刻，
         *    它不带时区概念，本质就是"从1970-01-01 00:00:00 UTC 起的时间点"
         */
        System.out.println(instant); // 2026-09-05T12:40:56.735980Z

        /*
         * - atOffset() 里传的是 ZoneOffset（时区偏移量），
         *    如 ZoneOffset.ofHours(8) 表示东八区(北京时间)，把 UTC 时间 +8 小时。
         * - 不想手动写 8，可以用系统默认时区：
         *    例如 instant.atZone(ZoneId.systemDefault()) 会自动按本机所在时区转换，无需手填数字。
         * - 西半球时区是负偏移，比如 ZoneOffset.ofHours(-5) 表示 UTC-5（美国东部）。
         *    所以 UTC+0 并不是"最小时间"，往西还有 UTC-1 到 UTC-12，比 UTC+0 更小。
         */
        OffsetDateTime instant1 = instant.atOffset(ZoneOffset.ofHours(8));

        /*
         *   2026-09-05  → 日期(年-月-日)
         *   T           → 日期与时间的分隔符
         *   20:40:56    → 时:分:秒
         *   .735980     → 秒的小数部分(微秒)
         *   +08:00      → 时区偏移量，表示这是 UTC+8 的时间
         */
        System.out.println(instant1); // 2026-09-05T20:40:56.735980+08:00

        Instant instant2 = Instant.ofEpochMilli(12321425123434L);
        System.out.println(instant2); // 2360-06-14T02:05:23.434Z

        long milliTime = instant.toEpochMilli();
        /*
         * 运行时刻不同导致的。
         * instant 是在方法开头 Instant.now() 那一刻创建的，
         * 而 new Date() 是在方法快结束时才执行的，两句代码执行之间过了几毫秒，
         * 所以时间戳差了 3 毫秒(285-282)，属于正常现象，不是 bug。
         */
        System.out.println(milliTime); // 1788612268282
        System.out.println(new Date().getTime()); // 1788612268285
    }

    /**
     * JDK8的api: DateTimeFormatter
     */
    @Test
    public void test12() {
        // 自定义的格式。如：ofPattern(“yyyy-MM-dd hh:mm:ss”)
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化：日期、时间 --> 字符串
        LocalDateTime localDateTime = LocalDateTime.now();
        String strDateTime = dateTimeFormatter.format(localDateTime);
        System.out.println(strDateTime); // 2026-09-05 21:07:47

        // 解析：字符串 ---> 日期、时间
        TemporalAccessor temporalAccessor = dateTimeFormatter.parse("2026-09-05 21:07:47");
        /*
         * 解析的意义在于"把字符串变成可运算的对象"，而不是为了打印好看。
         * 字符串 "2026-09-05 21:07:47" 只是一串文本，你不能对它做加减天数、取月份等操作；
         * 而解析成 LocalDateTime 对象后，就能调用 plusDays()、getDayOfMonth() 等方法做运算。
         * 打印出来看着像，是因为对象的 toString() 又把它转回了可读文本，但内部已是结构化时间对象。
         */
        LocalDateTime localDateTime1 = LocalDateTime.from(temporalAccessor);
        System.out.println(localDateTime1); // 2026-09-05T21:07:47
    }

    /**
     * 案例：百天推算
     * 使用Calendar获取当前时间, 把这个时间设置为你的生日, 再获取你的百天(出生后100天)日期。
     */
    @Test
    public void test13() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        System.out.println("你的生日为：" + date); // 你的生日为：Sat Sep 05 21:24:20 CST 2026

        /*
         * 这里用 DAY_OF_YEAR 还是 DAY_OF_MONTH，对"加100天"的结果是一样的
         * 因为 Calendar.add() 很智能，会自动处理进位：
         *   即使用 DAY_OF_MONTH 加100，超过当月天数时也会自动跨月、跨年，最终结果相同。
         * 区别只在语义表达上：DAY_OF_YEAR 表示"一年中的第几天"，语义上更贴合"往后推N天"。
         * 结论：两者加100天结果一致，选哪个都行，DAY_OF_YEAR 读起来意思更顺。
         */
        calendar.add(Calendar.DAY_OF_YEAR, 100);
        Date newDate = calendar.getTime();
        System.out.println(newDate); // Mon Dec 14 21:24:20 CST 2026
    }

    /**
     * 使用LocalDateTime获取当前时间, 把这个时间设置为你的生日, 再获取你的百天(出生后100天)日期。
     */
    @Test
    public void test14() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("你的生日为：" + localDateTime); // 你的生日为：2026-09-05T21:28:35.368689

        /*
         * 对比 test13：新 API 的写法明显更简洁、更安全。
         * plusDays(100) 直接返回加100天后的新对象，且不改变原对象(不可变性)，
         * 不用像 Calendar 那样区分 DAY_OF_YEAR / DAY_OF_MONTH，可读性也更好。
         * 这也是为什么实际开发推荐用 JDK8 新 API 而非 Calendar。
         */
        LocalDateTime localDateTime1 = localDateTime.plusDays(100);
        System.out.println(localDateTime1); // 2026-12-14T21:28:35.368689
    }
}
