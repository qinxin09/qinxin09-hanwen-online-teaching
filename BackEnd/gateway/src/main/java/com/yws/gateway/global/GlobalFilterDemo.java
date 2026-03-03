package com.yws.gateway.global;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@Order(1)
public class GlobalFilterDemo implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求参数
        ServerHttpRequest request = exchange.getRequest();
//        对访问用户模块进行过滤
        if(request.getPath().toString().startsWith("/user/")){
            Object role = request.getQueryParams().getFirst("role");
            if (role != null) {
                if ("0".equals(role.toString()) || "1".equals(role.toString())) {
                    return chain.filter(exchange);
                }
            }
            System.out.println("验证失败！");
            //设置响应失败的信息
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}