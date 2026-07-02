package chapter06;

import java.util.Scanner;

public class Person3Test {
    public static void main(String[] args) {

        Person3 p1 = new Person3();

        p1.eat();

        Person3 p2 = new Person3(1);
        System.out.println(p2.age); // 1


        Scanner scan = new Scanner(System.in);

    }
}
