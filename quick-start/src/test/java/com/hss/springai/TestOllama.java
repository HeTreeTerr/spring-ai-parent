package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import reactor.core.publisher.Flux;

/**
 * 测试ollama模型模型
 */
@SpringBootTest
public class TestOllama {

    /**
     * 阻塞式响应
     * @param ollamaChatModel
     */
    @Test
    public void testOllama(@Autowired OllamaChatModel ollamaChatModel) {
        //String content = ollamaChatModel.call("你好，你是谁？");

        //软式关闭深度思考
        String content = ollamaChatModel.call("你好，你是谁？/no_think");
        
        System.out.println(content);
    }

    /**
     * 流式响应
     * @param ollamaChatModel
     */
    @Test
    public void testOllamaStream(@Autowired OllamaChatModel ollamaChatModel) {
        Flux<String> stream = ollamaChatModel.stream("你好，你是谁");
        stream.toIterable().forEach(System.out::println);
    }

    /**
     * 流式响应，多模态输入输出
     * @param ollamaChatModel
     */
    @Test
    public void testMultimodlity(@Autowired OllamaChatModel ollamaChatModel){
        var imageResource = new ClassPathResource("/files/KD_CT5.jpg");

        Media media = new Media(MimeTypeUtils.IMAGE_JPEG, imageResource);

        OllamaOptions options = OllamaOptions.builder()
            .model("gemma3")
            .build();
        
        Prompt prompt = new Prompt(
            UserMessage.builder()
                .media(media)
                .text("识别图片")
                .build()
            ,options);

        ChatResponse response = ollamaChatModel.call(prompt);
        System.out.println(response.getResult().getOutput().getText());
    }
}
