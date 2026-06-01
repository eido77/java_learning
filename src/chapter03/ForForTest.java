package chapter03;

public class ForForTest {
    public static void main(String[] args) {
        /*
        嵌套循环：一个循环结构A的循环体是另一个循环结构B
        外层循环（循环结构A），内层循环（循环结构B）
        实际开发中，三层的循环结构都少见
         */
        for (int j = 1; j <= 5; j++) {
            for (int i = 1; i <= 6; i++) {
                System.out.print("*");
            }
            System.out.println();
        } // 5行“******”

        for (int c = 1; c <= 4; c++) {
            for (int b = 1; b <= 5; b++) {
                for (int a = 1; a <= 6; a++) {
                    System.out.print("*");
                }
                System.out.println();
            }
            System.out.println();
        } // 四段上面的那个，每段之间有空行

        /*
         *
         **
         ***
         ****
         *****
         */
        for (int m = 1; m <= 5; m++) {
            for (int n = 1; n <= m; n++) {
                System.out.print("*");
            }
            System.out.println();
        }

        /*
         ******
         *****
         ****
         ***
         **
         *
         */
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 7 - i; j++) {
                System.out.print("*");
            }
            System.out.println();
        }

        //Cmd + Shift + Option + V（粘贴时不格式化 / Paste Simple）
        /*                                  i（第几行）   j（-）    k（*）
            *                               1           8(视频)   1      2 * i + j = 10;
          * * *                             2           6        3      --j = 10 - 2 * i;
        * * * * *                           3           4        5      k = 2 * i - 1
      * * * * * * *                         4           2        7
    * * * * * * * * * // 从这分上下两部分      5           0        9   ---------------------
      * * * * * * *                        -1           2        7      -2 * i + 9 = k;
        * * * * *                           2           4        5      j = 2 * i;
          * * *                             3           6        3
            *                               4           8        1
        */
        //上半部分
        for (int i = 1; i <= 5; i++) {
            // \t
            for (int j = 1; j <= 10 - 2 * i;j++) {
                System.out.print(" "); // 为了方便看先用“-”代替空格
            }
            // *
            for (int k = 1; k <= 2 * i - 1; k++) {
                System.out.print("* ");
            }
            System.out.println();
        }
        //下半部分
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 2 * i; j++) {
                System.out.print(" ");
            }
            for (int k = 1; k <= -2 * i + 9; k++) {
                System.out.print("* ");
            }
            System.out.println();
        }

    }
}
