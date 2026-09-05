package dybr.dev.notificationservice.model;

public enum OperationsOnUser {

    USER_DELETION("Здравствуйте! Ваш аккаунт был удалён."),
    USER_CREATION("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");

    String message;

    private OperationsOnUser(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}