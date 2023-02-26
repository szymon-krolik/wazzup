package com.wazzup.notificationservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    private static final String EMAIL_FROM = "Wazzup";
    public EmailService(){}
    private void sendMail(String recipient, String subject, String content) {
        javaMailSender.setDefaultEncoding("UTF-8");
        MimeMessage mail  = javaMailSender.createMimeMessage();
        try {
            mail.setContent(content, "text/html; charset=ISO-8859-2");
            MimeMessageHelper helper = new MimeMessageHelper(mail, "UTF-8");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setFrom(EMAIL_FROM);
            javaMailSender.send(mail);
        } catch (MessagingException e) {
            log.info("Error send mail: {}", e.getStackTrace());
        }
    }

    public void sendConfirmationToken(String mail, String content) {
        this.sendMail(mail, "REGISTRATION", content);
    }
}
