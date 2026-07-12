package chapter07;

public class BankTest {
    public static void main(String[] args) {
        Bank bank = new Bank();

        bank.addCustomer("John", "Smith");
        bank.addCustomer("Jane", "Smith");

        bank.getCustomer(0).setAccount(new Account(1000));
        bank.getCustomer(0).getAccount().withdraw(50); // Withdrawn 50.0
        System.out.println("账户余额为：" +bank.getCustomer(0).getAccount().getBalance()); // 950.0

    }
}
