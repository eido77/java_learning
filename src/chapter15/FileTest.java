package chapter15;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileTest {
    /*
    1. File类的理解
    > File类位于java.io包下，本章中涉及到的相关流也都声明在java.io包下。
    > File类的一个对象，对应与操作系统下的一个文件或一个文件目录（或文件夹）
    > File类中声明了新建、删除、获取名称、重命名等方法，并没有涉及到文件内容的读写操作。要想实现文件内容的读写，
      我们就需要使用io流。
    > File类的对象，通常是作为io流操作的文件的端点出现的。
           > 代码层面，将File类的对象作为参数传递到IO流相关类的构造器中。
    2. 内部api使用说明
    2.1 构造器
    public File(String pathname)：以pathname为路径创建File对象，可以是绝对路径或者相对路径，如果pathname是相对路径，则默认的当前路径在系统属性user.dir中存储。
    public File(String parent, String child)：以parent为父路径，child为子路径创建File对象。
    public File(File parent, String child)：根据一个父File对象和子文件路径创建File对象
    2.2 方法
    获取文件和目录基本信息
        * public String getName() ：获取名称
        * public String getPath() ：获取路径
        * public String getAbsolutePath()：获取绝对路径
        * public File getAbsoluteFile()：获取绝对路径表示的文件
        * public String getParent()：获取上层文件目录路径。若无，返回null
        * public long length() ：获取文件长度（即：字节数）。不能获取目录的长度。
        * public long lastModified() ：获取最后一次的修改时间，毫秒值
    列出目录的下一级
         * public String[] list() ：返回一个String数组，表示该File目录中的所有子文件或目录。
         * public File[] listFiles() ：返回一个File数组，表示该File目录中的所有的子文件或目录。
    File类的重命名功能
        - public boolean renameTo(File dest):把文件重命名为指定的文件路径。
    判断功能的方法
        - public boolean exists() ：此File表示的文件或目录是否实际存在。
        - public boolean isDirectory() ：此File表示的是否为目录。
        - public boolean isFile() ：此File表示的是否为文件。
        - public boolean canRead() ：判断是否可读
        - public boolean canWrite() ：判断是否可写
        - public boolean isHidden() ：判断是否隐藏
    创建、删除功能
        - public boolean createNewFile() ：创建文件。若文件存在，则不创建，返回false。
        - public boolean mkdir() ：创建文件目录。如果此文件目录存在，就不创建了。如果此文件目录的上层目录不存在，也不创建。
        - public boolean mkdirs() ：创建文件目录。如果上层文件目录不存在，一并创建。
        - public boolean delete() ：删除文件或者文件夹
          删除注意事项：① Java中的删除不走回收站。② 要删除一个文件目录，请注意该文件目录内不能包含文件或者文件目录。
    3. 概念：
    绝对路径：以windows操作系统为例，包括盘符在内的文件或文件目录的完整路径。
    相对路径：相对于某一个文件目录来讲的相对的位置。
            在IDEA中，如果使用单元测试方法：相对于当前的module来讲
                     如果使用main()方法：相对于当前的project来讲
     */
    @Test
    public void test1() {
        /**
         * public File(String pathname)：以pathname为路径创建File对象，可以是绝对路径或者相对路径，如果pathname是相对路径，则默认的当前路径在系统属性user.dir中存储。
         */
        /*
         * 实际工程更推荐“相对路径 + 可配置”，避免把绝对路径硬编码进代码（换电脑/换环境就失效）。
         * 常见做法：路径写进配置文件（如 application.yml），或用类路径资源，或运行时用参数传入。
         * new File(...) 只在内存里创建了一个File对象（路径的封装），磁盘上不会产生任何文件。真正创建磁盘文件要调用 createNewFile()。
         */
        File file1 = new File("/Users/Shared/java_learning/hello.txt");
        File file2 = new File("ab");
        System.out.println(file1.getAbsoluteFile()); // /Users/Shared/java_learning/hello.txt
        System.out.println(file2.getAbsoluteFile()); // /Users/用户名/1/javacode/ab
        System.out.println(System.getProperty("user.dir")); // /Users/用户名/1/javacode
    }

    public static void main(String[] args) {
        File file2 = new File("abc");
        /*
         * 相对路径的基准是 JVM 启动时的“工作目录”，即 System.getProperty("user.dir")。
         * “单元测试相对module、main相对project”这个说法只在“多module（多模块）项目”里才有差别。
         * 项目只有一个module（单模块），或者project目录和module目录重合，两者的 user.dir 就一样，所以路径相同，属于正常现象。
         * 想验证，可在两处都打印：System.out.println(System.getProperty("user.dir"));
         */
        System.out.println(file2.getAbsoluteFile()); // /Users/用户名/1/javacode/abc
        System.out.println(System.getProperty("user.dir")); // /Users/用户名/1/javacode
    }

    @Test
    public void test2() {
        /**
         * public File(String parent, String child)：以parent为父路径，child为子路径创建File对象。
         * 参数1：一定是一个文件目录
         * 参数2：可以是一个文件，也可以是一个文件目录
         */
        /*
         * file1 = new File(父目录字符串, 子路径)  -> 结果路径 = 父/子
         * file2 = new File("abc", "a12")       -> 相对路径拼接，结果相对 user.dir
         * file3 = new File(file2, "ab.txt")    -> 用File对象当父，再拼子文件
         */
        File file1 = new File("/Users/Shared/java_learning/io", "abc.txt");
        System.out.println(file1); // /Users/Shared/java_learning/io/abc.txt
        File file2 = new File("abc", "a12");
        System.out.println(file2); // abc/a12

        /**
         * public File(File parent, String child)：根据一个父File对象和子文件路径创建File对象
         */
        File file3 = new File(file2, "ab.txt");
        System.out.println(file3); // abc/a12/ab.txt
    }

    /**
     * 获取文件和目录基本信息
     */
    @Test
    public void test3() {
        /*
         * getName/getPath/getAbsolutePath/getParent 只跟“路径字符串”有关，
         * 跟文件是否存在、内容多少完全无关，所以无论文件空不空、加没加内容，它们都不变。
         * 而 length() 和 lastModified() 是“读磁盘上真实文件”的信息：
         *   - 文件不存在或内容为空 -> length()=0；写入 "abc123"(6个ASCII字符=6字节) -> length()=6。
         *   - lastModified 文件不存在返回0；文件被修改后返回真实的时间戳(毫秒)。
         * 所以：加内容后只有 length() 和 lastModified() 变，其余不变，就是这个原因。
         */
        // hello.txt里面添加：abc123
        File file1 = new File("hello.txt");
        /**
         * public String getName()：获取名称
         */
        // 输出hello.txt，加入abc123之后不改变
        System.out.println(file1.getName());

        /**
         * public String getPath()：获取路径
         */
        // 输出hello.txt，加入abc123之后不改变
        System.out.println(file1.getPath());

        /**
         * public String getAbsolutePath()：获取绝对路径
         */
        // 输出 /Users/用户名/1/javacode/hello.txt，加入abc123之后不改变
        System.out.println(file1.getAbsolutePath());

        /**
         * public File getAbsoluteFile()：获取绝对路径表示的文件
         */
        // 输出 /Users/用户名/1/javacode/hello.txt，加入abc123之后不改变
        System.out.println(file1.getAbsoluteFile());

        /**
         * public String getParent()：获取上层文件目录路径。若无，返回null
         */
        // 输出null，加入abc123之后不改变
        // 因为file1用相对路径"hello.txt"创建，路径里没有父目录部分，所以getParent()拿不到，返回null。
        System.out.println(file1.getParent());

        /*
         * “链式调用”：先 getAbsoluteFile() 得到带绝对路径的File对象，再对它调 getParent()。
         * 因为绝对路径里含有父目录（/Users/用户名/1/javacode），所以这次能拿到父目录，不再是null。
         */
        // 输出 /Users/用户名/1/javacode，加入abc123之后不改变
        System.out.println(file1.getAbsoluteFile().getParent());

        /**
         * public long length()：获取文件长度（即：字节数）。不能获取目录的长度。
         */
        // 开始输出 0，加入abc123之后输出 6
        System.out.println(file1.length());

        /**
         * public long lastModified()：获取最后一次的修改时间，毫秒值
         */
        // 开始输出 0，加入abc123之后输出 1789722565086
        // 这个毫秒值可用 new java.util.Date(file1.lastModified()) 或 new SimpleDateFormat 转成可读时间。
        System.out.println(file1.lastModified());
    }

    @Test
    public void test4() {
        File file1 = new File("/Users/Shared/java_learning/io/io1");
        /**
         * public String getName()：获取名称
         */
        // io1
        System.out.println(file1.getName());

        /**
         * public String getPath()：获取路径
         */
        // /Users/Shared/java_learning/io/io1
        System.out.println(file1.getPath());

        /**
         * public String getAbsolutePath()：获取绝对路径
         */
        // /Users/Shared/java_learning/io/io1
        System.out.println(file1.getAbsolutePath());

        /**
         * public File getAbsoluteFile()：获取绝对路径表示的文件
         */
        // /Users/Shared/java_learning/io/io1
        System.out.println(file1.getAbsoluteFile());

        /**
         * public String getParent()：获取上层文件目录路径。若无，返回null
         */
        // /Users/Shared/java_learning/io
        System.out.println(file1.getParent());

        // /Users/Shared/java_learning/io
        System.out.println(file1.getAbsoluteFile().getParent());

        /**
         * public long length()：获取文件长度（即：字节数）。不能获取目录的长度。
         */
        /*
         * length() 对“目录”的返回值在不同操作系统上没有统一规定，行为依赖平台。
         * 在 Windows 上目录通常返回 0；
         * 在 mac/Linux 上，目录本身在文件系统里也占一个“目录项”，系统会返回该目录条目占用的字节数，
         * 常见就是 64、128 等（跟文件系统实现有关），并不代表里面有内容。
         * length() 只对“文件”有意义，对“目录”不要用、结果不可靠。
         */
        // 64
        System.out.println(file1.length());

        /**
         * public long lastModified()：获取最后一次的修改时间，毫秒值
         */
        // 1789724071850
        System.out.println(file1.lastModified());
    }

    /**
     * 列出目录的下一级
     */
    @Test
    public void test5() {
        File file1 = new File("/Users/Shared/java_learning/io");

        /*
         * list() 返回 String[]（只有名字，不含路径）；listFiles() 返回 File[]（是对象，能继续调 isFile/length 等方法）。
         * 用哪个看需求：只想要名字用 list()；还要对子项做判断/递归就用 listFiles()（更常用）。
         * 如果 file1 不是目录或无访问权限，返回值可能为 null，遍历前应判空避免空指针。
         */
        /**
         * public String[] list() ：返回一个String数组，表示该File目录中的所有子文件或目录。
         */
        String[] fileArr = file1.list();
        // 内容是 io 目录下“所有子文件和子目录”的名字（不只是目录，文件也在内）。
        for (String s : fileArr) {
            System.out.println(s);
        }

        /**
         * public File[] listFiles() ：返回一个File数组，表示该File目录中的所有的子文件或目录。
         */
        /*
         * file.getName() -> 只打印名字，如 abc.txt（和上面 list() 的效果一样）。
         * println(file)  -> 底层调用 file.toString()，即 getPath()，
         */
        File[] files = file1.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
//            System.out.println(file);
        }
    }

    /**
     * File类的重命名功能
     * public boolean renameTo(File dest):把文件重命名为指定的文件路径。
     * 举例：
     * file1.renameTo(file2):要想此方法执行完，返回true。
     * 要求：
     * file1必须存在，且file2必须不存在，且file2所在的文件目录需要存在。
     */
    /*
     * renameTo 是真正操作磁盘：它会把磁盘上 file1 对应的真实文件“重命名/移动”到 file2 指定的路径。
     * file2 这个File对象本身只是“目标路径的描述”，但 renameTo 调用后，磁盘上的文件确实被改名/移动了。
     * file1 这个变量执行后仍指向旧路径（旧路径此时已不存在），因为File对象是不可变的、不会自己更新。
     */
    @Test
    public void test6() {
        File file1 = new File("hello.txt");
        File file2 = new File("/Users/Shared/java_learning/abc.txt");

        boolean renameSuccess = file1.renameTo(file2);
        System.out.println(renameSuccess ? "重命名成功" : "重命名失败");

        System.out.println(file1.getName());
        System.out.println(file2.getName());
    }

    /**
     * 判断功能的方法
     */
    @Test
    public void test7() {
        File file1 = new File("/Users/Shared/java_learning/abc.txt");
        /**
         * public boolean exists() ：此File表示的文件或目录是否实际存在。
         */
        System.out.println(file1.exists());

        /**
         * public boolean isDirectory() ：此File表示的是否为目录。
         */
        System.out.println(file1.isDirectory());

        /**
         * public boolean isFile() ：此File表示的是否为文件。
         */
        System.out.println(file1.isFile());

        /**
         * public boolean canRead() ：判断是否可读
         */
        System.out.println(file1.canRead());

        /**
         * public boolean canWrite() ：判断是否可写
         */
        System.out.println(file1.canWrite());

        /**
         * public boolean isHidden() ：判断是否隐藏
         */
        System.out.println(file1.isHidden());

        File file2 = new File("/Users/Shared/java_learning/io");
        System.out.println(file2.exists());
        System.out.println(file2.isDirectory());
        System.out.println(file2.isFile());
        System.out.println(file2.canRead());
        System.out.println(file2.canWrite());
        System.out.println(file2.isHidden());
    }

    /**
     * 创建、删除功能
     * 删除注意事项：① Java中的删除不走回收站。② 要删除一个文件目录，请注意该文件目录内不能包含文件或者文件目录。
     */
    @Test
    public void test8() throws IOException {
        File file1 = new File("/Users/Shared/java_learning/io/hello.txt");

        // 测试文件的创建、删除
        if (!file1.exists()) {
            /**
             * public boolean createNewFile() ：创建文件。若文件存在，则不创建，返回false。
             */
            /*
             * createNewFile() 要真正操作磁盘（IO操作），可能失败（如上层目录不存在、无权限、磁盘错误），
             * 所以它声明抛出 IOException（受检异常/编译期异常），调用者必须处理。
             * boolean 返回值与“是否真实创建”的关系：
             *   - 返回true：磁盘上确实新建了这个文件；
             *   - 返回false：文件已存在，所以没有再创建；
             *   - 若发生IO错误：不是返回false，而是直接抛IOException。
             */
            boolean isSuccessed = file1.createNewFile();
            if (isSuccessed) {
                System.out.println("创建成功");
            }
        } else {
            System.out.println("此文件已存在");

            /**
             * public boolean delete() ：删除文件或者文件夹
             * 删除注意事项：① Java中的删除不走回收站。② 要删除一个文件目录，请注意该文件目录内不能包含文件或者文件目录。
             */
            System.out.println(file1.delete() ? "文件删除成功" : "文件删除失败");
        }
    }

    /**
     * public boolean mkdir() ：创建文件目录。如果此文件目录存在，就不创建了。如果此文件目录的上层目录不存在，也不创建。
     * public boolean mkdirs() ：创建文件目录。如果上层文件目录不存在，一并创建。
     */
    @Test
    public void test9() {
        // 前提：io文件目录存在，io2或io3目录是不存在的。
        File file1 = new File("/Users/Shared/java_learning/io/io2");
        System.out.println(file1.mkdir()); // true

        File file2 = new File("/Users/Shared/java_learning/io/io3");
        System.out.println(file2.mkdirs()); // true

        // 前提：io文件目录存在，io6或io7目录是不存在的。
        /*
         * file3 用 mkdir() 创建 io/io6/io4，但上层 io6 不存在，mkdir 不会自动补建上层，所以返回 false。
         * file4 用 mkdirs() 创建 io/io7/io5，上层 io7 不存在也会“一并创建”，所以返回 true。
         */
        File file3 = new File("/Users/Shared/java_learning/io/io6/io4");
        System.out.println(file3.mkdir()); // false

        File file4 = new File("/Users/Shared/java_learning/io/io7/io5");
        System.out.println(file4.mkdirs()); // true
    }

    @Test
    public void test10() {
        /**
         * public boolean delete() ：删除文件或者文件夹
         * 删除注意事项：① Java中的删除不走回收站。② 要删除一个文件目录，请注意该文件目录内不能包含文件或者文件目录。
         */
        File file1 = new File("/Users/Shared/java_learning/io/io7/io5");
        System.out.println(file1.delete());
    }
}

/**
 * 案例1
 * 创建一个与hello.txt文件在相同文件目录下的另一个名为abc.txt文件
 */
class Exer01 {
    public static void main(String[] args) {
        File file1 = new File("hello.txt");
        /*
         * getAbsoluteFile() 返回 File 对象，getAbsolutePath() 返回 String。
         * 打印时因为println会调用File.toString()，两者显示结果一样。
         * 只想“看/拼字符串”用 getAbsolutePath()；后面还要继续调File方法（如getParent）用 getAbsoluteFile()。
         */
//        System.out.println(file1.getAbsoluteFile());
        System.out.println(file1.getAbsolutePath()); // /Users/用户名/1/javacode/hello.txt

        // file1 用相对路径"hello.txt"创建，路径字符串里没有父目录部分，getParent 只按路径字符串取，取不到就返回null。
        System.out.println(file1.getParent()); // null

        // 获取file1的绝对路径，获取此路径的上层文件目录。
        System.out.println(file1.getAbsoluteFile().getParent()); // /Users/用户名/1/javacode

        /*
         * file1.getAbsolutePath()不能getParent()
         * getAbsolutePath() 返回的是 String（字符串），String 类根本没有 getParent() 方法，不能调。
         * 要想链式 getParent()，必须先得到 File 对象，即用 getAbsoluteFile().getParent()。
         */
        File file2 = new File(file1.getAbsoluteFile().getParent(), "abc.txt");
        System.out.println(file2.getAbsolutePath());
    }
}

/**
 * 案例2
 * 判断指定目录下是否有后缀名为.jpg的文件，如果有，就输出该文件名称
 * 提示：File类提供了文件过滤器方法(拓展)
 */
class Exer02 {
    @Test
    public void test1() {
        File dir = new File("/Users/Shared/java_learning");

        // 方式1
//        String[] listFiles = dir.list();
//        for (String s : listFiles) {
        // String.endsWith(后缀) 判断字符串是否以指定后缀结尾，返回boolean。常用于按扩展名筛选文件（.jpg/.txt等）。
//            if (s.endsWith(".jpg")) {
//                System.out.println(s);
//            }
//        }

        // 方式2
//        public String[] list(FilenameFilter filter)
        /*
         * “文件名过滤器”：list(FilenameFilter) 会在列目录时，对每个子项调用一次 accept()，
         * accept 返回true的才会被收进结果数组。这是回调机制（策略模式思想），把“过滤规则”交给你自定义。
         * 这里用的是匿名内部类来实现 FilenameFilter 接口。JDK8+还可用Lambda简化：
         * dir.list((d, name) -> name.endsWith(".jpg"));
         */
        String[] listFiles = dir.list(new FilenameFilter() {
            @Override
            // name:即为子文件或子文件目录的名称
            public boolean accept(File dir, String name) {
                /*
                 * 注释掉的if-else写法与下面 return name.endsWith(".jpg"); 完全等价，
                 * 直接return布尔表达式更简洁，是更推荐的写法。
                 */
//                if (name.endsWith(".jpg")) {
//                    return true;
//                } else {
//                    return false;
//                }
                return name.endsWith(".jpg");
            }
        });

        for (String s : listFiles) {
            System.out.println(s);
        }
    }
}

/**
 * 案例3
 * 遍历指定文件目录下的所有文件的名称，包括子文件目录中的文件。
 */
class Exer03 {
    /**
     * public void printFileName(File file) // file可能是文件，也可能是文件目录
     */
    @Test
    public void test1() {
        File file = new File("/Users/用户名/1/javacode/src");
        printFileName(file);
    }

    /*
     * 这是“递归”遍历目录：
     * 如果 file 是文件(isFile)，直接打印它的名字（递归的“出口/终止条件”）。
     * 如果 file 是目录(isDirectory)，先取出它的所有下一级(listFiles)，
     * 再对每个下一级 f 调用自己 printFileName(f) —— 目录里套目录时会一层层深入，最终把所有文件名都打印出来。
     * 递归核心：大问题(遍历整个目录)拆成小问题(遍历每个子项)，直到遇到文件为止。
     */
    public void printFileName(File file) {
        if (file.isFile()) {
            System.out.println(file.getName());
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                printFileName(f);
            }
        }
    }

    /**
     * 拓展1：计算指定文件目录占用空间的大小
     * public long getDirectorySize(File file) // file可能是文件，也可能是文件目录
     */
    public long getDirectorySize(File file) {
        /*
         * 递归求和：
         * 是文件：大小就是 file.length()（终止条件）。
         * 是目录：遍历下一级，把每个子项的 getDirectorySize 结果累加，得到整个目录总大小。
         * 这样即使多层子目录，也能把最里层文件的字节数一层层加回来。
         */
        long size = 0;

        if (file.isFile()) {
            size = file.length();
        } else {
            File[] all = file.listFiles(); // 获取file的下一级
            // 累加all[i]的大小
            for (File f : all) {
                size += getDirectorySize(f); // f的大小
            }
        }
        return size;
    }

    @Test
    public void testGetDirectorySize() {
        File dir = new File("/Users/Shared/java_learning");
        System.out.println("总文件大小为：" + getDirectorySize(dir) + "字节");
    }

    /**
     * 拓展2：删除指定文件目录及其下的所有文件
     * public void deleteDirectory(File file) // file可能是文件，也可能是文件目录
     */
    public void deleteDirectory(File file) {
        /*
         * 递归删除的关键顺序：必须“先删光子项，再删自己”。
         * 因为 File.delete() 不能删除“非空目录”，所以要先把里面掏空，最后目录才删得掉。
         * 如果file是文件，直接delete
         * 如果file是目录，先把它的下一级干掉，然后删除自己
         */
        if (file.isDirectory()) {
            File[] all = file.listFiles();
            // 循环删除的是file的下一级
            for (File f : all) { // f代表file的每一个下级
                deleteDirectory(f);
            }
        }
        // 删除自己
        file.delete();
    }

    @Test
    public void testDeleteDirectory() {
        File dir = new File("/Users/Shared/java_learning/io/io7/ioabc123");
        deleteDirectory(dir);
        System.out.println("删除完毕");
    }
}
