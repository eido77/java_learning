package chapter06;

public class BookTest {
    public static void main(String[] args) {
        /*
        案例：自定义图书类

        设定属性包括：
        书名bookName，
        作者author，
        价格price；

        方法包括：
        相应属性的get/set方法，
        图书信息介绍等。
         */
        Book book1 = new Book();
        book1.setBookName("Java");
        book1.setAuthor("Jack");
        book1.setPrice(100.0);

        //没显示
        // book1.showInfo();
        System.out.println(book1.showInfo()); // bookName = Java, author = Jack, price = 100.0

    }
}
