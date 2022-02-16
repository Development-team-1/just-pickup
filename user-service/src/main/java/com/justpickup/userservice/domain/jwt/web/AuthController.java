package com.justpickup.userservice.domain.jwt.web;

import com.justpickup.userservice.domain.jwt.service.RefreshTokenServiceImpl;
import com.justpickup.userservice.domain.user.dto.JwtTokenDto;
import com.justpickup.userservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    @GetMapping("/refreshToken")
    public ResponseEntity<Result> refreshToken(@RequestHeader("X-AUTH-TOKEN") String accessToken,
                                               @RequestHeader("REFRESH-TOKEN") String refreshToken) {

        JwtTokenDto jwtTokenDto = refreshTokenServiceImpl.refreshJwtToken(accessToken, refreshToken);

        return ResponseEntity.ok(Result.createSuccessResult(new RefreshTokenResponse(jwtTokenDto)));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class RefreshTokenResponse {
        private String accessToken;
        private String refreshToken;

        public RefreshTokenResponse(JwtTokenDto jwtTokenDto) {
            this.accessToken = jwtTokenDto.getAccessToken();
            this.refreshToken = jwtTokenDto.getRefreshToken();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Result> logout(@RequestHeader("X-AUTH-TOKEN") String accessToken,
                                         @RequestHeader("REFRESH-TOKEN") String refreshToken) {
        log.info("########### logout!");
        // TODO: 2022/02/16 logout 구현 필요
        return ResponseEntity.ok(Result.createSuccessResult("success"));
    }
}
