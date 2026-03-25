package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
