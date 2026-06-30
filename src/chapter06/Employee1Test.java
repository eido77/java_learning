package chapter06;

import java.util.Scanner;

public class Employee1Test {
    public static void main(String[] args) {
        // Employee1 e = new Employee1(); // 造一个员工对象
        // 创建Employee1[]
        Employee1[] emps = new Employee1[2]; // 造一个能放 2 个员工的「盒子架」

        Scanner input = new Scanner(System.in);

        for (int i = 0; i < emps.length; i++) {
            emps[i] = new Employee1();
            System.out.println("----------添加第" + (i + 1) + "个员工----------");
            System.out.println("姓名：");
            String name = input.next();
            System.out.println("性别：");
            // charAt(0) 的意思是「取字符串里第 0 个位置的字符」。charAt = char At = 「在某位置的字符」，括号里的 0 是位置编号。
            // 把一个字符（char）和空字符串拼接，结果就变成了 String。
//            String gender = input.next().charAt(0) + ""; // char '男' 拼上空串 → 变成 String "男"
            char gender = input.next().charAt(0);
            System.out.println("年龄：");
            int age = input.nextInt();
            System.out.println("电话：");
            String phoneNumber = input.next();

            //给指定的employee对象的各属性赋值
            emps[i].setName(name);
            emps[i].setGender(gender);
            emps[i].setAge(age);
            emps[i].setPhoneNumber(phoneNumber);
        }

        //遍历员工列表
        System.out.println("----------员工列表----------");
        System.out.println("编号\t姓名\t性别\t年龄\t电话");
        for (int i = 0; i < emps.length; i++) {
            System.out.println((i + 1) + "\t" + emps[i].getInfo());
        }
        System.out.println("----------员工列表完成----------");
    }
}
