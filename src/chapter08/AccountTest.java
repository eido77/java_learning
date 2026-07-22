package chapter08;

public class AccountTest {
    /*
    编写一个类实现银行账户的概念，包含的属性有“帐号”、“密码”、“存款余额”、“利率”、“最小余额”，
    定义封装这些属性的方法。账号要自动生成。
    编写主类，使用银行账户类，输入、输出3个储户的上述信息。
    考虑：哪些属性可以设计成static属性。
     */
    public static void main(String[] args) {
        Account account1 = new Account();
        System.out.println(account1); // Account{id=1001, password='123456', balance=0.0}

        Account account2 = new Account("000000", 2000);
        System.out.println(account2); // Account{id=1002, password='000000', balance=2000.0}

        Account.setInterestRate(0.0123);
        Account.setMinBalance(10);
        System.out.println("利率为：" + Account.getInterestRate()); // 利率为：0.0123
        System.out.println("最小存款额度为：" + Account.getMinBalance()); // 最小存款额度为：10.0
    }
}

class Account {
    private int id;
    private String password;
    private double balance;

    private static double interestRate; // 利率
    private static double minBalance = 1.0;

    private static int init = 1001; // 用于自动生成id的基数

    public Account() {
        this.id = init;
        init++;
        password = "123456";
    }

    public Account(String password, double balance) {
        this.password = password;
        this.balance = balance;
        this.id = init;
        init++;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static double getInterestRate() {
        return interestRate;
    }

    public static void setInterestRate(double interestRate) {
        Account.interestRate = interestRate;
    }

    public static double getMinBalance() {
        return minBalance;
    }

    public static void setMinBalance(double minBalance) {
        Account.minBalance = minBalance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}
