-- 数据准备
drop table if exists employee;
create table IF NOT EXISTS emp
(
    id          int comment '编号',
    workno      varchar(10) comment '工号',
    name        varchar(10) comment '姓名',
    gender      char(1) comment '性别',
    age         tinyint unsigned comment '年龄',
    idcard      char(18) comment '身份证号',
    workaddress varchar(50) comment '工作地址',
    entrydate   date comment '入职时间'
) comment '员工表';

-- SQL 语句可以换行，但不要在字符串内部随意换行。单引号内的换行符属于字符串内容，不会被自动忽略。
-- 在 '2000-01-' 和 '01' 之间插入换行符，会改变字符串内容，导致 DATE 类型转换失败。
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (1, '00001', '柳岩666', '女', 20, '123456789012345678', '北京', '2000-01-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (2, '00002', '张无忌', '男', 18, '123456789012345670', '北京', '2005-09-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (3, '00003', '韦一笑', '男', 38, '123456789712345670', '上海', '2005-08-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (4, '00004', '赵敏', '女', 18, '123456757123845670', '北京', '2009-12-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (5, '00005', '小昭', '女', 16, '123456769012345678', '上海', '2007-07-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (6, '00006', '杨逍', '男', 28, '12345678931234567X', '北京', '2006-01-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (7, '00007', '范瑶', '男', 40, '123456789212345670', '北京', '2005-05-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (8, '00008', '黛绮丝', '女', 38, '123456157123645670', '天津', '2015-05-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (9, '00009', '范凉凉', '女', 45, '123156789012345678', '北京', '2010-04-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (10, '00010', '陈友谅', '男', 53, '123456789012345670', '上海', '2011-01-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (11, '00011', '张士诚', '男', 55, '123567897123465670', '江苏', '2015-05-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (12, '00012', '常遇春', '男', 32, '123446757152345670', '北京', '2004-02-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (13, '00013', '张三丰', '男', 88, '123656789012345678', '江苏', '2020-11-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (14, '00014', '灭绝', '女', 65, '123456719012345670', '西安', '2019-05-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (15, '00015', '胡青牛', '男', 70, '12345674971234567X', '西安', '2018-04-01');
INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (16, '00016', '周芷若', '女', 18, null, '北京', '2012-06-01');

/*
 * DQL英文全称是Data Query Language(数据查询语言)，用来查询数据库中表的记录。
 * 查询关键字: SELECT

 * 基础查询
 * 查询多个字段
 * SELECT 字段1, 字段2, 字段3 ... FROM 表名 ;
 * SELECT * FROM 表名 ;
 * 注意 : * 号代表查询所有字段，在实际开发中尽量少用。

 * 字段设置别名
 * SELECT 字段1 [ AS 别名1 ] , 字段2 [ AS 别名2 ] ... FROM 表名;
 * SELECT 字段1 [ 别名1 ] , 字段2 [ 别名2 ] ... FROM 表名;

 * 去除重复记录
 * SELECT DISTINCT 字段列表 FROM 表名;
 */

-- 案例：
-- A. 查询指定字段 name, workno, age并返回
/*
 SELECT emp.name, emp.workno, emp.age
 FROM emp;
 * 建议：单表查询直接写字段名；多表查询给表起短别名，再写 e.name 这种形式。
 */
SELECT name, workno, age
FROM emp;

-- B. 查询返回所有字段
# SELECT id, workno, name, gender, age, idcard, workaddress, entrydate FROM emp;
/*
 * 项目代码里建议把需要的字段写全。
 * 只取需要的列，传输量更少；读代码时一眼能看出取了哪些列；
 * 表结构以后增删字段时，程序拿到的列也不会悄悄跟着变化。
 */
SELECT *
FROM emp;

-- C. 查询所有员工的工作地址,起别名
/*
 * 别名只在这一条查询的结果里有效，它不会修改表结构，表里的字段仍然叫 workaddress；
 * 它也不是 comment，comment 是建表时写给字段的说明文字。
 * 下一条 SELECT 如果不写别名，列标题还是 workaddress。
 */
# SELECT workaddress AS '工作地址' FROM emp;
/*
 * SQL 语法允许省略 AS：字段后面紧跟的一个名字会被当成别名。
 * 别名用单引号包起来是 MySQL 的宽松用法；更通用的写法是不加引号，或者用反引号 `工作地址`。
 */
SELECT workaddress '工作地址'
FROM emp;

-- D. 查询公司员工的上班地址有哪些(不要重复)
SELECT DISTINCT emp.workaddress '工作地址'
FROM emp;


-- 条件查询
-- SELECT 字段列表 FROM 表名 WHERE 条件列表 ;
/*
 * 条件查询：常用比较与匹配运算
 *
 * >、>=       ：大于、大于或等于。
 * <、<=       ：小于、小于或等于。
 * =           ：判断是否相等。
 * <> 或 !=    ：判断是否不相等。
 *
 * BETWEEN 下界 AND 上界：判断是否处于指定范围，包含两个边界。
 *   下界写在前，上界写在后；MySQL 不会自动交换写反的边界。
 *
 * IN (值1, 值2, 值3)：与列表中的任意一个值相等即匹配。
 *   例如 age IN (18, 20, 25)，表示年龄为这三个值之一。
 *
 * LIKE '匹配模式'：按字符串模式匹配，常配合以下通配符：
 *   _：匹配恰好一个字符。
 *   %：匹配零个或多个字符，也可以不匹配任何字符。
 *   例如 name LIKE '张%'：匹配以“张”开头的名字。
 *        name LIKE '张_'：匹配以“张”开头、总共两个字符的名字。
 *        name LIKE '%明%'：匹配包含“明”的名字。

 * IS NULL 和 IS NOT NULL 都返回 1（真）或 0（假），不会返回 NULL。
 * 对同一个值，两者的结果相反：
 *   被判断的值       IS NULL       IS NOT NULL
 *   NULL              1                0
 *   0                 0                1
 *   ''（空字符串）      0                1
 *
 * 放在 SELECT 后，会显示判断结果。例如：
 *   SELECT NULL IS NULL AS a, NULL IS NOT NULL AS b;
 *   返回一行两列：a 为 1，b 为 0。
 * a 和 b 是两个别名，即结果里两列的列名。查询结果只有一行两列：a 列的值是 1，b 列的值是 0。

 * 放在 WHERE 后，则用于筛选行：
 *   WHERE name IS NULL     保留 name 为 NULL 的行。
 *   WHERE name IS NOT NULL 保留 name 不为 NULL 的行。
 * 查询显示哪些列，仍由 SELECT 决定，不会自动多出一列判断结果。
 *
 * NULL = NULL 是 NULL。
 * WHERE 只保留条件为真的行，所以 WHERE idcard = NULL 一行也查不出来，而且不会报错
 * 因此判断 NULL 必须用专门的 IS NULL 和 IS NOT NULL。
 *   判断 NULL 不要写 = NULL 或 != NULL，这两种比较的结果都是 NULL。
 */

/*
 * 条件查询：常用逻辑运算符
 *
 * 1. AND 或 &&：逻辑与，表示“并且”。
 *    连接的条件全部为真时，整体才为真。
 *
 * 2. OR 或 ||：逻辑或，表示“或者”。
 *    连接的条件至少一个为真时，整体就为真，也允许多个同时为真。
 *
 * 3. NOT 或 !：逻辑非，表示“取反”。
 *    真变假，假变真；但对未知结果取反仍是未知，即 NOT NULL 仍为 NULL。
 *
 * 建议优先使用 AND、OR、NOT，含义清晰，也更便于跨数据库使用。
 *
 * 这三个逻辑关键字的优先级由高到低为：NOT、AND、OR。
 * 混用时建议加括号，明确条件的组合方式：
 *   age >= 18 AND (city = '北京' OR city = '上海')
 *   表示年龄至少 18 岁，并且城市为北京或上海。
 *
 * 注意：条件的结果除了真、假，还可能是 NULL（未知）。
 * WHERE 只保留条件为真的行，条件为假或 NULL 的行都不会被选中。
 */
-- 案例:
-- A. 查询年龄等于 88 的员工
SELECT *
FROM emp
WHERE age = 88;

-- B. 查询年龄小于 20 的员工信息
SELECT *
FROM emp
WHERE age < 20;

-- C. 查询年龄小于等于 20 的员工信息
SELECT *
FROM emp
WHERE age <= 20;

-- D. 查询没有身份证号的员工信息
SELECT *
FROM emp
WHERE idcard IS NULL;

-- E. 查询有身份证号的员工信息
SELECT *
FROM emp
WHERE idcard IS NOT NULL;

-- F. 查询年龄不等于 88 的员工信息
-- <> 是 SQL 标准写法，所有数据库都支持；!= 是常见扩展，MySQL 等主流数据库也都支持。
SELECT *
FROM emp
WHERE age != 88;

SELECT *
FROM emp
WHERE age <> 88;

-- G. 查询年龄在15岁(包含) 到 20岁(包含)之间的员工信息
/*
 * && 是 MySQL 特有写法，从 MySQL 8.0.17 起已被标记为弃用，
 * 某些 sql_mode 下 || 的含义还会变成字符串拼接，容易出错。
 */
SELECT *
FROM emp
WHERE age >= 15
  AND age <= 20;

SELECT *
FROM emp
WHERE age >= 15 && age <= 20;

-- BETWEEN a AND b 等价于 age >= a AND age <= b。
SELECT *
FROM emp
WHERE age BETWEEN 15 AND 20;

-- H. 查询性别为 女 且年龄小于 25岁的员工信息
SELECT *
FROM emp
WHERE gender = '女'
  AND age < 25;

-- I. 查询年龄等于18 或 20 或 40 的员工信息
SELECT *
FROM emp
WHERE age = 18
   OR age = 20
   OR age = 40;

SELECT *
FROM emp
WHERE age IN (18, 20, 40);

/*
 * LIKE 会拿字段值和“模式”逐个字符对照，模式里有两个特殊符号：
 *   _ ：恰好占 1 个字符，内容不限。
 *   % ：占 0 个到任意多个字符，内容不限。
 * 其他普通字符必须原样对上。整个字段值必须从头到尾都和模式对上，才算匹配。
 */
-- J. 查询姓名为两个字的员工信息
SELECT *
FROM emp
WHERE name LIKE '__';

-- K. 查询身份证号最后一位是X的员工信息
SELECT *
/*
 * 【X区分大小写吗？】
 * 取决于字段的排序规则（collation）。MySQL 8 默认的 utf8mb4_0900_ai_ci 中，ci 表示不区分大小写，
 * 所以小写的 x 也会被匹配到。如果字段使用区分大小写的规则（例如 _bin、_cs 结尾的规则），就会区分大小写。
 */
FROM emp
-- 以 % 开头的模式无法利用普通索引，数据量很大时会比较慢。
WHERE idcard LIKE '%X';

SELECT *
FROM emp
WHERE idcard LIKE '_________________X';


-- 聚合函数：将一列数据作为一个整体，进行纵向计算。
/*
 * MAX、MIN、AVG、SUM 都忽略 NULL，不把 NULL 当作 0。
 * AVG 的分母是该字段非 NULL 值的数量，不一定是总行数。
 *
 * 例如，salary 列有三行数据：1000、2000、NULL。
 *   COUNT(*)      返回 3。
 *   COUNT(salary) 返回 2。
 *   AVG(salary)   返回 1500，即 (1000 + 2000) / 2。
 */
-- SELECT 聚合函数(字段列表) FROM 表名 ;
-- 聚合函数作用于某个字段时会忽略 NULL；COUNT(*) 统计的是行数，不受 NULL 影响

-- 案例：
-- A. 统计该企业员工数量
-- COUNT() 必须指定参数，不能省略。
SELECT COUNT(*)
FROM emp;

-- 统计的是idcard字段不为null的记录数
SELECT COUNT(emp.idcard)
FROM emp;

-- 对于count聚合函数，统计符合条件的总记录数，
-- 还可以通过 count(数字/字符串)的形式进行统计查询，比如：
/*
 * COUNT(表达式) 统计表达式不为 NULL 的行数。1 是一个常量，每一行算出来都是 1，永远不是 NULL，
 * 所以 COUNT(1) 等于总行数，结果与 COUNT(*) 相同。
 * 换成 COUNT(2)、COUNT('a') 结果也一样，这里的数字不表示“第几列”。
 * COUNT(NULL) 永远返回 0。
 */
SELECT count(1)
from emp;

-- B. 统计该企业员工的平均年龄
SELECT AVG(emp.age)
FROM emp;

-- C. 统计该企业员工的最大年龄
SELECT MAX(emp.age)
FROM emp;

-- D. 统计该企业员工的最小年龄
SELECT MIN(emp.age)
FROM emp;

-- E. 统计西安地区员工的年龄之和
-- SELECT 后面写的是“想要输出什么”，可以是字段、表达式或聚合函数。
SELECT SUM(age)
FROM emp
WHERE workaddress = '西安';


-- 分组查询
-- SELECT 字段列表 FROM 表名 [ WHERE 条件 ] GROUP BY 分组字段名 [ HAVING 分组后过滤条件 ];
/*
 * where与having区别
 * 执行时机不同：where是分组之前进行过滤，不满足where条件，不参与分组；
 *            而having是分组之后对结果进行过滤。
 * 判断条件不同：where不能对聚合函数进行判断，而having可以。

 * 注意事项:
 * 分组之后，查询的字段一般为聚合函数和分组字段，查询其他字段无任何意义。
 * 执行顺序: where > 聚合函数 > having 。
 * 支持多字段分组, 具体语法为 : group by columnA,columnB
 */

-- 案例:
-- A. 根据性别分组 , 统计男性员工 和 女性员工的数量
/*
 * GROUP BY gender 会把 16 行按性别分成两堆：男一堆、女一堆。每一堆最后只输出一行结果。
 * COUNT(*) 统计每一堆有多少行。gender 写在 SELECT 里，是为了显示这一行代表哪一堆。
 * 不写 gender 也能执行，但结果只有两个数字，你分不清哪个是男、哪个是女，所以通常要把分组字段一起查出来。
 * SELECT 里除了聚合函数，其他字段原则上都必须出现在 GROUP BY 中。
 */
SELECT gender, COUNT(*)
FROM emp
GROUP BY gender;

-- B. 根据性别分组 , 统计男性员工 和 女性员工的平均年龄
/*
 * 先按 gender 分成男、女两组，只是把 COUNT(*) 换成 AVG(age)，计算每组 age 的平均值。
 * 每组输出一行：性别、该组平均年龄。AVG 会忽略 NULL。
 */
SELECT gender, AVG(age)
FROM emp
GROUP BY gender;

-- C. 查询年龄小于45的员工 , 并根据工作地址分组 , 获取员工数量大于等于3的工作地址
/*
 * GROUP BY workaddress：按工作地址分组。
 * HAVING：每组统计人数后，只保留人数 >= 3 的组；这是对分组结果的筛选。
 */
SELECT workaddress, COUNT(*) address_count
FROM emp
WHERE age < 45
GROUP BY workaddress
/*
 * 在 HAVING 中使用 SELECT 定义的别名是 MySQL 的扩展，有些数据库不支持。
 * 只用 MySQL 时两种都可以；想通用、不产生歧义，就用 COUNT(*)。
 * 别名主要用来给结果列起清晰的名字。
 */
# HAVING COUNT(*) >= 3;
HAVING address_count >= 3;

-- D. 统计各个工作地址上班的男性及女性员工的数量
/*
 * 多字段分组：只有 gender 和 workaddress 都相同的行才在同一组。
 * 每种组合输出一行，并统计该组合的人数。
 */
SELECT workaddress, gender, COUNT(*) `数量`
FROM emp
GROUP BY gender, workaddress;

-- 排序查询
-- SELECT 字段列表 FROM 表名 ORDER BY 字段1 排序方式1 , 字段2 排序方式2 ;
/*
 * 排序方式
 * ASC : 升序（默认值）
 * DESC: 降序
 * 如果是升序, 可以不指定排序方式ASC;
 * 如果是多字段排序，当第一个字段值相同时，才会根据第二个字段进行排序
 */
-- 案例:
-- A. 根据年龄对公司的员工进行升序排序
SELECT *
FROM emp
ORDER BY age ASC;

SELECT *
FROM emp
ORDER BY age;

-- B. 根据入职时间, 对员工进行降序排序
SELECT *
FROM emp
ORDER BY entrydate DESC;

-- C. 根据年龄对公司的员工进行升序排序 , 年龄相同 , 再按照入职时间进行降序排序
SELECT *
FROM emp
ORDER BY age ASC, entrydate DESC;


-- 分页查询
-- SELECT 字段列表 FROM 表名 LIMIT 起始索引, 查询记录数 ;
/*
 * 注意事项:
 * 起始索引从0开始，起始索引 = （查询页码 - 1）* 每页显示记录数。
 * 分页查询是数据库的方言，不同的数据库有不同的实现，MySQL中是LIMIT。
 * 如果查询的是第一页数据，起始索引可以省略，直接简写为 limit 10。

 * 这里的 * 是乘号。以每页 10 条为例，起始索引分别为：
 * 第 1 页 (1-1)×10=0，取第 0~9 条；第 2 页 (2-1)×10=10，取第 10~19 条；第 3 页从 20 开始。

 * 每页条数由你写的第二个参数决定，每次查询都是同一个值，所以除最后一页外都正好是这个数。
 * 最后一页有多少条就返回多少条，可能不足一页。总页数可以先用 COUNT(*) 统计总数，再向上取整计算。
 * 如果翻页期间有人插入或删除数据，后续页的内容可能会偏移。
 */
-- 案例:
-- A. 查询第1页员工数据, 每页展示10条记录
SELECT *
FROM emp
LIMIT 0, 10;

SELECT *
FROM emp
LIMIT 10;

-- B. 查询第2页员工数据, 每页展示10条记录
-- 起始索引 = （查询页码 - 1）* 每页显示记录数
SELECT *
FROM emp
LIMIT 10, 10;


-- 练习
-- 1). 查询年龄为20,21,22,23岁的员工信息。
SELECT *
FROM emp
WHERE age IN (20, 21, 22, 23);

-- 2). 查询性别为 男 ，并且年龄在 20-40 岁(含)以内的姓名为三个字的员工。
SELECT *
FROM emp
WHERE gender = '男'
  AND age BETWEEN 20 AND 40
  AND name LIKE '___';

-- 3). 统计员工表中, 年龄小于60岁的 , 男性员工和女性员工的人数。
SELECT gender, COUNT(*)
FROM emp
WHERE age < 60
GROUP BY gender;

-- 4). 查询所有年龄小于等于35岁员工的姓名和年龄，并对查询结果按年龄升序排序，
-- 如果年龄相同按入职时间降序排序。
SELECT name, age
FROM emp
WHERE age <= 35
ORDER BY age ASC, entrydate DESC;

-- 5). 查询性别为男，且年龄在20-40 岁(含)以内的前5个员工信息，
-- 对查询的结果按年龄升序排序，年龄相同按入职时间升序排序。
SELECT *
FROM emp
WHERE gender = '男'
  AND age BETWEEN 20 AND 40
ORDER BY age ASC, emp.entrydate ASC
LIMIT 5;


/*
 * 编写顺序：SQL 语句中各子句的排列顺序。
 *   SELECT → FROM → WHERE → GROUP BY → HAVING → ORDER BY → LIMIT
 *
 *   WHERE    分组前的筛选条件
 *   GROUP BY 分组字段
 *   HAVING   分组后的筛选条件
 *   ORDER BY 排序字段或表达式
 *   LIMIT    分页参数;
 *
 * 逻辑处理顺序：用于理解查询结果如何形成。
 *   FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT
 *
 *   1. FROM：确定数据来自哪些表；有连接时，根据连接条件组合数据。
 *   2. WHERE：筛选原始行，只有条件为真的行才能进入后续处理。
 *   3. GROUP BY：将行分组；有聚合函数时，对各组进行统计。
 *   4. HAVING：筛选分组或整体聚合后的结果，可以使用聚合函数作为条件。
 *   5. SELECT：确定最终输出哪些字段、表达式或聚合结果。
 *   6. ORDER BY：对结果排序。
 *   7. LIMIT：限制返回的行数，或跳过指定行数后取出一部分结果。
 */
-- 验证：
-- 查询年龄大于15的员工姓名、年龄，并根据年龄进行升序排序。
/*
 * FROM emp e 先确定表和表别名，所以 WHERE 中可以使用 e.age；
 * WHERE 在 SELECT 之前处理，此时列别名 eage 还没有产生，所以 WHERE 中不能写 eage，否则会报“未知列”。
 * ORDER BY 在 SELECT 之后处理，所以可以使用列别名 eage 排序。
 */
SELECT e.name ename, e.age eage
FROM emp e
WHERE e.age > 15
ORDER BY eage ASC;
