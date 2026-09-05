package dybr.dev.notificationservice.kafka;

import dybr.dev.notificationservice.model.NotificationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(
            @Valid @RequestBody NotificationRequest request
    ) {

        notificationService.sendMessage(
                request.email(),
                request.operation().getMessage()
        );

        return ResponseEntity.ok().build();
    }
}