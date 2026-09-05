package dybr.dev.notificationservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(String to, String text) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject("От Макара");
            message.setText(text);
            mailSender.send(message);

            logger.info("Сообщение отправленно на почту {}", to);

        } catch (Exception e) {

            logger.info("На почту {} сообщение не отправленно {}", to, e.getMessage());

            e.printStackTrace();
        }
    }
}