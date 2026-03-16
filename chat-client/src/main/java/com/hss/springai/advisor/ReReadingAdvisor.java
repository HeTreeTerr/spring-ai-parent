package com.hss.springai.advisor;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

/**
 * 重新读取Advisor
 */
public class ReReadingAdvisor implements BaseAdvisor{

    private static final String DEFAULT_USER_TEXT_ADVISOR = """
        {re2_input_query}
        Read the question again: {re2_input_query}
    """;

    /**
     * 重新读取Advisor之前，对请求进行处理
     */
    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        //用户提示词
        String contents = chatClientRequest.prompt().getContents();

        String re2InputQuery = PromptTemplate.builder().template(DEFAULT_USER_TEXT_ADVISOR).build()
                    .render(Map.of("re2_input_query", contents));

        ChatClientRequest build = chatClientRequest.mutate()
                .prompt(Prompt.builder().content(re2InputQuery).build())
                .build();
        return build;
    }

    /**
     * 重新读取Advisor之后，对响应进行处理
     */
    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    /**
     * 重新读取Advisor的顺序，默认0
     */
    @Override
    public int getOrder() {
        return 0;
    }
    
}
