package dybr.dev.notificationservice.kafka;

import dybr.dev.notificationservice.model.UserNotificationEvent;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);
    private final NotificationService sender;

    public UserEventConsumer(NotificationService sender) {
        this.sender = sender;
    }

    @KafkaListener(
            topics = "users",
            containerGroup = "notification-service"
    )
    public void listener(UserNotificationEvent notification) {

        sender.sendMessage(notification.email(), notification.operation().getMessage());

        logger.info("{}",  notification.operation());
        logger.info("{}",  notification);
    }
}