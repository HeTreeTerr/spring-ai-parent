package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;

/**
 * 测试deepseek模型模型
 */
@SpringBootTest
public class TestDeepseek {
    
    /**
     * 阻塞式响应
     * @param deepSeekChatModel
     */
    @Test
    public void testDeepseek(@Autowired DeepSeekChatModel deepSeekChatModel) {
        String content = deepSeekChatModel.call("你好，你是谁");
        System.out.println(content);
    }

    /**
     * 流式响应
     * @param deepSeekChatModel
     */
    @Test
    public void testDeepseekStream(@Autowired DeepSeekChatModel deepSeekChatModel) {
        Flux<String> stream = deepSeekChatModel.stream("你好，你是谁");
        stream.toIterable().forEach(System.out::println);
    }


    /**
     * 测试deepseek模型模型的选项配置
     * @param deepSeekChatModel
     */
    @Test
    public void testChatOptions(@Autowired DeepSeekChatModel deepSeekChatModel) {
        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .model("deepseek-chat")
                .temperature(0.1d)
                .build();
        Prompt prompt = new Prompt("请写一句诗描述清晨", options);

        Flux<ChatResponse> stream = deepSeekChatModel.stream(prompt);
        stream.toIterable().forEach(e -> {
            System.out.println(e.getResult().getOutput().getText());
        });
    }
}
