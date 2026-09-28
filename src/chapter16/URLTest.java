package chapter16;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLTest {
    /*
    URL(Uniform Resource Locator):统一资源定位符，一段用来"定位并访问某个资源"的地址字符串
    1. 作用：
    一个具体的url就对应着互联网上某一资源的地址。
    2. URL的格式：
    http://192.168.21.107:8080/examples/abcd.jpg?name=Tom   ---> "万事万物皆对象"
     * ? 后面是"查询参数(query string)"，形如 key=value，多个用 & 连接，用来给服务器传参数。
     * http = 应用层协议；192.168.21.107 = ip地址(主机)；8080 = 端口号；
     * /examples/abcd.jpg = 资源地址(路径)；name=Tom = 参数列表(查询参数)。
    3. URL类的实例化及常用方法
    见代码
    4. 下载指定的URL的资源到本地（了解）
     */
    public static void main(String[] args) {
        String str = "http://192.168.21.107:8080/examples/abcd.jpg?name=Tom";

        try {
            // URL 提供了构造器 URL(String spec)：接收一个字符串，内部解析成 URL 对象。
            URL url = new URL(str);

            /**
             * public String getProtocol( )：获取该URL的协议名
             */
            // 协议名就是 URL 最前面那段，如 http、https、ftp、file。它是"URL 的一部分"
            System.out.println(url.getProtocol()); // http

            /**
             * public String getHost( )：获取该URL的主机名
             */
            // 返回"主机部分"，它既可能是域名(如 www.xxx.com)，也可能是 IP。
            System.out.println(url.getHost()); // 192.168.21.107

            /**
             * public int getPort( )：获取该URL的端口号（未写端口时返回 -1）
             */
            System.out.println(url.getPort()); // 8080

            /**
             * public String getPath( )：获取该URL的文件路径
             */
            // "路径"是 URL 里相对服务器根的资源路径
            System.out.println(url.getPath()); // /examples/abcd.jpg

            /**
             * public String getQuery(  )：获取该URL的查询部分（query）
             */
            // query 是 ? 后面的查询字符串
            System.out.println(url.getQuery()); // name=Tom

            /**
             * public String getFile( )：获取该URL的文件名
             */
            // getFile = getPath + "?" + getQuery
            System.out.println(url.getFile()); // /examples/abcd.jpg?name=Tom
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 需求：将URL代表的资源下载到本地
     */
    @Test
    public void test1() {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            // 1. 获取URL实例
            // new URL(...) 里是一个 http 网络地址，指向的是"服务器上的资源"
            URL url = new URL("http://127.0.0.1:8080/examples/abcd.jpg");

            // 2. 建立与服务器端的连接
            /**
             * public URLConnection openConnection()：
             * 打开一个指向该 URL 所代表资源的连接，返回代表这次通信的 URLConnection 对象。
             */
            /*
             * URLConnection 是抽象父类，表示"通用的资源连接"；
             * HttpURLConnection 是它针对 HTTP 的子类，额外提供 disconnect()、
             * getResponseCode()、setRequestMethod() 等 HTTP 专用方法。
             */
            urlConnection = (HttpURLConnection) url.openConnection();

            // 3. 获取输入流、创建输出流
            is = urlConnection.getInputStream();
            File file = new File("dest.jpg");
            fos = new FileOutputStream(file);

            // 4. 读写数据
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            System.out.println("文件下载完成");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 5. 关闭资源
            try {
                if (fos != null) {
                    fos.close();
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

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
