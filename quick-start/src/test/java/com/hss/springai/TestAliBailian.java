package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import reactor.core.publisher.Flux;

/**
 * 测试阿里百炼模型
 */
@SpringBootTest
public class TestAliBailian {
        
    /**
     * 阻塞式响应
     * @param dashScopeChatModel
     */
    @Test
    public void testQwen(@Autowired DashScopeChatModel dashScopeChatModel){
        String content = dashScopeChatModel.call("你好，你是谁");
        System.out.println(content);
    }

    /**
     * 流式响应
     * @param dashScopeChatModel
     */
    @Test
    public void testQwenStream(@Autowired DashScopeChatModel dashScopeChatModel){
        
        Flux<ChatResponse> stream = dashScopeChatModel.stream(new Prompt("你好，你是谁", DashScopeChatOptions
            .builder()
            .withModel(DashScopeApi.ChatModel.QWEN_PLUS.getModel())
            .build()));
        stream.toIterable().forEach(e -> System.out.println(e.getResult().getOutput().getText()));
    }
}
