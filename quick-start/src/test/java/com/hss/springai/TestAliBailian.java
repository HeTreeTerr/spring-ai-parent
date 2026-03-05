package com.hss.springai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;

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
}
