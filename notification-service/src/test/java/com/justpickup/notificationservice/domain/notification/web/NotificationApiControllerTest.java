package com.justpickup.notificationservice.domain.notification.web;

import com.justpickup.notificationservice.config.TestConfig;
import com.justpickup.notificationservice.domain.notification.service.NotificationService;
import com.justpickup.notificationservice.global.dto.Yn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "https://just-pickup.com", uriPort = 8000)
class NotificationApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NotificationService notificationService;

    private final String url = "/api/";

    @Test
    @DisplayName("[API] [GET] 읽지 않은 알림 개수 가져오기")
    void getNotificationCounts() throws Exception {
        // GIVEN
        Long userId = 1L;
        given(notificationService.findNotificationCounts(userId, Yn.N))
                .willReturn(10L);

        // THEN
        ResultActions actions = mockMvc.perform(get(url + "/notification/counts")
                .header("user-id", String.valueOf(userId)));

        // WHEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("api-get-notification-counts",
                                requestHeaders(
                                        headerWithName("user-id").description("회원 고유번호")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과코드 SUCCESS/ERROR"),
                                        fieldWithPath("message").description("메시지"),
                                        fieldWithPath("data").description("알림 개수")
                                )
                        )
                )
        ;
    }

}