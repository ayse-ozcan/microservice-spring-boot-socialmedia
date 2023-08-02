package com.socialmedia.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    //Auth register consumer
    private final String userRegisterQueue = "user-register-queue";
    private final String userForgotPasswordQueue = "user-forgot-password-queue";

    @Bean
    Queue userRegisterQueue() {
        return new Queue(userRegisterQueue);
    }

    @Bean
    Queue userForgotPasswordQueue() {
        return new Queue(userForgotPasswordQueue);
    }

}
