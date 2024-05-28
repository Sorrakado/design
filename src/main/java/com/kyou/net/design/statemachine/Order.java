package com.kyou.net.design.statemachine;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Order implements Serializable {
    private String orderId;

    private String productId;

    private OrderState orderState;

    private Float price;
}
