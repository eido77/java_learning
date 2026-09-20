package chapter15;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileStreamTest {
    /*
    1. IO流的分类（输入input / 输出output）
    流的分类.png
    > 流向的不同：输入流、输出流
    > 处理单位的不同：字节流、字符流
    > 流的角色的不同：节点流、处理流
    2. 基础IO流的框架：
      抽象基类          4个节点流(文件流)      流的分类(功能说明)
      InputStream      FileInputStream       字节输入流
      OutputStream     FileOutputStream      字节输出流
      Reader           FileReader            字符输入流
      Writer           FileWriter            字符输出流
      记忆规律：
      InputStream/OutputStream 以 Stream 结尾 -> 字节流；Reader/Writer -> 字符流。
      带 Input/Reader 的是输入(读)，带 Output/Writer 的是输出(写)。
     */
}

/*
3. FileReader \ FileWriter 的使用
    3.1 执行步骤：
    第1步：创建读取或写出的File类的对象
    第2步：创建输入流或输出流
    第3步：具体的读入或写出的过程。
         读入：read(char[] cbuffer)
         写出：write(String str) / write(char[] cbuffer,0,len)
    第4步：关闭流资源，避免内存泄漏
    3.2 注意点：
    ① 因为涉及到流资源的关闭操作，所以出现异常的话，需要使用try-catch-finally的方式来处理异常
    ② 对于输入流来讲，要求File类的对象对应的物理磁盘上的文件必须存在。否则，会报FileNotFoundException
       对于输出流来讲，File类的对象对应的物理磁盘上的文件可以不存在。
            > 如果此文件不存在，则在输出的过程中，会自动创建此文件，并写出数据到此文件中。
            > 如果此文件存在，使用 FileWriter(File file) 或 FileWriter(File file,false):
                                                    输出数据过程中，会新建同名的文件对现有的文件进行覆盖。
                           FileWriter(File file,true) : 输出数据过程中，会在现有的文件的末尾追加写出内容。
 */
class FileReaderWriterTest {
    /**
     * 需求：读取hello.txt中的内容，显示在控制台上。
     * 异常使用throws的方式处理，不太合适。见 test2()
     * hello.txt内容如下：
     * helloworld123
     */
    @Test
    public void test1() throws IOException {
        // 1.创建File类的对象，对应着hello.txt文件
        File file = new File("hello.txt");

        // 2.创建输入型的字符流，用于读取数据
        /*
         * new FileReader(file) 会抛出 FileNotFoundException(属于IOException的子类)，
         * 这是“受检异常(checked exception)”，编译器强制必须处理，否则编译不通过，所以“报异常”。
         */
        FileReader fr = new FileReader(file);

        // 3.读取数据，并显示在控制台上
        /**
         * int read()：读取单个字符。
         * 如果已到达流末尾则返回 -1。
         * 不能强转成 String，因为 read() 一次只读一个字符(一个int)，String是字符序列，类型不兼容。
         */
        // 方式1
//        int data = fr.read();
//        while (data != -1) {
//            System.out.print((char) data); // helloworld123
        // 读下一个字符，进入下一轮判断。
//            data = fr.read();
//        }

        // 方式2
        int data;
        while ((data = fr.read()) != -1) {
            System.out.print((char) data);
        }

        // 4.流资源的关闭操作（必须要关闭，否则会内存泄漏）
        fr.close();
    }

    /**
     * 需求：读取hello.txt中的内容，显示在控制台上。
     * 使用try-catch-finally的方式处理异常。确保流一定可以关闭，避免内存泄漏
     * hello.txt内容如下：
     * helloworld123
     */
    @Test
    public void test2() {
        FileReader fr = null;
        try {
            // 1.创建File类的对象，对应着hello.txt文件
            File file = new File("hello.txt");

            // 2.创建输入型的字符流，用于读取数据
            fr = new FileReader(file);

            // 3.读取数据，并显示在控制台上
            // 方法1
//            int data;
//            while ((data = fr.read()) != -1) {
//                System.out.print((char) data);
//            }

            /**
             * int read(char[] cbuf)：尝试把字符读入到 cbuf 数组中，尽量填满数组。
             * 实际读取到的字符个数(可能小于数组长度)；如果已到达流末尾则返回 -1。
             * 返回的 len 才是本次有效字符数，遍历时必须用 len 而不是数组长度，
             * 否则会打印到上一轮残留的旧数据。
             */
            // 方法2
            /*
             * 建一个 char[] 当“缓冲区”，每次 read(cbuffer) 一次读入多个字符放进数组，
             * 返回值 len 表示这次读进来几个字符。然后用 for 循环遍历数组的前 len 个字符打印出来，
             * 再继续读下一批，直到 read 返回 -1(读完)。这样比一个字符一个字符读少了很多次磁盘IO，更快。
             */
//            char[] cbuffer = new char[5];
//            int len = fr.read(cbuffer);
//            while (len != -1) {
            // 遍历数组
            /*
             * 最后一次读取可能读不满整个数组，只读进了 len 个新字符，
             * 数组后面剩下的是上一轮的旧数据。用 i < len 只遍历本次真正读到的字符，
             * 用 cbuffer.length 会把旧数据也打印出来，导致结果出错。
             */
//                for (int i = 0; i < len; i++) {
//                    System.out.print(cbuffer[i]);
//                }
//
//                len = fr.read(cbuffer);
//            }

            // 方法3
            char[] cbuffer = new char[5];
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                // 遍历数组
                for (int i = 0; i < len; i++) {
                    System.out.print(cbuffer[i]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4.流资源的关闭操作（必须要关闭，否则会内存泄漏）
            /*
             * finally 里的代码无论 try 中是否发生异常都“一定会执行”(除非JVM退出)。
             * 关闭流放在 finally，是为了保证即使读取过程中抛了异常，流也能被关闭，避免资源泄漏。
             *
             * fr.close() 本身也声明 throws IOException(受检异常)，
             * 所以不管放在 finally 里还是放在整个结构外面，都必须处理这个异常，
             */
            try {
                /*
                 * 如果第2步 new FileReader(file) 失败了，fr 仍然是 null，
                 * 此时若直接 fr.close() 会抛 NullPointerException(空指针)。
                 * 加 if (fr != null) 判断，确保只有流成功创建后才关闭，避免空指针
                 */
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        /*
         * 【改进建议】JDK7+ 推荐使用 try-with-resources 自动关闭流，代码更简洁、更规范，
         * try (FileReader fr = new FileReader("hello.txt")) {
         *     char[] cbuffer = new char[1024];
         *     int len;
         *     while ((len = fr.read(cbuffer)) != -1) {
         *         System.out.print(new String(cbuffer, 0, len));
         *     }
         * } catch (IOException e) {
         *     e.printStackTrace();
         * }
         */
    }

    /**
     * 需求：将内存中的数据写出到指定的文件中
     */
    /*
     * 这是“输出(写)”操作(从内存写到磁盘文件)。
     * 只有“输出流”能在文件不存在时自动创建文件；
     * “输入流(读)”：文件不存在会抛 FileNotFoundException，因为没东西可读。
     *
     * “内容覆盖”，不是删除重建文件。使用 FileWriter(file) 或 FileWriter(file,false) 时，
     * 会清空原文件的内容再写入新内容(文件本身及其inode通常还是原来的)。
     *
     * “覆盖还是追加”由构造器的第二个参数 append 决定(false覆盖/true追加)
     */
    @Test
    public void test3() {
        FileWriter fw = null;
        try {
            // 1. 创建File类的对象，指明要写出的文件的名称
            File file = new File("info.txt");

            // 2. 创建输出流
            // 覆盖文件，使用的构造器：
            fw = new FileWriter(file);
//            fw = new FileWriter(file, false);

            // 在现有的文件基础上，追加内容使用的构造器：
            /*
             * new FileWriter(file) 等价于 new FileWriter(file, false)，
             * 两者完全一样，都是“覆盖”模式(false是默认值)。
             * 而 new FileWriter(file, true) 是“追加”模式，在文件末尾接着写，不清空原内容。
             */
//            fw = new FileWriter(file, true);

            // 3. 写出的具体过程
            // 输出的方法：write(String str) / write(char[] cdata)
            fw.write("I love U!\n");
            fw.write("You love him!\n");
            fw.write("He love me!!!");

            System.out.println("输出成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 关闭资源
            try {
                // 防止流创建失败时 fw 为 null，直接 close 会空指针，所以先判空。
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 需求：复制一份hello.txt文件，命名为hello_copy.txt
     */
    @Test
    public void test4() {
        FileReader fr = null;
        FileWriter fw = null;
        try {
            // 1. 创建File类的对象
            File srcFile = new File("hello.txt");
            File destFile = new File("hello_copy.txt");

            // 2. 创建输入流、输出流
            /*
             * 复制 = 从源文件“读”出来，再“写”到目标文件。
             * srcFile 是源文件(要被复制的 hello.txt)，destFile 是目标文件(复制生成的 hello_copy.txt)。
             * FileReader 是输入流(负责读)，所以要读源文件 -> 放 srcFile；
             * FileWriter 是输出流(负责写)，所以要写到目标文件 -> 放 destFile。
             */
            fr = new FileReader(srcFile);
            fw = new FileWriter(destFile);

            // 3. 数据的读入和写出的过程
            char[] cbuffer = new char[5];
            // 记录每次读入到cbuffer中的字符的个数
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                /*
                 * write(cbuffer) 会把整个数组(全部5个)都写出，包括最后一次残留的旧字符，
                 * 导致目标文件多出错误内容(和test2里遍历用cbuffer.length同理)。
                 * write(cbuffer, 0, len) 只写本次真正读到的 len 个字符
                 */
                fw.write(cbuffer, 0, len);
            }

            System.out.println("复制成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 关闭流资源
            try {
                // 防止流创建失败时对象为null，直接close会空指针，先判空更健壮。
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        /*
         * 【改进建议】用 try-with-resources 可自动关闭两个流，无需手写两段 finally：
         * try (FileReader fr = new FileReader("hello.txt");
         *      FileWriter fw = new FileWriter("hello_copy.txt")) {
         *     char[] cbuffer = new char[1024];
         *     int len;
         *     while ((len = fr.read(cbuffer)) != -1) {
         *         fw.write(cbuffer, 0, len);
         *     }
         * } catch (IOException e) {
         *     e.printStackTrace();
         * }
         */
    }

    /**
     * 需求：复制一份playgirl.jpg文件，命名为playgirl_copy.jpg
     * 复制失败！因为字符流不适合用来处理非文本文件。
     */
    /*
     * copy文件之所以存在，是因为输出流一创建(new FileWriter(destFile))就自动建好了目标文件，
     * 只不过写进去的字节被“字符流的编解码”破坏了，所以文件在但打不开。
     * 一般不能救回。字符流按字符集解码/编码时，无法映射的字节会被替换(如变成 '?' 或替换符)，
     * 属于“不可逆的信息丢失”，无法还原成正确的jpg。
     */
    @Test
    public void test5() {
        FileReader fr = null;
        FileWriter fw = null;
        try {
            // 1. 创建File类的对象
            File srcFile = new File("playgirl.jpg");
            File destFile = new File("playgirl_copy.jpg");

            // 2. 创建输入流、输出流
            fr = new FileReader(srcFile);
            fw = new FileWriter(destFile);

            // 3. 数据的读入和写出的过程
            char[] cbuffer = new char[5];
            // 记录每次读入到cbuffer中的字符的个数
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                fw.write(cbuffer, 0, len);
            }

            System.out.println("复制成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 关闭流资源
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/*
4. FileInputStream \ FileOutputStream 的使用
    4.1 执行步骤：
    第1步：创建读取或写出的File类的对象
    第2步：创建输入流或输出流
    第3步：具体的读入或写出的过程。
         读入：read(byte[] buffer)
         写出：write(byte[] buffer,0,len)
    第4步：关闭流资源，避免内存泄漏
    4.2 注意点：
     --> 3.2 注意点：
         ① 因为涉及到流资源的关闭操作，所以出现异常的话，需要使用try-catch-finally的方式来处理异常
         ② 对于输入流来讲，要求File类的对象对应的物理磁盘上的文件必须存在。否则，会报FileNotFoundException
            对于输出流来讲，File类的对象对应的物理磁盘上的文件可以不存在。
                 > 如果此文件不存在，则在输出的过程中，会自动创建此文件，并写出数据到此文件中。
                 > 如果此文件存在，使用 FileWriter(File file) 或 FileWriter(File file,false):
                                                         输出数据过程中，会新建同名的文件对现有的文件进行覆盖。
                                FileWriter(File file,true) : 输出数据过程中，会在现有的文件的末尾追加写出内容。
    > 在3.2 注意点的基础之上，看其他的注意点。
    > 对于字符流，只能用来操作文本文件，不能用来处理非文本文件的。
      对于字节流，通常是用来处理非文本文件的。但是，如果涉及到文本文件的复制操作，也可以使用字节流。
    说明：
    文本文件：.txt 、.java 、.c、.cpp、.py等
    非文本文件：.doc、.xls 、.jpg 、.pdf、.mp3、.mp4、.avi 等
 */
class FileStreamTest2 {
    /**
     * 需求：复制一份playgirl.jpg文件，命名为playgirl_copy.jpg
     */
    @Test
    public void test1() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 1. 创建相关的File类的对象
            File srcFile = new File("playgirl.jpg");
            File destFile = new File("playgirl_copy.jpg");

            // 2. 创建相关的字节流
            /*
             * InputStream(输入=读)读的是源文件，所以放 srcFile；
             * OutputStream(输出=写)写到目标文件，所以放 destFile。
             */
            fis = new FileInputStream(srcFile);
            /*
             * new FileOutputStream(destFile) == new FileOutputStream(destFile, false)：覆盖(清空重写)。
             * new FileOutputStream(destFile, true)：追加(在文件末尾接着写字节)。
             * 图片确实“能追加”，但那样只是把新字节接到旧字节后面，得到的是一个更大的、损坏的文件，无法正常显示。
             */
            fos = new FileOutputStream(destFile);

            // 3. 数据的读入和写出
            /*
             * 实际开发通常不手写：会用 BufferedInputStream 缓冲、或直接
             * Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING)、
             * 或 commons-io 的 IOUtils.copy() 等封装好的方法
             */
            byte[] buffer = new byte[1024]; // 1kb
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

    /**
     * 需求：复制一份hello.txt文件，命名为hello_copy1.txt
     * 可以使用字节流实现文本文件的复制。
     * hello.txt内容如下：
     * helloworld你好123
     */
    /*
     * 这里是“复制文件”，不是“显示到控制台”。
     * 字节流只是把每个字节原样读出、再原样写入目标文件，从不解析这些字节是不是半个汉字，
     * 所以哪怕缓冲区把“好”字的字节切成两半，分两次读、分两次写，拼到目标文件里字节序列依然完整，
     * 用编辑器打开自然不乱码。乱码只会发生在“把不完整字节当字符串解码显示”的时候(见test3)。
     */
    @Test
    public void test2() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 1. 创建相关的File类的对象
            File srcFile = new File("hello.txt");
            File destFile = new File("hello_copy1.txt");

            // 2. 创建相关的字节流
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);

            // 3. 数据的读入和写出
            byte[] buffer = new byte[5];
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

    /**
     * 需求：读取hello.txt文件，将数据显示在控制台上。
     * 可能出现乱码。
     * hello.txt内容如下：
     * helloworld你好123
     */
    @Test
    public void test3() {
        FileInputStream fis = null;
        try {
            // 1. 创建相关的File类的对象
            File srcFile = new File("hello.txt");

            // 2. 创建相关的字节流
            fis = new FileInputStream(srcFile);

            // 3. 数据的读入和写出
            byte[] buffer = new byte[5];
            int len; // 记录每次读入到buffer中字节的个数
            while ((len = fis.read(buffer)) != -1) {
                /*
                 * test2是“字节->字节”原样复制到文件，不解码，所以不乱码；
                 * 这里是把字节 new String(...) 立即“解码成字符”显示到控制台。
                 * 如果缓冲区边界把一个汉字的多个字节切开，这一批里就出现“半个汉字”，
                 * 解码时凑不成完整字符，就显示成乱码。区别就在于“是否在字节不完整时进行解码”。
                 */
                String str = new String(buffer, 0, len);
                System.out.print(str);
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
        }
    }
}

/**
 * 案例
 * 实现图片加密操作。
 */
 /*
 提示：
 int b = 0;
 while((b = fis.read()) != -1){
    fos.write(b ^ 5);
 }

 * “异或加密”：对每个字节和固定密钥(如5)做按位异或(^)，
 * 得到加密后的字节写出；由于异或的可逆性(a^5^5==a)，用同一个程序对加密文件再跑一遍即可解密。
 */
class PicEncryptTest {
    /**
     * 加密：对源图片每个字节 ^ 5 后写出到加密文件
     * 将 playgirl.jpg 加密为 playgirl_secret.jpg
     */
    @Test
    public void encrypt() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 1. 创建源文件与目标(加密后)文件对象
            File srcFile = new File("playgirl.jpg");
            File destFile = new File("playgirl_secret.jpg");

            // 2. 创建字节输入流、输出流
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);

            // 3. 逐字节读入，异或密钥5后写出
            // b 读到的是0~255的字节值，(b ^ 5) 做按位异或加密；到末尾 read 返回 -1 结束
            int b;
            while ((b = fis.read()) != -1) {
                fos.write(b ^ 5);
            }

            System.out.println("加密成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 关闭资源(各自 try-catch、判空，防止空指针与资源泄漏)
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

    /**
     * 解密：因为异或可逆(a^5^5==a)，对加密文件再 ^ 5 即可还原
     * 将 playgirl_secret.jpg 解密为 playgirl_decrypt.jpg
     */
    @Test
    public void decrypt() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File srcFile = new File("playgirl_secret.jpg");
            File destFile = new File("playgirl_decrypt.jpg");

            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);

            int b;
            while ((b = fis.read()) != -1) {
                // 再异或一次相同密钥即可还原原始字节
                fos.write(b ^ 5);
            }

            System.out.println("解密成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
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
