package chapter17;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

public class AnnotationReflectionTest {
    /*
    针对于注解中信息的获取
    1. 公式： 框架 = 反射 + 注解 + 设计模式
    2. （了解）自定义注解在框架中的使用
     */
}

/*
 * 「元注解」= 用来修饰注解的注解（meta-annotation）。
 * JDK 自带的元注解一共 5 个（前两个最重要，必须掌握；后三个了解即可）：
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ① @Target        —— 规定这个注解「能贴在什么地方」                          │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ 括号能不能空：  不能空！它的 value 没有 default，必须给值。                    │
 * │               不写 @Target（整个元注解都省略）是合法的，表示「哪都能贴」。       │
 * │ 括号里填什么：  ElementType 枚举常量，可以填一个，也可以填多个（数组）。          │
 * ├── ElementType 常用取值 ──────────────────────────────────────────────────┤
 * │   TYPE               类、接口、枚举、注解类型上                             │
 * │   FIELD              成员变量（含枚举常量）上                               │
 * │   METHOD             方法上                                              │
 * │   PARAMETER          方法/构造器的形参上                                   │
 * │   CONSTRUCTOR        构造器上                                             │
 * │   LOCAL_VARIABLE     局部变量上                                           │
 * │   ANNOTATION_TYPE    只能贴在注解上（元注解自己就是这么限定的）                │
 * │   PACKAGE            包上（写在 package-info.java 里）                     │
 * │   TYPE_PARAMETER     泛型参数上           （JDK 8 新增）                   │
 * │   TYPE_USE           任何「用到类型」的地方（JDK 8 新增，最宽松）              │
 * │   MODULE             模块上               （JDK 9 新增）                   │
 * │   RECORD_COMPONENT   record 的组件上      （JDK 16 新增）                  │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ② @Retention     —— 规定这个注解「能活到什么阶段」（生命周期/保留策略）         │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ 括号能不能空：  不能空！同样没有 default，@Retention() 编译报错。              │
 * │               不写 @Retention 时，默认按 CLASS 处理。                      │
 * │ 括号里填什么：  RetentionPolicy 枚举常量，只能填一个（不是数组）。              │
 * ├── RetentionPolicy 三个取值 ──────────────────────────────────────────────┤
 * │   SOURCE    只存在于源码，javac 编译完就丢掉，.class 里没有。                │
 * │             典型：@Override、@SuppressWarnings —— 只给编译器/IDE 看。      │
 * │   CLASS     保留到 .class 文件，但 JVM 加载类时不读进内存（默认值）。          │
 * │             典型：字节码增强工具（Lombok、插桩框架）用。                      │
 * │   RUNTIME   保留到 .class，并且运行期 JVM 会加载进内存。                     │
 * │             ★ 只有 RUNTIME 才能被反射读到！                                │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ③ @Documented    —— 被它修饰的注解，会出现在 javadoc 生成的 API 文档里       │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ 括号能不能空：  它本身没有任何属性，所以写 @Documented 就行，                   │
 * │               写成 @Documented() 也合法但没人这么写。                       │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ④ @Inherited     —— 父类上的该注解，可以被子类「继承」到                     │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ 括号能不能空：  没有属性，同 @Documented，直接写 @Inherited。                 │
 * │ 易错点：        只对「类上的注解」生效，方法/属性上的注解不会被继承。             │
 * │                 加了它以后，子类用 getAnnotation() 能拿到父类的注解。         │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ⑤ @Repeatable    —— 允许同一个注解在同一个位置重复贴多次（JDK 8 新增）         │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ 括号能不能空：  不能空！必须填「容器注解」的 Class，例如 @Repeatable(Cs.class)  │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * @Documented / @Inherited 可以不带括号；
 * @Target / @Retention / @Repeatable 必须带括号且必须有值。
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface Table {
    String value() default "";
}

/*
 * 上面只有一个属性且叫 value，所以使用时能省略 "value ="；
 * 这里有两个属性 columnName、columnType，而且都没有 default，于是：
 *   - 使用 @Column 时「两个都必须显式赋值」，少写一个就编译报错；
 *   - 因为不止一个属性，所以不能省略属性名，必须写成 columnName = "...", columnType = "..."。
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface Column {
    String columnName();
    String columnType();
}

/*
 * value="t_customer"：给注解的属性赋值
 *   "t_customer" → 这个属性传的实际值，即这个类对应的数据库表名
 *
 * 可以写成 @Table("t_customer")，和现在的 @Table(value="t_customer") 等价。
 * 当本次赋值「只涉及名为 value 的那一个属性」时，才能省略 "value ="。
 */
@Table(value = "t_customer")
class Customer {
    /*
     * SQL 的习惯是「全小写 + 下划线分词」（snake_case），例如 cust_name、create_time
     * varchar(15) 表示最多 15 个字符的可变长字符串，和 Java 的 String 对应
     */
    @Column(columnName = "cust_name", columnType = "varchar(15)")
    private String name;
    @Column(columnName = "cust_age", columnType = "int")
    public int age;

    public Customer() {
//        System.out.println("Customer()...");
    }

    public Customer(int age) {
        this.age = age;
    }

    private Customer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void show() {
        System.out.println("你好，我是一个Customer");
    }

    private String showNation(String nation) {
        return "我的国籍是：" + nation;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

class AnnotationTest {
    /**
     * 获取类声明上的注解
     */
    @Test
    public void test1() {
        Class<Customer> clazz = Customer.class;

        /*
         * getDeclaredAnnotation(X.class)
         *   「在当前这个元素（这里是 Customer 类）自己声明的注解里，找类型为 X 的那一个；找到返回它，找不到返回 null。」
         *
         * 填的是「类字面量」：类型名 + .class，不是字符串，也不是全类名字符串。
         *  - Table 和当前代码在同一个包（chapter17）里，所以直接写 Table.class；
         *  - 如果在别的包，就先 import 那个包的 Table，然后同样写 Table.class；
         *  - 只有「同名类冲突、无法 import」时才会写成 chapter17.Table.class 这种带包名的形式。
         */
        Table annotation = clazz.getDeclaredAnnotation(Table.class);

        /*
         * 定义：@interface Table { String value() default "abc"; }    ← 声明了属性 value
         * 使用：@Table(value = "t_customer")                          ← 给 value 赋值
         * 读取：annotation.value()                                    ← 把值取出来，得到 "t_customer"
         */
        System.out.println(annotation.value()); // t_customer
    }

    /**
     * 获取属性声明的注解
     */
    @Test
    public void test2() throws Exception {
        Class<Customer> clazz = Customer.class;

        /*
         * getDeclaredField()
         * 按「成员变量名（字段名）」从当前类中找出那个字段，返回一个 Field 对象，用来后续读写值或读注解。
         */
        Field nameField = clazz.getDeclaredField("name");

        // 获取属性声明上的注解
        Column nameColumn = nameField.getDeclaredAnnotation(Column.class);

        System.out.println(nameColumn.columnName()); // cust_name
        System.out.println(nameColumn.columnType()); // varchar(15)
    }
}
