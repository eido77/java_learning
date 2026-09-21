package chapter15;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedStreamTest {
    /*
    1. 基础IO流的框架
    抽象基类          4个节点流 (也称为文件流)   4个缓冲流（处理流的一种）    流的分类(功能说明)
    InputStream      FileInputStream         BufferedInputStream       字节输入流
    OutputStream     FileOutputStream        BufferedOutputStream      字节输出流
    Reader           FileReader              BufferedReader            字符输入流
    Writer           FileWriter              BufferedWriter            字符输出流
    记忆规律：
        InputStream/OutputStream 以 Stream 结尾 -> 字节流；Reader/Writer -> 字符流。
        带 Input/Reader 的是输入(读)，带 Output/Writer 的是输出(写)。
    2. 缓冲流的作用：
    提升文件读写的效率。
    3.
    4个缓冲流                   使用的方法
    处理非文本文件的字节流：
    BufferedInputStream        read(byte[] buffer)
    BufferedOutputStream       write(byte[] buffer,0,len) 、flush()

    处理文本文件的字符流：
    BufferedReader            read(char[] cBuffer) / String readLine()
    BufferedWriter            write(char[] cBuffer,0,len) / write(String )  、flush()
    3. 实现的步骤
    第1步：创建File的对象、流的对象（包括文件流、缓冲流）
    第2步：使用缓冲流实现 读取数据 或 写出数据的过程（重点）
        读取：int read(char[] cbuf/byte[] buffer) : 每次将数据读入到cbuf/buffer数组中，并返回读入到数组中的字符的个数
        写出：void write(String str)/write(char[] cbuf):将str或cbuf写出到文件中
             void write(byte[] buffer) 将byte[]写出到文件中
    第3步：关闭资源
     */

    /**
     * 需求：使用BufferedInputStream \ BufferedOutputStream复制一个图片
     * 注意：如下的操作应该使用try-catch-finally处理异常。
     */
    /*
     * 如果只用 try-catch 不用 finally：一旦 read/write 中途抛异常，close 代码就被跳过，
     * 流没关闭 -> 资源泄漏（文件句柄一直被占用）。所以规范情况下【必须】用 finally 关流。
     * Java7 之后更推荐用 try-with-resources，可自动关流，更简洁安全。
     */
    @Test
    public void test1() throws IOException {
        // 1. 创建相关的File类的对象
        File srcFile = new File("playgirl.jpg");
        File destFile = new File("playgirl_copy1.jpg");

        // 2. 创建相关的字节流、缓冲流
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);

        /*
         * BufferedInputStream 的构造器里放的是一个 InputStream（这里是 fis，负责“读”），
         * BufferedOutputStream 的构造器里放的是一个 OutputStream（这里是 fos，负责“写”）。
         * “装饰器模式”：缓冲流不直接连文件，而是包在文件流外面，给它加一层缓冲区。
         */
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        // 3. 数据的读入和写出
        byte[] buffer = new byte[1024]; // 1kb
        int len; // 记录每次读入到buffer中字节的个数
        /*
         * 用 fis/fos 是“每读写一个 buffer 就直接访问一次磁盘”；用 bis/bos 会先把数据
         * 攒在内存缓冲区里（默认8KB），攒满或flush时才真正访问磁盘，磁盘IO次数大大减少，所以更快。
         */
        while ((len = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        System.out.println("复制成功");

        // 4. 关闭资源
        /*
         * 关闭时【先关外层（缓冲流 bis/bos），再关内层（文件流 fis/fos）】。
         * 因为外层缓冲流关闭时会先把缓冲区剩余数据 flush 到内层流再关闭，如果先关内层，
         * 外层再想 flush 就会因底层已关而出错。
         *
         * 创建顺序：必须先创建内层（FileInputStream 等），再创建外层（Buffered 等），
         * 因为外层构造器需要拿一个已存在的内层流作为参数。所以创建和关闭顺序正好相反。
         */
        // 外层的流的关闭
        // 由于外层流的关闭也会自动的对内层的流进行关闭操作。所以可以省略内层流的关闭。
        /*
         * 缓冲流内部持有内层流的引用，它的 close() 源码里会调用内层流的
         * close()，形成“连锁关闭”。所以关了外层，内层也一起被关了。
         */
        bos.close();
        bis.close();
        // 内层的流的关闭
//        fis.close();
//        fos.close();
    }
}

/**
 * 测试FileInputStream + FileOutputStream 复制文件
 * BufferedInputStream + BufferedOutputStream 复制文件
 * 测试二者的效率。
 */
class CopyFileTest {
    @Test
    public void testSpendTime() {
        long start = System.currentTimeMillis();

        /*
         * 文件在操作系统里就是用“一串字符组成的路径”来定位的，所以 Java 里自然用 String 表示路径。
         * new File(String) 这个构造器就是设计成接收一个字符串路径，File 会把这个字符串解析成
         * 实际的文件/目录对象，所以“能识别”。
         */
        String src = "/Users/Shared/java_learning/io/1.MOV";
        String dest = "/Users/Shared/java_learning/io/3.MOV";
        /*
         * 磁盘是按“簇/块”分配空间的（比如每块4KB），文件最后一块用不满也要整块占用，
         * 所以“占用空间”通常略大于“实际大小”，且会因磁盘簇大小、文件系统不同而不同。
         * 两个复制文件字节数完全相同，但显示的“磁盘上大小”出现细微差异，属于正常的簇对齐现象
         */
//        copyFileWithFileStream(src, dest); // 818
//        copyFileWithBufferedStream(src, dest); // 21

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    /**
     * 使用BufferedInputStream + BufferedOutputStream 复制文件
     */
    public void copyFileWithBufferedStream(String src, String dest) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            // 1. 创建相关的File类的对象
            File srcFile = new File(src);
            File destFile = new File(dest);

            // 2. 创建相关的字节流、缓冲流
            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(destFile);

            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            // 3. 数据的读入和写出
            byte[] buffer = new byte[50];
            int len; // 记录每次读入到buffer中字节的个数
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            System.out.println("复制成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 关闭资源
            try {
                /*
                 * 需要判空是为了防止“空指针异常”。bos 一开始被初始化为 null，如果在创建 bos 之前
                 * （比如 new FileInputStream 时文件不存在就抛异常）就跳到了 finally，此时 bos 还是 null，
                 * 直接调用 null.close() 会报 NullPointerException。所以不管是不是缓冲流，
                 * 只要它可能还没被成功赋值，就都要先判断 != null 再关闭。
                 */
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 使用FileInputStream + FileOutputStream 复制文件
     */
    public void copyFileWithFileStream(String src, String dest) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 1. 创建相关的File类的对象
            File srcFile = new File(src);
            File destFile = new File(dest);

            // 2. 创建相关的字节流
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);

            // 3. 数据的读入和写出
            byte[] buffer = new byte[50];
            int len; // 记录每次读入到buffer中字节的个数
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            System.out.println("复制成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 关闭资源
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/**
 * 测试BufferedReader和BufferedWriter的使用
 */
class BufferedReaderWriterTest {
    /**
     * 使用BufferedReader将dbcp_utf-8.txt中的内容显式在控制台上。
     */
    @Test
    public void test1() throws IOException {
        File file = new File("/Users/Shared/java_learning/io/dbcp_utf-8.txt");

        /*
         * “匿名对象”写法：new FileReader(file) 创建出来后没有用变量名接住，直接当参数传给
         * BufferedReader 的构造器，用一次就够了，所以不必单独命名，代码更简洁。
         */
        BufferedReader br = new BufferedReader(new FileReader(file));

        // 读取的过程
        // 方式1：read(char[] cBuffer)
//        char[] cBuffer = new char[1024];
//        int len; // 记录每次读入到cBuffer中的字符的个数
//        while ((len = br.read(cBuffer)) != -1) {
        // 方法1
//            for (int i = 0; i < len; i++) {
//                System.out.print(cBuffer[i]);
//            }

        // 方法2
//            String str = new String(cBuffer, 0, len);
//            System.out.print(str);
//        }

        // 方式2：
        /**
         * readLine():每调用一次就读文件里的“一整行”，返回这一行的字符串内容（不含换行符）
         * 当读到文件末尾、没有更多行时，返回 null。
         */
        String data;
        while ((data = br.readLine()) != null) {
            System.out.println(data);
        }

        br.close();
    }

    /**
     * 使用BufferedReader和BufferedWriter实现文本文件的复制
     * 注意：开发中，还是需要使用try-catch-finally来处理流的异常。
     */
    @Test
    public void test2() throws IOException {
        // 1.造文件、造流
        File file1 = new File("/Users/Shared/java_learning/io/dbcp_utf-8.txt");
        File file2 = new File("/Users/Shared/java_learning/io/dbcp_utf-8_copy1.txt");

        BufferedReader br = new BufferedReader(new FileReader(file1));
        BufferedWriter bw = new BufferedWriter(new FileWriter(file2));

        // 2.文件的读写操作
        String data;
        while ((data = br.readLine()) != null) {
            /*
             * \r 是“回车符（Carriage Return）”，\n 是“换行符（Line Feed）”。
             * Windows 的换行是 \r\n 两个字符，Linux/Mac 是 \n。所以直接写 data+"\n" 在不同系统上
             * 换行可能不一致。更好的做法是用 bw.newLine()，它会自动使用当前系统的换行符。
             * bw.newLine() 比 data+"\n" 更规范，因为它自动适配当前操作系统的换行符，跨平台更安全。
             */
//            bw.write(data +"\n");
            bw.write(data);
            // 表示换行操作
            bw.newLine();
            /*
             * flush()“刷新”就是主动命令：把缓冲区里现在攒着的数据立刻写到磁盘文件中。防止数据一直卡在内存里没落盘。
             * 缓冲流写数据时，数据是先攒在内存缓冲区里的，并不会马上写进磁盘文件。
             * 但放在循环里每写一行就 flush 一次并不高效（频繁写盘），规范做法是【循环外统一 flush 或直接 close】，
             * 因为 close() 内部会自动先 flush 再关闭。所以这里循环内的 flush 可以去掉，靠最后的 bw.close() 兜底。
             *
             * 既不 flush 也不 close，会空白：数据全攒在内存缓冲区，缓冲区数据从没被写进磁盘，文件自然是空的。
             */
            bw.flush();
        }

        System.out.println("复制成功");

        // 3.关闭资源
        bw.close();
        br.close();
    }
}

/**
 * 案例
 * 分别使用文件流(FileInputStream、FileOutputStream)和缓冲流(BufferedInputStream、BufferedOutputStream)
 * 实现文本文件/图片/视频文件的复制。并比较二者在数据复制方面的效率。
 */
class InputOutputStreamTest {
    // 提供一个使用FileInputStream和FileOutputStream实现非文本文件复制的方法
    public void copyFileWithFile(String srcPath, String destPath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 1.
            File srcFile = new File(srcPath);
            File destFile = new File(destPath);

            // 2.
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);

            // 3. 读写过程
            int len;
            byte[] buffer = new byte[100];
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 4. 关闭资源
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCopyFileWithFile() {
        long start = System.currentTimeMillis();

        String srcPath = "/Users/Shared/java_learning/io/test-1.mp4";
        String destPath = "/Users/Shared/java_learning/io/test-2.mp4";

        copyFileWithFile(srcPath, destPath);

        long end = System.currentTimeMillis();

        System.out.println("花费的时间为：" + (end - start)); // 11302
    }


    // 提供一个使用BufferedInputStream和BufferedOutputStream实现非文本文件复制的方法
    public void copyFileWithBuffered(String srcPath, String destPath) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            // 1.
            File srcFile = new File(srcPath);
            File destFile = new File(destPath);

            // 2.
            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(destFile);

            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            // 3. 读写过程
            int len;
            byte[] buffer = new byte[100];
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 4. 关闭资源(1. 需要先关闭缓冲流，再关闭文件流 2. 默认情况下，关闭外层流时，也会自动关闭内部的流)
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 可以省略
//        fos.close();
//        fis.close();
        }
    }

    @Test
    public void testCopyFileWithBuffered() {
        long start = System.currentTimeMillis();

        String srcPath = "/Users/Shared/java_learning/io/test-1.mp4";
        String destPath = "/Users/Shared/java_learning/io/test-3.mp4";

        copyFileWithBuffered(srcPath, destPath);

        long end = System.currentTimeMillis();

        System.out.println("花费的时间为：" + (end - start)); // 11302  -- 643
    }
}
