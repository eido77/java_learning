CREATE DATABASE atguigu;

USE atguigu;

CREATE TABLE t_emp
(
    emp_id     INT AUTO_INCREMENT COMMENT '员工编号' PRIMARY KEY,
    emp_name   VARCHAR(100)  NOT NULL COMMENT '员工姓名',
    emp_salary DOUBLE(10, 5) NOT NULL COMMENT '员工薪资',
    emp_age    INT           NOT NULL COMMENT '员工年龄'
);

INSERT INTO t_emp (emp_name, emp_salary, emp_age)
VALUES ('andy', 777.77, 32),
       ('大风哥', 666.66, 41),
       ('康师傅', 111, 23),
       ('Gavin', 123, 26),
       ('小鱼儿', 123, 28);

-- 银行账户表（事务操作练习）
CREATE TABLE t_bank
(
    id      INT PRIMARY KEY AUTO_INCREMENT COMMENT '账号主键',
    account VARCHAR(20) NOT NULL UNIQUE COMMENT '账号',
    money   INT UNSIGNED COMMENT '金额，不能为负值'
);

INSERT INTO t_bank(account, money)
VALUES ('zhangsan', 1000),
       ('lisi', 1000);
