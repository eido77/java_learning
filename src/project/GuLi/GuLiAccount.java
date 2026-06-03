package project.GuLi;

//Fn+cmomand+F2是停止
public class GuLiAccount {
    public static void main(String[] args) {
        boolean isFlag = true; // 控制循环的结束
        //变量声明在循环体 {} 里面 → 每次循环重新创建;声明在循环外面 → 跨循环保留。
        int balance = 10000; // 初始金额
        String info = ""; // 记录收支信息
        //阶段一：谷粒记账软件
        while (isFlag) {
            System.out.println("-----------------谷粒记账软件-----------------\n");
            System.out.println("                   1 收支明细");
            System.out.println("                   2 登记收入");
            System.out.println("                   3 登记支出");
            System.out.println("                   4 退    出\n");
            System.out.print("                   请选择(1-4)：");
            //如果在while(true)的大括号外面会报错，死循环下面的代码不会执行，这样selection声明了但是没使用
            char selection = Utility.readMenuSelection(); // 获取用户的选项'1' '2' '3' '4'
            switch (selection) {
                case '1':
                    System.out.println("-----------------当前收支明细记录-----------------");
                    System.out.println("收支\t账户金额\t收支金额\t说    明\n");
                    System.out.println(info);
                    System.out.println("--------------------------------------------------");
                    break; // 退出switch
                case '2':
                    System.out.println("本次收入金额：");
                    int money1 = Utility.readNumber();
                    if (money1 > 0) {
                        balance += money1;
                    }
                    System.out.println("本次收入说明：");
                    String addDesc = Utility.readString();
                    //info = info + ("收入\t" + balance + ...)
                    info += "收入\t" + balance + "\t" + money1 + "\t\t" + addDesc + "\n";
                    System.out.println("---------------------登记完成---------------------");
                    break;
                case '3':
                    System.out.println("本次支出金额：");
                    int money2 = Utility.readNumber();
                    if (money2 > 0 && money2 <= balance) {
                        balance -= money2;
                    }
                    System.out.println("本次支出说明：");
                    String subDesc = Utility.readString();
                    //字符串没法做减法，不是-=
                    info += "支出\t" + balance + "\t" + money2 + "\t\t" + subDesc + "\n";
                    break;
                case '4':
                    System.out.println("\n确认是否退出（Y/N）：");
                    char isExit = Utility.readConfirmSelection();
                    if (isExit == 'Y') {
                        isFlag = false;
                    }
                    break;
                default:
                    break;
            }
        }


    }
}