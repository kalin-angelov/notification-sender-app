package app.web;

import app.exceptions.UserNotificationInformationNotFound;
import app.model.EmailNotification;
import app.model.NotificationSetting;
import app.servise.NotificationService;
import app.web.dto.NotificationPreferenceRequest;
import app.web.dto.NotificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static app.TestBuilder.*;
import static org.hamcrest.Matchers.hasSize;
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

    @Test
    void getRequestToGetUserNotificationSettings_whenThereIsNoNotificationSettingForTheUser_thenAExceptionIsThrown() throws Exception {

        UUID userId = UUID.randomUUID();

        when(notificationService.getUserNotificationSetting(userId)).thenThrow(UserNotificationInformationNotFound.class);

        MockHttpServletRequestBuilder request = get("/api/v1/notifications/preferences")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(jsonPath("timeStamp").isNotEmpty());

        verify(notificationService, times(1)).getUserNotificationSetting(userId);
    }

    @Test
    void getRequestToGetUserNotificationSetting_happyPath() throws Exception{

        NotificationSetting notificationSetting = aRandomNotificationSetting();

        when(notificationService.getUserNotificationSetting(notificationSetting.getUserId())).thenReturn(notificationSetting);

        MockHttpServletRequestBuilder request = get("/api/v1/notifications/preferences")
                .param("userId", notificationSetting.getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("enabled").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty());

    }

    @Test
    void getRequestToGetNotificationHistory_whenTheUserIdIsNotValid_thenExceptionIsThrown() throws Exception{
        UUID userId = UUID.randomUUID();

        when(notificationService.getNotificationHistory(userId)).thenThrow(UserNotificationInformationNotFound.class);

        MockHttpServletRequestBuilder request = get("/api/v1/notifications")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(jsonPath("timeStamp").isNotEmpty());
    }

    @Test
    void getRequestToGetNotificationHistory_happyPath() throws Exception{
        UUID userId = UUID.randomUUID();

        List<EmailNotification> list = List.of(aRandomEmailNotification(), aRandomEmailNotification());

        when(notificationService.getNotificationHistory(userId)).thenReturn(list);

        MockHttpServletRequestBuilder request = get("/api/v1/notifications")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void postRequestToSendNotification_happyPath() throws Exception{

        when(notificationService.sendNotification(aRandomNotificationRequest())).thenReturn(aRandomEmailNotification());

        MockHttpServletRequestBuilder request = post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomNotificationRequest()));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("subject").isNotEmpty())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("createdOn").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty());
    }
}
