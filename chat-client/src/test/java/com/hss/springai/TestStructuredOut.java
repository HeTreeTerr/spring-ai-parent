package com.hss.springai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;

/**
 * 结构化输出
 */
@SpringBootTest
public class TestStructuredOut {
    
    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired DashScopeChatModel dashScopeChatModel){
        chatClient = ChatClient.builder(dashScopeChatModel)
                .build();
    }

    /**
     * 测试布尔值输出
     */
    @Test
    public void testBoolOut(){
        Boolean isComplain = chatClient.prompt()
                    .system("""
                    请判断用户信息是否表达了投诉意图
                    只能用 true 或 false 回答，不要输出其他内容       
                    """)
                    //.user("你们家的快递迟迟不到，我要退货！")
                    .user("我叫法外狂徒！！！")
                    .call()
                    .entity(Boolean.class);

        //分支逻辑
        if(Boolean.TRUE.equals(isComplain)){
            System.out.println("用户是投诉，转接人工客服！");
        }else{
            System.out.println("用户不是投诉，自动流转客服机器人。");
            //todo 继续调用 客服ChatClient 进行对话
        }
    }

    public record Address(
        String name, // 收件人姓名
        String phone, // 联系电话
        String province, // 省
        String city, // 市
        String district, // 区/县
        String detail // 详细地址
    ){}

    /**
     * 测试实体输出
     */
    @Test
    public void testEntityOut(){
        Address address = chatClient.prompt()
                    .system("""
                    请从下条文本中提取收货信息
                    """)
                    .user("收货人：法外狂徒\n手机号：13800000000\n地址：浙江省杭州市西湖区文一西路100号8赚202室")
                    .call()
                    .entity(Address.class);
        System.out.println(address);
    }
}
