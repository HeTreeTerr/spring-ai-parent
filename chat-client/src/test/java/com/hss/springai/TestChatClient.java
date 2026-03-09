package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;

/**
 * ChatClient 基础使用
 */
@SpringBootTest
public class TestChatClient {
    
    /**
     * 测试 ChatClient 基础使用
     * @param chatClientBuilder
     */
    @Test
    public void testChatClient(@Autowired ChatClient.Builder chatClientBuilder) {
        ChatClient chatClient = chatClientBuilder.build();
        String response = chatClient.prompt()
            .user("你好")
            .call()
            .content();
        System.out.println(response);
    }

    /**
     * 测试 ChatClient 流式调用
     * @param chatClientBuilder
     */
    @Test
    public void testStreamChatClient(@Autowired ChatClient.Builder chatClientBuilder) {
        ChatClient chatClient = chatClientBuilder.build();
        Flux<String> response = chatClient.prompt()
            .user("你好")
            .stream()
            .content();
        response.toIterable().forEach(System.out::println);
    }
}
