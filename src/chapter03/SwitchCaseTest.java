package chapter03;

public class SwitchCaseTest {
    public static void main(String[] args) {
        /*
        switch-case的使用，分支结构
        1.语法格式
        switch(表达式) {
            case 常量1:
                //执行语句1
                //break;
            case 常量2:
                //执行语句2
                //break;
            ......
            default:
                //执行语句3
                //break;
        }
        2.执行过程：
        根据表达式的值，依次匹配case语句，一旦与某一个case中的常量相等，那么就执行此case中的执行语句，执行完此执行语句之后
            情况1:遇到break，则执行break后，跳出当前的switch-case结构
            情况2:没有遇到break，则继续执行其后的case中的执行语句，直到遇到break或执行完所有case及default中的语句，退出当前switch-case结构
        3.说明
        （1）switch中的表达式只能是特定的数据类型（byte short char int 枚举 String）
        （2）case后都是跟的常量，使用表达式与这些常量做相等的判断，不能进行范围的判断
        （3）使用switch-case时，case匹配的的情况有限。
        （4）break 可以使用在switch-case结构中，一旦执行break关键字，就跳出当前switch-case结构
        （5）default 类似于if-else中的else，也是可选的，而且位置时灵活的，可以放在任何位置，规范上推荐放最后
         */

        int num = 1;
        switch (num) {

            case 0:
                System.out.println("0");
                break;
            case 1:
                System.out.println("1");
                break; // 结束（跳出）当前的switch-case结构
            case 2:
                System.out.println("2");
                break;
            case 3:
                System.out.println("3");
                break;
            default:
                System.out.println("other");
                break; // 规范上推荐保留
        } // 1
        //如果都没有break，则输出的是 1 2 3 other

        //例子
        String season = "summer";
        switch (season) {
            case "spring":
                System.out.println("spring");
                break;
            case "summer":
                System.out.println("summer");
                break;
            case "autumn":
                System.out.println("autumn");
                break;
            case "winter":
                System.out.println("winter");
                break;
            default:
                System.out.println("other");
                break;
        }

        /*
        结果为 summer
        新式写法，这种写法不需要加break，但不能累加输出下一个
        String season = "summer";
        switch (season) {
            case "spring" -> System.out.println("spring");
            case "summer" -> System.out.println("summer");
            case "autumn" -> System.out.println("autumn");
            case "winter" -> System.out.println("winter");
            default -> System.out.println("other");
        }
         */

    }
}
