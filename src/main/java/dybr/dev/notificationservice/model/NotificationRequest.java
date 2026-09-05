package dybr.dev.notificationservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(

        @Email
        @NotBlank
        String email,
        @NotNull
        OperationsOnUser operation
) {
}