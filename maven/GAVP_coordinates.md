## 基于IDEA创建Maven工程

### 概念梳理Maven工程的GAVP

Maven工程相对之前的项目，多出一组gavp属性，gav需要我们在创建项目的时候指定，p有默认值，我们先行了解下这组属性的含义：

Maven 中的 GAVP 是指 GroupId、ArtifactId、Version、Packaging 等四个属性的缩写，其中前三个是必要的，而 Packaging
属性为可选项。这四个属性主要为每个项目在maven仓库中做一个标识，类似人的姓-名！有了具体标识，方便后期项目之间相互引用依赖等！

GAV遵循以下规则：

1） **GroupID 格式**：com.{公司/BU }.业务线.\[子业务线]，最多 4 级。

		说明：{公司/BU} 例如：alibaba/taobao/tmall/aliexpress 等 BU 一级；子业务线可选。

		正例：com.taobao.tddl 或 com.alibaba.sourcing.multilang

2） **ArtifactID 格式**：产品线名-模块名。语义不重复不遗漏，先到仓库中心去查证一下。

		正例：tc-client / uic-api / tair-tool / bookstore

3） **Version版本号格式推荐**：主版本号.次版本号.修订号

		1） 主版本号：当做了不兼容的 API 修改，或者增加了能改变产品方向的新功能。

		2） 次版本号：当做了向下兼容的功能性新增（新增类、接口等）。

		3） 修订号：修复 bug，没有修改方法签名的功能加强，保持 API 兼容性。

		例如： 初始 → 1.0.0  修改bug → 1.0.1  功能调整 → 1.1.1等

> 当次版本号增加时，修订号必须归零；当主版本号增加时，次版本号和修订号都必须归零。

**Packaging定义规则：**

	指示将项目打包为什么类型的文件，idea根据packaging值，识别maven项目类型！

	packaging 属性为 jar（默认值），代表普通的Java工程，打包以后是.jar结尾的文件。

	packaging 属性为 war，代表Java的web工程，打包以后.war结尾的文件。

	packaging 属性为 pom，代表不会打包，用来做继承的父工程。
