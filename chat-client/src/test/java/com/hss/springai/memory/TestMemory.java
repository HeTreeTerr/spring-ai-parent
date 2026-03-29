package com.hss.springai.memory;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;

/**
 * 测试Memory 记忆
 */
@SpringBootTest
public class TestMemory {
    
    /**
     * 测试Memory 记忆
     * 手动实现记忆
     * @param dashScopeChatModel
     */
    @Test
    public void testMemory(@Autowired DashScopeChatModel dashScopeChatModel) {
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel).build();
        
        //历史消息
        StringBuilder chatHis = new StringBuilder();
        String q1 = "我叫法外狂徒";
        chatHis.append("问：").append(q1).append(";");
        String r1 = chatClient.prompt()
            .user(q1)
            .call()
            .content();
        //chatHis.append("答：").append(r1).append(";");
        System.out.println(chatHis.toString());
        System.out.println("===================");
        
        String q2 = "我叫什么？";
        chatHis.append("问：").append(q2).append(";");
        String r2 = chatClient.prompt()
            .user(chatHis.toString())
            .call()
            .content();
        //chatHis.append("答：").append(r2).append(";");
        //System.out.println(chatHis.toString());
        System.out.println(r2);
    }

    /**
     * 测试Memory 记忆
     * ChatMemory 实现记忆
     * @param chatModel
     */
    @Test
    public void testMemory2(@Autowired DashScopeChatModel chatModel) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        String conversationId = "xs001";

        UserMessage userMessage = new UserMessage("我叫法外狂徒");
        chatMemory.add(conversationId, userMessage);
        ChatResponse r1 = chatModel.call(new Prompt(chatMemory.get(conversationId)));
        System.out.println(r1.getResult().getOutput().getText());
        chatMemory.add(conversationId, r1.getResult().getOutput());

        System.out.println("===================");
        UserMessage userMessage2 = new UserMessage("我叫什么？");
        chatMemory.add(conversationId, userMessage2);
        ChatResponse r2 = chatModel.call(new Prompt(chatMemory.get(conversationId)));
        System.out.println(r2.getResult().getOutput().getText());
        chatMemory.add(conversationId, r2.getResult().getOutput());
    }

    /**
     * 测试Memory 记忆
     * PromptChatMemoryAdvisor 实现记忆
     * @param chatModel
     * @param chatMemory
     */
    @Test
    public void testMemoryAdvisor(@Autowired DashScopeChatModel dashScopeChatModel,
                                @Autowired ChatMemory chatMemory) {
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(
                    PromptChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
        
        String r1 = chatClient.prompt()
            .user("我叫法外狂徒")
            .call()
            .content();
        System.out.println(r1);
        System.out.println("===================");

        String r2 = chatClient.prompt()
            .user("我叫什么？")
            .call()
            .content();
        System.out.println(r2);
        System.out.println("===================");

        String r3 = chatClient.prompt()
            .user("我叫什么？")
            .call()
            .content();
        System.out.println(r3);
    }

    /**
     * 测试用户记忆隔离
     * @param dashScopeChatModel
     * @param chatMemory
     */
    @Test
    public void testChatOptions(@Autowired DashScopeChatModel dashScopeChatModel,
                                @Autowired ChatMemory chatMemory){
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(
                    PromptChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

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
        
        @Bean
        public ChatMemory chatMemory(@Autowired ChatMemoryRepository chatMemoryRepository) {
            return MessageWindowChatMemory.builder()
            .maxMessages(5)
            .chatMemoryRepository(chatMemoryRepository)
            .build();
        }
    }
}
