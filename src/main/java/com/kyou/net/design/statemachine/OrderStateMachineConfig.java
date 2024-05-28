package com.kyou.net.design.statemachine;

import com.kyou.net.design.util.RedisCommonProcessor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState,OrderStateChangeAction> {


    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateChangeAction> states) throws Exception {
      states.withStates().initial(OrderState.ORDER_WAIT_PAY).states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateChangeAction> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderState.ORDER_WAIT_PAY)
                .target(OrderState.ORDER_WAIT_SEND)
                .event(OrderStateChangeAction.PAY_ORDER)
                .and()

                .withExternal()
                .source(OrderState.ORDER_WAIT_SEND)
                .target(OrderState.ORDER_WAIT_RECEIVE)
                .event(OrderStateChangeAction.SEND_ORDER)
                .and()

                .withExternal()
                .source(OrderState.ORDER_WAIT_RECEIVE).
                target(OrderState.ORDER_FINISH)
                .event(OrderStateChangeAction.RECEIVE_ORDER)
                .and()

                .withExternal()
                .source(OrderState.ORDER_WAIT_PAY)
                .target(OrderState.ORDER_CANCEL)
                .event(OrderStateChangeAction.CANCEL_ORDER);
    }

    @Resource
    RedisConnectionFactory  redisConnectionFactory;
    @Bean(name = "orderStateMachineRedisPersister")
    public RedisStateMachinePersister<OrderState, OrderStateChangeAction> getRedisPersister(){
        RedisStateMachineContextRepository<OrderState, OrderStateChangeAction> repository = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist<OrderState, OrderStateChangeAction> persist = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(persist);

    }
}
