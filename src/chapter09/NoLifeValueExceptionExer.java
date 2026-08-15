package chapter09;

public class NoLifeValueExceptionExer {
    /*
    案例：游戏角色
    在一款角色扮演游戏中，每一个人都会有名字和生命值，角色的生命值不能为负数。
    要求：当一个人物的生命值为负数的时候需要抛出自定义的异常
    操作步骤描述：
    （1）自定义异常类NoLifeValueException继承RuntimeException
    ①提供空参和有参构造
    ②在有参构造中，需要调用父类的有参构造，把异常信息传入
    （2）定义Person类
    ①属性：名称(name)和生命值(lifeValue)
    ②提供setter和getter方法：
    在setLifeValue(int lifeValue)方法中，首先判断，如果 lifeValue为负数,就抛出NoLifeValueException，
    异常信息为：生命值不能为负数：xx；
    然后再给成员lifeValue赋值。
    ③提供空参构造
    ④提供有参构造：使用setXxx方法给name和lifeValue赋值
    （3）定义测试类NoLifeValueExceptionExer
    ① 使用满参构造方法创建Person对象，生命值传入一个负数
    由于一旦遇到异常,后面的代码的将不在执行,所以需要注释掉上面的代码
    ② 使用空参构造创建Person对象
    调用setLifeValue(int lifeValue)方法,传入一个正数,运行程序
    调用setLifeValue(int lifeValue)方法,传入一个负数,运行程序
    ③ 分别对①和②处理异常和不处理异常进行运行看效果
     */
    public static void main(String[] args) {
        // 1.使用有参的构造器
        Person p1 = new Person("Tom", 10);
        System.out.println(p1); // Person{name='Tom', lifeValue=10}

        // RuntimeException（运行时异常）如果不用 try-catch 处理，一旦抛出，
        // 当前方法（这里是 main）会立即终止，它后面的所有代码都不再执行。
        try {
            Person p2 = new Person("Jerry", -10);
            // 这行 System.out.println(p2) 打印 p2 的信息，本例中其实“没必要写”，也不会执行到。
            // 原因：上一行传入 -10，在构造 p2 时 setLifeValue 就会抛异常，
            // 程序立刻跳到下面的 catch，永远走不到这行打印，所以写不写都不影响本例结果。
            // 只有当传入的是正数（不抛异常）时，这行才会真正执行并打印出对象信息。
            System.out.println(p2);
        } catch (NoLifeValueException e) {
            System.out.println(e.getMessage()); // 打印：生命值不能为负数：-10
        }

        // 2.使用空参的构造器
        Person p3 = new Person();
        p3.setName("Jerry");
        p3.setLifeValue(-20); // NoLifeValueException: 生命值不能为负数：-20
    }
}

class NoLifeValueException extends RuntimeException {
    // 关于 serialVersionUID
    // 1) 不写也不会报错，程序能正常运行，它不是必须的（只是编译器可能给个黄色警告提示）
    // 2) 它跟“对象序列化”的版本控制有关，值可以是任意 long 数字，通常由 IDE 自动生成
    // 3) 自动生成方法：IDEA 中把光标放到类名上，option + Enter
    // 4) 不用手动去 RuntimeException 或 Exception 里复制，学习阶段这行甚至可以直接删掉
    static final long serialVersionUID = -7034897190745739L;

    public NoLifeValueException() {
    }

    public NoLifeValueException(String message) {
        // 这里的 super(message) 就是在完成题目要求②：
        // “在有参构造中，需要调用父类的有参构造，把异常信息传入”。
        // super(message) 会调用父类 RuntimeException 的有参构造，
        // 把 message 存到父类里，之后 getMessage() 或打印异常时就能显示这条信息。
        super(message);
    }
}

class Person {
    private String name;
    private int lifeValue;

    public Person() {
    }

    // 关于“构造器和方法执行顺序”：构造器在 new 对象时执行；构造器内部“调用”方法时，
    // 会先跳进那个方法执行完，再回到构造器继续往下走（是按代码顺序、一步步同步执行的）
    public Person(String name, int lifeValue) {
        setName(name);
        setLifeValue(lifeValue);

        // 反面示例：直接赋值会跳过校验，不推荐，故注释掉
//        this.lifeValue = lifeValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLifeValue() {
        return lifeValue;
    }

    public void setLifeValue(int lifeValue) {
        if (lifeValue < 0) {
            throw new NoLifeValueException("生命值不能为负数：" + lifeValue);
        }
        this.lifeValue = lifeValue;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", lifeValue=" + lifeValue +
                '}';
    }
}
