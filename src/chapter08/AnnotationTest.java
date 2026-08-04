package chapter08;

import java.util.Date;

public class AnnotationTest {
    /*
    注解的使用
    1. Annotation的理解
    > 注解（Annotation）是从`JDK5.0`开始引入，以“`@注解名`”在代码中存在。
    > Annotation 可以像修饰符一样被使用，可用于修饰包、类、构造器、方法、成员变量、参数、局部变量的声明。
      还可以添加一些参数值，这些信息被保存在 Annotation 的 “name=value” 对中。
    > 注解可以在类编译、运行时进行加载，体现不同的功能。
    2. 注解的应用场景：
    示例1：生成文档相关的注解（如 @author、@see 等）
    示例2：在编译时进行格式检查(JDK内置的三个基本注解)
    示例3：跟踪代码依赖性，实现替代配置文件功能（框架中大量使用）
    3. Java基础涉及到的三个常用注解
    `@Override`: 限定重写父类方法，该注解只能用于方法
    `@Deprecated`: 用于表示所修饰的元素(类，方法等)已过时。通常是因为所修饰的结构危险或存在更好的选择
    `@SuppressWarnings`: 抑制编译器警告
    4. 自定义注解
    以@SuppressWarnings为参照，进行定义即可。（见文件末尾 MyAnnotation）。
       > 注解成员以“方法”的形式声明，可以用 default 指定默认值。
       > 若成员名为 value 且只需赋这一个值，使用时可省略 "value="。
    5. 元注解的理解：
    元注解：对现有的注解进行解释说明的注解。
    讲4个元注解：
    （1）@Target：用于描述注解的使用范围
    可以通过枚举类型ElementType的10个常量对象来指定
    TYPE，METHOD，CONSTRUCTOR，PACKAGE.....
    （2）@Retention：用于描述注解的生命周期
    可以通过枚举类型RetentionPolicy的3个常量对象来指定
    SOURCE（源代码）、CLASS（字节码）、RUNTIME（运行时）
    唯有RUNTIME阶段才能被反射读取到。
    （3）@Documented：表明这个注解应该被 javadoc工具记录。
    （4）@Inherited：允许子类继承父类中的注解
    拓展： 元数据（metadata），即“描述数据的数据”，如 String name = "Tom"; 中name、String 这类信息。
    String name = "Tom";
    框架 = 注解 + 反射 + 设计模式
     */
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date); // Tue Aug 04 20:28:14 CST 2026

        @SuppressWarnings("deprecation")
        Date date1 = new Date(2026, 8, 3);
        System.out.println(date1); // Fri Sep 03 00:00:00 CST 3926

        Person3 p1 = new Person3();
        Person3 p2 = new Person3("Tom");
        System.out.println(p2); // Person3{name='Tom', age=0}

        // @SuppressWarnings 修饰局部变量，抑制“变量未使用”警告
        @SuppressWarnings("unused") int num = 10;
    }
}

// value 是唯一成员，使用时可省略 "value="，直接写 @MyAnnotation("class")
@MyAnnotation(value = "class")
class Person3 {
    String name;
    int age;

    // 关于括号能否省略：
    // - MyAnnotation 的成员 value 有 default 值，所以调用时可不传值。
    // - 当注解“所有成员都有 default 值”或“没有任何成员”时，括号可整个省略，
    //   即可写成 @MyAnnotation；空括号 @MyAnnotation() 也合法。
    // - 若存在没有 default 值的成员，则必须写括号并在其中赋值。
    @MyAnnotation()
    public Person3() {
    }

    // @Deprecated：标记此构造器已过时，调用处会出现删除线警告
    @Deprecated
    public Person3(String name) {
        this.name = name;
    }

    public void eat() {
        System.out.println("eating");
    }

    @Override
    public String toString() {
        return "Person3{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

class Student1 extends Person3 {
    // @Override 会让编译器检查该方法是否真的重写了父类方法。
    // 若方法名写错、参数列表不一致、父类没有该方法等导致“并未构成重写”，
    // 就会报错：method does not override ... from a supertype。
    @Override
    public void eat() {
        System.out.println("student eating");
    }
}

// 自定义注解：
// - 成员以“无参方法”的形式声明；
// - default 指定默认值；
// - 通常还会配 @Target / @Retention 等元注解来限定用法
@interface MyAnnotation {
    String value() default "hello";
}
