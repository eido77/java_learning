package com.example.phase3;

import com.example.phase3.pojo.Dog;
import com.example.phase3.pojo.Person;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTest {
    /**
     * 对象 -> JSON（序列化）
     */
    @Test
    public void testWriteJson() {
        // 实例化Person对象，将Person对象转换为JSON串
        Dog dog = new Dog("小黄");
        Person person = new Person("张三", 10, dog);

        // 将Person对象转换成一个字符串
        /*
         * ObjectMapper 是 Jackson 里负责“对象 <-> JSON”转换的核心类：
         * writeValueAsString 把对象转成 JSON，readValue 把 JSON 转成对象，它们都是实例方法，
         * 所以必须先 new 出一个 ObjectMapper 才能调用。
         */
        ObjectMapper objectMapper = new ObjectMapper();
        /*
         * 签名：public String writeValueAsString(Object value)
         * 作用：序列化，把 Java 对象转成 JSON 字符串，返回值就是这个字符串。
         * value：要转换的那个对象。
         * 转换失败会抛异常：Jackson 3 的异常是运行时异常，不写 throws 也能编译；
         * 如果用 Jackson 2，则是必须处理的受检异常，要 try/catch 或在方法上写 throws。
         */
        String personStr = objectMapper.writeValueAsString(person);
        System.out.println(personStr); // {"age":10,"dog":{"name":"小黄"},"name":"张三"}
    }

    /**
     * JSON -> 对象（反序列化）
     */
    @Test
    public void testReadJson() {
        String str = "{\"age\":10,\"dog\":{\"name\":\"小黄\"},\"name\":\"张三\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        /*
         * 签名：public <T> T readValue(String content, Class<T> valueType)
         * 作用：反序列化，把 JSON 字符串解析成指定类型的对象。
         * content：要解析的 JSON 字符串；
         * valueType：目标类型的 Class 对象，告诉 Jackson 要创建什么类型的对象。
         */
        Person person = objectMapper.readValue(str, Person.class);
        System.out.println(person); // Person{name='张三', age=10, dog=Dog{name='小黄'}}
    }

    /**
     * Map -> JSON（序列化）
     */
    @Test
    public void testMapToJson() {
        Map data = new HashMap();
        data.put("A", "valueA");
        data.put("B", "valueB");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);
        System.out.println(json); // {"A":"valueA","B":"valueB"}
    }

    /**
     * List / 数组 -> JSON（序列化）
     */
    @Test
    public void testListToJson() {
        List data = new ArrayList();
        data.add("A");
        data.add("B");
        data.add("C");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);
        System.out.println(json); // ["A","B","C"]

        String[] data2 = {"A", "B", "C"};
        ObjectMapper objectMapper2 = new ObjectMapper();
        String json2 = objectMapper2.writeValueAsString(data2);
        System.out.println(json2); // ["A","B","C"]

        Dog dog = new Dog("小黄");
        Person person = new Person("张三", 10, dog);
        List data3 = new ArrayList();
        data3.add(person);
        ObjectMapper objectMapper3 = new ObjectMapper();
        String json3 = objectMapper3.writeValueAsString(data3);
        System.out.println(json3); // [{"age":10,"dog":{"name":"小黄"},"name":"张三"}]
    }
}
