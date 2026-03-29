package com.hss.springai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.hss.springai.tool.ToolService;

/**
 * 测试mcp tool
 */
@SpringBootTest
public class TestTool {
    
    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired DashScopeChatModel dashScopeChatModel,
                    @Autowired ChatMemory chatMemory,
                    @Autowired ToolService toolService){
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultTools(toolService)
                .defaultAdvisors(
                    PromptChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Test
    public void testTool(){
        String r1 = chatClient.prompt()
            .user("我的姓名是法外狂徒")
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "001"))
            .call()
            .content();
        System.out.println(r1);

        String r2 = chatClient.prompt()
            .user("我要退票！！！")
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "001"))
            .call()
            .content();
        System.out.println(r2);
        System.out.println("===================");

        String r3 = chatClient.prompt()
            .user("预定号是：tk_123456")
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "001"))
            .call()
            .content();
        System.out.println(r3);
    }


    /**
     * 测试配置类
     * 只会在该单元测类中生效，不会影响其他单元测类和主程序的运行
     */
    @TestConfiguration
    static class Config {
        
        @Bean
        public ChatMemory chatMemory(@Autowired ChatMemoryRepository chatMemoryRepository) {
            return MessageWindowChatMemory.builder()
            .maxMessages(10)
            .chatMemoryRepository(chatMemoryRepository)
            .build();
        }
    }
}
