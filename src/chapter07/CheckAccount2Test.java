package chapter07;

public class CheckAccount2Test {
    /*
   类图Account2.png Account2调用结果.png CheckAccount2调用结果.png
   案例：
   1、写一个名为Account2的类模拟账户。该类的属性和方法如下图所示。
   该类包括的属性：账号id，余额balance，年利率annualInterestRate；
   包含的方法：访问器方法（getter和setter方法），返回月利率的方法getMonthlyInterest()，取款方法withdraw()，存款方法deposit()。

   写一个用户程序测试Account2类。在用户程序中，创建一个账号为1122、余额为20000、年利率4.5%的Account对象。
   使用withdraw方法提款30000元，并打印余额。
   再使用withdraw方法提款2500元，使用deposit方法存款3000元，然后打印余额和月利率。

   提示：在提款方法withdraw中，需要判断用户余额是否能够满足提款数额的要求，如果不能，应给出提示。
   运行结果如图所示。
   2、创建Account2类的一个子类CheckAccount2代表可透支的账户，该账户中定义一个属性overdraft代表可透支限额。
   在CheckAccount2类中重写withdraw方法，其算法如下：
   ————————————————————————————————————————
   如果（取款金额<账户余额），
       可直接取款
   如果（取款金额>账户余额），
       计算需要透支的额度
       判断可透支额overdraft是否足够支付本次透支需要，如果可以
           将账户余额修改为0，冲减可透支金额
       如果不可以
           提示用户超过可透支额的限额
   ————————————————————————————————————————
   要求：写一个用户程序测试CheckAccount2类。
   在用户程序中，创建一个账号为1122、余额为20000、年利率4.5%，可透支限额为5000元的CheckAccount2对象。

   使用withdraw方法提款5000元，并打印账户余额和可透支额。
   再使用withdraw方法提款18000元，并打印账户余额和可透支额。
   再使用withdraw方法提款3000元，并打印账户余额和可透支额。

   提示：
   （1）子类CheckAccount2的构造方法需要将从父类继承的3个属性和子类自己的属性全部初始化。
   （2）父类Account2的属性balance被设置为private，但在子类CheckAccount2的withdraw方法中需要修改它的值，
       因此应修改父类的balance属性，定义其为protected。
        */
    public static void main(String[] args) {
        CheckAccount2 checkAccount2 = new CheckAccount2(1122, 20000, 0.045, 5000);

        checkAccount2.withdraw(5000);
        System.out.println("账户余额：" + checkAccount2.getBalance() + "可透支额度：" + checkAccount2.getOverdraft()); // 账户余额：15000.0 可透支额度：5000.0

        checkAccount2.withdraw(18000);
        System.out.println("账户余额：" + checkAccount2.getBalance() + "可透支额度：" + checkAccount2.getOverdraft()); // 账户余额：0.0 可透支额度：2000.0

        checkAccount2.withdraw(3000);
        System.out.println("账户余额：" + checkAccount2.getBalance() + "可透支额度：" + checkAccount2.getOverdraft()); // 超过可透支限额！账户余额：0.0 可透支额度：2000.0
    }
}
