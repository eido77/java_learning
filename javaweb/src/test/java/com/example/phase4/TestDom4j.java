package com.example.phase4;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

public class TestDom4j {
    @Test
    public void testRead() throws DocumentException {
        // 读取jdbc.xml配置文件，获得document对象
        // SAXReader 是 dom4j 提供的“读取器”，底层用 SAX 解析器读 XML，再把结果组装成一个 Document（内存中的节点树）。
        SAXReader saxReader = new SAXReader();

        // 通过类加载器获得指向字节码根路径下的指定文件的输入流
        /*
         * jdbc.xml 通常放在 resources（或 src）目录，项目构建后会被复制到 classpath 根目录（如 target/classes）。
         * 类加载器负责在 classpath 上查找类和资源，所以可以用“相对 classpath 根的名字”找到文件，不依赖电脑上的绝对路径，换电脑或打成 jar 包后也能找到。
         * getResourceAsStream("jdbc.xml") 在 classpath 上找到该文件，返回一个 InputStream（字节输入流）；找不到时返回 null。
         * 需要它，因为下面的 saxReader.read(...) 要的是“数据来源”，而不是文件名字符串。
         */
        InputStream resourceAsStream = TestDom4j.class.getClassLoader().getResourceAsStream("jdbc.xml");

        // 通过输入流获得配置文件，解析成一个dom对象
        /*
         * 签名：public Document read(InputStream in) throws DocumentException。SAXReader 里还有 read(File)、read(URL)、read(Reader) 等重载。
         * 参数：in 是 XML 内容的数据来源，read 从中读取字节并解析。
         * 解析整个 XML，返回代表整个文档的 Document 对象。
         */
        Document document = saxReader.read(resourceAsStream);

        // 从document对象上获取配置文件中的信息
        /*
         * Node         节点
         *   Element    元素节点
         *   attribute  属性节点
         *   Text       文本节点
         */
        /*
         * 签名：Element getRootElement()，属于 Document 接口。
         * 返回整个 XML 的根元素（最外层、唯一的那个标签）。
         * XML 文档只能有一个根元素，它是后面逐层向下读取的起点。
         */
        Element rootElement = document.getRootElement();
        /*
         * 签名：String getName()，是 Element（继承自 Node）上的方法。
         * 作用：返回这个节点的名字；对元素来说就是标签名（不含尖括号）。
         */
        System.out.println(rootElement.getName()); // jdbc
        // 获取元素下的子元素
        /*
         * elements()
         *   签名：List<Element> elements()
         *   返回：当前元素的全部直接子元素；没有子元素时返回空列表。
         * elements("名字")
         *   签名：List<Element> elements(String name)
         *   name 是子标签名。
         *   返回：所有同名的直接子元素；没有同名的返回空列表。
         * element("名字")
         *   签名：Element element(String name)
         *   name 是子标签名。
         *   返回：第一个同名的直接子元素；没有同名的返回 null。
         *
         * elements() 只返回一层（直接子元素），不会递归取孙子元素，也不包含属性和文字。
         * 要取孙子元素，需要对子元素再调用一次 elements()，属性用 attribute()/attributes() 取，文字用 getText() 取。
         */
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            // element 是根元素的某一个子标签，同样是 Element 类型，getName() 同样返回它的标签名。
            System.out.println("\t" + element.getName());
            // 从元素上获取属性
            /*
             * attributes()
             *   签名：List<Attribute> attributes()
             *   返回：当前元素的全部属性；没有属性时返回空列表。
             * attribute("名字")
             *   签名：Attribute attribute(String name)
             *   name 是属性名，区分大小写。
             *   返回：该名字的属性；没有这个属性时返回 null。
             * attribute(下标)
             *   签名：Attribute attribute(int index)
             *   index 是属性的位置，从 0 开始数。
             *   返回：该位置的属性，一般按属性在标签里的书写顺序排列。
             */
            Attribute idAttribute = element.attribute("id");
            // getName() 返回属性名，getValue() 返回属性值（字符串）。拼起来才是 id=值 的格式。
            System.out.println("\t\t" + idAttribute.getName() + "=" + idAttribute.getValue());

            // 继续读取子元素
            List<Element> eles = element.elements();
            for (Element ele : eles) {
                // getName() 返回子标签名；getText() 返回这个标签里直接写的文字，不包含子标签里的文字；没有文字时返回空字符串，不是 null。
                // getText() 会保留文字首尾的空白和换行。可以改用 getTextTrim() 去掉首尾空白。
                System.out.println("\t\t" + ele.getName() + ":" + ele.getText());
            }
        }
    }
}
