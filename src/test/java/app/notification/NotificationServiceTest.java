package app.notification;

import app.exceptions.UserNotificationSettingNotFound;
import app.model.NotificationSetting;
import app.model.NotificationType;
import app.repository.EmailNotificationRepository;
import app.repository.NotificationSettingRepository;
import app.servise.NotificationService;
import app.web.dto.NotificationPreferenceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;

import java.util.Optional;
import java.util.UUID;

import static app.TestBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private EmailNotificationRepository emailNotificationRepository;

    @Mock
    private NotificationSettingRepository notificationSettingRepository;

    @Mock
    private MailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void givenUpsertNotificationSetting_whenNotificationSettingsDosNotExist_expectToCreateNewNotificationSetting() {

        NotificationSetting notificationSetting = aRandomNotificationSetting();
        NotificationPreferenceRequest request = aRandomNotificationPreferenceRequest();

        when(notificationSettingRepository.findByUserId(request.getUserId())).thenReturn(Optional.empty());

        notificationService.upsertNotificationSetting(request);

        assertEquals(notificationSetting.getContactInfo(), request.getContactInfo());
        assertEquals(notificationSetting.getType(), NotificationType.EMAIL);
        assertEquals(notificationSetting.isEnabled(), request.isNotificationEnabled());
        verify(notificationSettingRepository, times(1)).save(any());
    }

    @Test
    void givenUpsertNotificationSetting_whenNotificationSettingsExist_expectToUpdateNotificationSetting() {

        NotificationPreferenceRequest request = aRandomNotificationPreferenceRequest();
        NotificationSetting notificationSetting = NotificationSetting.builder()
                .userId(request.getUserId())
                .contactInfo("DoNotSendMeNotificationHere@gmail.com")
                .build();

        when(notificationSettingRepository.findByUserId(request.getUserId())).thenReturn(Optional.of(notificationSetting));

        notificationService.upsertNotificationSetting(request);

        assertEquals(notificationSetting.getContactInfo(), request.getContactInfo());
        assertEquals(notificationSetting.getUserId(), request.getUserId());
        verify(notificationSettingRepository, times(1)).save(notificationSetting);
    }

    @Test
    void givenNotExistingNotificationSetting_whenGetUserNotificationSetting_thenExceptionIsThrown() {

        when(notificationSettingRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThrows(UserNotificationSettingNotFound.class, () -> notificationService.getUserNotificationSetting(UUID.randomUUID()));
    }

    @Test
    void givenNotificationSendingIsOn_whenChangeNotificationSendingToOff_thenTheNotificationSendingIsTurnOff() {

        UUID userId = UUID.randomUUID();
        NotificationSetting notificationSetting = NotificationSetting.builder()
                .userId(userId)
                .isEnabled(true)
                .build();

        when(notificationSettingRepository.findByUserId(userId)).thenReturn(Optional.of(notificationSetting));

        notificationService.changeNotificationSending(userId, false);

        assertFalse(notificationSetting.isEnabled());
        verify(notificationSettingRepository, times(1)).save(notificationSetting);
    }

    @Test
    void givenChangingNotificationSendingIsOff_whenChangeNotificationSendingToOn_thenTheNotificationSendingIsTurnOn() {

        UUID userId = UUID.randomUUID();
        NotificationSetting notificationSetting = NotificationSetting.builder()
                .userId(userId)
                .isEnabled(false)
                .build();

        when(notificationSettingRepository.findByUserId(userId)).thenReturn(Optional.of(notificationSetting));

        notificationService.changeNotificationSending(userId, true);

        assertTrue(notificationSetting.isEnabled());
        verify(notificationSettingRepository, times(1)).save(notificationSetting);
    }
}
