package chapter16;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {
    /*
    1. 要想实现网络通信，需要解决的三个问题：
    - 问题1：如何准确地定位网络上一台或多台主机
    - 问题2：如何定位主机上的特定的应用
    - 问题3：找到主机后，如何可靠、高效地进行数据传输
    2. 实现网络传输的三个要素：（对应解决三个问题）
    > 使用IP地址（准确地定位网络上一台或多台主机）
    > 使用端口号（定位主机上的特定的应用）
    > 规范网络通信协议（可靠、高效地进行数据传输）
    3. 通信要素1：IP地址
    3.1 作用
    IP地址用来给网络中的一台计算机设备做唯一的编号
    3.2 IP地址分类
    > IP地址分类方式1
    IPv4 = 32位 = 4字节，理论约42.9亿个地址(2^32)，全球早已基本分配完毕，
    现在主要靠 NAT（多台内网设备共用一个公网IP）来续命。
    IPv6 = 128位 = 16字节，地址量近乎无限(2^128)，就是为了解决IPv4枯竭。
    “占几个字节”决定了地址总数：字节越多，可编号的设备越多。
    > IP地址分类方式2
    公网地址( 万维网使用）和 私有地址( 局域网使用。以192.168开头）
    3.3 本地回路地址：127.0.0.1
    3.4 域名:便捷的记录ip地址
    www.baidu.com  www.atguigu.com  www.bilibili.com
    4. 通信要素2：端口号
    > 可以唯一标识主机中的进程（应用程序）
    > 不同的进程分配不同的端口号
    > 范围：0~65535
    5. InetAddress的使用
    5.1 作用
    InetAddress类的一个实例就代表一个具体的ip地址。
    5.2 实例化方式
    InetAddress getByName(String host):获取指定ip对应的InetAddress的实例
    InetAddress getLocalHost():获取本地ip对应的InetAddress的实例
    5.3 常用方法
    getHostName()
    getHostAddress()
    6. 通信要素3：通信协议
    6.1 网络通信协议的目的
    为了实现可靠而高效的数据传输。
    6.2 网络参考模型
    OSI参考模型：将网络分为7层，过于理想化，没有实施起来。
    TCP/IP参考模型：将网络分为4层：应用层、传输层、网络层、物理+数据链路层。事实上使用的标准。
     */
    public static void main(String[] args) {
        // 1. 实例化
        /**
         * getByName(String host)：获取指定ip对应的InetAddress的实例
         * 把传入的字符串解析成InetAddress对象；host既能是IP，也能是域名。
         */
        try {
            InetAddress inet1 = InetAddress.getByName("192.168.23.31");
            /*
             * InetAddress.toString() 的格式固定是 "主机名/IP地址"。
             * 这里没有解析到主机名，所以斜杠前面为空、只剩 /IP。
             */
            System.out.println(inet1); // /192.168.23.31

            // 通过 DNS 把域名 www.baidu.com 解析成对应的 IP 地址。
            InetAddress inet2 = InetAddress.getByName("www.baidu.com");
            System.out.println(inet2); // www.baidu.com/198.18.0.10

            /**
             * getLocalHost():获取本地ip对应的InetAddress的实例
             */
            InetAddress inet3 = InetAddress.getLocalHost();
            System.out.println(inet3); // 主机名/127.0.0.1

            InetAddress inet4 = InetAddress.getByName("127.0.0.1");
            System.out.println(inet4); // /127.0.0.1

            // 2.两个常用的方法
            /**
             * getHostName()：返回主机名(域名/机器名)。若对象是用IP创建的，
             * 可能直接返回该IP字符串，也可能触发反向DNS去查主机名。
             */
            System.out.println(inet1.getHostName()); // 192.168.23.31
            System.out.println(inet2.getHostName()); // www.baidu.com

            /**
             * getHostAddress()：返回IP地址的字符串形式(如 "198.18.0.10")。
             */
            System.out.println(inet1.getHostAddress()); // 192.168.23.31
            System.out.println(inet2.getHostAddress()); // 198.18.0.10
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
