package com.yicj.contentcenter.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


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
