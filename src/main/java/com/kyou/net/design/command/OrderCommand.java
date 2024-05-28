package com.kyou.net.design.command;

import com.kyou.net.design.statemachine.Order;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class OrderCommand implements OrderCommandInterface{

    @Resource
    private OrderCommandReceiver receiver;
    @Override
    public void execute(Order order) {
        this.receiver.action(order);
    }
}
