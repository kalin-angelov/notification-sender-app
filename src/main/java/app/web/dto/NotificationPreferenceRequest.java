package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationPreferenceRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private NotificationTypeRequest type;

    private boolean notificationEnabled;

    @NotNull
    @NotBlank
    private String contactInfo;
}
