package app.servise;

import app.exceptions.UserNotificationInformationNotFound;
import app.model.EmailNotification;
import app.model.NotificationSetting;
import app.model.NotificationStatus;
import app.model.NotificationType;
import app.repository.EmailNotificationRepository;
import app.repository.NotificationSettingRepository;
import app.web.dto.NotificationPreferenceRequest;
import app.web.dto.NotificationRequest;
import app.web.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final MailSender mailSender;

    @Autowired
    public NotificationService(EmailNotificationRepository notificationRepository, NotificationSettingRepository notificationSettingRepository, MailSender mailSender) {
        this.emailNotificationRepository = notificationRepository;
        this.notificationSettingRepository = notificationSettingRepository;
        this.mailSender = mailSender;
    }

    public NotificationSetting upsertNotificationSetting(NotificationPreferenceRequest dto) {

        Optional<NotificationSetting> optionalUserNotification = notificationSettingRepository.findByUserId(dto.getUserId());

        if (optionalUserNotification.isPresent()) {
            NotificationSetting notification = optionalUserNotification.get();
            notification.setType(DtoMapper.fromNotificationTypeRequest(dto.getType()));
            notification.setEnabled(dto.isNotificationEnabled());
            notification.setContactInfo(dto.getContactInfo());
            notification.setUpdatedOn(LocalDateTime.now());
            return notificationSettingRepository.save(notification);
        }

        NotificationSetting notification = NotificationSetting.builder()
                .userId(dto.getUserId())
                .type(DtoMapper.fromNotificationTypeRequest(dto.getType()))
                .isEnabled(dto.isNotificationEnabled())
                .contactInfo(dto.getContactInfo())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        return notificationSettingRepository.save(notification);
    }

    public NotificationSetting getUserNotificationSetting(UUID userId) {
        NotificationSetting notificationSetting = notificationSettingRepository.findByUserId(userId).orElseThrow(UserNotificationInformationNotFound::new);
        log.info("Notification setting for user with id [%s] was not found.".formatted(userId));
        return notificationSetting;
    }

    public EmailNotification sendNotification(NotificationRequest notificationRequest) {

        UUID userId = notificationRequest.getUserId();
        NotificationSetting userNotificationSetting = getUserNotificationSetting(userId);

        if (!userNotificationSetting.isEnabled()) {
            throw new IllegalArgumentException("User with id [%s] don't want to receive notification".formatted(userId));
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userNotificationSetting.getContactInfo());
        message.setSubject(notificationRequest.getSubject());
        message.setText(notificationRequest.getBody());

        EmailNotification notification = EmailNotification.builder()
                .subject(notificationRequest.getSubject())
                .body(notificationRequest.getBody())
                .createdOn(LocalDateTime.now())
                .type(NotificationType.EMAIL)
                .userId(userId)
                .isDeleted(false)
                .build();

        try {
            mailSender.send(message);
            notification.setStatus(NotificationStatus.SUCCEEDED);
        } catch (Exception exception) {
            notification.setStatus(NotificationStatus.FAILED);
            log.info("There was an issue sending an email to [%s] due to {%s}".formatted(userNotificationSetting.getContactInfo(), exception.getMessage()));
        }

        return emailNotificationRepository.save(notification);
    }

    public List<EmailNotification> getNotificationHistory(UUID userId) {
        return emailNotificationRepository.findAllByUserIdAndIsDeleted(userId, false).orElseThrow(UserNotificationInformationNotFound::new);
    }

    public NotificationSetting changeNotificationSendingSetting(UUID userId, boolean enabled) {

        NotificationSetting notificationSetting = getUserNotificationSetting(userId);
        notificationSetting.setEnabled(enabled);
        return notificationSettingRepository.save(notificationSetting);
    }
}
