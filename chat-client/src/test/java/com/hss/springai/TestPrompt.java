package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;

import reactor.core.publisher.Flux;

/**
 * 测试系统提示词
 */
@SpringBootTest
public class TestPrompt {
    /**
     * 测试系统提示词
     */
    public static final String TEST_PROMPT = "你是一个专业的翻译，能够将中文翻译成英文。";

    /**
     * 测试系统提示词
     */
    public static final String TEST_LAWYER_PROMPT = "你是一个专业的律师，能够回答法律问题。";



    /**
     * 测试系统提示词
     */
    @Test
    public void testPrompt(@Autowired DashScopeChatModel dashScopeChatModel){
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
            // 全局有效
            //.defaultSystem(TEST_PROMPT)
            //.defaultSystem(TEST_LAWYER_PROMPT)
            .build();

        Flux<String> response = chatClient.prompt()
            .user("你是谁")
            // 仅当前对话有效
            //.system(TEST_PROMPT)
            .system(TEST_LAWYER_PROMPT)
            .stream()
            .content();
        response.toIterable().forEach(System.out::println);
    }
}
