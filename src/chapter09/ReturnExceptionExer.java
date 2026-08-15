package chapter09;

public class ReturnExceptionExer {
    public static void main(String[] args) {
        try {
            methodA();
        } catch (Exception e) {
            // catch捕获methodA抛出的异常，打印异常信息"制造异常"
            System.out.println(e.getMessage());
        }

        methodB();
         /*
        实际输出结果（按顺序）：
        进入方法A            —— methodA的try先执行
        用A方法的finally      —— 抛异常前，finally一定先执行
        制造异常             —— 异常被抛出后，被main的catch捕获并打印
        进入方法B            —— methodB的try执行
        调用B方法的finally    —— return之前，finally一定先执行
         */
    }

    // methodA：演示 抛异常 时 finally 的执行时机
    static void methodA() throws Exception {
        try {
            System.out.println("进入方法A");
            // throw后不会立刻退出，会先执行finally，再把异常向上抛给调用者
            /*
            throw是"两步"动作：
            第①步：先 new Exception(...) 创建异常对象（在finally之前执行）
                  —— 所以debug单步时，光标会先停在这一行
            第②步：再真正"抛出"这个对象 —— 这一步才触发finally，之后异常才向上抛

            finally 保证在"方法真正离开之前"执行；但如果 throw/return 后面有
            表达式需要求值，求值动作会先发生（要先把"异常对象"或"返回值"准备好），
            之后才轮到 finally。
            */
            throw new Exception("制造异常");
        } finally {
            // finally在异常真正抛出之前执行，所以它比catch里的"制造异常"先打印
            System.out.println("用A方法的finally");
        }
    }

    // methodB：演示 return 时 finally 的执行时机
    static void methodB() {
        try {
            System.out.println("进入方法B");
            // return不会立刻返回，会先执行finally，再真正返回
            /*
            return; 是"零步"动作：后面没有表达式，没有值需要计算，
            所以没有任何东西可以在finally之前执行 —— debug时根本不会停在这一行，
            会直接跳去执行finally，finally走完后再真正返回。

            对比：如果写成 return 表达式（如 return getX();），
            那个表达式会先被求值（在finally之前），debug就能停在这一行了。
            */
            return;
        } finally {
            // finally在方法真正return之前执行
            System.out.println("调用B方法的finally");
        }
    }
}
