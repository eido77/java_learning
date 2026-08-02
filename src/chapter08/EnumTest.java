package chapter08;

public class EnumTest {
    /*
    枚举类的使用
    1. 枚举类的理解：
    枚举类型本质上也是一种类，只不过是这个类的对象是有限的、固定的几个，不能让用户随意创建。
    2. 举例：
    - `星期`：Monday(星期一)......Sunday(星期天)
    - `性别`：Man(男)、Woman(女)
    - `月份`：January(1月)......December(12月)
    - `季节`：Spring(春天)......Winter(冬天)
    - `三原色`：red(红色)、green(绿色)、blue(蓝色)
    - `支付方式`：Cash（现金）、WeChatPay（微信）、Alipay(支付宝)、BankCard(银行卡)、CreditCard(信用卡)
    - `就职状态`：Busy(忙碌)、Free(空闲)、Vocation(休假)、Dimission(离职)
    - `订单状态`：Nonpayment（未付款）、Paid（已付款）、Fulfilled（已配货）、Delivered（已发货）、Checked（已确认收货）、Return（退货）、Exchange（换货）、Cancel（取消）
    - `线程状态`：创建、就绪、运行、阻塞、死亡
    3. 开发中的建议：
    > 开发中，如果针对于某个类，其实例是确定个数的。则推荐将此类声明为枚举类。
    > 如果枚举类的实例只有一个，则可以看做是单例的实现方式。
    4. JDK5.0 之前如何自定义枚举类 （了解）：见下方 Season 类
    5. JDK5.0中使用enum定义枚举类：见下方 Season1 类
    6. Enum中的常用方法:
    6.1 使用enum关键字定义的枚举类，默认其父类是java.lang.Enum类
        因此不要再显式地为它指定其它父类，否则会报错（Java是单继承，父类已被占用）。
    6.2 熟悉Enum类中常用的方法
        String  toString()          : 默认返回常量名（对象名），可手动重写。
        static  枚举类型[] values()  : 返回该枚举类所有对象组成的数组，常用于遍历。【静态方法】（重点）
        static  枚举类型 valueOf(String name) : 把字符串转为对应名字的枚举对象；
                                       若name不是任何一个枚举对象的名字，抛 IllegalArgumentException。（重点）
        String  name()              : 返回当前枚举对象的名称。（一般更推荐用toString()）
        int     ordinal()           : 返回当前枚举对象的次序号（索引），默认从0开始。
    7. 枚举类实现接口的操作
       情况1：枚举类实现接口，在枚举类中重写接口中的抽象方法。当通过不同的枚举类对象调用此方法时，执行的是同一个方法。
       情况2：让枚举类的每一个对象重写接口中的抽象方法。当通过不同的枚举类对象调用此方法时，执行的是不同的实现的方法。
     */
    public static void main(String[] args) {
        System.out.println(Season.SPRING); // Season{seasonName='春天', seasonDesc='春暖花开'}
        System.out.println(Season.SUMMER.getSeasonDesc()); // 夏日炎炎

        // 验证enum枚举类的继承关系：Season1 -> Enum -> Object
        System.out.println(Season1.SPRING.getClass()); // class chapter08.Season1
        System.out.println(Season1.SPRING.getClass().getSuperclass()); // class java.lang.Enum
        System.out.println(Season1.SPRING.getClass().getSuperclass().getSuperclass()); // class java.lang.Object

        // ===== Enum常用方法测试 =====
        // 1.toString()：未重写时返回对象名(SPRING)；本例已重写，故打印的是自定义格式
        System.out.println(Season1.SPRING); // SPRING
        // 重写之后：Season{seasonName='春天', seasonDesc='春暖花开'}

        // 2.name()：返回枚举对象的名称，不受toString重写影响
        System.out.println(Season1.SPRING.name()); // SPRING

        // 3.vlaues()：返回所有枚举对象的数组，方便遍历
        Season1[] values = Season1.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println(values[i]);
        }
        /*
        Season{seasonName='春天', seasonDesc='春暖花开'}
        Season{seasonName='夏天', seasonDesc='夏日炎炎'}
        Season{seasonName='秋天', seasonDesc='秋高气爽'}
        Season{seasonName='冬天', seasonDesc='白雪皑皑'}
         */

        // 4.valueOf(String objName)：按名字返回对应的枚举对象；名字不存在则抛 IllegalArgumentException
        // 返回当前枚举类中名称为objName的枚举类对象，如果枚举类中不存在objName名称的对象，则报错
        String objName = "WINTER";
//        objName = "Winter"; // IllegalArgumentException
        // option + enter（快速修复代码、自动导包、生成修改建议）
        Season1 season1 = Season1.valueOf(objName);
        System.out.println(season1); // Season{seasonName='冬天', seasonDesc='白雪皑皑'}

        // 5.ordinal()：返回枚举对象的次序号（从0开始）。AUTUMN是第3个，索引为2
        // 角标（0、1、2、3）
        System.out.println(Season1.AUTUMN.ordinal()); // 2

        // ===== 7.枚举类实现接口 =====
        // 情况1（当前被注释掉的写法）：所有对象共用同一个show()，输出都是“这是一个季节”
        // <<<说明：当前代码走的是“情况2”，这里实际输出“夏”，若启用情况1才输出“这是一个季节”
        Season1.SUMMER.show(); // 这是一个季节

        // 情况2（当前生效的写法）：每个对象各自重写show()，输出各不相同
        Season1[] values1 = Season1.values();
        for (int i = 0; i < values1.length; i++) {
            values1[i].show(); // 春 夏 秋 冬
        }
    }
}

// ================= JDK5.0之前：手动定义枚举类 =================
class Season {
    // 2.声明实例变量，用 private final 修饰（一旦赋值不可改，保证枚举对象的属性固定）
    private final String seasonName;
    private final String seasonDesc; // 季节的描述

    // 1.私有化类的构造器（外部无法new，保证对象个数受控）
    private Season(String seasonName, String seasonDesc) {
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    // 3.只提供get方法，不提供set（对象属性只读，不可修改）
    public String getSeasonName() {
        return seasonName;
    }

    public String getSeasonDesc() {
        return seasonDesc;
    }

    // 4.在类内部创建固定的几个对象，用 public static final 修饰
    public static final Season SPRING = new Season("春天", "春暖花开");
    public static final Season SUMMER = new Season("夏天", "夏日炎炎");
    public static final Season AUTUMN = new Season("秋天", "秋高气爽");
    public static final Season WINTER = new Season("冬天", "白雪皑皑");

    @Override
    public String toString() {
        return "Season{" +
                "seasonName='" + seasonName + '\'' +
                ", seasonDesc='" + seasonDesc + '\'' +
                '}';
    }
}

interface Info {
    void show();
}

// ================= JDK5.0：使用 enum 关键字定义枚举类 =================
enum Season1 implements Info {
    // 1.必须在枚举类【开头】列出所有对象，对象之间用","隔开，最后用";"结束
    //   下面每个对象后面的 { } 中重写了show()方法 —— 对应“情况2”（每个对象各自实现）
    // 情况1的写法（统一实现，此处对比留存）：直接写 SPRING("春天","春暖花开"), ... WINTER("冬天","白雪皑皑");
    //   然后在类里统一重写一个show()即可
    SPRING("春天", "春暖花开") {
        public void show() {
            System.out.println("春");
        }
    },
    SUMMER("夏天", "夏日炎炎") {
        public void show() {
            System.out.println("夏");
        }
    },
    AUTUMN("秋天", "秋高气爽") {
        public void show() {
            System.out.println("秋");
        }
    },
    WINTER("冬天", "白雪皑皑") {
        public void show() {
            System.out.println("冬");
        }
    };

    // 2.声明当前类的对象的实例变量，使用private final修饰
    private final String seasonName;
    private final String seasonDesc; // 季节的描述

    // 3.私有化类的构造器（enum的构造器默认就是private，写不写private都行，但语义上建议保留）
    private Season1(String seasonName, String seasonDesc) {
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    // 4.提供实例变量的get方法
    public String getSeasonName() {
        return seasonName;
    }

    public String getSeasonDesc() {
        return seasonDesc;
    }

    @Override
    public String toString() {
        return "Season{" +
                "seasonName='" + seasonName + '\'' +
                ", seasonDesc='" + seasonDesc + '\'' +
                '}';
    }

    // 7. 枚举类实现接口的操作
    // 情况1：枚举类统一重写接口方法，所有对象调用结果相同
    // 若要测试情况1：把上面每个对象后面的{...show...}删掉，取消下面注释即可
//    @Override
//    public void show() {
//        System.out.println("这是一个季节");
//    }
}
