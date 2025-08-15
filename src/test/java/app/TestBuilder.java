package app;

import app.model.EmailNotification;
import app.model.NotificationSetting;
import app.model.NotificationStatus;
import app.model.NotificationType;
import app.web.dto.NotificationPreferenceRequest;
import app.web.dto.NotificationRequest;
import app.web.dto.NotificationResponse;
import app.web.dto.NotificationTypeRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
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

    public static NotificationResponse aRandomNotificationResponse() {

        return NotificationResponse.builder()
                .subject("This is my subject")
                .status(NotificationStatus.SUCCEEDED)
                .createdOn(LocalDateTime.now())
                .type(NotificationType.EMAIL)
                .build();
    }

    public static EmailNotification aRandomEmailNotification() {

        return EmailNotification.builder()
                .userId(userId)
                .subject("This is my subject")
                .body("This is my body")
                .createdOn(LocalDateTime.now())
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.SUCCEEDED)
                .isDeleted(false)
                .build();
    }

    public static NotificationRequest aRandomNotificationRequest() {

        return NotificationRequest.builder()
                .userId(userId)
                .subject("This is my subject")
                .body("This is my body")
                .build();
    }
}
