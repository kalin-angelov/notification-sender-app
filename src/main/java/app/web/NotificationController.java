package app.web;

import app.model.EmailNotification;
import app.model.NotificationSetting;
import app.servise.NotificationService;
import app.web.dto.NotificationPreferenceRequest;
import app.web.dto.NotificationPreferenceResponse;
import app.web.dto.NotificationRequest;
import app.web.dto.NotificationResponse;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> upsertNotificationPreference (@RequestBody NotificationPreferenceRequest notificationPreference) {

        NotificationSetting notificationSetting = notificationService.upsertNotificationSetting(notificationPreference);
        NotificationPreferenceResponse response = DtoMapper.fromNotificationSetting(notificationSetting);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> getUserNotification(@RequestParam (name = "userId")UUID userId) {

        NotificationSetting notificationSetting = notificationService.getUserNotificationSetting(userId);
        NotificationPreferenceResponse response = DtoMapper.fromNotificationSetting(notificationSetting);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest notificationRequest) {

        EmailNotification notification = notificationService.sendNotification(notificationRequest);
        NotificationResponse response = DtoMapper.fromNotification(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotificationHistory(@RequestParam(name = "userId") UUID userId) {

        List<NotificationResponse> response = notificationService.getNotificationHistory(userId)
                .stream().map(DtoMapper::fromNotification).toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
