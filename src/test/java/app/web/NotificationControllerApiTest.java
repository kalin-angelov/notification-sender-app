package app.web;

import app.servise.NotificationService;
import app.web.NotificationController;
import app.web.dto.NotificationPreferenceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static app.TestBuilder.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postRequestToUpsertNotificationPreference_happyPath() throws Exception {

        NotificationPreferenceRequest notificationPreferenceRequest = aRandomNotificationPreferenceRequest();

        when(notificationService.upsertNotificationSetting(notificationPreferenceRequest)).thenReturn(aRandomNotificationSetting());

        MockHttpServletRequestBuilder request = post("/api/v1/notifications/preferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomNotificationPreferenceRequest()));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("contactInfo").isNotEmpty());

        verify(notificationService, times(1)).upsertNotificationSetting(notificationPreferenceRequest);

    }
}
