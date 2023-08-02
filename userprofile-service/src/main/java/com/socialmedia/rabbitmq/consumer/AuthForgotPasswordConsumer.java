package com.socialmedia.rabbitmq.consumer;

import com.socialmedia.rabbitmq.model.UserForgotPasswordModel;
import com.socialmedia.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthForgotPasswordConsumer {
    private final UserProfileService userProfileService;

    @RabbitListener(queues = "user-forgot-password-queue")
    public void consumerForgotPassword(UserForgotPasswordModel model){
        userProfileService.forgotPasswordWithRabbitMq(model);
    }
}
