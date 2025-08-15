package app.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private String subject;

    @NotNull
    private String body;
}
