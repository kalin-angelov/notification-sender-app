package app.exceptions;

public class UserNotificationInformationNotFound extends RuntimeException {
    public UserNotificationInformationNotFound(String message) {
        super(message);
    }

    public UserNotificationInformationNotFound() {}
}
