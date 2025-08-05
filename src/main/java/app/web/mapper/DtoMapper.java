package app.web.mapper;

import app.model.NotificationSetting;
import app.model.NotificationType;
import app.web.dto.NotificationPreferenceResponse;
import app.web.dto.NotificationTypeRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static NotificationType fromNotificationTypeRequest(NotificationTypeRequest notificationTypeRequest) {

        return switch (notificationTypeRequest) {
            case EMAIL -> NotificationType.EMAIL;
        };
    }

    public static NotificationPreferenceResponse fromNotification(NotificationSetting entity) {

        return NotificationPreferenceResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .contactInfo(entity.getContactInfo())
                .enabled(entity.isEnabled())
                .build();
    }
}
