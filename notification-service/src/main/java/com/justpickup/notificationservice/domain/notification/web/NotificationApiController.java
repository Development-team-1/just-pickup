package com.justpickup.notificationservice.domain.notification.web;

import com.justpickup.notificationservice.domain.notification.service.NotificationService;
import com.justpickup.notificationservice.global.dto.Result;
import com.justpickup.notificationservice.global.dto.Yn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/notification/counts")
    public ResponseEntity<Result> getNotificationCounts(@RequestHeader("user-id") String userIdHeader) {

        Long userId = Long.valueOf(userIdHeader);
        Yn readYn = Yn.N;
        Long counts = notificationService.findNotificationCounts(userId, readYn);

        return ResponseEntity.ok(Result.createSuccessResult(counts));
    }
}
