package com.kyou.net.design.statemachine;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;


    @GetMapping("/create")
    public Order createOrder(@RequestParam String productId) {
        return orderService.createOrder(productId);
    }
    @GetMapping("/pay")
    public Order payOrder(@RequestParam  String orderId) {
        return orderService.pay(orderId);
    }

    @GetMapping("/send")
    public Order sendOrder(@RequestParam String orderId) {
       return orderService.send(orderId);
    }

    @GetMapping("/receive")
    public Order receiveOrder(@RequestParam String orderId) {
       return orderService.receive(orderId);
    }
}
