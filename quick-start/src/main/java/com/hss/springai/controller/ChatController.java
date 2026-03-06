package com.hss.springai.controller;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import reactor.core.publisher.Flux;

/**
 * 聊天控制器
 * @author hss
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    
    @Autowired 
    private DashScopeChatModel dashScopeChatModel;

    /**
     * 阻塞式响应
     * @param query
     * @return
     */
    @GetMapping("/call")
    public String call(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query) {
        ChatResponse callRes = dashScopeChatModel.call(new Prompt(query, DashScopeChatOptions
                    .builder()
                    .withModel(DashScopeApi.ChatModel.QWEN_PLUS.getModel())
                    .build()));
        return callRes.getResult().getOutput().getText();
    }

    /**
     * 流式响应
     * @param query
     * @return
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query) {
        
        Flux<ChatResponse> streamRes = dashScopeChatModel.stream(new Prompt(query, DashScopeChatOptions
            .builder()
            .withModel(DashScopeApi.ChatModel.QWEN_PLUS.getModel())
            .build()));
        return streamRes.map(e -> e.getResult().getOutput().getText());
    }
}
