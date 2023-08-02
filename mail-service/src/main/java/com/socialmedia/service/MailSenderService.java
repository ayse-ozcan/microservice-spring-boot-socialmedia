package com.socialmedia.service;

import com.socialmedia.rabbitmq.model.MailForgotPasswordModel;
import com.socialmedia.rabbitmq.model.MailRegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailSenderService {
   private final JavaMailSender javaMailSender;
   public void sendRegisterUsersInfo(MailRegisterModel mailRegisterModel) {
      //MimeMessage, SimpleMailMessage a alternatif
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom("${spring.mail.username}");
      mailMessage.setTo(mailRegisterModel.getEmail());
      mailMessage.setSubject("DOGRULAMA KODU");
      mailMessage.setText("Tebrikler, basariyla kayit oldunuz. Giris ve onay bilgileriniz asagidaki gibidir.\n"
      + "Kullanici adi: " + mailRegisterModel.getUsername()
              + "\n Parola: " + mailRegisterModel.getDecodedPassword()
              +  "\n Dogrulama kodu: " + mailRegisterModel.getActivationCode()
      );
      javaMailSender.send(mailMessage);
   }
   public void sendForgotPassword(MailForgotPasswordModel mailForgotPasswordModel) {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom("${spring.mail.username}");
      mailMessage.setTo(mailForgotPasswordModel.getEmail());
      mailMessage.setSubject("Sifremi unuttum");
      mailMessage.setText("Yeni parola olusturuldu.\n" + "Yeni parolaniz: "
              + mailForgotPasswordModel.getRandomPassword() + "\nLutfen giris yaptiktan sonra sifrenizi degistirin");
      javaMailSender.send(mailMessage);
   }
}
