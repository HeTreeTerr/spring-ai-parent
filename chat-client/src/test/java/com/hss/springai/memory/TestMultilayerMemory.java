package com.hss.springai.memory;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * 多层次记忆架构
 */
@SpringBootTest
public class TestMultilayerMemory {
    
    /*
    记忆多=聪明，记忆多会出发token上限

    多层次记忆架构（模拟人类）
    * 近期记忆：保留在上下文窗口中的最近几轮对话，每轮对话完成后立即存储（可通过charMemory）
    * 中期记忆：通过RAG检索的相关历史对话（每轮对话完成后，异步将对话内容转换为向量并存入向量数据库）
    * 长期记忆：关键信息的固化总结
    
    方式一：定时批处理
        1）通过定时任务（如每天或每周）多积累的对话进行总结和提炼
        2）提取关键信息、用户偏好、重要事实等
        3）批处理方式降低计算成本，适合大规模处理
    
    方式二：关键点实时处理
        1）在对话中识别出关键信息点时立即提取并存储
        2）例如，当用户明确表达偏好、提供个人信息并设置持久性指令时
        3）采用“写入触发器”机制，在特定条件下自动更新长期记忆
    */
}
