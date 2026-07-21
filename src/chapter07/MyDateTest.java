package chapter07;

public class MyDateTest {
    /*
    请根据以下代码自行定义能满足需要的MyDate类,在MyDate类中覆盖equals方法，
    使其判断当两个MyDate类型对象的年月日都相同时，结果为true，否则为false。
    public boolean equals(Object o)
     */
    public static void main(String[] args) {
        MyDate1 m1 = new MyDate1(14, 3, 1976);
        MyDate1 m2 = new MyDate1(14, 3, 1976);

        // == 比较引用类型时比的是地址值，m1、m2 是两次 new，地址不同
        if (m1 == m2) {
            System.out.println("m1==m2");
        } else {
            System.out.println("m1!=m2"); // m1 != m2
        }

        // 重写前：调用的是 Object.equals，内部就是 this == obj，仍然比地址
        // 重写后：调用 MyDate1.equals，比 year/month/day 三个属性值
        if (m1.equals(m2)) {
            System.out.println("m1 is equal to m2"); // 重写后输出这句
        } else {
            System.out.println("m1 is not equal to m2"); // 重写前输出这句
        }
    }
}

class MyDate1 {
    private int day;
    private int month;
    private int year;

    public MyDate1() {
    }

    public MyDate1(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // 手写equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof MyDate1) {
            MyDate1 myDate1 = (MyDate1) obj;
            return this.getDay() == myDate1.getDay() && this.getMonth() == myDate1.getMonth() && this.getYear() == myDate1.getYear();
        }
        return false;
    }
}
