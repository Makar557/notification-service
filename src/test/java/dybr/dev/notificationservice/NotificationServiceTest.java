package dybr.dev.notificationservice;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import dybr.dev.notificationservice.model.OperationsOnUser;
import dybr.dev.notificationservice.model.UserNotificationEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class NotificationKafkaIntegrationTest {

    @Container
    static KafkaContainer kafka =
            new KafkaContainer(
                    DockerImageName.parse("apache/kafka:3.8.0")
            );

    private static final GreenMail greenMail =
            new GreenMail(ServerSetupTest.SMTP);

    @Autowired
    private KafkaTemplate<Long, UserNotificationEvent> kafkaTemplate;

    @BeforeAll
    static void setUp() {
        greenMail.start();
    }

    @AfterAll
    static void tearDown() {
        greenMail.stop();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {

        registry.add(
                "spring.kafka.bootstrap-servers",
                kafka::getBootstrapServers
        );

        registry.add(
                "spring.mail.host",
                () -> "localhost"
        );

        registry.add(
                "spring.mail.port",
                () -> 3025
        );

        registry.add(
                "spring.mail.username",
                () -> ""
        );

        registry.add(
                "spring.mail.password",
                () -> ""
        );

        registry.add(
                "spring.mail.properties.mail.smtp.auth",
                () -> false
        );

        registry.add(
                "spring.mail.properties.mail.smtp.starttls.enable",
                () -> false
        );
    }

    @Test
    void shouldSendEmailWhenUserCreated() throws Exception {

        greenMail.reset();

        UserNotificationEvent notification =
                new UserNotificationEvent(
                        1L,
                        "test@example.com",
                        OperationsOnUser.USER_CREATION
                );

        kafkaTemplate
                .send("users", 1L, notification)
                .get();

        assertTrue(
                greenMail.waitForIncomingEmail(10000, 1)
        );

        MimeMessage message =
                greenMail.getReceivedMessages()[0];

        assertEquals(
                "test@example.com",
                message.getRecipients(Message.RecipientType.TO)[0]
                        .toString()
        );

        assertEquals(
                "От Макара",
                message.getSubject()
        );

        assertEquals(
                "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.",
                message.getContent().toString().trim()
        );
    }

    @Test
    void shouldSendEmailWhenUserDeleted() throws Exception {

        greenMail.reset();

        UserNotificationEvent notification =
                new UserNotificationEvent(
                        2L,
                        "test@example.com",
                        OperationsOnUser.USER_DELETION
                );

        kafkaTemplate
                .send("users", 2L, notification)
                .get();

        assertTrue(
                greenMail.waitForIncomingEmail(10000, 1)
        );

        MimeMessage message =
                greenMail.getReceivedMessages()[0];

        assertEquals(
                "test@example.com",
                message.getRecipients(Message.RecipientType.TO)[0]
                        .toString()
        );

        assertEquals(
                "От Макара",
                message.getSubject()
        );

        assertEquals(
                "Здравствуйте! Ваш аккаунт был удалён.",
                message.getContent().toString().trim()
        );
    }
}