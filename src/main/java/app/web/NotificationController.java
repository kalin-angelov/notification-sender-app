package app.web;

import app.model.NotificationSetting;
import app.servise.NotificationService;
import app.web.dto.NotificationPreferenceRequest;
import app.web.dto.NotificationPreferenceResponse;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> upsertNotificationPreference (@RequestBody NotificationPreferenceRequest notificationPreference) {

        NotificationSetting notification = notificationService.upsertNotification(notificationPreference);
        NotificationPreferenceResponse response = DtoMapper.fromNotification(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> getUserNotification(@RequestParam (name = "userId")UUID userId) {

        NotificationSetting notification = notificationService.getUserNotification(userId);
        NotificationPreferenceResponse response = DtoMapper.fromNotification(notification);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
