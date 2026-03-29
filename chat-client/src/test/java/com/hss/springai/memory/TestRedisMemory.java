package com.hss.springai.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;

/**
 * 测试RedisChatMemoryRepository 持久化记忆到redis
 */
@SpringBootTest
public class TestRedisMemory {

    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired DashScopeChatModel dashScopeChatModel,
                    @Autowired ChatMemory chatMemory){
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(
                    PromptChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
    
    @Test
    public void testChatOptions(){
        String r1 = chatClient.prompt()
            .user("我叫法外狂徒")
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "001"))
            .call()
            .content();
        System.out.println(r1);

        String r2 = chatClient.prompt()
            .user("我叫什么？")
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "001"))
            .call()
            .content();
        System.out.println(r2);
        System.out.println("===================");

        String r3 = chatClient.prompt()
            .user("我叫什么？")
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "002"))
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

        @Value("${spring.ai.memory.redis.host}")
        private String redisHost;

        @Value("${spring.ai.memory.redis.port}")
        private Integer redisPort;

        @Value("${spring.ai.memory.redis.password}")
        private String redisPassword;
        
        @Value("${spring.ai.memory.redis.timeout}")
        private Integer redisTimeout;

        @Bean
        public RedisChatMemoryRepository redisChatMemoryRepository(){
            return RedisChatMemoryRepository.builder()
                    .host(redisHost)
                    .port(redisPort)
                    .password(redisPassword)
                    .timeout(redisTimeout)
                    .build();
        }

        @Bean
        public ChatMemory chatMemory(@Autowired RedisChatMemoryRepository chatMemoryRepository) {
            return MessageWindowChatMemory.builder()
            .maxMessages(10)
            .chatMemoryRepository(chatMemoryRepository)
            .build();
        }
    }
}
