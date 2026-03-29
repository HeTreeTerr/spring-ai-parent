package com.hss.springai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.hss.springai.service.TicketService;

import jakarta.annotation.Resource;

/**
 * mcp tool 服务
 */
@Service
public class ToolService {
    
    @Resource
    private TicketService ticketService;


    /**
     * 退票工具
     * 描述越详细越好
     * 最终兜底手段：加强代码校验、添加人工审核流程
     * @param ticketNumber
     * @param name
     * @return
     */
    @Tool(description = "退票")
    public String cancelTicket(@ToolParam(description = "预定号(必填，必须是有效的信息。严谨用其他信息代替；如缺失请传null)",required = true) String ticketNumber,
                               @ToolParam(description = "姓名(必填，必须是有效的信息。严谨用其他信息代替；如缺失请传null)",required = true) String name){
        System.out.println("退票 -> " + ticketNumber + " " + name);
        ticketService.cancelTicket();
        return "退票成功";
    }
}
