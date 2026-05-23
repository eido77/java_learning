package chapter01;

class SomeoneInfo {
    /*多行注释
    （单行注释用//，在01有出现，只能注释//所在的一行，//后面被注释）
    class里不体现，注释不参与编译
    多行注释不能嵌套
     */

    /**
     * 文档注释
     *
     * @param args
     * @author Eido
     */
    public static void main(String[] args) {
        System.out.println("姓名：XXX");
        //换行的操作
        System.out.println();
        System.out.println("性别：X");
        System.out.println("家庭住址：XXX，XXX");
    }
}