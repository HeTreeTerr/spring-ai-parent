package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.deepseek.DeepSeekChatModel;
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
}
