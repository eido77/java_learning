/*
 * DDL 的完整名称是 Data Definition Language，即数据定义语言，用来定义和修改数据库、表、字段等对象。
 * SQL 通常分四类：
 * DDL：定义数据库、表、字段等结构；DML：增、删、改数据；DQL：查询数据；DCL：管理用户和权限。
 * DDL 有个重要特点：执行后会自动提交事务（叫“隐式提交”），不能用 ROLLBACK 撤销。
 * 所以删库、删表、删字段之前要先确认。
 */
-- 查看当前账户有权限看到的数据库
SHOW DATABASES;

-- 查看当前选择的数据库；尚未选择时返回 NULL
SELECT DATABASE();

/*
  创建数据库
  create database [ if not exists ] 数据库名 [ default charset 字符集 ] [ collate 排序规则 ] ;
  * COLLATE 用来指定“排序规则”（collation），它决定字符串怎样比较是否相等、怎样排序。
  * 例如 MySQL 8.0 中 utf8mb4 的默认排序规则是 utf8mb4_0900_ai_ci
  * ai 表示不区分重音，ci 表示不区分大小写。
  * 所以 'abc' = 'ABC' 的结果为真；换成 utf8mb4_bin（按二进制逐字节比较）后，两者就不相等。
  * 不写 COLLATE 时，MySQL 使用这个字符集的默认排序规则。
  * MySQL 8.0 通常是 utf8mb4_0900_ai_ci，5.7 是 utf8mb4_general_ci。
  * 字符集和排序规则必须配套，例如 utf8mb4 只能搭配名字以 utf8mb4_ 开头的排序规则。

  CREATE TABLE student_ddl_demo LIKE student;
  LIKE 会按原表定义创建一张空表，复制列属性和索引，不会复制表中的记录，也不会复制外键定义。
  * CREATE TABLE 新表 LIKE 旧表：照着旧表的结构建一张新的空表，常用来做备份表或测试表。
  * 它和 WHERE 里做模糊匹配的 LIKE 不是一回事，只是用了同一个关键字。
  * 它只复制结构，不复制数据。想连数据一起复制，可以接着执行 INSERT INTO 新表 SELECT * FROM 旧表。
  * 另一种写法 CREATE TABLE 新表 AS SELECT 会复制数据，但通常不保留主键、索引、自增这些定义。
 */
-- IF NOT EXISTS：数据库不存在时才创建，已存在时不因同名而报错。不会修改已有数据库的字符集。
CREATE DATABASE IF NOT EXISTS sql_study
    -- CHARACTER SET utf8mb4：指定数据库默认字符集，可保存中文及 Emoji 等字符。
    /*
     * CHARACTER SET 是一个整体，意思是“字符集”
     * 你看到的“没加 SET”的写法，一般用的是同义词 CHARSET，比如上面块注释里的 default charset。
     * 下面几种写法效果相同：DEFAULT CHARACTER SET utf8mb4、CHARACTER SET utf8mb4、CHARSET utf8mb4、CHARSET = utf8mb4。
     * 也就是说，DEFAULT 和等号都可以省略。
     * 如果整个字符集子句都不写，就用服务器的默认字符集（变量 character_set_server）。
     * MySQL 8.0 默认是 utf8mb4，5.7 默认是 latin1，后者存中文可能乱码或报错，所以显式写出来更稳妥。
     * utf8mb4 是 MySQL 里真正完整的 UTF-8 编码。mb4 可以理解为 multi-byte 4，即每个字符最多占 4 个字节。
     * MySQL 早期的 utf8（现在叫 utf8mb3）每个字符最多 3 个字节，存不了 Emoji 和部分生僻字。
     * 常用汉字在 utf8mb4 中占 3 个字节，Emoji 占 4 个字节。
     */
    DEFAULT CHARACTER SET utf8mb4;

-- 选择后续操作默认使用的数据库；重新连接后可能需要再次选择。
USE sql_study;

-- 查看数据库的创建定义。
/*
 * 结果会返回一条 CREATE DATABASE 语句。
 * MySQL 根据这个库“当前的定义”重新生成的，不是当初输入的原文，所以没写的默认项也会显示出来，
 * 例如 COLLATE utf8mb4_0900_ai_ci、DEFAULT ENCRYPTION='N'。
 * 结果里有些内容包在一种以“斜杠、星号、感叹号、版本号”开头的特殊注释中，这是 MySQL 的版本条件注释：
 * 版本号不低于其中数字的 MySQL 会执行里面的内容，更低的版本当作注释忽略。
 * 这条命令常用来确认一个库实际用的字符集和排序规则。
 */
SHOW CREATE DATABASE sql_study;

CREATE TABLE tb_user
(
    /*
     * comment 是给字段或表加说明
     * COMMENT '编号' 作为表定义的一部分保存在数据库里。
     * DESC 的结果里不显示 COMMENT。
     */
    id     INT COMMENT '编号',
    name   VARCHAR(50) COMMENT '姓名',
    age    TINYINT UNSIGNED COMMENT '年龄',
    gender CHAR(1) COMMENT '性别'
) COMMENT '用户表';

-- 查询当前数据库所有表
SHOW TABLES;

-- 查看指定表结构
-- 查看指定表的字段，字段的类型、是否可以为NULL，是否存在默认值等信息。
/*
 * DESC 是 DESCRIBE 的简写，用来查看表和视图的列结构（视图：保存起来的查询，用起来像一张表）。
 * DESC 的结果有 6 列：
 * Field 字段名、Type 类型、Null 是否允许为 NULL、Key 索引信息（PRI 主键、UNI 唯一、MUL 普通索引）、
 * Default 默认值、Extra 额外信息（例如 auto_increment）。
 * DESC 不显示字段的 COMMENT；要看注释，可以用 SHOW FULL COLUMNS FROM 表名。
 */
DESC tb_user;

-- 查询指定表的建表语句
/*
 * 没有指定的存储引擎来自服务器默认值（变量 default_storage_engine，通常是 InnoDB）；
 * 字符集和排序规则默认继承所在数据库 sql_study 的设置。
 * 显示出来的语句是 MySQL 根据表的当前定义重新生成的，关键字大小写、反引号、属性顺序等可能和写的不一样。

 * DESC：用表格列出每个字段的名称、类型、是否允许 NULL、键、默认值、额外信息，适合快速浏览字段。
 *   它不显示 COMMENT、存储引擎、字符集。
 * SHOW CREATE TABLE：输出一条完整的建表语句，包括字段 COMMENT、主键、唯一、外键等约束、索引、
 *   存储引擎、字符集、排序规则和表注释。适合查看完整细节，或者复制出来在别处建一张一样的表。
 */
SHOW CREATE TABLE tb_user;

-- 创建表结构
/*
设计一张员工信息表，要求如下：
1. 编号（纯数字）
2. 员工工号 (字符串类型，长度不超过10位)
3. 员工姓名（字符串类型，长度不超过10位）
4. 性别（男/女，存储一个汉字）
5. 年龄（正常人年龄，不可能存储负数）
6. 身份证号（二代身份证号均为18位，身份证中有X这样的字符）
7. 入职时间（取值年月日即可）
 */
CREATE TABLE emp
(
    id        INT COMMENT '编号',
    workno    VARCHAR(10) COMMENT '工号',
    name      VARCHAR(10) COMMENT '姓名',
    gender    CHAR(1) COMMENT '性别',
    age       TINYINT UNSIGNED COMMENT '年龄',
    idcard    CHAR(18) COMMENT '身份证号',
    entrydate DATE COMMENT '入职时间'
) COMMENT '员工表';
DESC emp;

-- 案例：为emp表增加一个新的字段“昵称”为nickname，类型为varchar(20)
/*
 * 新字段默认加在表的最后一列。
 * 想指定位置，可以在末尾加 FIRST（放到第一列）或 AFTER 某字段（放到该字段后面）
 */
ALTER TABLE emp
    ADD nickname VARCHAR(20) COMMENT '昵称';
DESC emp;

-- 修改字段名和字段类型
-- 将emp表的nickname字段修改为username，类型为varchar(30)
-- ALTER TABLE 表名 CHANGE 旧字段名 新字段名 类型 (长度) [ COMMENT 注释 ] [ 约束 ];
/*
 * 约束（constraint）是对字段数据的限制规则，例如 NOT NULL（不能为空）、DEFAULT（默认值）、UNIQUE（不能重复）、
 * PRIMARY KEY（主键）、CHECK（检查条件）
 */
ALTER TABLE emp
    CHANGE nickname username VARCHAR(30) COMMENT '用户名';
DESC emp;

-- 删除字段：将emp表的字段username删除
/*
 * DROP 是 DROP COLUMN 的简写。删除字段会把这一列的所有数据一起永久删除，而且 DDL 不能用 ROLLBACK 撤销。
 * 字段不存在时会报错。MySQL 不支持 DROP COLUMN IF EXISTS 这种写法（MariaDB 支持），不要混用。
 */
ALTER TABLE emp
    DROP username;
DESC emp;

-- 修改表名：将emp表的表名修改为 employee
ALTER TABLE emp
    RENAME TO employee;
# desc emp; -- 报错
DESC employee;

/*
 多个修改操作
 * 一条 ALTER TABLE 里可以写多个修改操作，用逗号分隔，MySQL 把它们当作一条语句执行。

 ALTER TABLE emp
    ADD nickname VARCHAR(20) COMMENT '昵称',
    ADD age INT COMMENT '年龄',
    MODIFY name VARCHAR(50);
 */

-- 删除表
-- 案例：如果tb_user表存在，则删除tb_user表
-- DROP TABLE 会删除表结构和全部数据。执行后不能用 ROLLBACK 恢复，因为 DDL 会自动提交事务。
DROP TABLE IF EXISTS tb_user;
SHOW TABLES;

-- 删除指定表, 并重新创建表
-- 在删除表的时候，表中的全部数据也都会被删除。
/*
 * TRUNCATE 的效果是“清空表中所有数据，但保留表结构”，所以下面的 show tables 仍然能看到 employee。
 * “删除表再重新创建”说的是它内部的做法。比 DELETE FROM employee 快得多，但也有代价：
 * 1. 不能加 WHERE，只能全部清空；
 * 2. AUTO_INCREMENT 计数会重置；
 * 3. 它属于 DDL，执行后不能 ROLLBACK；
 * 4. 如果这张表被其他表的外键引用，会执行失败。
 */
TRUNCATE TABLE employee;
SHOW TABLES;
