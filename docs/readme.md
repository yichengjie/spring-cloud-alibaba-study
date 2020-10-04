1. MyBatis通用Mapper:https://github.com/abel533/Mapper
2. idea中Mapper注入报错提示：http://www.imooc.com/article/287865
3. 研究@Repository @Component @Service @Controller的区别

###方式一(Schema First)
1. 分析业务(流程图、用例图....架构图) ---> 建模业务，并且确定架构
2. 确定业务流程（评审）
3. 设计API（需要哪些API）/数据模型（表结构设计|类图/ER图）
4. 编写API文档
5. 编写代码
### 方式二（API First）
1. 分析业务(流程图、用例图....架构图) ---> 建模业务，并且确定架构
2. 确定业务流程（评审）
3. 设计API（需要哪些API）/数据模型（表结构设计|类图/ER图）
4. 编写代码
5. 编写API文档
```
@SpringBootApplication
// 扫描mybatis哪些包里面的接口
@MapperScan(basePackages = "com.yicj.contentcenter.dao")
public class ContentCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args) ;
    }
}
```
