package com.socialmedia.rabbitmq.producer;

import com.socialmedia.rabbitmq.model.MailForgotPasswordModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailForgotPasswordProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMailForgotPassword(MailForgotPasswordModel mailForgotPasswordModel) {
        rabbitTemplate.convertAndSend("auth-exchange", "mail-forgot-password-binding"
                , mailForgotPasswordModel);

    }
}
