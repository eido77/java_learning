package chapter18;

import org.junit.jupiter.api.Test;

public class BlockTest {
    /*
    文本块的使用
    JDK13的预览特性
    JDK14中二次预览特性
    JDK15中功能转正
     */
    /*
     * Text Block(文本块)，一种新的【字符串字面量写法】，类型仍然是 java.lang.String
     * 文本块 = 用三个双引号 """ 包起来的字符串字面量，可以直接换行书写，
     * 不用再写 \n、不用再写 + 拼接、不用再给引号加反斜杠转义。
     *
     * 开头的 """ 后面必须紧跟换行，不能在同一行写内容，否则编译报错；
     * 代码里为了对齐而打的空格(叫"附带空白"/incidental whitespace)会被自动去掉，
     * 去掉多少由【所有非空内容行 + 结束 """ 所在行】里最小的缩进决定；
     * 结束的 """ 单独占一行时，字符串末尾会多一个 \n；紧跟在内容后面则没有。
     */

    /**
     * 对比1
     */
    @Test
    public void test1() {
        String info = "<html>\n" +
                /*
                 * <body>  = 开始标签
                 * </body> = 结束标签(注意多了一个斜杠 /)
                 */
                "  <body>\n" +
                /*
                 *   <p>  Hello, 尚硅谷  </p>
                 *   开始标签   内容     结束标签(带 /)
                 * <p> 表示一个段落(paragraph)，"Hello, 尚硅谷"是段落里的文字。
                 */
                "      <p>Hello, 尚硅谷</p>\n" +
                "  </body>\n" +
                /*
                 * </html> 是整个文档的结束标签，和第一行的 <html> 配对。
                 * 这一行【故意没有写 \n】，因为它是最后一行，
                 * 再加 \n 就会在输出末尾多出一个空行。
                 */
                "</html>";
        System.out.println(info);
    }

    /*
     * 和 test1 输出同一段 HTML，但改用 JDK 15 正式转正的文本块写法：
     * 没有 \n、没有 +、没有行尾引号，源码长什么样，字符串基本就长什么样。
     */
    @Test
    public void test2() {
        /*
         * 开始分隔符 """ 后面必须【立刻换行】。
         *
         * 附带空白(incidental whitespace)自动剥离：
         * 编译器会找出【所有非空内容行】和【结束 """ 所在行】中最小的缩进量，
         * 然后把每一行都去掉这么多前导空格。
         *
         * 结束分隔符 """ 单独占一行 -> 最后一行内容 </html> 后面会【带一个 \n】。
         * 所以 test2 的输出末尾有换行，而 test1 的输出末尾没有换行。
         * 这是这一对方法唯一的实际差异，面试/考试最爱考这一点。
         *
         * 每行行尾的空格也会被自动删掉(避免看不见的尾随空格)，
         * 如果确实需要保留行尾空格，就要用 test7 里的 \s。
         */
        String info = """
                <html>
                  <body>
                      <p>Hello, 尚硅谷</p>
                  </body>
                </html>
                """;
        System.out.println(info);
    }

    /**
     * 对比2
     */
    @Test
    public void test3() {
        /*
         * \"  = 一个双引号(转义字符)
         * 下面的 test4 用文本块重写，\" 可以直接写成 "，一个转义都不需要。
         *
         * 另外注意最后一行 "}" 后面【没有 \n】，所以输出末尾不带换行，
         * 这一点正好和 test4 的写法对应(test4 的结束 """ 紧贴 } ，也不带换行)。
         */
        String myJson = "{\n" +
                "    \"name\":\"Song Hongkang\",\n" +
                "    \"gender\":\"男\",\n" +
                "    \"address\":\"www.atguigu.com\"\n" +
                "}";
        System.out.println(myJson);
    }

    @Test
    public void test4() {
        /*
         * 内容里需要出现连续三个双引号时，才必须转义成 \"""。
         *
         * 【重点】结束的 """ 没有单独占一行，而是紧跟在 } 后面写成 }"""，
         *        这表示字符串到 } 就结束了，末尾【不带换行符】。
         *        对比 test2(结束 """ 单独一行 -> 末尾有换行)，这两种写法的差别
         *        就是"最后有没有多一个 \n"，是最容易被忽略的考点。
         */
        String myJson1 = """
                {
                    "name":"Song Hongkang",
                    "gender":"男",
                    "address":"www.atguigu.com"
                }""";
        System.out.println(myJson1);
    }

    /**
     * 对比3
     */
    @Test
    public void test5() {
        String query = "SELECT employee_id,last_name,salary,department_id\n" +
                "FROM employees\n" +
                "WHERE department_id IN (40,50,60)\n" +
                "ORDER BY department_id ASC";
        System.out.println(query);
    }

    @Test
    public void test6() {
        /*
         * 【末尾多了一个换行】
         * 结束的 """ 单独占一行，所以最后一行 ORDER BY ... ASC 后面会补一个 \n，
         * 而 test5 末尾没有换行。
         */
        String newQuery = """
                    SELECT employee_id,last_name,salary,department_id
                    FROM employees
                    WHERE department_id IN (40,50,60)
                    ORDER BY department_id ASC
                """;
        System.out.println(newQuery);
    }

    /*
     * JDK14新特性
     * \:取消换行操作
     * 行尾的 \ ：叫"行连接符"(line continuation)。
     * 写在某一行的【最末尾】，表示"取消这一行的换行"，
     * 硬性要求：\ 必须是该行最后一个字符，后面连一个空格都不能有，否则编译错误。
     *
     * \s:表示一个空格，等价于一个 ' '
     * 编译器先剥离附带空白(含删除行尾空白)，【之后】才解释转义字符。
     * 所以 \s 能活下来，而你手打的行尾空格会被删掉。
     */
    @Test
    public void test7() {
        String newQuery1 = """
                SELECT id,name,email \
                FROM customers\s\
                WHERE id > 4 \
                ORDER BY email DESC
                """;
        System.out.println(newQuery1);
        // SELECT id,name,email FROM customers WHERE id > 4 ORDER BY email DESC
    }
}
