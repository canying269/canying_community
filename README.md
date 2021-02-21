## canying_community

### 测试地址：<http://106.13.80.98>

### bug修改

#### 1. 引入的bootstrap和jQuery失效 -> (1)更换cdn 或者(2)下载到本地 引入本地文件
(1). 更换cdn ！！！！！！！jQuery.js放在bootstrap.js上面
* bootstrap.min.css
~~~html
<link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
~~~
* jquery.js
~~~html
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js" integrity="sha256-2Kok7MbOyxpgUVvAk/HJ2jigOSYS2auK4Pfzbm7uH60=" crossorigin="anonymous"></script>
~~~
* bootstrap.js
~~~html
<script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.js"></script>
~~~


#### 2. 建议所有dao包下所有Mapper加上 @Mapper @Repository 这两个注解

#### 3. 分页点击页码无法跳转 -> 修改index.html即可
```java 
<nav class="mt-5" th:if="${page.rows>0}" th:fragment="pagination">
    <ul class="pagination justify-content-center">
	<li class="page-item">
	    <a class="page-link" th:href="@{${page.path}(current=1)}">首页</a>
	</li>
	<li th:class="|page-item ${page.current==1?'disabled':''}|">
	    <a class="page-link" th:href="@{${page.path}(current=${page.current-1})}">上一页</a>
	</li>
	<li th:class="|page-item ${i==page.current?'active':''}|"th:each="i:${#numbers.sequence(page.from,page.to)}">
	    <a class="page-link" th:href="@{${page.path}(current=${i})}" th:text="${i}">1</a>
	</li>
        <li th:class="|page-item ${page.current==page.total?'disabled':''}|">
	    <a class="page-link" th:href="@{${page.path}(current=${page.current+1})}">下一页</a>
        </li>
	<li class="page-item">
	    <a class="page-link" th:href="@{${page.path}(current=${page.total})}">末页</a>
	</li>
    </ul>
</nav>
```
#### 4. 添加拦截器后无法访问静态资源 -> 修改 WebMvcConfig.java 
~~~java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");
    }
}
~~~

#### 5. user-mapper.xml 内 selectByName 若有两人相同用户名，其中一人激活，一人未激活 查询时会报错
* 解决方法：增加sql语句，限定查询已激活的用户
~~~xml
<select id="selectByName" resultType="User">
    select id, username, password, salt, email, type, status, activation_code, header_url, create_time
    from user
    where username = #{username}
    and status = 1
</select>
~~~

#### 6. MessageController中，getNoticeList方法内 当message为空时 messageVo存入任何值 thymeleaf取不到对应的值
~~~java
//查询评论通知
	......
        Message message = messageService.findLastNotice(user.getId(), TOPIC_COMMENT);
        Map<String, Object> messageVo = new HashMap<>();
        if (message != null) {
            messageVo.put("message", message);
	......
~~~
两种修改思路
* 1. if后添加else thymeleaf需要的变量手动设置空值 
* 2. messageVo在if内部new
使用第二种修改,如下
~~~java
//查询关注通知
	...
        message = messageService.findLastNotice(user.getId(), TOPIC_FOLLOW);
        if (message != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", message);
	...
	model.addAttribute("followNotice", messageVo);
~~~
评论、点赞同样的方式修改
再修改thymeleaf 
每一类通知的li标签添加判断，当xxxNotice不为空时就显示 如点赞类通知：th:if="${likeNotice!=null}"

#### 7. 在与某个好友私信详情中，发私信，会发两条？？？
定位到 messageMapper.insertMessage(message) 执行后会向数据库插入两条一模一样的数据<br/>
->可能异步请求发送了两次请求<br/>
->修改letter.js 内 <br/>
$("#sendBtn").click(send_letter); ->$("#sendBtn").off().click(send_letter); <br/>
完活

### 版本信息
* SpringBoot 2.1.6.RELEASE
* jdk        1.8.0_271
* MySQL      5.7.31
* Redis      3.2.1
* Kafka      2.13
* Elasticsearch 6.4.3
