package chapter16;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTest {
    /*
       UDP 网络编程说明
      1. UDP 的核心特点
        - 面向"数据报（Datagram）"：把要发送的数据封装成一个个独立的数据报进行传输，
          数据报是 UDP 传输的基本单位。
        - 无连接：发送前不需要像 TCP 那样先"三次握手"建立连接，直接发。
        - 不可靠：发送端不管接收端是否准备好、也不确认对方是否收到，可能丢包、乱序、重复。
        - 单个数据报大小有限：理论上限约 64K（更精确说，IPv4 下 UDP 有效载荷约 65507 字节）。
        - 开销小、效率高：没有连接的建立与释放过程。
      2. UDP 通信涉及的两个核心类
        - DatagramSocket：收发数据报的"端点/通道"，负责真正把数据报发出去或收进来。
        - DatagramPacket：数据报本身，封装了"要传输的字节数据"，
          发送时还封装"目的地址+目的端口"，接收时用来当"容器"存放收到的数据。
      3. 基本收发流程
        发送端：new DatagramSocket() -> 把数据/目的IP/目的端口封装进 DatagramPacket
               -> ds.send(packet) -> close。
        接收端：new DatagramSocket(固定端口) -> 准备一个字节数组缓冲区封装进 DatagramPacket
               -> ds.receive(packet)（阻塞等待）-> 从 packet 中取出数据 -> close。
      4. 容易踩坑的点
        - 收发两端字符编码必须一致，否则中文乱码。
        - 接收端必须绑定一个"固定端口"，发送端才知道发到哪里。
        - 接收时要用 getLength() 截取实际收到的字节，不能把整个缓冲区都转成字符串。
     */
    // 发送端
    @Test
    public void sender() throws IOException {
        // 1. 创建DatagramSocket的实例
        /*
         * DatagramSocket 是 UDP 通信的"收发端点/通道"，底层封装了一个 UDP socket。
         * 发送端用它把数据报发出去；这里用无参构造，系统会自动分配一个临时端口，
         * 因为发送端一般不需要固定端口（对方不主动来找它）。
         */
        DatagramSocket ds = new DatagramSocket();

        // 2. 将数据、目的地的ip，目的地的端口号都封装在DatagramPacket数据报中
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        int port = 12345;
        byte[] bytes = "我是发送端".getBytes("UTF-8");

        /*
         * DatagramPacket 就是 UDP 传输的基本单位——数据报，它封装了要传输的字节数据。
         * 发送端用它装"数据 + 目的地址 + 目的端口"；接收端用它当"容器"来接收数据。
         * 因为发送要有东西可发、接收要有东西可存，所以两端都用到它。
         *
         *   bytes        -> buf：要发送的字节数据（数据来源数组）
         *   0            -> offset：从数组的第几个下标开始取，这里从头开始
         *   bytes.length -> length：要发送的字节长度，这里发送整个数组
         *   inetAddress  -> address：目的地 IP 地址
         *   port         -> port：目的地端口号
         * 发哪些数据(buf,offset,length) + 发给谁(address,port)
         */
        DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length, inetAddress, port);

        // 发送数据
        /*
         * UDP 是"面向数据报"的：一次发送就是发出一个完整的数据报，
         * 而"数据 + 目的地址 + 目的端口"这些信息都被打包在 DatagramPacket 里。
         * send(packet) 需要知道发什么、发给谁，这些恰好都在 packet 中，所以必须传 packet。
         */
        ds.send(packet);

        ds.close();
    }

    // 接收端
    @Test
    public void receiver() throws IOException {
        // 1. 创建DatagramSocket的实例
        int port = 12345;
        /*
         * DatagramSocket 是收发数据报的端点，收和发都要有这个端点才能工作。
         * 接收端必须绑定一个"固定端口"(new DatagramSocket(port))，
         * 这样发送端才知道数据往哪个端口发；而发送端不需要固定端口，系统自动分配即可。
         */
        DatagramSocket ds = new DatagramSocket(port);

        // 2. 创建数据包的对象，用于接收发送端发送过来的数据
        /*
         * 1024 * 64 = 65536 字节 = 64KB，接近 UDP 单个数据报的理论上限
         * （IPv4 下 UDP 有效载荷实际约 65507 字节）。
         * 缓冲区设这么大是为了保证能装下最大的数据报，避免数据被截断。
         */
        byte[] buffer = new byte[1024 * 64];
        // 这里构造器里面需要填写的和上面一样吗？必须要求一样还是规范一样还是建议一样啊？为什么？比上面少了一点东西，少的都是为什么啊？为什么可以少呢？
        /*
         * 接收端用的是 (buf, offset, length) 这个重载，
         * 比发送端少了 address（目的地址）和 port（目的端口）。
         * 接收方不需要指定"来源是谁"，数据来自哪个 IP、哪个端口，
         * 会在 receive() 收到数据后由实际收到的包自动填充进 packet 里。
         * 发送端必须写地址端口（要知道发给谁），接收端可以省略（谁发来都能收）。
         * 用哪块内存(buf,offset)来接、最多接多少(length)
         */
        DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);

        // 3. 接收数据
        /*
         * receive() 需要一个"容器"来存放收到的字节数据，这个容器就是 DatagramPacket。
         * 调用 receive(packet) 会阻塞（一直等），直到收到一个数据报，
         * 然后把数据写进 packet 的缓冲区，并回填来源地址、端口、实际长度等信息。
         */
        ds.receive(packet);

        // 4.获取数据，并打印到控制台上
        /*
         * new String(packet.getData(), 0, packet.getLength()) 把收到的字节转成字符串：
         *   packet.getData()   -> 整个缓冲区数组（长度是 64K，多数是没用到的空字节）
         *   0                  -> 从下标 0 开始转换
         *   packet.getLength() -> 实际收到的字节数
         * 用 getLength() 而不是 buffer.length，是为了避免把未使用的空字节也转进字符串。
         */
        String str = new String(packet.getData(), 0, packet.getLength());
        System.out.println(str);

        ds.close();
    }
}
