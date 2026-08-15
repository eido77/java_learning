package chapter09;

public class CustomExceptionTest {
    /*
    1. 如何自定义异常类？
    ① 继承于现有的异常体系。通常继承于RuntimeException（运行时异常） \ Exception（编译时异常/非运行时异常）
    ② 通常提供几个重载的构造器
    ③ 提供一个全局常量，声明为：static final long serialVersionUID = 某个long值;
    2. 如何使用自定义异常类？
    > 在具体的代码中，满足指定条件的情况下，需要手动的使用"throw + 自定义异常类的对象"方式，将异常对象抛出。
    > 如果自定义异常类是非运行时异常（继承自Exception），则必须考虑如何处理此异常类的对象。（具体的：① try-catch-finally ② throws）
    3. 为什么需要自定义异常类？
    我们其实更关心的是，通过异常的名称就能直接判断此异常出现的原因。既然如此，我们就有必要在实际开发场景中，
    不满足我们指定的条件时，指明我们自己特有的异常类。通过此异常类的名称，就能判断出具体出现的问题。

    Throwable
     ├── Error
     └── Exception
          ├── RuntimeException
          └── 其他Exception子类
     */
    int id;

    /*
   - 不是BelowZeroException继承什么，这里就throws什么，而是看你抛出的异常"是不是编译时异常"。
   - 因为 BelowZeroException 继承的是 Exception（编译时异常），编译器强制要求你处理，
     所以方法上必须 throws（或者在方法内部 try-catch）。
   - 这里 throws Exception 是可以的（因为 BelowZeroException 是 Exception 的子类，
     用父类声明也合法）。但更规范、更清晰的写法是直接声明具体的异常类型：
         public void register(int id) throws BelowZeroException {
   - 如果当初 BelowZeroException 继承的是 RuntimeException，那这里就可以【不写】throws，
     调用处也可以【不强制】try-catch，编译能通过（运行时才会报错）。
   */
    public void register(int id) throws Exception {
        if (id > 0) {
            this.id = id;
        } else {
            throw new BelowZeroException();
        }
    }
}

/*
【Q：为什么有这么多构造器？都是干什么的？为什么只有构造器？】
  这些构造器不是我们凭空写的，而是"对应/调用"了父类 Exception 已有的构造器，作用是：
    - 无参构造器 BelowZeroException()：创建异常时不带任何信息。
    - 带 String message 的：创建异常时带一句"错误提示信息"，之后可用 getMessage() 取出来。
    - 带 message + Throwable cause 的：既带提示信息，又带"引起本异常的原因异常"（异常链）。
  只有构造器、没有别的方法，是因为异常类本身的功能（如 getMessage、printStackTrace）
  都从父类 Exception 继承来了，我们通常不需要再写额外方法，只需要"提供入口"即可。

【Q：构造器数量比 Exception 少，比 Java 自带的那些少，有影响吗？】
  没有影响。你想提供几个就提供几个，甚至只写无参构造器也能用。
  提供多个只是为了"用起来更灵活"（既能不带信息抛，也能带提示信息抛）。
  少写一两个不会报错，只是抛异常时能传的信息少一些而已。

【Q：try-catch 里面能写这个自定义异常吗？】
  能。catch 后面既可以写 catch (Exception e)，也可以写 catch (BelowZeroException e)，
  因为它就是一个正常的异常类，用法和 Java 自带异常完全一样。

【Q：自定义异常类和普通类有什么区别？随便声明一个类都能当自定义异常类吗？】
  不能随便一个类就当异常用。关键区别在于：自定义异常类必须【直接或间接继承 Throwable】，
  实际开发中一般继承 Exception 或 RuntimeException。
  只有继承了异常体系，才能被 throw 抛出、被 catch 捕获。普通类不继承这些是不能 throw 的。
*/
class BelowZeroException extends Exception {
    // serialVersionUID 序列化版本号：用于在序列化和反序列化时判断类版本是否一致。
    // 用于标识类的版本。一般由IDE自动生成，也可以手动指定。
    static final long serialVersionUID = -3387516993124228L;

    public BelowZeroException() {
    }

    public BelowZeroException(String message) {
        super(message);
    }

    public BelowZeroException(String message, Throwable cause) {
        super(message, cause);
    }
}
