package chapter18;

import org.junit.jupiter.api.Test;

public class SwitchTest {
    /*
     * switch 是"多分支等值选择"语句：拿同一个表达式的值去和一组常量逐个比对，
     * 命中哪个标签就进哪个分支，等价于一长串 if (x == 常量) ... else if ...。
     *
     * 版本对照：
     *   JDK12 预览 -> JDK13 二次预览（新增 yield）-> JDK14 转正：switch 表达式；
     *   JDK17 预览 -> JDK21 转正：switch 模式匹配。
     */
}

enum Week {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
}

class SwitchExpressTest {
    /*
     * 举例1：
     * JDK12之前的写法
     */
    @Test
    public void test1() {
        Week day = Week.FRIDAY;
        switch (day) {
            case MONDAY:
                System.out.println(1);
                break;
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
                System.out.println(2);
                break;
            case FRIDAY:
                System.out.println(3);
                break;
            case SATURDAY:
            case SUNDAY:
                System.out.println(4);
                break;
            default:
                throw new RuntimeException("What day is today?" + day);
        }
    }

    /*
     * 举例1：
     * JDK12中的写法：switch表达式，省去了break语句，避免了因少写break而出现case穿透
     */
    @Test
    public void test2() {
        Week day = Week.FRIDAY;
        switch (day) {
            case MONDAY -> System.out.println(1);
            case TUESDAY, WEDNESDAY, THURSDAY -> System.out.println(2);
            case FRIDAY -> System.out.println(3);
            case SATURDAY, SUNDAY -> System.out.println(4);
            default -> throw new RuntimeException("What day is today?" + day);
        }
    }

    /*
     * 举例1：
     * JDK12中的写法：还可以使用变量接收switch表达式的结果。
     *
     * 演示 switch 表达式（switch expression）：整个 switch 会"算出一个值"，
     * 所以可以像调用方法那样写在赋值号右边，每个分支负责交出这个值。
     *
     * 有返回值，就要求"所有可能到达的分支都必须给出值"，
     * 这就是为什么这里 default 不能省（即使枚举常量已经写全，编译器仍要求穷尽）
     */
    @Test
    public void test3() {
        Week day = Week.FRIDAY;
        int result = switch (day) {
            case MONDAY -> 1;
            case TUESDAY, WEDNESDAY, THURSDAY -> 2;
            case FRIDAY -> 3;
            case SATURDAY, SUNDAY -> 4;
            default -> throw new RuntimeException("What day is today?" + day);
        };

        System.out.println(result);
    }

    /*
     * 举例1：(在default中返回值5)
     * JDK13中的写法：引入了yield关键字，用于返回指定的数据，结束switch结构。
     * switch表达式(返回值)应该使用yield，switch语句(不返回值)应该使用break。
     *
     * return会直接跳出当前方法，而yield只会跳出当前switch块。
     * yield = "把值交出去，同时结束本分支"。
     * yield 只跳出当前这个 switch，方法继续往下走。
     *
     * 当分支右边不再是"单个表达式"，而是一个 { } 代码块时，
     * 编译器不知道块里这么多语句中哪一句是结果 —— 于是必须用 yield 显式指出。
     *   1) 箭头 + 单表达式：case A -> 1;            直接就是值，不需要 yield；
     *   2) 箭头 + 代码块：  case A -> { ... yield 1; }  必须 yield，否则编译不过；
     *   3) 冒号标签 + yield：case A: yield 1;       也能编过（见 test5），但不推荐。
     * yield 是"上下文关键字"：它只在 switch 表达式里的分支中才是关键字，
     * 在别处仍然可以当普通变量/方法名用（比如 Thread.yield()），
     */
    @Test
    public void test4() {
        Week day = Week.FRIDAY;
        int result = switch (day) {
            case MONDAY -> {
                yield 1;
            }
            case TUESDAY, WEDNESDAY, THURSDAY -> {
                yield 2;
            }
            case FRIDAY -> {
                yield 3;
            }
            case SATURDAY, SUNDAY -> {
                yield 4;
            }
            default -> {
                System.out.println("值未找到");
                yield 5;
            }
        };

        System.out.println(result);
    }

    /*
     * 演示"冒号标签 + yield"的混搭写法 —— 语言允许，但不是好写法。
     * 在 switch 表达式中，用冒号标签时，每个分支都必须以 yield（或 throw）结束
     */
    @Test
    public void test5() {
        Week day = Week.FRIDAY;
        int result = switch (day) {
            case MONDAY:
                yield 1;
            case TUESDAY, WEDNESDAY, THURSDAY:
                yield 2;
            case FRIDAY:
                yield 3;
            case SATURDAY, SUNDAY:
                yield 4;
            default:
                yield 5;
        };

        System.out.println(result);
    }
}


/**
 * switch模式匹配（pattern matching）
 */
/*
 * formatter         —— JDK17 之前的写法：一串 if (o instanceof XXX x) 类型判断；
 * formatterSwitchPattern —— JDK17 预览写法：把同样的类型判断写成 case 标签。
 * switch 模式匹配在 JDK17 是预览特性，JDK21 才转正；
 * 预览特性必须加 --enable-preview 编译运行。
 */
class SwitchPatternTest {
    public static void main(String[] args) {

        System.out.println(formatter(12));
//        System.out.println(formatterSwitchPattern(12));
    }

    /**
     * JDK17之前
     */
    /*
     * 演示"传统做法"：用 if-else + instanceof 做类型分派。
     * 分支顺序有讲究：这里 Integer / Long / Double / String 之间没有子类关系，
     * 顺序随便；但如果加了 Number 这类父类判断，就必须把更具体的类型写在前面，
     * 否则永远轮不到后面的分支（这点在下面的 switch 版本里同样成立）；
     */
    static String formatter(Object o) {
        String formatted = "unknown";
        if (o instanceof Integer i) {
            formatted = "int " + i;
        } else if (o instanceof Long l) {
            formatted = "long " + l;
        } else if (o instanceof Double d) {
            formatted = "double " + d;
        } else if (o instanceof String s) {
            formatted = "String " + s;
        }
        return formatted;
    }

    /**
     * JDK17中switch的模式匹配
     */
    /*
     * 这是上一段 formatter 的"新写法"版本：把 if-else 链整体搬进 switch，
     * case 标签不再是常量而是"类型模式"（case Integer i），
     * 命中后 i 就是这个分支里的强转结果变量。
     */
//    static String formatterSwitchPattern(Object o) {
//        String formatted = switch (o) {
//            case Integer i:
//                yield "int " + i;
//            case Long l:
//                yield "long " + l;
//            case Double d:
//                yield "double " + d;
//            case String s:
//                yield "String " + s;
//            default:
//                yield o.toString();
//        };
//
//        return formatted;
//    }
}
