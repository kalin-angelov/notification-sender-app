package app.web.mapper;

import app.model.EmailNotification;
import app.model.NotificationSetting;
import app.model.NotificationType;
import app.web.dto.NotificationPreferenceResponse;
import app.web.dto.NotificationResponse;
import app.web.dto.NotificationTypeRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DtoMapper {

    public static NotificationType fromNotificationTypeRequest(NotificationTypeRequest notificationTypeRequest) {

        return switch (notificationTypeRequest) {
            case EMAIL -> NotificationType.EMAIL;
        };
    }

    public static NotificationPreferenceResponse fromNotificationSetting(NotificationSetting entity) {

        return NotificationPreferenceResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .contactInfo(entity.getContactInfo())
                .enabled(entity.isEnabled())
                .build();
    }

    public static NotificationResponse fromNotification(EmailNotification entity) {

        return NotificationResponse.builder()
                .subject(entity.getSubject())
                .createdOn(LocalDateTime.now())
                .status(entity.getStatus())
                .type(entity.getType())
                .build();
    }
}
