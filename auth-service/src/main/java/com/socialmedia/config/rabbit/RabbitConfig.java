package com.socialmedia.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    private final String exchange = "auth-exchange";

    @Bean
    DirectExchange authExchange() {
        return new DirectExchange(exchange);
    }

    //User register producer
    private final String userRegisterQueue = "user-register-queue";
    private final String userRegisterBinding = "user-register-binding";

    @Bean
    Queue userRegisterQueue() {
        return new Queue(userRegisterQueue);
    }

    @Bean
    public Binding userRegisterBinding(final DirectExchange authExchange,
                                       final Queue userRegisterQueue) {
        return BindingBuilder.bind(userRegisterQueue)
                .to(authExchange)
                .with(userRegisterBinding);
    }
    //user forgot password
    private final String userForgotPasswordQueue = "user-forgot-password-queue";
    private final String userForgotPasswordBinding = "user-forgot-password-binding";
    @Bean
    Queue userForgotPasswordQueue(){
        return new Queue(userForgotPasswordQueue);
    }
    @Bean
    public Binding userForgotPassword(final DirectExchange authExchange,
                                      final Queue userForgotPasswordQueue){
        return BindingBuilder.bind(userForgotPasswordQueue)
                .to(authExchange)
                .with(userForgotPasswordBinding);
    }
    //MAIL SENDER REGISTER
    private final String mailRegisterQueue = "mail-register-queue";
    private final String mailRegisterBinding = "mail-register-binding";
    @Bean
    Queue mailRegisterQueue(){
        return new Queue(mailRegisterQueue);
    }
    @Bean
    public Binding mailRegisterBinding(final DirectExchange authExchange,
                                       final Queue mailRegisterQueue){
        return BindingBuilder.bind(mailRegisterQueue)
                .to(authExchange)
                .with(mailRegisterBinding);

    }
    private final String mailForgotPasswordQueue = "mail-forgot-password-queue";
    private final String mailForgotPasswordBinding = "mail-forgot-password-binding";
    @Bean
    Queue mailForgotPasswordQueue(){
        return new Queue(mailForgotPasswordQueue);
    }
    @Bean
    public Binding mailForgotPasswordBinding(final DirectExchange authExchange,
                                             final Queue mailForgotPasswordQueue){
        return BindingBuilder.bind(mailForgotPasswordQueue)
                .to(authExchange)
                .with(mailForgotPasswordBinding);
    }
}
