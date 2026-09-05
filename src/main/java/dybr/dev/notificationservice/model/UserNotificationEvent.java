package dybr.dev.notificationservice.model;

public record UserNotificationEvent(
        Long userID,
        String email,
        OperationsOnUser operation
) {
}
