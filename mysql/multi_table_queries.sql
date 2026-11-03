-- 多表查询
-- 多表关系：一对多(多对一)、多对多、一对一

-- 一对多
-- 实现: 在多的一方建立外键，指向一的一方的主键

-- 多对多
-- 实现: 建立第三张中间表，中间表至少包含两个外键，分别关联两方主键
/*
 * 中间表的作用：专门记录“谁和谁有关系”，每一行就是一条“某学生选了某门课”。
 * 为什么只用两张表不行：
 *   在 student 里加 courseid：一个格子只能放一个值，一个学生只能记一门课；
 *   在 course 里加 studentid：一门课只能记一个学生。
 * 怎么做到多对多：中间表与两边分别是“一对多”。
 *   student(一) 对 student_course(多)：一个学生可以在中间表出现多行；
 *   course(一)  对 student_course(多)：一门课也可以在中间表出现多行；
 *   两个一对多拼在一起，就是多对多。
 */
-- 案例: 学生 与 课程的关系
-- 关系: 一个学生可以选修多门课程，一门课程也可以供多个学生选择
CREATE TABLE student
(
    id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(10) COMMENT '姓名',
    no   VARCHAR(10) COMMENT '学号'
) COMMENT '学生表';

INSERT INTO student
VALUES (NULL, '黛绮丝', '2000100101'),
       (NULL, '谢逊', '2000100102'),
       (NULL, '殷天正', '2000100103'),
       (NULL, '韦一笑', '2000100104');

CREATE TABLE course
(
    id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(10) COMMENT '课程名称'
) COMMENT '课程表';

INSERT INTO course
VALUES (NULL, 'Java'),
       (NULL, 'PHP'),
       (NULL, 'MySQL'),
       (NULL, 'Hadoop');

CREATE TABLE student_course
(
    id        INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    studentid INT NOT NULL COMMENT '学生ID',
    courseid  INT NOT NULL COMMENT '课程ID',
    CONSTRAINT fk_courseid FOREIGN KEY (courseid) REFERENCES course (id),
    CONSTRAINT fk_studentid FOREIGN KEY (studentid) REFERENCES student (id)
) COMMENT '学生课程中间表';

INSERT INTO student_course
VALUES (NULL, 1, 1),
       (NULL, 1, 2),
       (NULL, 1, 3),
       (NULL, 2, 2),
       (NULL, 2, 3),
       (NULL, 3, 4);

-- 一对一
-- 实现: 在任意一方加入外键，关联另外一方的主键，并且设置外键为唯一的(UNIQUE)
-- 案例: 用户 与 用户详情的关系
-- 关系: 一对一关系，多用于单表拆分，将一张表的基础字段放在一张表中，
--      其他详情字段放在另一张表中，以提升操作效率
/*
 * 一对一本质上是“加了 UNIQUE 的一对多”。
 * 外键保证 userid 必须是存在的用户；UNIQUE 保证一个用户最多只有一条教育信息。
 * 去掉 UNIQUE，一个用户就可以有多条教育信息，变回一对多。
 */
CREATE TABLE tb_user
(
    id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name   VARCHAR(10) COMMENT '姓名',
    age    INT COMMENT '年龄',
    gender CHAR(1) COMMENT '1: 男 , 2: 女',
    phone  CHAR(11) COMMENT '手机号'
) COMMENT '用户基本信息表';

CREATE TABLE tb_user_edu
(
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    degree        VARCHAR(20) COMMENT '学历',
    major         VARCHAR(50) COMMENT '专业',
    primaryschool VARCHAR(50) COMMENT '小学',
    middleschool  VARCHAR(50) COMMENT '中学',
    university    VARCHAR(50) COMMENT '大学',
    userid        INT UNIQUE COMMENT '用户ID',
    CONSTRAINT fk_userid FOREIGN KEY (userid) REFERENCES tb_user (id)
) COMMENT '用户教育信息表';

INSERT INTO tb_user (id, name, age, gender, phone)
VALUES (NULL, '黄渤', 45, '1', '18800001111'),
       (NULL, '冰冰', 35, '2', '18800002222'),
       (NULL, '码云', 55, '1', '18800008888'),
       (NULL, '李彦宏', 50, '1', '18800009999');

INSERT INTO tb_user_edu (id, degree, major, primaryschool, middleschool, university, userid)
VALUES (NULL, '本科', '舞蹈', '静安区第一小学', '静安区第一中学', '北京舞蹈学院', 1),
       (NULL, '硕士', '表演', '朝阳区第一小学', '朝阳区第一中学', '北京电影学院', 2),
       (NULL, '本科', '英语', '杭州市第一小学', '杭州市第一中学', '杭州师范大学', 3),
       (NULL, '本科', '应用数学', '阳泉第一小学', '阳泉区第一中学', '清华大学', 4);


-- 多表查询概述
-- 数据准备
-- 创建dept表，并插入数据
CREATE TABLE dept
(
    id   INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    name VARCHAR(50) NOT NULL COMMENT '部门名称'
) COMMENT '部门表';

INSERT INTO dept (id, name)
VALUES (1, '研发部'),
       (2, '市场部'),
       (3, '财务部'),
       (4, '销售部'),
       (5, '总经办'),
       (6, '人事部');

-- 创建emp表，并插入数据
CREATE TABLE emp
(
    id        INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    name      VARCHAR(50) NOT NULL COMMENT '姓名',
    age       INT COMMENT '年龄',
    job       VARCHAR(20) COMMENT '职位',
    salary    INT COMMENT '薪资',
    entrydate DATE COMMENT '入职时间',
    managerid INT COMMENT '直属领导ID',
    dept_id   INT COMMENT '部门ID'
) COMMENT '员工表';

-- 添加外键
ALTER TABLE emp
    ADD CONSTRAINT fk_emp_dept_id
        FOREIGN KEY (dept_id) REFERENCES dept (id);

INSERT INTO emp (id, name, age, job, salary, entrydate, managerid, dept_id)
VALUES (1, '金庸', 66, '总裁', 20000, '2000-01-01', NULL, 5),
       (2, '张无忌', 20, '项目经理', 12500, '2005-12-05', 1, 1),
       (3, '杨逍', 33, '开发', 8400, '2000-11-03', 2, 1),
       (4, '韦一笑', 48, '开发', 11000, '2002-02-05', 2, 1),
       (5, '常遇春', 43, '开发', 10500, '2004-09-07', 3, 1),
       (6, '小昭', 19, '程序员鼓励师', 6600, '2004-10-12', 2, 1),
       (7, '灭绝', 60, '财务总监', 8500, '2002-09-12', 1, 3),
       (8, '周芷若', 19, '会计', 48000, '2006-06-02', 7, 3),
       (9, '丁敏君', 23, '出纳', 5250, '2009-05-13', 7, 3),
       (10, '赵敏', 20, '市场部总监', 12500, '2004-10-12', 1, 2),
       (11, '鹿杖客', 56, '职员', 3750, '2006-10-03', 10, 2),
       (12, '鹤笔翁', 19, '职员', 3750, '2007-05-09', 10, 2),
       (13, '方东白', 19, '职员', 5500, '2009-02-12', 10, 2),
       (14, '张三丰', 88, '销售总监', 14000, '2004-10-12', 1, 4),
       (15, '俞莲舟', 38, '销售', 4600, '2004-10-12', 14, 4),
       (16, '宋远桥', 40, '销售', 4600, '2004-10-12', 14, 4),
       (17, '陈友谅', 42, NULL, 2000, '2011-10-12', 1, NULL);

-- 概述
-- 多表查询就是指从多张表中查询数据。
-- 原来查询单表数据，执行的SQL形式为：
SELECT *
FROM emp;

-- 要执行多表查询，就只需要使用逗号分隔多张表即可
SELECT *
FROM emp,
     dept;
/*
 * 查询结果中包含了大量的结果集，总共102条记录
 * 员工表emp所有的记录(17)与部门表dept所有记录(6)的所有组合情况，这种现象称之为笛卡尔积。
 * 笛卡尔积: 笛卡尔乘积是指在数学中，两个集合A集合 和 B集合的所有组合情况。
 * 而在多表查询中，我们是需要消除无效的笛卡尔积的，只保留两张表关联部分的数据。
 */
/*
 * 笛卡尔积：数学里两个集合（A 集合和 B 集合）之间的所有组合情况。
 *   行数 = A 的行数 × B 的行数
 *
 * 左表（2 行）      右表（4 行）      全部组合（8 行）
 * ┌─────┐           ┌─────┐           ┌─────┬─────┐
 * │  A  │     ×     │  1  │     →     │  A  │  1  │
 * │  B  │           │  2  │           │  A  │  2  │
 * └─────┘           │  3  │           │  A  │  3  │
 *                   │  4  │           │  A  │  4  │
 *                   └─────┘           │  B  │  1  │
 *                                     │  B  │  2  │
 *                                     │  B  │  3  │
 *                                     │  B  │  4  │
 *                                     └─────┴─────┘
 * 行数计算：左表 m 行 × 右表 n 行 = m × n 行。
 * 本例为 2 × 4 = 8 行（所有组合 → 笛卡尔积）；
 * 任意一张表没有数据，笛卡尔积就是 0 行。

 * 多表查询中要做的事
 *   多表查询要消除无效的笛卡尔积，只留下两张表关联部分的数据。
 *   做法：加上连接条件（如 WHERE 表1.外键 = 表2.主键）
 *
 * 消除无效笛卡尔积（只保留有关联的）
 * 假设图中的关联关系是 A 对应 3、B 对应 2：
 *
 *                    +---+
 *   +---+            | 1 |
 *   | A |            | 2 |                  +---+---+
 *   +---+     ×      | 3 |     ======>      | A | 3 |   ← A 关联 3
 *   | B |            | 4 |                  | B | 2 |   ← B 关联 2
 *   +---+            +---+                  +---+---+
 *
 *   8 行组合  →  按连接条件过滤  →  只剩 2 行有效数据
 */
SELECT *
FROM emp,
     dept
WHERE emp.dept_id = dept.id;
-- 而由于id为17的员工，没有dept_id字段值，所以在多表查询时，根据连接查询的条件并没有查询到。


-- 多表查询分类（A 表示左表，B 表示右表）
/*
 * 多表查询
 * ├── 连接查询
 * │   ├── 内连接 INNER JOIN
 * │   │   └── 查 A、B 的交集部分。
 * │   │
 * │   ├── 外连接
 * │   │   ├── 左外连接 LEFT JOIN
 * │   │   │   └── 查左表全部数据，加上两表交集部分。
 * │   │   └── 右外连接 RIGHT JOIN
 * │   │       └── 查右表全部数据，加上两表交集部分。
 * │   │
 * │   └── 自连接
 * │       └── 表和它自己做连接查询，必须给表起别名。
 * │
 * └── 子查询
 *     └── 将一个查询嵌套在另一个 SQL 语句中，供外层语句使用。
 *
 * 示意图（A = 左表，B = 右表，## = 交集）
 *          _________          _________
 *        /           \      /           \
 *       /             \####/             \
 *      |      A       |####|      B       |
 *      |              |####|              |
 *       \             /####\             /
 *        \_________ /        \_________ /
 *
 *   内连接   ：只要 ####                → A ∩ B
 *   左外连接 ：A 整个圆（含 ####）       → A 全部 + 交集
 *   右外连接 ：B 整个圆（含 ####）       → B 全部 + 交集
 */


-- 内连接
/*
 * 示意图（A = 左表，B = 右表，## = 交集）
 *          _________          _________
 *        /           \      /           \
 *       /             \####/             \
 *      |      A       |####|      B       |
 *      |              |####|              |
 *       \             /####\             /
 *        \_________ /        \_________ /
 *
 *   内连接   ：只要 ####                → A ∩ B
 */
-- 内连接的语法分为两种: 隐式内连接、显式内连接。
-- 1). 隐式内连接
-- SELECT 字段列表 FROM 表1 , 表2 WHERE 条件 ... ;
/*
 A. 查询每一个员工的姓名 , 及关联的部门的名称 (隐式内连接实现)
 表结构: emp , dept
 连接条件: emp.dept_id = dept.id
 */
SELECT emp.name, dept.name
FROM emp,
     dept
WHERE emp.dept_id = dept.id;

SELECT e.name, d.name
FROM emp AS e,
     dept AS d
WHERE e.dept_id = d.id;

-- 2). 显式内连接
-- JOIN：把另一张表“接”进来一起查；ON：写两张表怎么对应（连接条件）。
-- SELECT 字段列表 FROM 表1 [ INNER ] JOIN 表2 ON 连接条件 ... ;
/*
 B. 查询每一个员工的姓名 , 及关联的部门的名称 (显式内连接实现)
 表结构: emp , dept
 连接条件: emp.dept_id = dept.id
 */
SELECT e.name, d.name
FROM emp AS e
         INNER JOIN dept AS d ON e.dept_id = d.id;


-- 外连接
-- 外连接分为两种：左外连接 和 右外连接。
/*
 * 示意图（A = 左表，B = 右表，## = 交集）
 *          _________          _________
 *        /           \      /           \
 *       /             \####/             \
 *      |      A       |####|      B       |
 *      |              |####|              |
 *       \             /####\             /
 *        \_________ /        \_________ /
 *
 *   左外连接 ：A 整个圆（含 ####）       → A 全部 + 交集
 *   右外连接 ：B 整个圆（含 ####）       → B 全部 + 交集

 * 左外连接和右外连接是可以相互替换的，只需要调整在连接查询时SQL中，
 * 表结构的先后顺序就可以了。在日常开发使用时，更偏向于左外连接。
 */
-- 1). 左外连接
-- SELECT 字段列表 FROM 表1 LEFT [ OUTER ] JOIN 表2 ON 条件 ... ;
/*
 * 左表某行在右表找到 1 行  → 拼成 1 行；
 * 找到多行               → 左表这行重复多次，各拼一行；
 * 一行都找不到            → 仍保留左表这行，右表的列全部填 NULL。

 * 【为什么用左连接】
 * 需求是“emp 的所有数据”，emp 写在 FROM 后面（左边），所以用 LEFT JOIN。
 * 这样没有部门的陈友谅也会出现，d.name 为 NULL；用内连接他会被丢掉。
 */
/*
 案例:
 A. 查询emp表的所有数据, 和对应的部门信息(左外连接)
 由于需求中提到，要查询emp的所有数据，所以是不能内连接查询的，需要考虑使用外连接查询。
 表结构: emp, dept
 连接条件: emp.dept_id = dept.id
 */
SELECT e.*, d.name
FROM emp AS e
         LEFT OUTER JOIN dept AS d ON e.dept_id = d.id;

-- 2). 右外连接
-- SELECT 字段列表 FROM 表1 RIGHT [ OUTER ] JOIN 表2 ON 条件 ... ;
/*
 * 【为什么用右外连接】
 * 需求是“dept 表的所有数据”，而这里 dept 写在 JOIN 的右边，所以用 RIGHT JOIN 保留 dept 全部行。
 * 没有员工的人事部也会出现，员工列全为 NULL；
 * 没有部门的陈友谅不会出现（他在 emp 里，emp 这次不是主表）。
 */
/*
 B. 查询dept表的所有数据, 和对应的员工信息(右外连接)
 由于需求中提到，要查询dept表的所有数据，所以是不能内连接查询的，
 需要考虑使用外连接查询。
 表结构: emp, dept
 连接条件: emp.dept_id = dept.id
 */
SELECT d.*, e.*
FROM emp AS e
         RIGHT OUTER JOIN dept AS d ON e.dept_id = d.id;

-- 把右外连接改成左外连接
SELECT d.*, e.*
FROM dept AS d
         LEFT OUTER JOIN emp AS e ON e.dept_id = d.id;


-- 自连接：自己连接自己，也就是把一张表连接查询多次。
-- SELECT 字段列表 FROM 表A 别名A JOIN 表A 别名B ON 条件 ... ;
/*
 * FROM 表A 别名A     把表A 当作第一份，叫 别名A；
 * JOIN 表A 别名B     把同一张表A 再当作第二份，叫 别名B；
 */
-- 对于自连接查询，可以是内连接查询，也可以是外连接查询。
-- 注意事项:
-- 在自连接查询中，必须要为表起别名，要不然我们不清楚所指定的条件、返回的字段，
-- 到底是哪一张表的字段。
/*
 案例：
 A. 查询员工 及其 所属领导的名字
 表结构: emp
 */
/*
 * 员工和领导都存在 emp 这一张表里，领导本身也是员工。
 * 员工那一行只记着领导的 id（managerid），没有领导名字；
 * 想拿到领导名字，就要回到 emp 里再找 id = managerid 的那一行。
 * 一次只看一份表，无法把“员工这一行”和“领导那一行”拼到同一行结果里，
 * 所以把 emp 当成两份：a 当“员工表”，b 当“领导表”。
 */
SELECT a.name, b.name
FROM emp AS a,
     emp AS b
WHERE a.managerid = b.id;

/*
 B. 查询所有员工 emp 及其领导的名字 emp , 如果员工没有领导, 也需要查询出来
 表结构: emp a , emp b
 */
/*
 * COMMENT 是建表时给表或列存的“说明文字”，保存在表结构里，只能用在 CREATE / ALTER 中；
 * AS 是查询时给结果列临时起的名字，只对这一次查询生效。
 * 两者用途不同，SELECT 里不能写 COMMENT。
 */
SELECT a.name AS '员工', b.name AS '领导'
FROM emp AS a
         LEFT OUTER JOIN emp AS b ON a.managerid = b.id;


-- 联合查询（union）：把多次查询的结果合并起来，形成一个新的查询结果集。
/*
 SELECT 字段列表 FROM 表A ...
 UNION [ ALL ]
 SELECT 字段列表 FROM 表B ....;

 对于联合查询的多张表的列数必须保持一致，字段类型也需要保持一致。
 union all 会将全部的数据直接合并在一起，union 会对合并之后的数据去重。
 */
/*
 案例:
 A. 将薪资低于 5000 的员工，和 年龄大于 50 岁的员工全部查询出来.
 也可以直接使用多条件查询，使用逻辑运算符 or 连接。
 */
SELECT *
FROM emp
WHERE salary < 5000
UNION ALL
SELECT *
FROM emp
WHERE age > 50;

-- 如果多条查询语句查询出来的结果，字段数量不一致，在进行union/union all联合查询时，将会报错。
SELECT *
FROM emp
WHERE salary < 5000
UNION
SELECT *
FROM emp
WHERE age > 50;


-- 子查询：SQL语句中嵌套SELECT语句，称为嵌套查询，又称子查询。
-- SELECT * FROM t1 WHERE column1 = ( SELECT column1 FROM t2 );
-- 子查询外部的语句可以是INSERT / UPDATE / DELETE / SELECT 的任何一个。
/*
 分类
 根据子查询结果不同，分为：
 A. 标量子查询（子查询结果为单个值）
 B. 列子查询(子查询结果为一列)
 C. 行子查询(子查询结果为一行)
 D. 表子查询(子查询结果为多行多列)
 根据子查询位置，分为：
 A. WHERE之后
 B. FROM之后
 C. SELECT之后
 */

-- 标量子查询：子查询返回的结果是单个值（数字、字符串、日期等），最简单的形式。
/*
 * <>  （不等于，也可写 !=，<> 是 SQL 标准写法）
 * 标量子查询只返回一个值，所以能像普通值一样直接用这些符号比较。
 */
-- 常用的操作符：= <> > >= < <=
/*
 案例:
 A. 查询 "销售部" 的所有员工信息
 完成这个需求时，我们可以将需求分解为两步：
 */
-- ①. 查询 "销售部" 部门ID
SELECT id
FROM dept
WHERE name = '销售部';

-- ②. 根据 "销售部" 部门ID, 查询员工信息
SELECT *
FROM emp
WHERE dept_id = 4;

-- 合并上述两步查询
-- SQL 语法规定，作为一个值使用的子查询，需要用圆括号括起来。
SELECT *
FROM emp
WHERE dept_id =
      (SELECT id
       FROM dept
       WHERE name = '销售部');

/*
 B. 查询在 "方东白" 入职之后的员工信息
 完成这个需求时，我们可以将需求分解为两步：
 */
-- ①. 查询 方东白 的入职日期
SELECT entrydate
FROM emp
WHERE name = '方东白';

-- ②. 查询指定入职日期之后入职的员工信息
SELECT *
FROM emp
WHERE entrydate > '2009-02-12';

-- 合并上述两步查询
SELECT *
FROM emp
WHERE entrydate > (SELECT entrydate
                   FROM emp
                   WHERE name = '方东白');

-- 列子查询：子查询返回的结果是一列（可以是多行）
-- 常用的操作符：IN 、NOT IN 、 ANY 、SOME 、 ALL
/*
 * IN：与结果中的任意一个值相等，即为真。
 * NOT IN：与结果中的每一个值都不相等，才为真。
 * ANY：至少有一次比较为真，整体就为真。
 * SOME：与 ANY 含义相同，可以互换。
 * ALL：与子查询返回的所有值比较，全部为真时，整体才为真。
 *
 * IN、NOT IN 后可以接普通值列表，也可以接子查询；
 * ANY、SOME、ALL 后接子查询，不能直接写成 ANY (10, 20, 30)。
 *
 * 与子查询配合时：
 *   IN     等价于 = ANY。
 *   NOT IN 等价于 <> ALL，不等价于 <> ANY。
 */
/*
 案例:
 A. 查询 "销售部" 和 "市场部" 的所有员工信息
 分解为以下两步:
 */
-- ①. 查询 "销售部" 和 "市场部" 的部门ID
SELECT id
FROM dept
WHERE name = '销售部'
   -- OR 连接的是两个完整的条件，而不是单独的值。MySQL 不会自动把前面的 name = 延续到下一行
   OR name = '市场部';

-- ②. 根据部门ID, 查询员工信息
SELECT *
FROM emp
WHERE dept_id IN (2, 4);

-- 合并上述两步查询
SELECT *
FROM emp
WHERE dept_id IN (SELECT id
                  FROM dept
                  WHERE name = '销售部'
                     OR name = '市场部');

/*
 B. 查询比 财务部 所有人工资都高的员工信息
 分解为以下两步:
 */
-- ①. 查询所有 财务部 人员工资
SELECT salary
FROM emp
WHERE dept_id = (SELECT id
                 FROM dept
                 WHERE name = '财务部');

-- ②. 比 财务部 所有人工资都高的员工信息
SELECT *
FROM emp
WHERE salary > ALL (SELECT salary
                    FROM emp
                    WHERE dept_id = (SELECT id
                                     FROM dept
                                     WHERE name = '财务部'));

/*
 C. 查询比研发部其中任意一人工资高的员工信息
 分解为以下两步:
 */
-- ①. 查询研发部所有人工资
SELECT salary
FROM emp
WHERE dept_id = (SELECT id
                 FROM dept
                 WHERE name = '研发部');

-- ②. 比研发部其中任意一人工资高的员工信息
SELECT *
FROM emp
WHERE salary > ANY (SELECT salary
                    FROM emp
                    WHERE dept_id = (SELECT id
                                     FROM dept
                                     WHERE name = '研发部'));

-- 行子查询：子查询返回的结果是一行（可以是多列）
-- 常用的操作符：= 、<> 、IN 、NOT IN
/*
 案例:
 A. 查询与 "张无忌" 的薪资及直属领导相同的员工信息 ;
 这个需求同样可以拆解为两步进行:
 */
-- ①. 查询 "张无忌" 的薪资及直属领导
SELECT salary, managerid
FROM emp
WHERE name = '张无忌';

-- ②. 查询与 "张无忌" 的薪资及直属领导相同的员工信息
SELECT *
FROM emp
WHERE (salary, managerid) = (SELECT salary, managerid
                             FROM emp
                             WHERE name = '张无忌');

-- 表子查询：子查询返回的结果是多行多列
-- 常用的操作符：IN
/*
 案例:
 A. 查询与 "鹿杖客" , "宋远桥" 的职位和薪资相同的员工信息
 分解为两步执行:
 */
-- ①. 查询 "鹿杖客" , "宋远桥" 的职位和薪资
SELECT job, salary
FROM emp
WHERE name = '鹿杖客'
   OR name = '宋远桥';

-- ②. 查询与 "鹿杖客" , "宋远桥" 的职位和薪资相同的员工信息
SELECT *
FROM emp
WHERE (job, salary) IN (SELECT job, salary
                        FROM emp
                        WHERE name = '鹿杖客'
                           OR name = '宋远桥');

/*
 B. 查询入职日期是 "2006-01-01" 之后的员工信息 , 及其部门信息
 分解为两步执行:
 */
-- ①. 入职日期是 "2006-01-01" 之后的员工信息
SELECT *
FROM emp
WHERE entrydate > '2006-01-01';

-- ②. 查询这部分员工, 对应的部门信息
SELECT e.*, d.*
FROM (SELECT *
      FROM emp
      WHERE entrydate > '2006-01-01') AS e
         LEFT JOIN dept AS d ON e.dept_id = d.id;


-- 多表查询案例
-- 数据环境准备:
CREATE TABLE salgrade
(
    grade INT,
    losal INT,
    hisal INT
) COMMENT '薪资等级表';

INSERT INTO salgrade (grade, losal, hisal)
VALUES (1, 0, 3000),
       (2, 3001, 5000),
       (3, 5001, 8000),
       (4, 8001, 10000),
       (5, 10001, 15000),
       (6, 15001, 20000),
       (7, 20001, 25000),
       (8, 25001, 30000);

/*
 * 隐式内连接：FROM 表1, 表2 WHERE 连接条件
 * 显式内连接：FROM 表1 INNER JOIN 表2 ON 连接条件
 * 【区别】
 * 结果和性能相同；显式写法把“连接条件”和“筛选条件”分开，更清晰，
 * 隐式写法漏写条件就会变成笛卡尔积，且外连接只能用显式写法。
 * 建议优先使用显式写法。
 */
/*
 1). 查询员工的姓名、年龄、职位、部门信息 （隐式内连接）
 表: emp , dept
 连接条件: emp.dept_id = dept.id
 */
SELECT e.name, e.age, e.job, d.name
FROM emp AS e,
     dept AS d
WHERE e.dept_id = d.id;

/*
 2). 查询年龄小于30岁的员工的姓名、年龄、职位、部门信息（显式内连接）
 表: emp , dept
 连接条件: emp.dept_id = dept.id
 */
-- ON 写“两表怎么关联”，WHERE 写“要哪些行”。
SELECT e.name, e.age, e.job, d.name
FROM emp AS e
         INNER JOIN dept AS d ON e.dept_id = d.id
WHERE e.age < 30;

/*
 3). 查询拥有员工的部门ID、部门名称
 表: emp , dept
 连接条件: emp.dept_id = dept.id
 */
-- DISTINCT 用来去掉结果中完全重复的行。
SELECT DISTINCT d.id, d.name
FROM emp AS e,
     dept AS d
WHERE e.dept_id = d.id;

/*
 4). 查询所有年龄大于40岁的员工, 及其归属的部门名称;
 如果员工没有分配部门, 也需要展示出来(外连接)
 表: emp , dept
 连接条件: emp.dept_id = dept.id
 */
SELECT e.*, d.name
FROM emp AS e
         LEFT JOIN dept AS d ON e.dept_id = d.id
WHERE e.age > 40;

/*
 5). 查询所有员工的工资等级
 表: emp , salgrade
 连接条件 : emp.salary >= salgrade.losal and emp.salary <= salgrade.hisal
 */
SELECT e.*, s.grade, s.losal, s.hisal
FROM emp AS e,
     salgrade AS s
WHERE e.salary >= s.losal
  AND e.salary <= s.hisal;

SELECT e.*, s.grade, s.losal, s.hisal
FROM emp AS e,
     salgrade AS s
WHERE e.salary BETWEEN s.losal
          AND s.hisal;

/*
 6). 查询 "研发部" 所有员工的信息及 工资等级
 表: emp , salgrade , dept
 连接条件 : emp.salary between salgrade.losal and salgrade.hisal ,
 emp.dept_id = dept.id
 查询条件 : dept.name = '研发部'
 */
SELECT e.*, s.grade
FROM emp AS e,
     dept AS d,
     salgrade AS s
WHERE e.dept_id = d.id
  AND (e.salary BETWEEN s.losal AND s.hisal)
  AND d.name = '研发部';

/*
 7). 查询 "研发部" 员工的平均工资
 表: emp , dept
 连接条件 : emp.dept_id = dept.id
 */
SELECT AVG(e.salary)
FROM emp AS e,
     dept AS d
WHERE e.dept_id = d.id
  AND d.name = '研发部';

/*
 8). 查询工资比 "灭绝" 高的员工信息。
 */
-- ①. 查询 "灭绝" 的薪资
SELECT emp.salary
FROM emp
WHERE name = '灭绝';

-- ②. 查询比她工资高的员工数据
SELECT *
FROM emp
WHERE salary > (SELECT emp.salary
                FROM emp
                WHERE name = '灭绝');

-- 9). 查询比平均薪资高的员工信息
-- ①. 查询员工的平均薪资
SELECT AVG(salary)
FROM emp;

-- ②. 查询比平均薪资高的员工信息
SELECT *
FROM emp
WHERE salary > (SELECT AVG(salary)
                FROM emp);

-- 10). 查询低于本部门平均工资的员工信息
-- ①. 查询指定部门平均薪资
SELECT AVG(e1.salary)
FROM emp AS e1
WHERE e1.dept_id = 1;

-- ②. 查询低于本部门平均工资的员工信息
SELECT *,
       (SELECT AVG(e1.salary)
        FROM emp AS e1
        WHERE e1.dept_id = e2.dept_id) AS 平均
FROM emp AS e2
WHERE e2.salary < (SELECT AVG(e1.salary)
                   FROM emp AS e1
                   WHERE e1.dept_id = e2.dept_id);

-- 11). 查询所有的部门信息, 并统计部门的员工人数
SELECT d.id,
       d.name,
       (SELECT COUNT(*)
        FROM emp AS e
        WHERE e.dept_id = d.id) AS '人数'
FROM dept AS d;

/*
 12). 查询所有学生的选课情况, 展示出学生名称, 学号, 课程名称
 表: student , course , student_course
 连接条件: student.id = student_course.studentid , course.id =
 student_course.courseid
 */
/*
 * 多对多查询要“经过中间表”：学生 → 中间表 → 课程。
 *   s.id = sc.studentid：找到这个学生的所有选课记录；
 *   sc.courseid = c.id：再根据每条记录的课程号找到课程名称。
 * 三张表用两个连接条件串起来，这里是隐式内连接。
 */
SELECT s.name, s.no, c.name
FROM student AS s,
     student_course AS sc,
     course AS c
WHERE s.id = sc.studentid
  AND sc.courseid = c.id;
