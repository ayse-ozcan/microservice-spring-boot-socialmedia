package com.socialmedia.rabbitmq.producer;

import com.socialmedia.rabbitmq.model.UserForgotPasswordModel;
import com.socialmedia.rabbitmq.model.UserRegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserForgotPasswordProducer {
    private final RabbitTemplate rabbitTemplate;
    public void sendForgotPasswordMessage(UserForgotPasswordModel userForgotPasswordModel){
        rabbitTemplate.convertAndSend("auth-exchange",
                "user-forgot-password-binding",userForgotPasswordModel);
    }

}
