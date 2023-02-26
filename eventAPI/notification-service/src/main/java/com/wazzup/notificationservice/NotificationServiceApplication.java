package com.wazzup.notificationservice;

import com.wazzup.notificationservice.dto.notification.CreateAccountDTO;
import com.wazzup.notificationservice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    @Autowired
    private EmailService emailService;
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleCreateAccountNotification(CreateAccountDTO createAccountDTO) {
        emailService.sendConfirmationToken(createAccountDTO.getEmail(), createAccountDTO.getMailContent());
        log.info("CREATE ACCOUNT SEND EMAIL: {}", createAccountDTO.getEmail());
    }

}
