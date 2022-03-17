package com.justpickup.notificationservice.domain.notification.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.notificationservice.config.TestConfig;
import com.justpickup.notificationservice.domain.notification.dto.FindNotificationDto;
import com.justpickup.notificationservice.domain.notification.service.NotificationService;
import com.justpickup.notificationservice.domain.notification.web.NotificationController.PatchNotificationRequest;
import com.justpickup.notificationservice.global.dto.Code;
import com.justpickup.notificationservice.global.dto.Yn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "https://just-pickup.com", uriPort = 8000)
class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    NotificationService notificationService;

    private final String url = "/notification";

    @Test
    @DisplayName("[GET] 회원 고유번호로 알림 가져오기")
    void getNotificationByUserId() throws Exception {
        // GIVEN
        long userId = 1L;
        given(notificationService.findNotificationByUserId(userId))
                .willReturn(getNotificationByUserIdWillReturn(userId));

        // THEN
        ResultActions actions
                = mockMvc.perform(get("/notifications").header("user-id", String.valueOf(userId)));

        // WHEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andExpect(jsonPath("data").isNotEmpty())
                .andDo(print())
                .andDo(document("notification-get",
                        requestHeaders(
                                headerWithName("user-id").description("회원 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.notifications[*].id").description("알림 고유번호"),
                                fieldWithPath("data.notifications[*].message").description("알림 내용"),
                                fieldWithPath("data.notifications[*].title").description("알림 제목"),
                                fieldWithPath("data.notifications[*].read").description("알림 읽음 여부"),
                                fieldWithPath("data.notifications[*].time").description("알림 생성 시간 [YYYY-MM-DD HH:MM]")
                        )
                        )
                )
        ;
    }

    private List<FindNotificationDto> getNotificationByUserIdWillReturn(long userId) {
        List<FindNotificationDto> returnList = new ArrayList<>();
        for (long id = 1; id <= 5; id++) {
            returnList.add(FindNotificationDto.of(id, userId, id + "번 메시지 예시입니다.", "제목" + id, Yn.Y, LocalDateTime.now()));
        }
        for (long id = 6; id <= 10; id++) {
            returnList.add(FindNotificationDto.of(id, userId, id + "번 메시지 예시입니다.", "제목" + id, Yn.N, LocalDateTime.now()));
        }
        return returnList;
    }

    @Test
    @DisplayName("[API] 알림 수정")
    void patchNotification() throws Exception {
        // GIVEN
        long notificationId = 1L;
        PatchNotificationRequest request = new PatchNotificationRequest(true);
        String requestBody = objectMapper.writeValueAsString(request);

        // WHEN
        ResultActions actions = mockMvc.perform(
                patch(url + "/{notificationId}", String.valueOf(notificationId))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notification-patch",
                        pathParameters(
                                parameterWithName("notificationId").description("알림 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("read").description("읽음 여부")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("데이터")
                        )
                        ))
        ;
    }
}
