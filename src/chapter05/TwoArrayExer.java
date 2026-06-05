package chapter05;

public class TwoArrayExer {
    public static void main(String[] args) {
        //案例1：获取arr数组中所有元素的和。提示：使用for的嵌套循环即可。
        int[][] arr = new int[][]{{3, 5, 8}, {12, 9}, {7, 0, 6, 4}};
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                sum += arr[i][j];
            }
        }
        System.out.println("总和为：" + sum); // 54

        /*
        案例2：声明：int[] x,y[]; 在给x,y变量赋值以后，以下选项允许通过编译的是：
        声明：int[] x,y[]; 在给x,y变量赋值以后，以下选项允许通过编译的是： x：一维int[]   y：二维int[][]
        赋值时，把等号左右两边都看成"元素类型 + 维度"。两个条件必须同时满足：
            1.维度数相同 2.最底层的元素类型相同
        [I@24d46ca6
        数左边的 [ 定维度，看紧跟的字母（或 L类名;）定元素类型。
        a)    x[0] = y;             no
        b)    y[0] = x;             yes
        c)    y[0][0] = x;          no
        d)    x[0][0] = y;          no
        e)    y[0][0] = x[0];       yes
        f)    x = y;                no
        提示：
        一维数组：int[] x  或者int x[]
        二维数组：int[][] y 或者  int[] y[]  或者 int  y[][]
         */

        //案例2的举例
        int[] ar1 = new int[10];
        byte[] ar2 = new byte[20];
        //编译不通过，int[] byte[]是两种不同类型的引用变量
        //ar1 = ar2;
        System.out.println(ar1); // [I@24d46ca6
        System.out.println(ar2); // [B@4517d9a3

        int[][] ar3 = new int[3][2];
        //编译不通过
        //ar3 = ar1;
        ar3[0] = ar1;
        //地址类型一样
        System.out.println(ar3[0]); // [I@24d46ca6

        /*
        案例3：二维数组存储数据，并遍历
        其中"10"代表普通职员，"11"代表程序员，"12"代表设计师，"13"代表架构师。显示效果如图。
         */
        String[][] employees = {
                {"10", "1", "段 誉", "22", "3000"},
                {"13", "2", "令狐冲", "32", "18000", "15000", "2000"},
                {"11", "3", "任我行", "23", "7000"},
                {"11", "4", "张三丰", "24", "7300"},
                {"12", "5", "周芷若", "28", "10000", "5000"},
                {"11", "6", "赵 敏", "22", "6800"},
                {"12", "7", "张无忌", "29", "10800", "5200"},
                {"13", "8", "韦小宝", "30", "19800", "15000", "2500"},
                {"12", "9", "杨 过", "26", "9800", "5500"},
                {"11", "10", "小龙女", "21", "6600"},
                {"11", "11", "郭 靖", "25", "7100"},
                {"12", "12", "黄 蓉", "27", "9600", "4800"}
        };
        System.out.println("员工类型\t编号\t姓名\t\t年龄\t薪资\t\t奖金\t\t股票");
        for (int i = 0; i < employees.length; i++) {
            String employeeType = employees[i][0];
            switch (employeeType) {
                case "10":
                    System.out.print("普通职员\t");
                    break;
                case "11":
                    System.out.print("程序员\t");
                    break;
                case "12":
                    System.out.print("设计师\t");
                    break;
                case "13":
                    System.out.print("架构师\t");
                    break;
            }

            for (int j = 1; j < employees[i].length; j++) {
                System.out.print(employees[i][j] + "\t");
            }
            System.out.println();
        }


    }
}
