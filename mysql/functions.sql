-- 函数 是指一段可以直接被另一段程序调用的程序或代码。
-- MySQL中的函数主要分为以下四类： 字符串函数、数值函数、日期函数、流程函数。

-- 演示如下：
-- A. CONCAT(s1, s2, ...)：将多个字符串拼接成一个字符串。
-- 任意一个参数为 NULL，整个结果就是 NULL。
SELECT CONCAT('Hello', ' MySQL');

-- B. LOWER(str)：将字符串中的字母转换为小写。
SELECT LOWER('Hello');

-- C. UPPER(str)：将字符串中的字母转换为大写。
SELECT UPPER('Hello');

-- D. LPAD(str, n, pad)：在字符串左侧使用 pad 填充，使总长度达到 n 个字符。
/*
 * 两种填充函数中的 n 都是“最终总长度”，不是“需要补多少个字符”。
 * 如果原字符串长度超过 n，两者都会只保留原字符串左侧的 n 个字符。
 * 例如 LPAD('ABCDE', 3, '0') 和 RPAD('ABCDE', 3, '0') 都返回 'ABC'。
 * pad 是多个字符组成的字符串的时候，最后一次只取需要的部分。
 */
SELECT LPAD('01', 5, '-');

-- E. RPAD(str, n, pad)：在字符串右侧使用 pad 填充，使总长度达到 n 个字符。
SELECT RPAD('01', 5, '-');

-- F. TRIM(str)：去掉字符串开头和结尾的空格，保留中间的空格。
-- 默认去除的是普通空格，不会自动去掉所有制表符、换行符等空白字符。
/*
 * 只去一侧可用 LTRIM(str)、RTRIM(str)。
 * 去掉指定字符可写 TRIM(BOTH 'x' FROM 'xxabcxx')，返回 'abc'；
 * BOTH 也可换成 LEADING（开头）或 TRAILING（结尾）。
 */
SELECT TRIM(' Hello MySQL ');

-- G. SUBSTRING(str, start, len)：从 start 位置开始，最多截取 len 个字符。
/*
 * 正向位置从 1 开始计数，不是从 0 开始。
 * len 表示截取长度，不是结束位置；剩余字符不足时，截取到字符串末尾。
 * start 也可以为负数，表示从字符串末尾倒数定位。
 * 例如 SUBSTRING('MySQL', -2, 2) 返回 'QL'。

 * SQL 的字符串位置从 1 开始，这是 SQL 标准的设计，
 * 与 Java 等编程语言数组下标从 0 开始不同。
 */
SELECT SUBSTRING('Hello MySQL', 1, 5);

-- 案例:
-- 由于业务需求变更，企业员工的工号，统一为5位数，目前不足5位数的全部在前面补0。
-- 比如：1号员工的工号应该为00001。
-- 使用 emp 表练习员工工号补零
USE sql_study;

SELECT *
FROM emp;

-- SET 里的 = 是“赋值”：把右边算出来的值写进左边的列，不是比较是否相等。
UPDATE emp
SET workno = LPAD(workno, 5, '0');


-- 数值函数
-- 演示如下：
-- A. CEIL(x)：向上取整，返回大于或等于 x 的最小整数。
SELECT CEIL(1.5);

-- B. FLOOR(x)：向下取整，返回小于或等于 x 的最大整数。
SELECT FLOOR(1.5);

-- C. MOD(x, y)：返回x/y的模（求 x 除以 y 的余数，不是求商）
-- 也可写 3 % 2；y 为 0 时返回 NULL，不会报错。
SELECT MOD(3, 2);

-- D. RAND()：返回大于或等于 0、且小于 1 的随机小数。
-- 范围是 [0, 1)，包含 0，不包含 1。
SELECT RAND();

-- E. ROUND(x, y)：求参数x的四舍五入的值，保留y位小数。
/*
 * 恰好处于中间时，向远离 0 的方向舍入。
 * 例如 ROUND(2.5, 0) 返回 3；ROUND(-2.5, 0) 返回 -3。
 *
 * y > 0：保留 y 位小数。
 * y = 0：舍入到整数；省略 y 时，默认按 0 处理。
 * y < 0：舍入到小数点左侧，-1 对应十位，-2 对应百位。
 *   例如 ROUND(123.45, -1) 返回 120。
 */
SELECT ROUND(2.23, 2);

/*
 * 案例：
 * 通过数据库的函数，生成一个六位数的随机验证码。
 * 思路：获取随机数可以通过rand()函数，但是获取出来的随机数是在0-1之间的，
 * 所以可以在其基础上乘以1000000，然后舍弃小数部分，如果长度不足6位，补0
 */
/*
 * 错误原因：ROUND 四舍五入可能得到 1000000，与“舍弃小数部分、生成六位数”的目标冲突，
 *          LPAD 截断后得到错误且分布不均的结果。

 *          000000 出现概率只有其他值的一半，分布不均。
 *          结果为 0（0 ～ 0.5）范围长度约为 0.5
 *          结果为 1（0.5 ～ 1.5）范围长度约为 1
 *
 * FLOOR 直接向下取整，结果为 0 ~ 999999，补零后恰好 6 位。
 */
# SELECT LPAD(ROUND(RAND() * 1000000, 0), 6, '0');
SELECT LPAD(FLOOR(RAND() * 1000000), 6, '0');


-- 日期函数
-- 假设当前会话时间为 2026-10-23 14:30:00。
-- 演示如下：
-- A. curdate：返回当前日期，不包含时间。
-- 示例结果：'2026-10-23'。
SELECT CURDATE();

-- B. curtime：返回当前时间，不包含日期。
-- 示例结果：'14:30:00'。
SELECT CURTIME();

-- C. now：返回当前日期和时间。
-- 示例结果：'2026-10-23 14:30:00'。
-- 取当前语句开始执行时的时间，同一条语句内多次调用结果一致。
SELECT NOW();

-- D. YEAR , MONTH , DAY：从“传入的日期”中提取年、月、日，不一定是当前日期
-- YEAR(date)：提取指定日期或日期时间中的年份。
-- 例如 YEAR('2026-10-23') 返回 2026。
SELECT YEAR(NOW());
-- MONTH(date)：提取指定日期或日期时间中的月份。
-- 例如 MONTH('2026-10-23') 返回 10。
SELECT MONTH(NOW());
-- DAY(date)：提取指定日期是“当月的第几天”，不是返回完整日期。
-- 例如 DAY('2026-10-23') 返回 23。
SELECT DAY(NOW());

-- E. DATE_ADD(date, INTERVAL expr type)：在指定日期或时间上增加一个时间间隔。
/*
 * date：原始日期或时间。
 * INTERVAL 是表示时间间隔的关键字。
 * expr：增加的数量；可以为负数，表示减去对应的时间间隔。
 * type：时间单位，常见有 YEAR、MONTH、DAY、HOUR、MINUTE、SECOND。
 *
 *    例如 DATE_ADD('2026-10-23', INTERVAL -1 DAY)
 *    返回 '2026-10-22'。
 */
SELECT DATE_ADD(NOW(), INTERVAL 70 DAY);

-- F. DATEDIFF(date1, date2)：返回两个日期相减得到的天数。
/*
 * 计算方向是“第一个日期减去第二个日期”，结果可以为正数、负数或 0。
 * 要计算“结束日期减去开始日期”，应写 DATEDIFF(结束日期, 开始日期)。
 *
 *    例如 DATEDIFF('2026-10-25', '2026-10-23') 返回 2。
 *    交换参数后，DATEDIFF('2026-10-23', '2026-10-25') 返回 -2。
 *
 * DATEDIFF 只使用日期部分，忽略时分秒。
 * 同一天的两个时间，结果为 0；跨日期但不足 24 小时，也可能返回 1。
 * 例如 DATEDIFF('2026-10-24 00:01:00', '2026-10-23 23:59:00')
 * 返回 1，不是按实际经过了几个完整的 24 小时计算。
 */
SELECT DATEDIFF('2021-12-01', '2021-11-01');

/*
 * 案例：
 * 查询所有员工的入职天数，并根据入职天数倒序排序。
 * 思路：入职天数，就是通过当前日期 - 入职日期，所以需要使用datediff函数来完成。
 */
-- entrydate 为 NULL 的员工，计算结果也是 NULL，倒序时会排在最后。
SELECT name, DATEDIFF(CURDATE(), entrydate) `entrydays`
FROM emp
ORDER BY entrydays DESC;


-- 流程函数
-- 演示如下：
-- A. IF(value, t, f)：根据一个条件，从两个结果中选择一个返回。
/*
 * value 为真时返回 t；为假或 NULL 时返回 f。
 * t、f 是返回结果，可以是数值、字符串或表达式。

 * MySQL 中 TRUE 和 FALSE 分别对应 1 和 0。判断规则：
 *   非 0 数值：条件成立，返回 t。
 *   0 或 NULL：条件不成立，返回 f。
 *   字符串会进行隐式数值转换，例如 'abc' 转为 0，因此条件不成立。
 *
 * 实际使用时，建议传入明确的条件表达式。
 */
SELECT IF(true, 'OK', 'Error');

-- B. IFNULL(value1, value2)：value1 不是 NULL，返回 value1；否则返回 value2。
-- IFNULL('', '未填写') 返回空字符串
SELECT IFNULL('OK', 'Default');
SELECT IFNULL(NULL, '1');

-- C. case when then else end
/*
 * CASE WHEN 条件 THEN 结果 ... ELSE 默认结果 END
 *    按条件判断。
 *    按书写顺序选择第一个条件为真的分支，并返回对应结果。
 *
 * CASE expr WHEN 值 THEN 结果 ... ELSE 默认结果 END
 *    按值匹配，将 expr（表达式，如列名 status）与各个 WHEN 后的值进行相等比较，
 *    返回第一个匹配分支的结果。
 *
 * 两种 CASE 的共同规则：
 *   没有匹配分支时返回 ELSE 的结果；省略 ELSE 且没有匹配时，返回 NULL。
 *   条件为 NULL 不算成立；简单 CASE 中的 WHEN NULL 也不能匹配 NULL。
 */
-- 需求: 查询emp表的员工姓名和工作地址 (北京/上海 ----> 一线城市 , 其他 ----> 二线城市)
SELECT emp.name,
    /*
     * “CASE 列 WHEN 值”：每个 WHEN 后只能放一个值做相等比较，
     * 写成 WHEN '北京' OR '上海' 会变成先算 '北京' OR '上海'（结果是 0），逻辑完全错误。
     * 改用“CASE WHEN 条件”写法：
     *   CASE WHEN emp.workaddress IN ('北京', '上海') THEN '一线城市' ELSE '二线城市' END
     */
       (CASE emp.workaddress WHEN '北京' THEN '一线城市' WHEN '上海' THEN '一线城市' ELSE '二线城市' END) `工作地址`
FROM emp;

-- 案例：统计班级各个学员的成绩，展示的结果如下：
-- >= 85，展示优秀
-- >= 60，展示及格
-- 否则，展示不及格
create table score
(
    id      int comment 'ID',
    name    varchar(20) comment '姓名',
    math    int comment '数学',
    english int comment '英语',
    chinese int comment '语文'
) comment '学员成绩表';
insert into score(id, name, math, english, chinese)
VALUES (1, 'Tom', 67, 88, 95),
       (2, 'Rose', 23, 66, 90),
       (3, 'Jack', 56, 98, 76);

SELECT id,
       name,
       (CASE WHEN math >= 85 THEN '优秀' WHEN math >= 60 THEN '及格' ELSE '不及格' END)       `数学`,
       (CASE WHEN english >= 85 THEN '优秀' WHEN english >= 60 THEN '及格' ELSE '不及格' END) `英语`,
       (CASE WHEN chinese >= 85 THEN '优秀' WHEN chinese >= 60 THEN '及格' ELSE '不及格' END) `语文`
FROM score;
