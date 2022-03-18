package com.justpickup.userservice.domain.jwt.service;

import com.justpickup.userservice.domain.user.dto.OAuthAttributeDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.repository.CustomerRepository;
import com.justpickup.userservice.domain.user.service.UserServiceImpl;
import com.justpickup.userservice.global.utils.CookieProvider;
import com.justpickup.userservice.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceImpl userServiceImpl;
    private final CookieProvider cookieProvider;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        //OAuth 서비스 id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //OAuth 로그인 진행시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // OAuth2UserService
        OAuthAttributeDto attributeDto = OAuthAttributeDto.of(registrationId, userNameAttributeName,oAuth2User.getAttributes());

        Customer customer = saveCustomer(attributeDto);

        String userEmail = customer.getEmail();

        Collection<? extends GrantedAuthority> authorities = userServiceImpl.loadUserByUsername(userEmail).getAuthorities();

        return new DefaultOAuth2User(
                authorities
                , attributeDto.getAttributes()
                , attributeDto.getNameAttributeKey());

    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String userEmail = String.valueOf(((DefaultOAuth2User) authentication.getPrincipal()).getAttributes().get("email"));


        String refreshToken = jwtTokenProvider.createJwtRefreshToken();
        Long customerId = customerRepository.findByEmail(userEmail).get().getId();
        refreshTokenService.updateRefreshToken(customerId, jwtTokenProvider.getRefreshTokenId(refreshToken));

        // 쿠키 설정
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(refreshToken);

        Cookie cookie = cookieProvider.of(refreshTokenCookie);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.addCookie(cookie);

        // body 설정
        String accessToken = jwtTokenProvider.createJwtAccessToken(String.valueOf(customerId), request.getRequestURI(), authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        Date expiredTime = jwtTokenProvider.getExpiredTime(accessToken);

        response.sendRedirect("https://just-pickup.com:8080/auth?" +
                "accessToken="+accessToken+
                "&expiredTime="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime));

    }


    @Transactional
    public Customer saveCustomer(OAuthAttributeDto attributeDto){
        return customerRepository.save(
                customerRepository.findByEmail(attributeDto.getEmail())
                        .orElse(attributeDto.toEntity(attributeDto))
        );
    }

}
