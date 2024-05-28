package com.kyou.net.design.command;

import com.kyou.net.design.statemachine.Order;

public interface OrderCommandInterface {
    void execute(Order order);
}
