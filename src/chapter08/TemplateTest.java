package chapter08;

public class TemplateTest {
    // 抽象应用案例：模板方法的设计模式
    public static void main(String[] args) {
        PrintPrimeNumber p = new PrintPrimeNumber();
        p.spendTime();
    }
}

abstract class Template {
    // 计算某段代码的执行需要花费的时间
    public void spendTime() {
        long start = System.currentTimeMillis();
        code();
        long end = System.currentTimeMillis();
        System.out.println("spend time: " + (end - start) + "ms");
    }

    public abstract void code();
}

class PrintPrimeNumber extends Template {
    @Override
    public void code() {
        for (int i = 2; i <= 100000; i++) {
            boolean isPrime = true;

            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                System.out.println(i);
            }
        }
    }
}
