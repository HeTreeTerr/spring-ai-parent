package com.hss.springai;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

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
     * 流式响应，深度思考
     * @param deepSeekChatModel
     */
    @Test
    public void testDeepseekStreamReasoning(@Autowired DeepSeekChatModel deepSeekChatModel){
        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                // 模型名称
                .model("deepseek-reasoner")
                .build();

        Flux<ChatResponse> stream = deepSeekChatModel.stream(new Prompt("你好，你是谁", options));
        stream.toIterable().forEach(e -> {
            DeepSeekAssistantMessage assistantMessage = (DeepSeekAssistantMessage)e.getResult().getOutput();
            if(StringUtils.hasLength(assistantMessage.getReasoningContent())){
                System.out.println(assistantMessage.getReasoningContent());
            }
        });

        System.out.println("-----------------");

        stream.toIterable().forEach(e -> {
            if(StringUtils.hasLength(e.getResult().getOutput().getText())){
                System.out.println(e.getResult().getOutput().getText());
            }
        });
    }

    /**
     * 测试deepseek模型模型的选项配置
     * @param deepSeekChatModel
     */
    @Test
    public void testChatOptions(@Autowired DeepSeekChatModel deepSeekChatModel) {
        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                // 模型名称
                .model("deepseek-chat")
                // 温度，控制生成的文本的随机性，值越大，生成的文本越随机，值越小，生成的文本越确定（0-2 浮点数组） 
                .temperature(0.1d)
                // 最大生成的 token 数量，值越大，控制生成的文本越长，值越小，控制生成的文本越短（默认值：1024）
                .maxTokens(200)
                // 停止生成的条件，当生成的文本包含这些字符串时，停止生成（默认值：空字符串）
                .stop(Arrays.asList("END", "STOP","最后总结一下"))
                .build();
                
        Prompt prompt = new Prompt("请写一句诗描述清晨", options);

        Flux<ChatResponse> stream = deepSeekChatModel.stream(prompt);
        stream.toIterable().forEach(e -> {
            System.out.println(e.getResult().getOutput().getText());
        });
    }
}
