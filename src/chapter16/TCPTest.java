package chapter16;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TCPTest {
    /*
    1. TCP与UDP网络编程对比（熟悉）
    TCP协议：
    TCP协议进行通信的两个应用进程：客户端、服务端。
    使用TCP协议前，须先建立TCP连接，形成基于字节流的传输数据通道
    传输前，采用“三次握手”方式，点对点通信，是可靠的
    TCP协议使用重发机制，当一个通信实体发送一个消息给另一个通信实体后，需要收到另一个通信实体确认信息，如果没有收到另一个通信实体确认信息，则会再次重复刚才发送的消息。
    在连接中可进行大数据量的传输
    传输完毕，需释放已建立的连接，效率低

    UDP协议：
    UDP协议进行通信的两个应用进程：发送端、接收端。
    将数据、源、目的封装成数据包（传输的基本单位），不需要建立连接
    发送不管对方是否准备好，接收方收到也不确认，不能保证数据的完整性，故是不可靠的
    每个数据报的大小限制在64K内
    发送数据结束时无需释放资源，开销小，通信效率高
    适用场景：音频、视频和普通数据的传输。例如视频会议
    2. TCP三次握手 与 TCP四次挥手（熟悉）
    握手用于建立连接，挥手通常指正常关闭连接。
    三次握手
    握手建立初始连接状态；后续传输的可靠性不能只归因于这三次交互。
    TCP协议中，在发送数据的准备阶段，客户端与服务器之间的三次交互，以保证连接的可靠。
    第一次握手，客户端向服务器端发起TCP连接的请求
    第二次通常是 SYN+ACK：既确认客户端的 SYN，也发送服务端自己的 SYN。
    第二次握手，服务器端发送针对客户端TCP连接请求的确认
    第三次 ACK 确认服务端的 SYN，使双方完成初始序号的确认。
    第三次握手，客户端发送确认的确认
     */
}

/**
 * 例题1：客户端发送内容给服务端，服务端将内容打印到控制台上。
 */
class TCPTest1 {
    // 客户端
    @Test
    public void client() {
        /*
         * 这里的 java.net.Socket 表示 TCP 通信的一端，封装连接与输入/输出流。
         * 客户端主动创建并连接 Socket；服务端由 ServerSocket.accept() 得到与某个客户端通信的 Socket。
         * ServerSocket 负责监听和接收新连接，Socket 负责已建立连接上的双向通信。
         * Socket 是通信端点对象；下面带地址和端口的构造方法会尝试建立 TCP 连接。
         * 因此 new Socket(...) 不只是创建普通对象，还可能等待连接或因连接失败抛出 IOException。
         */
        Socket socket = null;
        OutputStream os = null;
        try {
            // 1. 创建一个Socket
            // 声明对方的ip地址
            // getByName() 是 InetAddress 的静态工厂方法，可把 IP 文本或主机名解析为地址对象。
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            // 声明对方的端口号
            /*
             * port 是端口号，用于在目标主机上定位 TCP 服务的监听端点，本例目标端口是 12345。
             * 它不是客户端自己的端口；客户端的本地端口通常由操作系统自动分配。
             */
            int port = 12345;
            socket = new Socket(inetAddress, port);

            // 2. 发送数据
            // getOutputStream() 返回这条连接的字节输出流，write() 向它写入的字节会发送给对端。
            os = socket.getOutputStream();
            /*
             * OutputStream.write() 写的是字节；getBytes() 把字符串按字符集编码为 byte[]。
             * 不带参数会使用默认字符集，两端设置不同时可能乱码；建议双方明确采用 UTF-8，例如：
             * os.write("你好，我是客户端".getBytes(java.nio.charset.StandardCharsets.UTF_8));
             * 接收端也须按 UTF-8 解码；仅修改其中一端不够。
             */
            os.write("你好，我是客户端".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            /*
             * Socket.close() 会同时关闭其输入/输出流，所以成功关闭 socket 后再关闭 os 通常是重复操作。
             * 本例用关闭连接表示消息结束；仅 write() 完成或调用 flush()，不会让对端 read() 自动返回 -1。
             */
            // 3. 关闭socket、关闭流
            try {
                /*
                 * 创建 Socket 或获取流可能提前抛异常，使变量仍为 null；直接调用 null.close() 会发生空指针异常。
                 * finally 会在正常结束和异常退出时执行，因此需要兼顾资源尚未创建成功的情况。
                 */
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    // 服务端
    @Test
    public void server() {
        ServerSocket serverSocket = null;
        Socket socket = null; // 阻塞式的方法
        InputStream is = null;
        try {
            // 1. 创建一个ServerSocket
            int port = 12345;
            serverSocket = new ServerSocket(port);

            // 2. 调用accept()，接收客户端的Socket
            // accept() 返回服务端新建的“已连接 Socket”，不是把客户端 Java 进程中的对象搬到服务端。
            /*
             * accept() 从监听端点接受一个连接，返回用于和该客户端读写数据的 Socket。
             * 没有待接受连接时默认等待；它每次只返回一个连接，持续服务需要在循环中反复调用。
             */
            socket = serverSocket.accept();
            System.out.println("服务器端已开启");
            /*
             * socket.getInetAddress() 返回对端的 InetAddress；对端就是客户端。
             * 随后 getHostAddress() 返回数字 IP 字符串，例如 127.0.0.1；它不包含端口，也不是查询主机名。
             * 查看对端端口可用 socket.getPort()；查看服务端这侧地址则是 socket.getLocalAddress()。
             */
            System.out.println("收到了来自于" + socket.getInetAddress().getHostAddress() + "的连接");

            // 3. 接收数据
            /*
             * socket.getInputStream() 获取这条连接的字节输入流，再把引用赋给 is。
             * 它本身不代表“已经读到消息”；真正接收数据的是后面的 is.read(buffer)。
             */
            is = socket.getInputStream();
            /*
             * 按每次 read() 的结果独立解码，可能把一个多字节字符拆开；例如 UTF-8 中常见汉字占 3 字节。
             * TCP 只提供字节流，不保留 write() 边界，也不保证每次 read() 填满数组；改成 1024 仍不能保证字符完整。
             * 小消息可先收集全部字节，再用约定字符集统一解码；持续或较大文本应使用 InputStreamReader 连续解码。
             * 例如用 new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8) 持续读取字符。
             * 下面的历史乱码来自逐块 new String(...) 的写法；当前代码已改为先汇总字节，buffer 为 5 也可以正常拼接。
             */
            /*
            服务器端已开启
            你��，���是客��端
            数据接收关闭
             */
            byte[] buffer = new byte[5];
            int len;
            /*
             * ByteArrayOutputStream 会按顺序积累每次收到的原始字节，必要时扩容，不会在分块位置提前解码。
             * 等完整接收后再解码，跨两次 read() 的字符字节就能重新连在一起；字符集仍必须与发送端一致。
             */
            // 内部维护了一个byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                // 错误的，可能会出现乱码
                //            System.out.print(new String(buffer,0,len));
//                String str = new String(buffer, 0, len);
//                System.out.print(str);

                // 正确的
                // 先保留原始字节、最后统一解码；仍需保证消息完整、编码一致以及数据量受控。
                /*
                 * write(buffer, 0, len) 仅把本次有效的 len 个字节复制到累积缓冲区；不会把旧数据或未使用区域追加进去。
                 * 它没有把每块字节转为字符，所以即使一个汉字分成几次到达，也会在最终解码前重新拼接完整。
                 */
                baos.write(buffer, 0, len);
            }

            // 把累积的所有字节按默认字符集解码，然后 println 打印整段文字并换行。
            System.out.println(baos.toString());

            System.out.println("\n数据接收关闭");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 4. 关闭Socket、ServerSocket、流
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/**
 * 例题2：客户端发送文件给服务端，服务端将文件保存在本地。
 */
// 注意：因为涉及到相关资源的关闭，需要使用try-catch-finally处理异常
class TCPTest2 {
    // 客户端
    @Test
    public void client() throws IOException {
        // 1. 创建Socket
        // 指明对方（即为服务器端）的ip地址和端口号
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        int port = 12345;
        Socket socket = new Socket(inetAddress, port);

        // 2. 创建File的实例、FileInputStream的实例
        File file = new File("/Users/Shared/java_learning/io/pic.jpg");
        FileInputStream fis = new FileInputStream(file);

        // 3. 通过Socket，获取输出流
        /*
         * 取得连接的字节输出流，用于把从图片文件读到的字节发给服务器。
         * 图片属于二进制数据，不要先转成 String；按原始 byte[] 传输才能保持内容不变。
         */
        OutputStream os = socket.getOutputStream();

        // 4. 读写数据
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        System.out.println("数据发送完毕");

        // 5. 关闭Socket和相关的流
        os.close();
        fis.close();
        socket.close();
    }

    // 服务端
    @Test
    public void server() throws IOException {
        // 1. 创建ServerSocket
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);

        // 2. 接收来自于客户端的socket:accept()
        Socket socket = serverSocket.accept();

        // 3. 通过Socket获取一个输入流
        /*
         * 取得当前客户端连接的字节输入流；后面的 read() 才把网络字节读入 buffer。
         * 数据路径是客户端文件 → 客户端输出流 → TCP → 服务端输入流 → 服务端文件输出流。
         */
        InputStream is = socket.getInputStream();

        // 4. 创建File类的实例、FileOutputStream的实例
        File file = new File("/Users/Shared/java_learning/io/pic_copy.jpg");
        FileOutputStream fos = new FileOutputStream(file);

        // 5. 读写过程
        byte[] buffer = new byte[1024];
        int len;
        /*
         * 这个协议把客户端关闭发送方向当作文件结束标志，因此一次连接在该方向上只传一个文件。
         * 读取到 -1 仅说明对端正常结束发送，不验证文件是否符合预期；正式传输可增加长度和校验值。
         */
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        System.out.println("数据接收完毕");

        // 6. 关闭相关的Socket和流
        fos.close();
        is.close();
        socket.close();
        serverSocket.close();
    }
}

/**
 * 例题3：从客户端发送文件给服务端，服务端保存到本地。并返回“发送成功”给客户端。并关闭相应的连接。
 */
class TCPTest3 {
    // 客户端
    @Test
    public void client() throws IOException {
        // 1. 创建Socket
        // 指明对方（即为服务器端）的ip地址和端口号
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        int port = 12345;
        Socket socket = new Socket(inetAddress, port);

        // 2. 创建File的实例、FileInputStream的实例
        File file = new File("/Users/Shared/java_learning/io/pic.jpg");
        FileInputStream fis = new FileInputStream(file);

        // 3. 通过Socket，获取输出流
        OutputStream os = socket.getOutputStream();

        // 4. 读写数据
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        System.out.println("数据发送完毕");
        // 客户端表明不再继续发送数据
        /*
         * shutdownOutput() 关闭本端的发送方向，让此前写入的数据发送完毕后按 TCP 正常流程结束这个方向。
         * 服务端读完这些数据后得到 -1，退出接收循环并发送回复；客户端输入方向仍保持可用，因此能读回复。
         * 它不是关闭整个 socket，也不是发送一个值为 -1 的字节；调用后本端不能再写此输出流。
         * 若将来加了 BufferedOutputStream 等包装层，应先 flush() 包装层再半关闭；最终仍需 close() 释放连接。
         */
        socket.shutdownOutput();

        // 5. 接收来着于服务器端的数据
        InputStream is = socket.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer1 = new byte[5];
        int len1;
        while ((len1 = is.read(buffer1)) != -1) {
            baos.write(buffer1, 0, len1);
        }
        System.out.println(baos.toString());

        // 6. 关闭Socket和相关的流
        baos.close();
        is.close();
        os.close();
        fis.close();
        socket.close();
    }

    // 服务端
    @Test
    public void server() throws IOException {
        // 1. 创建ServerSocket
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);

        // 2. 接收来自于客户端的socket:accept()
        Socket socket = serverSocket.accept();

        // 3. 通过Socket获取一个输入流
        InputStream is = socket.getInputStream();

        // 4. 创建File类的实例、FileOutputStream的实例
        File file = new File("/Users/Shared/java_learning/io/pic_copy1.jpg");
        FileOutputStream fos = new FileOutputStream(file);

        // 5. 读写过程
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        System.out.println("数据接收完毕");

        // 6. 服务端发送数据给客户端
        OutputStream os = socket.getOutputStream();
        os.write("图片接收到了".getBytes());

        // 7. 关闭相关的Socket和流
        os.close();
        fos.close();
        is.close();
        socket.close();
        serverSocket.close();
    }
}

/*
 * 整体链路：键盘输入 → Send 线程按行发送 → 服务端对应 MessageHandler 读一行 → 广播 → 各客户端 Receive 打印。
 * 服务端主线程只负责接入新连接，每个已连接客户端由一个 MessageHandler 线程持续处理。
 */
/**
 * 聊天室的实现 （服务器端）
 */
class ChatServerTest {
    // 这个集合用来存储所有在线的客户端
    /*
     * online 保存服务端这侧的客户端连接 Socket，既不是客户端对象本身，也不是全部历史用户。
     * static 表示同一服务端 JVM 中各个 MessageHandler 共享这个集合，不会跨进程共享。
     */
    static ArrayList<Socket> online = new ArrayList<Socket>();

    public static void main(String[] args) throws Exception {
        // 1、启动服务器，绑定端口号
        ServerSocket server = new ServerSocket(8989);

        // 2、接收n多的客户端同时连接
        // 主线程一次 accept() 处理一个新连接；循环持续接入，多客户端的消息处理靠下面启动的各个线程并发完成。
        while (true) {
            Socket socket = server.accept(); // 阻塞式的方法

            online.add(socket); // 把新连接的客户端添加到online列表中

            // 一个 MessageHandler 对应一个客户端；socket 引用把该连接交给处理线程。
            // 主要负责获取当前socket中的数据，并分发给当前聊天室的所有的客户端。
            MessageHandler mh = new MessageHandler(socket);
            mh.start();
        }
    }

    static class MessageHandler extends Thread {
        private Socket socket;
        private String ip;

        public MessageHandler(Socket socket) {
            super();
            this.socket = socket;
        }

        public void run() {
            try {
                ip = socket.getInetAddress().getHostAddress();

                // 插入：给其他客户端转发“我上线了”
                sendToOther(ip + "上线了");

                // (1)接收该客户端的发送的消息
                InputStream input = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(reader);

                String str;
                while ((str = br.readLine()) != null) {
                    // (2)给其他在线客户端转发
                    sendToOther(ip + ":" + str);
                }

                sendToOther(ip + "下线了");
            } catch (IOException e) {
                try {
                    sendToOther(ip + "掉线了");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                // 从在线人员中移除我
                online.remove(socket);
            }
        }

        // 封装一个方法：给其他客户端转发xxx消息
        public void sendToOther(String message) throws IOException {
            // 遍历所有的在线客户端，一一转发
            for (Socket on : online) {
                // on 是本轮要接收消息的目标连接，every 是服务端向该目标发送字节的输出流。
                OutputStream every = on.getOutputStream();
                PrintStream ps = new PrintStream(every);

                ps.println(message);
            }
        }
    }
}

/**
 * 案例：聊天室的实现 （客户端）
 */
class ChatClientTest {
    public static void main(String[] args) throws Exception {
        // 1、连接服务器
        Socket socket = new Socket("127.0.0.1", 8989);

        // 2、开启两个线程
        // (1)一个线程负责看别人聊，即接收服务器转发的消息
        Receive receive = new Receive(socket);
        receive.start();

        // (2)一个线程负责发送自己的话
        Send send = new Send(socket);
        send.start();

        send.join(); // 等我发送线程结束了，才结束整个程序

        socket.close();
    }
}

class Send extends Thread {
    private Socket socket;

    public Send(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner input = new Scanner(System.in);

            OutputStream outputStream = socket.getOutputStream();
            // 按行打印
            PrintStream ps = new PrintStream(outputStream);

            // 从键盘不断的输入自己的话，给服务器发送，由服务器给其他人转发
            while (true) {
                System.out.print("自己的话：");
                String str = input.nextLine(); // 阻塞式的方法
                if ("bye".equals(str)) {
                    break;
                }
                ps.println(str);
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Receive extends Thread {
    private Socket socket;

    public Receive(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner input = new Scanner(inputStream);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
