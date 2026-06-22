package chapter06;

public class ArgsTest {
    public static void main(String[] args) {
        ArgsTest test = new ArgsTest();
        //编译器先找参数个数和类型精确匹配的固定方法，找不到才用可变参数兜底。
        test.print(); // 111
        test.print(1); // 222
        test.print(1, 2); // 333

        test.print(new int[]{1, 2, 3}); // 1 2 3
        //test.print(1, 2, 3); // 1 2 3


        /*
        练习：可变形参的方法
        n个字符串进行拼接，每一个字符串之间使用某字符进行分割，如果没有传入字符串，那么返回空字符串""
        */
        String info = test.concat("-", "hello", "world");
        System.out.println(info); // hello-world
        System.out.println(test.concat("/", "hello")); // hello
        System.out.println(test.concat("-")); //
    }

    public void print(int ... nums) {
        System.out.println("111");

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
    }
    //报错
    //int... nums 底层等价于 int[] nums，能接收 0 到 N 个 int 实参。
//     public void print(int[] nums) {
//        for (int i = 0; i < nums.length; i++) {
//            System.out.print(nums[i] + " ");
//        }
//     }

    public void print(int i, int ... nums) {

    }
    //报错，varargs parameter must be the last parameter
//    public void print(int ... nums, int i) {
//
//    }

    public void print(int i) {
        System.out.println("222");
    }

    public void print(int i, int j) {
        System.out.println("333");
    }

    /*
    场景举例：
    * String sql = "update customers set name = ?,email = ? where id = ?";
    *
    * String sql1 = "update customers set name = ? where id = ?";
    *
    * public void update(String sql,Object ... objs);
    *
    * */


    /*
    练习：可变形参的方法
    n个字符串进行拼接，每一个字符串之间使用某字符进行分割，如果没有传入字符串，那么返回空字符串""
    */
    public String concat(String operator, String ... strs) {
        String result = "";

        for (int i = 0; i < strs.length; i++) {
            if (i == 0) {
                result += strs[i];
            } else {
                result += operator + strs[i]; // hello-world
            }
        }
        return result;
    }


}
