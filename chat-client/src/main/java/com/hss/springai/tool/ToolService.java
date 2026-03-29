package com.hss.springai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.hss.springai.service.TicketService;

import jakarta.annotation.Resource;

@Service
public class ToolService {
    
    @Resource
    private TicketService ticketService;


    @Tool(description = "退票")
    public String cancelTicket(@ToolParam(description = "预定号") String ticketNumber,
                               @ToolParam(description = "姓名") String name){
        System.out.println("退票 -> " + ticketNumber + " " + name);
        ticketService.cancelTicket();
        return "退票成功";
    }
}
