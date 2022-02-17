package com.justpickup.userservice.domain.jwt.web;

import com.justpickup.userservice.domain.jwt.service.RefreshTokenServiceImpl;
import com.justpickup.userservice.domain.user.dto.JwtTokenDto;
import com.justpickup.userservice.global.dto.Result;
import com.justpickup.userservice.global.utils.CookieProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.HttpHeaders;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final CookieProvider cookieProvider;

    @GetMapping("/refreshToken")
    public ResponseEntity<Result> refreshToken(@RequestHeader("X-AUTH-TOKEN") String accessToken,
                                               @RequestHeader("REFRESH-TOKEN") String refreshToken) {

        JwtTokenDto jwtTokenDto = refreshTokenServiceImpl.refreshJwtToken(accessToken, refreshToken);

        ResponseCookie responseCookie = cookieProvider.createRefreshTokenCookie(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(Result.createSuccessResult(new RefreshTokenResponse(jwtTokenDto)));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class RefreshTokenResponse {
        private String accessToken;

        public RefreshTokenResponse(JwtTokenDto jwtTokenDto) {
            this.accessToken = jwtTokenDto.getAccessToken();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Result> logout(@RequestHeader("X-AUTH-TOKEN") String accessToken,
                                         @RequestHeader("REFRESH-TOKEN") String refreshToken) {

        refreshTokenServiceImpl.logoutToken(accessToken);

        ResponseCookie refreshCookie = cookieProvider.removeRefreshTokenCookie();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Result.createErrorResult(""));
    }
}
