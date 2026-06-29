package chapter06;

public class Book {
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
    private String bookName;
    private String author;
    private double price;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // 获取图书信息
    public String showInfo() {
        return "bookName = " + bookName + ", author = " + author + ", price = " + price;
    }

}
