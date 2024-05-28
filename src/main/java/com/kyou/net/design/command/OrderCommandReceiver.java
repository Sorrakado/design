package com.kyou.net.design.command;

import com.kyou.net.design.statemachine.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderCommandReceiver {
    public void action(Order order){
        switch(order.getOrderState()){
            case ORDER_WAIT_PAY -> System.out.println("创建订单");
            case ORDER_CANCEL -> System.out.println("取消订单");
            case ORDER_WAIT_SEND -> System.out.println("支付订单");
            case ORDER_WAIT_RECEIVE -> System.out.println("发货订单");
            case ORDER_FINISH -> System.out.println("收货订单");
            default -> throw new UnsupportedOperationException("不支持的订单状态");
        }
    }
}
