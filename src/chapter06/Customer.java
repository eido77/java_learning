package chapter06;

public class Customer {
    /*
    1、写一个名为Account的类模拟账户。该类的属性和方法如下图所示。
    该类包括的属性：账号id，余额balance，年利率annualInterestRate；
    包含的构造器：自定义
    包含的方法：访问器方法（getter和setter方法），取款方法withdraw()，存款方法deposit()。
    提示：在提款方法withdraw中，需要判断用户余额是否能够满足提款数额的要求，如果不能，应给出提示。

    2、创建Customer类。
    a. 声明三个私有对象属性：firstName、lastName和account。
    b. 声明一个公有构造器，这个构造器带有两个代表对象属性的参数（f和l）
    c. 声明两个公有存取器来访问该对象属性，方法getFirstName和getLastName返回相应的属性。
    d. 声明setAccount 方法来对account属性赋值。
    e. 声明getAccount 方法以获取account属性。

    3、写一个测试程序。
    （1）创建一个Customer ，名字叫 Jane Smith, 他有一个账号为1000，余额为2000元，年利率为 1.23％ 的账户。
    （2）对Jane Smith操作。
    存入 100 元，再取出960元。再取出2000元。
    打印出Jane Smith 的基本信息：
    成功存入 ：100.0
    成功取出：960.0
    余额不足，取款失败
    Customer [Smith, Jane] has a account: id is 1000, annualInterestRate is 1.23％, balance is 1140.0
     */


    // ========== 属性声明 ==========
    // 格式统一为：修饰符 + 类型 + 属性名
    // 类型可以是"基本类型"(int/double 等 8 种)，也可以是"任何一个类"。
    // String 和 Account 都属于后者——都是"用类当类型"，只不过 String 是 Java 官方写好的类，
    // Account 是我自己写的类，二者语法完全平级，用法没有任何区别。
    //   Java 里凡是"类"都能当类型用。
    //   String、Account、Customer... 定义出来后都能拿去声明变量/属性/参数/返回值。
    //   只有 int、double、char、boolean 等 8 个是"基本类型"，其余全是"引用类型(类)"。
    private String firstName; // 顾客的名。类型 String 是官方的类，存一个字符串
    private String lastName; // 顾客的姓。同上
    private Account account; // 顾客关联的账户。类型 Account 是我自己定义的类，存的是一个 Account 对象的引用（此处未赋值，默认 null）
    // → 体现"一个类的属性是另一个类的对象"，即类与类的关联关系

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

}
