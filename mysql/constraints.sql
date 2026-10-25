-- 约束：作用于表中字段上的规则，用于限制存储在表中的数据。
/*
 * MySQL 常用约束与默认值
 * 一个字段后面可以连续写多个约束，顺序一般不影响含义，
 *
 * 1. NOT NULL：非空约束，禁止字段保存 NULL。
 *
 * 2. UNIQUE：唯一约束，禁止出现重复的键值。
 *    对单列唯一约束，该列的非 NULL 值不能重复。
 *    MySQL 允许可空的唯一列中存在多个 NULL。
 *    多列组成的唯一约束检查的是“组合是否重复”，不是每列分别唯一。
 *
 * 3. PRIMARY KEY：主键约束，用于唯一标识一行数据，要求非空且唯一。
 *    一张表最多定义一个主键，但这个主键可以包含多列，称为复合主键。
 *    复合主键的各列都不能为 NULL，且整组值不能重复；
 *    其中某一列的值可以单独重复。
 *
 * 4. DEFAULT：默认约束，设置字段的默认值。
 *
 * 5. CHECK：检查约束，用条件表达式限制允许保存的数据。
 *
 * 6. FOREIGN KEY：外键约束，用于维护数据之间的引用关系。
 *    用来让两张表的数据之间建立连接，保证数据的一致性和完整性。
 *    被引用的表通常称为“父表”，定义外键的表通常称为“子表”。
 */
-- 案例需求： 根据需求，完成表结构的创建。需求如下：
/*
 * 字段名  | 字段含义    | 字段类型     | 约束条件                 | 约束关键字
 * -------|------------|------------|-------------------------|----------------------------
 * id     | ID唯一标识  | int         | 主键，并且自动增长        | PRIMARY KEY、AUTO_INCREMENT
 * name   | 姓名       | varchar(10) | 不能为 NULL，并且唯一     | NOT NULL、UNIQUE
 * age    | 年龄       | int         | 大于 0，并且小于等于 120  | CHECK
 * status | 状态       | char(1)     | 未指定该值时，默认值为 1   | DEFAULT
 * gender | 性别       | char(1)     | 无额外约束               | 无
 */
SELECT DATABASE();
USE sql_study;

CREATE TABLE user
(
    id     INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name   VARCHAR(10) NOT NULL UNIQUE COMMENT '姓名',
    age    INT CHECK ( age > 0 AND age <= 120) COMMENT '年龄',
    status CHAR(1) DEFAULT '1' COMMENT '状态',
    gender CHAR(1) COMMENT '性别'
) COMMENT '用户表';

INSERT INTO user(name, age, status, gender)
VALUES ('Tom', 19, '1', '男'),
       ('Tom2', 25, '0', '男');

INSERT INTO user(name, age, status, gender)
VALUES ('Tom3', 19, '1', '男');

-- 非空约束：name 是 NOT NULL
INSERT INTO user(name, age, status, gender)
VALUES (NULL, 19, '1', '男');

-- 唯一约束：'Tom3' 已经存在
INSERT INTO user(name, age, status, gender)
VALUES ('Tom3', 19, '1', '男');

/*
 * id少掉的 4 是被“唯一约束”那条失败的插入用掉的，非空约束那条没有用掉编号。
 *
 * 1. 非空约束：MySQL 在准备这一行数据时就发现 name 是 NULL，
 *    直接报错，还没走到“分配自增 id”这一步，所以不消耗编号。
 * 2. 唯一约束：要先分配好 id（此时拿到了 4），真正往表里写的时候，
 *    才通过唯一索引发现 'Tom3' 重复，于是报错。
 *    但已经分配出去的 4 不会还回去。
 *
 * 自增计数器“只增不减”。
 * 自增 id 只保证“唯一、递增”，不保证“连续”。
 * 如果把某个 id 改成比计数器更大的值（例如改成 100），
 * MySQL 8.0 会把计数器往上推，下次生成 101。
 * MySQL 8.0 起计数器还会持久化，重启数据库后也不会回退。
 * 确实要重置时可用 ALTER TABLE 表名 AUTO_INCREMENT = n;
 * 但 n 不能小于等于表中现有最大 id。
 */
INSERT INTO user(name, age, status, gender)
VALUES ('Tom4', 80, '1', '男');

-- 检查约束
-- CHECK 也是在写入前就被检查，失败通常不会消耗自增编号。
INSERT INTO user(name, age, status, gender)
VALUES ('Tom5', -1, '1', '男');
INSERT INTO user(name, age, status, gender)
VALUES ('Tom6', 121, '1', '男');

INSERT INTO user(name, age, gender)
VALUES ('Tom5', 80, '男');


-- 外键约束
-- 外键：用来让两张表的数据之间建立连接，从而保证数据的一致性和完整性。
-- 准备数据
create table dept
(
    id   int auto_increment comment 'ID' primary key,
    name varchar(50) not null comment '部门名称'
) comment '部门表';

INSERT INTO dept (id, name)
VALUES (1, '研发部'),
       (2, '市场部'),
       (3, '财务部'),
       (4,
        '销售部'),
       (5, '总经办');

create table emp
(
    id        int auto_increment comment 'ID' primary key,
    name      varchar(50) not null comment '姓名',
    age       int comment '年龄',
    job       varchar(20) comment '职位',
    salary    int comment '薪资',
    entrydate date comment '入职时间',
    managerid int comment '直属领导ID',
    dept_id   int comment '部门ID'
) comment '员工表';

INSERT INTO emp (id, name, age, job, salary, entrydate, managerid, dept_id)
VALUES (1, '金庸', 66, '总裁', 20000, '2000-01-01', null, 5),
       (2, '张无忌', 20, '项目经理', 12500, '2005-12-05', 1, 1),
       (3, '杨逍', 33, '开发', 8400, '2000-11-03', 2, 1),
       (4, '韦一笑', 48, '开发', 11000, '2002-02-05', 2, 1),
       (5, '常遇春', 43, '开发', 10500, '2004-09-07', 3, 1),
       (6, '小昭', 19, '程序员鼓励师', 6600, '2004-10-12', 2, 1);

/*
 * 方式一：建表时添加
 * CREATE TABLE 表名 (
 *     字段名 数据类型,
 *     ...
 *     [CONSTRAINT 外键名称] FOREIGN KEY (外键字段名) REFERENCES 主表 (主表列名)
 * );
 *
 * 方式二：建表后添加
 * ALTER TABLE 表名
 *     ADD CONSTRAINT 外键名称 FOREIGN KEY (外键字段名) REFERENCES 主表 (主表列名);

 * fk_emp_dept_id            -- 约束名，自己起的；常见命名 fk_子表_外键字段
 * 给 emp 表添加一条外键规则，这条规则叫 fk_emp_dept_id，要求 dept_id 引用 dept 表中的 id。
 * 外键约束不是一列数据，而是表结构中的一条规则。
 * 以后想删除这条规则时，可以通过名字找到它。
 * FOREIGN KEY 是制定规则，CONSTRAINT fk_emp_dept_id 是给规则起名字。

 * 往 emp 插入或修改 dept_id 时，值必须在 dept.id 中存在（NULL 除外），
 * 添加时会检查 emp 现有数据，若已有不存在的 dept_id，添加外键会失败。
 */
-- 案例：为emp表的dept_id字段添加外键约束,关联dept表的主键id。
ALTER TABLE emp
    ADD CONSTRAINT fk_emp_dept_id FOREIGN KEY (dept_id) REFERENCES dept (id);

-- dept表（父表）删除 id 为 1 的部门信息
DELETE
FROM dept
WHERE id = 1;
-- emp 中还有 dept_id = 1 的员工引用它，才拒绝删除；删除没人引用的部门（如 id = 2）是可以成功的。

-- 删除外键
-- ALTER TABLE 表名 DROP FOREIGN KEY 外键名称;
-- 案例：删除emp表的外键fk_emp_dept_id。
/*
 * 删除外键后，数据和 dept_id 字段都还在，只是不再检查引用关系；
 * 添加外键时自动建的索引 fk_emp_dept_id 也会保留，
 * 如需删除可再执行 ALTER TABLE emp DROP INDEX fk_emp_dept_id;
 */
ALTER TABLE emp
    DROP FOREIGN KEY fk_emp_dept_id;


-- 删除/更新行为：添加了外键之后，再删除父表数据时产生的约束行为
/*
 * MySQL 外键的删除、更新行为（以下以 InnoDB 为例）
 *
 * 父表：被引用的表；子表：定义外键、引用父表的表。
 * ON DELETE：指定删除父表记录时，如何处理引用它的子表记录。
 * ON UPDATE：指定父表中被引用的键值发生变化时，如何处理子表外键值。
 *
 * 1. NO ACTION：拒绝破坏引用关系的操作，是省略相应行为时的默认设置。
 *    如果仍有子表记录引用该父表记录，就不允许删除父记录，
 *    也不允许将其被引用的键值修改为其他值。
 *
 * 2. RESTRICT：限制删除或更新。
 *    在 InnoDB 中，效果与 NO ACTION 相同：
 *    存在子表记录引用时，拒绝删除父记录或修改其被引用的键值。
 *
 * 3. CASCADE：级联处理，即子表随父表的相应变化一起改变。
 *    ON DELETE CASCADE：删除父记录时，一并删除引用它的子表记录。
 *    ON UPDATE CASCADE：修改父表被引用的键值时，将相关子表记录的外键值同步改为新值。
 *    更新级联修改的是对应的外键值，不是把父表整行内容复制给子表。
 *
 * 4. SET NULL：将相关子表记录的外键值设置为 NULL。
 *    删除父记录或修改父表被引用的键值时，保留相关子表记录，
 *    但将这些记录的外键值置为 NULL。
 *    要使用这种行为，子表外键列必须允许 NULL，不能设置 NOT NULL。
 *
 * 5. SET DEFAULT：将相关子表记录的外键值设置为该列的默认值。
 *    InnoDB 不支持这种外键行为，指定它会导致外键定义被拒绝。

 * ALTER TABLE 表名
 *     ADD CONSTRAINT 外键名称 FOREIGN KEY (外键字段) REFERENCES 主表名 (主表字段名)
 *         ON UPDATE 行为
 *         ON DELETE 行为;
 *
 * - ON UPDATE 行为：父表被引用的字段值（这里是 dept.id）被修改时，子表怎么办；
 * - ON DELETE 行为：父表记录被删除时，子表怎么办。
 * “行为”填 NO ACTION、RESTRICT、CASCADE、SET NULL 之一。
 * 两个子句可以只写一个、顺序可互换，没写的默认 NO ACTION。
 */
-- 演示如下：
-- 1). CASCADE
-- 在一般的业务系统中，不会修改一张表的主键值。
ALTER TABLE emp
    ADD CONSTRAINT fk_emp_dept_id FOREIGN KEY (dept_id) REFERENCES dept (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- 删除两张表之后，重新执行上面的建表语句，恢复最开始的表。
-- 删表顺序要先子表后父表（先 emp 再 dept），因为有外键时父表不能先删。

-- 2). SET NULL
-- 前提：emp.dept_id 允许为 NULL。本例建表时没写 NOT NULL，满足条件。
ALTER TABLE emp
    ADD CONSTRAINT fk_emp_dept_id FOREIGN KEY (dept_id) REFERENCES dept (id) ON UPDATE SET NULL ON DELETE SET NULL;
