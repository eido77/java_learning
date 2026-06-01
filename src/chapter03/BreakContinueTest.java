package chapter03;

public class BreakContinueTest {
    public static void main(String[] args) {
        /*
        break和continue关键字
               使用范围               在循环结构中作用
        break：switch-case、循环结构  结束（跳出）当前循环结构
        continue：循环结构            结束（跳出）当次循环
        相同点：在此关键字后面不能声明执行语句
               嵌套循环中，只对直接包裹的那层循环起作用
         */

        for (int i = 1; i <= 10; i++) {
            if (i % 4 == 0) {
                //break; // 123
                continue; // 除了4和8，都有（123567910）
                //编译不通过
                //System.out.println("1");
            }

            System.out.print(i);
        }

        System.out.println();

        label: // 小驼峰（lowerCamelCase）
        for (int j = 1; j <= 4; j++) {
            for (int i = 1; i <= 10; i++) {
                if (i % 4 == 0) {
                    //break; // 4行123
                    //continue; // 4行123567910（输出一共显示5行是因为上面那个输出结果）
                    //标签写在被标记循环的前一行，break/continue 加标签可直接控制外层循环
                    // label，写在目标循环的前一行,中间不能插别的语句.break label;和continue label; 只能在这个循环内部使用。
                    //break label; // 123
                    continue label; // 123123123123,下面那个换行的也没执行
                }
                System.out.print(i);
            }
            System.out.println();
        }









    }
}
