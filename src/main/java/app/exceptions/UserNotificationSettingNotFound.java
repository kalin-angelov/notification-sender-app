package app.exceptions;

public class UserNotificationSettingNotFound extends RuntimeException {
    public UserNotificationSettingNotFound(String message) {
        super(message);
    }

    public UserNotificationSettingNotFound() {}
}
