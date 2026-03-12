package com.hss.springai.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.hss.springai.entity.MorePlatformAndModelOption;

import reactor.core.publisher.Flux;


/**
 * 多平台模型控制器
 */
@RestController
@RequestMapping("/more-platform-model")
public class MorePlatformModelController {

    Map<String,ChatModel> platforms = new HashMap<>();
   
    /**
     * 构造器
     * @param dashScopeChatModel
     * @param deepSeekChatModel
     * @param ollamaChatModel
     */
    public MorePlatformModelController(DashScopeChatModel dashScopeChatModel,
            DeepSeekChatModel deepSeekChatModel,
            OllamaChatModel ollamaChatModel){
        
        platforms.put("dashscope", dashScopeChatModel);
        platforms.put("deepseek", deepSeekChatModel);
        platforms.put("ollama", ollamaChatModel);
    }

    /**
     * 多平台模型聊天
     * @param message 消息
     * @param option 选项
     * @return 响应流
     */
    @RequestMapping(value = "/chat",produces = "text/stream;charset=UTF-8")
    public Flux<String> chat(String message,MorePlatformAndModelOption option){
        
        String platform = option.getPlatform();
        ChatModel chatModel = platforms.get(platform);
        if(chatModel == null){
            throw new IllegalArgumentException("平台不存在");
        }

        ChatClient.Builder builder = ChatClient.builder(chatModel);

        ChatClient chatClient = builder.defaultOptions(
                ChatOptions.builder()
                    .temperature(Objects.isNull(option.getTemperature()) ? 0.7 : option.getTemperature())
                    .model(option.getModel())
                    .build()
        ).build();

        Flux<String> content = chatClient.prompt().user(message).stream().content();
        return content;
    }
}
