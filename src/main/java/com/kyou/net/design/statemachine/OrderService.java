package com.kyou.net.design.statemachine;

import com.kyou.net.design.command.OrderCommand;
import com.kyou.net.design.command.OrderCommandInvoker;
import com.kyou.net.design.util.RedisCommonProcessor;
import jakarta.annotation.Resource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Resource
    private StateMachine<OrderState, OrderStateChangeAction> orderStateMachine;

    @Resource
    private StateMachinePersister<OrderState, OrderStateChangeAction, String> orderStateMachineRedisPersister;

    @Resource
    private RedisCommonProcessor  redisCommonProcessor;

    @Resource
    private OrderCommand orderCommand;

    public Order createOrder(String productId) {
        String orderId = "OID" + productId;
        Order order = Order.builder().orderId(orderId).productId(productId).orderState(OrderState.ORDER_WAIT_PAY).build();
        redisCommonProcessor.set(orderId,order,900);
        OrderCommandInvoker invoker = new OrderCommandInvoker();
        invoker.invoke(orderCommand,order);
        return order;

    }

    public Order pay(String orderId) {
        Order order = (Order) redisCommonProcessor.get(orderId);
        Message<OrderStateChangeAction> message = MessageBuilder.withPayload(OrderStateChangeAction.PAY_ORDER).setHeader("order", order).build();
        if(changeStateAction(message,order)){
            return order;
        }
        return null;

    }

    public Order send(String orderId){
        Order order = (Order) redisCommonProcessor.get(orderId);
        Message<OrderStateChangeAction> message = MessageBuilder.withPayload(OrderStateChangeAction.SEND_ORDER).setHeader("order", order).build();
        if(changeStateAction(message,order)){
            return order;
        }
        return null;
    }

    public Order receive(String orderId){
        Order order = (Order) redisCommonProcessor.get(orderId);
        Message<OrderStateChangeAction> message = MessageBuilder.withPayload(OrderStateChangeAction.RECEIVE_ORDER).setHeader("order", order).build();
        if(changeStateAction(message,order)){
            return order;
        }
        return null;
    }
    private boolean changeStateAction(Message<OrderStateChangeAction> message,Order order){
        try{
            orderStateMachine.start();
            //从redis读取
            orderStateMachineRedisPersister.restore(orderStateMachine,order.getOrderId() +"STATE");
            //发送message
            boolean res = orderStateMachine.sendEvent(message);

            orderStateMachineRedisPersister.persist(orderStateMachine,order.getOrderId() +"STATE");
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            orderStateMachine.stop();
        }
    }
}
