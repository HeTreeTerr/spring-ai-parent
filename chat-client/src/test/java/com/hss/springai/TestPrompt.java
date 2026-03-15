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

    /**
     * 测试系统提示词模板
     * @param dashScopeChatModel
     */
    @Test
    public void testSystemPromptTemplate(@Autowired DashScopeChatModel dashScopeChatModel){
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
            // 全局有效
            .defaultSystem("""
                # 角色说明
                你是一个专业的律师，能够回答法律问题。\s
                # 回复格式
                1. 问题分析
                2. 相关依据
                3. 梳理和建议\s
                # 当前服务的用户
                姓名：{name}，年龄：{age}，性别：{sex}\s
                """)
            .build();

        Flux<String> response = chatClient.prompt()
            .user("你好")
            // 仅当前对话有效
            .system(p -> 
                p.param("name", "张三")
                .param("age", "25")
                .param("sex", "男")
            )
            .stream()
            .content();
        response.toIterable().forEach(System.out::println);
    }

    /**
     * 伪系统提示词
     * @param dashScopeChatModel
     */
    @Test
    public void testSystemPromptTemplate2(@Autowired DashScopeChatModel dashScopeChatModel){
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
            .build();

        Flux<String> response = chatClient.prompt()
            .user(u -> u.text("""
                # 角色说明
                你是一个专业的律师，能够回答法律问题。\s
                # 回复格式
                1. 问题分析
                2. 相关依据
                3. 梳理和建议\s
                
                回答用户的法律咨询问题
                {question}\s
                """).param("question", "被裁的补偿金"))
            .stream()
            .content();
        response.toIterable().forEach(System.out::println);
    }
}
