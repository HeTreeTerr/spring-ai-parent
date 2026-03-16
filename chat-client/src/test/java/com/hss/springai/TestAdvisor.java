package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;

import reactor.core.publisher.Flux;

/**
 * 测试Advisor
 */
@SpringBootTest
public class TestAdvisor {
    
    /**
     * 测试Advisor
     * @param dashScopeChatModel
     */
    @Test
    public void testAdvisor(@Autowired DashScopeChatModel dashScopeChatModel) {
        
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        String response = chatClient.prompt()
            .user("你好")
            .call()
            .content();
        System.out.println(response);
    }

    /**
     * 测试StreamAdvisor
     * @param dashScopeChatModel
     */
    @Test
    public void testStreamAdvisor(@Autowired DashScopeChatModel dashScopeChatModel) {
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        Flux<String> response = chatClient.prompt()
            .user("你好")
            .stream()
            .content();
        response.toIterable().forEach(System.out::println);
    }
}
