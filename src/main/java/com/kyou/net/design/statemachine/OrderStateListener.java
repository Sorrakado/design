package com.kyou.net.design.statemachine;

import com.kyou.net.design.command.OrderCommand;
import com.kyou.net.design.command.OrderCommandInvoker;
import com.kyou.net.design.util.RedisCommonProcessor;
import jakarta.annotation.Resource;

import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {
    @Resource
    private RedisCommonProcessor redisCommonProcessor;

    @Resource
    private OrderCommand orderCommand;
    @OnTransition(source = "ORDER_WAIT_PAY",target = "ORDER_WAIT_SEND")
    public boolean payToSend(Message<OrderStateChangeAction> message){
        Order order = (Order)message.getHeaders().get("orderId");
        if(order.getOrderState()!= OrderState.ORDER_WAIT_PAY){
            throw  new UnsupportedOperationException("order state error");
        }
        order.setOrderState(OrderState.ORDER_WAIT_SEND);
        redisCommonProcessor.set(order.getOrderId(),order);
        OrderCommandInvoker invoker = new OrderCommandInvoker();
        invoker.invoke(orderCommand,order);
        return true;
    }

    @OnTransition(source = "ORDER_WAIT_SEND",target = "ORDER_WAIT_RECEIVE")
    public boolean sendToReceive(Message<OrderStateChangeAction> message){
        Order order = (Order)message.getHeaders().get("orderId");
        if(order.getOrderState()!= OrderState.ORDER_WAIT_SEND){
            throw  new UnsupportedOperationException("order state error");
        }
        order.setOrderState(OrderState.ORDER_WAIT_RECEIVE);
        redisCommonProcessor.set(order.getOrderId(),order);
        OrderCommandInvoker invoker = new OrderCommandInvoker();
        invoker.invoke(orderCommand,order);
        return true;
    }

    @OnTransition(source = "ORDER_WAIT_RECEIVE",target = "ORDER_FINISH")
    public boolean receiveToFinish(Message<OrderStateChangeAction> message){
        Order order = (Order)message.getHeaders().get("orderId");
        if(order.getOrderState()!= OrderState.ORDER_WAIT_RECEIVE){
            throw  new UnsupportedOperationException("order state error");
        }
        order.setOrderState(OrderState.ORDER_FINISH);
        redisCommonProcessor.remove(order.getOrderId());
        redisCommonProcessor.remove(order.getOrderId() + "STATE");
        OrderCommandInvoker invoker = new OrderCommandInvoker();
        invoker.invoke(orderCommand,order);
        return true;
    }

    @OnTransition(source = "ORDER_WAIT_PAY",target = "ORDER_CANCEL")
    public boolean payToCancel(Message<OrderStateChangeAction> message){
        Order order = (Order)message.getHeaders().get("orderId");
        if(order.getOrderState()!= OrderState.ORDER_WAIT_PAY){
            throw  new UnsupportedOperationException("order state error");
        }
        order.setOrderState(OrderState.ORDER_CANCEL);
        redisCommonProcessor.set(order.getOrderId(),order);
        OrderCommandInvoker invoker = new OrderCommandInvoker();
        invoker.invoke(orderCommand,order);
        return true;
    }
}
