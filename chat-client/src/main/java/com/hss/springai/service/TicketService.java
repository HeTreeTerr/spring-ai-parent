package com.hss.springai.service;

import org.springframework.stereotype.Service;

@Service
public class TicketService {
    
    public void cancelTicket(){
        System.out.println("core service -> 退票!!!");
    }
}
