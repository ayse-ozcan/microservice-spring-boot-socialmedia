package com.socialmedia.rabbitmq.consumer;

import com.socialmedia.rabbitmq.model.MailForgotPasswordModel;
import com.socialmedia.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailForgotPasswordConsumer {
    private final MailSenderService mailSenderService;
    @RabbitListener(queues = "mail-forgot-password-queue")
    public void sendForgotPassword(MailForgotPasswordModel mailForgotPasswordModel){
        mailSenderService.sendForgotPassword(mailForgotPasswordModel);
    }
}
