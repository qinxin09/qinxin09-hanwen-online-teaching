package com.yws.consumer.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleConfig {
    //配置负载均衡的策略为随机，默认算法为轮询算法
    @Bean
    public IRule myRule() {
        boolean randomMode = true;
        if (randomMode) {
            System.out.println("随机==========================================");
            return new RandomRule();
        }
        System.out.println("轮询==========================================");
        return new RoundRobinRule();
    }
}
