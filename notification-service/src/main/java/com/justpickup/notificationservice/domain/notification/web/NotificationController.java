package com.justpickup.notificationservice.domain.notification.web;

import com.justpickup.notificationservice.domain.notification.dto.FindNotificationDto;
import com.justpickup.notificationservice.domain.notification.dto.UpdateNotificationDto;
import com.justpickup.notificationservice.domain.notification.service.NotificationService;
import com.justpickup.notificationservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<Result> getNotificationByUserId(@RequestHeader("user-id") String userIdHeader) {
        Long userId = Long.valueOf(userIdHeader);

        List<FindNotificationDto> notifications = notificationService.findNotificationByUserId(userId);

        GetNotificationResponse response = new GetNotificationResponse(notifications);
        return ResponseEntity.ok(Result.createSuccessResult(response));
    }

    @Data @NoArgsConstructor
    static class GetNotificationResponse {
        private List<_Notification> notifications;

        public GetNotificationResponse(List<FindNotificationDto> notifications) {
            this.notifications =
                    notifications.stream().map(_Notification::new).collect(Collectors.toList());
        }

        @Data
        static class _Notification {
            private Long id;
            private String message;
            private String title;
            private boolean read;
            private String time;

            public _Notification(FindNotificationDto dto) {
                this.id = dto.getId();
                this.message = dto.getMessage();
                this.title = dto.getTitle();
                this.read = dto.getReadYn().isY();
                this.time = dto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
        }
    }

    @PatchMapping("/notification/{notificationId}")
    public ResponseEntity<Result> patchNotification(@PathVariable("notificationId") Long notificationId,
                                                    @RequestBody PatchNotificationRequest notificationRequest) {

        UpdateNotificationDto dto = UpdateNotificationDto.of(notificationId, notificationRequest.isRead());

        notificationService.updateNotification(dto);

        return ResponseEntity.ok(Result.createSuccessResult(null));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class PatchNotificationRequest {
        private boolean read;
    }
}
