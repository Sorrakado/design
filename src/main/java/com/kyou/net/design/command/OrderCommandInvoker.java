package com.kyou.net.design.command;

import com.kyou.net.design.statemachine.Order;

public class OrderCommandInvoker {
    public void invoke(OrderCommandInterface command, Order order){
        command.execute(order);
    }
}
