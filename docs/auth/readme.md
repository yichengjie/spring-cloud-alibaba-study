#### JWT操作工具类分享:http://www.imooc.com/article/290892
#### feign实现token传递
1. 方式1：使用@RequestHeader注解。eg：@RequestHeader("X-Token") String token  
2. 方式2：使用RequestInterceptor
    ```text
    编写RequestInterceptor实现类、
    public class TokenRelayRequestInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            //1. 获取到token
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes)requestAttributes ;
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("X-Token");
            if (StringUtils.isNoneBlank(token)){
                //3. 将token传递
                // 将指定的值设置到header上
                template.header("X-Token", token) ;
            }
        }
    }
    配置全局RequestInterceptor
   # 全局配置RequestInterceptor
   feign.client.config.default.request-interceptors[0]=com.yicj.contentcenter.feignclient.interceptor.TokenRelayRequestInterceptor
    ```
#### RestTemplate传递token
1. 方式1：使用exchange
2. 方式2：使用ClientHttpRequestInterceptor
    ```text
    编写ClientHttpRequestInterceptor实现类
    public class RestTemplateTokenRelayInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            String token = this.getToken();
            HttpHeaders headers = request.getHeaders();
            headers.add("X-Token", token);
            // 请求继续执行请求
            return execution.execute(request, body);
        }
        private HttpServletRequest getHttpServletRequest() {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            return attributes.getRequest();
        }
        private String getToken(){
            HttpServletRequest httpServletRequest = this.getHttpServletRequest();
            String token = httpServletRequest.getHeader("X-Token");
            return token ;
        }
    }
    RestTemplate中添加interceptor
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateTokenRelayInterceptor()));
        return restTemplate ;
    }
    ```
