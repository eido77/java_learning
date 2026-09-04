package chapter11;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 考查：方法参数的值传递机制、String的不可变性
 */
public class StringExer {
    /*
     * 1）str 是 String 类型（引用数据类型），change(ex.str, ...) 传的是「ex.str 所指向对象的地址值」的一份拷贝。
     *    在 change 内部 str = "test ok"，只是让「这份局部拷贝」指向了新的字符串对象，
     *    并没有改变 main 中 ex.str 的指向。加上 String 不可变，"good" 这个对象本身也不会被修改，
     *    所以 ex.str 依旧是 "good"。
     * 2）ch 是 char[] 类型（引用数据类型），传的同样是「数组地址值」的拷贝，
     *    但 change 内部执行的是 ch[0]='b'，这是通过地址去修改「同一个数组对象」里的元素，
     *    main 中的 ex.ch 和形参 ch 指向同一个数组，所以 ex.ch 变成了 best。
     */
    String str = "good";
    char[] ch = {'t', 'e', 's', 't'};

    public void change(String str, char ch[]) {
        str = "test ok";
        ch[0] = 'b';
    }

    public static void main(String[] args) {
        StringExer ex = new StringExer();
        ex.change(ex.str, ex.ch);
        System.out.println(ex.str); // good
        System.out.println(ex.ch); // best
    }
}

class StringAnswers {
    /**
     * 题目1：模拟一个trim方法，去除字符串两端的空格。
     */
    public String myTrim(String str) {
        if (str != null) {
            int start = 0; // 用于记录从前往后首次索引位置不是空格的位置的索引
            int end = str.length() - 1; // 用于记录从后往前首次索引位置不是空格的位置的索引

            // 从左往右跳过空格，找到第一个非空格字符的下标。注意条件 start < end，避免全空格串越界
            while (start < end && str.charAt(start) == ' ') {
                start++;
            }

            // 从右往左跳过空格，找到最后一个非空格字符的下标
            while (start < end && str.charAt(end) == ' ') {
                end--;
            }

            /*
             * 边界补丁：当字符串全是空格（或只剩一个空格）时，上面循环因 start < end 提前停止，
             * 此时 start 位置仍是空格，需单独返回空串，否则 substring 会把这个空格截进来。
             */
            if (str.charAt(start) == ' ') {
                return "";
            }

            // substring 前闭后开，所以结束下标要 +1 才能包含 end 处的字符
            return str.substring(start, end + 1);
        }
        return null;
    }

    @Test
    public void testMyTrim() {
        String str = "   a   ";
        String str1 = " ";
        String newStr = myTrim(str);
        System.out.println("---" + newStr + "---"); // ---a---
        String newStr1 = myTrim(str1);
        System.out.println("---" + newStr1 + "---"); // ------
    }

    /**
     * 题目2：将一个字符串进行反转。将字符串中指定部分进行反转。
     * 比如"abcdefg"反转为"abfedcg"
     */
    @Test
    public void test() {
        String s = "abcdefg";
        // start 和 end 都是「包含」的（闭区间），即反转下标 2~5 这几个字符
        String s1 = reverse1(s, 2, 5);
        String s2 = reverse2(s, 2, 5);
        System.out.println(s1);
        System.out.println(s2);
    }

    /**
     * 方式一：将String转为char[],针对char[]数组进行相应位置的反转，反转以后将char[]转为String
     */
    public String reverse1(String str, int start, int end) {
        if (str != null) {
            // 1. String 不可变，无法直接改某个位置，先转成可变的 char[] 才能原地交换
            char[] charArray = str.toCharArray();

            /*
             * 2.
             * 用 temp 做「三步交换」是最经典、最清晰的写法，完全规范，实际开发也这么写。
             * 双指针 i 从 start 向右、j 从 end 向左，相向而行，交换后同时收缩，i<j 时停止。
             */
            for (int i = start, j = end; i < j; i++, j--) {
                char temp = charArray[i];
                charArray[i] = charArray[j];
                charArray[j] = temp;
            }

            // 3. 把处理好的 char[] 重新包装成一个新的 String 返回
            return new String(charArray);
        }

        return null;
    }

    // 方式二：
    public String reverse2(String str, int start, int end) {
        // 1.截取 [0, start) 这段不需要反转的前缀，例如 "abcdefg" 取到 "ab"
        String newStr = str.substring(0, start); // ab

        /*
         * 2.把中间要反转的部分从后往前（i 从 end 递减到 start）逐个字符拼接，实现反转。
         * 提醒：这里用 String += 拼接，每次都会新建对象，效率低，
         * 数据量大时应改用方式三的 StringBuilder（下面 reverse3 就是改进版）。
         */
        for (int i = end; i >= start; i--) {
            newStr += str.charAt(i);
        } // abfedc

        // 3.拼上 [end+1, 末尾) 这段不需要反转的后缀
        newStr += str.substring(end + 1);
        return newStr;
    }

    // 方式三：推荐（相较于方式二做的改进）
    public String reverse3(String str, int start, int end) { // ArrayList list = new ArrayList(80);
        // 1. 用 StringBuilder 代替 String 拼接，避免循环里反复创建对象；预设容量 str.length() 减少扩容
        StringBuilder s = new StringBuilder(str.length());

        // 2. 先放入不反转的前缀 [0, start)
        s.append(str.substring(0, start)); // ab

        // 3. 中间部分倒序 append，完成反转
        for (int i = end; i >= start; i--) {
            s.append(str.charAt(i));
        }

        // 4. 再放入不反转的后缀 [end+1, 末尾)
        s.append(str.substring(end + 1));

        // 5. StringBuilder 转回 String 返回
        return s.toString();
    }

    @Test
    public void testReverse() {
        String str = "abcdefg";
        String str1 = reverse3(str, 2, 5);
        System.out.println(str1); // abfedcg
    }

    /**
     * 题目3：获取一个字符串在另一个字符串中出现的次数。
     * 比如：获取"ab"在 "abkkcadkabkebfkabkskab" 中出现的次数
     */
    public int getSubStringCount(String mainStr, String subStr) {
        // 前置判断：主串比子串短，肯定不可能包含，直接返回 0，避免无谓循环
        if (mainStr.length() >= subStr.length()) {
            int count = 0; // 记录出现的次数
            int index = 0;

//             while((index = mainStr.indexOf(subStr)) != -1) {
//                 count++;
//                 mainStr = mainStr.substring(index + subStr.length());
//             }

            /*
             * 改进点说明：
             * 上面注释掉的旧写法每次都 substring 截断主串，会不断创建新字符串，浪费内存。
             * 改进版改用 indexOf(subStr, index) 的重载：只移动查找起点 index，不动原串。
             * 找到一次就把 index 往后挪 subStr.length()（跳过刚匹配到的部分，避免重叠误判），count++。
             */
            while ((index = mainStr.indexOf(subStr, index)) != -1) {
                index += subStr.length();
                count++;
            }

            return count;
        } else {
            return 0;
        }
    }

    @Test
    public void testGetCount() {
        String str = "cdabkkcadkabkebfkabkskab";
        String subStr = "ab";
        int count = getSubStringCount(str, subStr);
        System.out.println(count);
    }

    /**
     * 题目4：对字符串中字符进行自然顺序排序。
     * 提示：
     * 1）字符串变成字符数组。
     * 2）对数组排序，选择，冒泡，Arrays.sort();
     * 3）将排序后的数组变成字符串。
     */
    @Test
    public void testSort() {
        String str = "abcwerthelloyuiodef";

        // 1. String 不能直接排序，先拆成 char[]
        char[] arr = str.toCharArray();

        // 2. Arrays.sort 对 char[] 按字符的自然顺序（本质是按 Unicode 码值）升序排列
        Arrays.sort(arr);

        // 3. 排好序的 char[] 再拼回 String
        String newStr = new String(arr);
        System.out.println(newStr);
    }
}

/**
 * 案例：模拟用户登录
 * （1）定义用户类，属性为用户名和密码，提供相关的getter和setter方法，构造器，toString()。
 * （2）使用数组存储多个用户对象。
 * （3）录入用户和密码，对比用户信息，匹配成功登录成功，否则登录失败。
 * > 登录失败时，当用户名错误，提示没有该用户。
 * > 登录失败时，当密码错误时，提示密码有误。
 * 效果如图所示：
 * User功能演示.jpg
 */
class User {
    private String name;
    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return name + "-" + password;
    }
}

class UserTest {
    public static void main(String[] args) {
        // 1. 创建数组，并初始化几个User对象
        User[] arr = new User[3];

        arr[0] = new User("Tom", "8888");
        arr[1] = new User("songhk", "123");
        arr[2] = new User("Jerry", "6666");

        System.out.println("库中的用户有：");

        for (int i = 0; i < arr.length; i++) {
            /*
             * 这里自动调用了 toString()。
             * 因为 println(Object) 内部会对参数调用 String.valueOf(obj)，而它最终会调用 obj.toString()。
             * 由于 User 重写了 toString()，所以打印出的是 "name-password"，
             * 若不重写，则会打印默认的「类名@哈希值」。
             */
            System.out.println(arr[i]);
        }

        // 2. 实例化Scanner，获取输入的用户名和密码
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名：");
        /*
         * scanner.next() 返回的就是 String。
         * 对比 nextLine() 会读一整行（含空格）；nextInt()/nextDouble() 读数字。
         * 这里读用户名用 next() 即可。
         */
        String userName = scanner.next();

        System.out.print("请输入密码：");
        String password = scanner.next();

        // 3.遍历数组元素，匹配用户名和密码
        /*
         * 用布尔「标志位」控制流程是很常见、可接受的做法。
         * 更好的命名如 boolean userNotFound = true; 或 boolean matched = false; 让含义自解释。
         * 实际工程里更常见的是：把查找逻辑抽成方法直接 return User（找到返回对象，找不到返回 null），
         * 用「返回值」代替「标志位」，比在循环外判断 flag 更清晰。
         */
        boolean isFlag = true;

        for (int i = 0; i < arr.length; i++) {
//            if (arr[i].getName().equals(userName) && arr[i].getPassword().equals(password)) {
//                System.out.println("登录成功，" + userName);
//                break;
//            }

            if (arr[i].getName().equals(userName)) {
                isFlag = false;

                if (arr[i].getPassword().equals(password)) {
                    System.out.println("登录成功，" + userName);
                    /*
                     * 这里的 break 不是「必须」，但推荐加/或靠外层 break 收口。
                     * 因为用户名已匹配（用户名通常唯一），后面元素不可能再匹配，继续循环是浪费。
                     * 本例外层 if 末尾已有一个 break，所以即使这里不写，匹配后也会走到那个 break 跳出，不会误判。
                     */
//                break;
                } else {
                    System.out.println("密码有误");
                }
                break;
            }
            /*
             * 不能直接在循环里这样打印。
             * 因为循环每遍历到一个「名字不匹配」的元素都会打印一次「没有该用户」，
             * 3 个用户里只要有 2 个名字不同，就会误打印多次。
             */
//            System.out.println("没有该用户");
        }
        /*
         * 必须写在 for 外面。
         * 写在循环里：每轮不匹配都会打印，会重复输出（原因同上）。
         * 写在循环外：等整个数组都找完、确认一个都没匹配上（isFlag 仍为 true）时，才打印一次，才正确。
         */
        if (isFlag) {
            System.out.println("没有该用户");
        }

        /*
         * 「自动关闭」指 try-with-resources：try (Scanner scanner = new Scanner(System.in)) { ... }
         *    离开 try 块自动调用 close()，无需手写 scanner.close()，实际开发很常用、更安全（异常时也能关）。
         */
        scanner.close();
    }
}
