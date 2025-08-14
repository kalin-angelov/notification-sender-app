package app;

import app.model.NotificationSetting;
import app.model.NotificationType;
import app.web.dto.NotificationPreferenceRequest;
import app.web.dto.NotificationTypeRequest;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TestBuilder {

    private final UUID userId = UUID.randomUUID();

    public static NotificationSetting aRandomNotificationSetting() {

        return NotificationSetting.builder()
                .userId(userId)
                .type(NotificationType.EMAIL)
                .contactInfo("SendMyNotificationHere@gmail.com")
                .isEnabled(true)
                .build();
    }

    public static NotificationPreferenceRequest aRandomNotificationPreferenceRequest() {

        return NotificationPreferenceRequest.builder()
                .userId(userId)
                .contactInfo("SendMyNotificationHere@gmail.com")
                .type(NotificationTypeRequest.EMAIL)
                .notificationEnabled(true)
                .build();
    }
}
