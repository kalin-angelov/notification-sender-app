package app.servise;

import app.exceptions.UserNotificationSettingNotFound;
import app.model.NotificationSetting;
import app.repository.NotificationRepository;
import app.web.dto.NotificationPreferenceRequest;
import app.web.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationSetting upsertNotification(NotificationPreferenceRequest dto) {

        Optional<NotificationSetting> optionalUserNotification = notificationRepository.findByUserId(dto.getUserId());

        if (optionalUserNotification.isPresent()) {
            NotificationSetting notification = optionalUserNotification.get();
            notification.setType(DtoMapper.fromNotificationTypeRequest(dto.getType()));
            notification.setEnabled(dto.isNotificationEnabled());
            notification.setContactInfo(dto.getContactInfo());
            notification.setUpdatedOn(LocalDateTime.now());
            return notificationRepository.save(notification);
        }

        NotificationSetting notification = NotificationSetting.builder()
                .userId(dto.getUserId())
                .type(DtoMapper.fromNotificationTypeRequest(dto.getType()))
                .isEnabled(dto.isNotificationEnabled())
                .contactInfo(dto.getContactInfo())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }

    public NotificationSetting getUserNotification(UUID userId) {
        NotificationSetting notificationSetting = notificationRepository.findByUserId(userId).orElseThrow(UserNotificationSettingNotFound::new);
        log.info("Notification setting for user with id [%s] was not found.".formatted(userId));
        return notificationSetting;
    }
}
