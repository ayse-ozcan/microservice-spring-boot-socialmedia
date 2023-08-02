package com.socialmedia.rabbitmq.producer;

import com.socialmedia.rabbitmq.model.MailRegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailRegisterProducer {
    private final RabbitTemplate rabbitTemplate;
    public void sendMailRegister(MailRegisterModel mailRegisterModel){
        rabbitTemplate.convertAndSend("auth-exchange",
                "mail-register-binding",mailRegisterModel);
    }
}
