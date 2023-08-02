package com.socialmedia.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    //auth register consumer
    private final String mailRegisterQueue = "mail-register-queue";
    @Bean
    Queue mailRegisterQueue(){
        return new Queue(mailRegisterQueue);
    }
    //Auth forgot password consumer
    private final String mailForgotPasswordQueue = "mail-forgot-password-queue";
    @Bean
    Queue mailForgotPasswordQueue(){
        return new Queue(mailForgotPasswordQueue);
    }

}
