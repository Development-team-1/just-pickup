package com.justpickup.userservice.domain.jwt.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.OAuthAttributeDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.repository.CustomerRepository;
import com.justpickup.userservice.domain.user.repository.UserRepository;
import com.justpickup.userservice.domain.user.service.UserService;
import com.justpickup.userservice.domain.user.service.UserServiceImpl;
import com.justpickup.userservice.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final CustomerRepository customerRepository;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceImpl userServiceImpl;

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


        // TODO: 2022/02/16 Response에 token 담아 보내기

        String userEmail = customer.getEmail();


        Collection<? extends GrantedAuthority> authorities = userServiceImpl.loadUserByUsername(userEmail).getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String accessToken = jwtTokenProvider.createJwtAccessToken(userEmail, request.getRequestURI(), roles);
        String refreshToken = jwtTokenProvider.createJwtRefreshToken();

        refreshTokenService.updateRefreshToken(customer.getId(), jwtTokenProvider.getRefreshTokenId(refreshToken));

        response.setHeader("Access-token",accessToken);
        response.setHeader("refresh-token",refreshToken);



        return new DefaultOAuth2User(
                authorities
                , attributeDto.getAttributes()
                , attributeDto.getNameAttributeKey());

    }

    @Transactional
    public Customer saveCustomer(OAuthAttributeDto attributeDto){
        return customerRepository.save(
                customerRepository.findByEmail(attributeDto.getEmail())
                        .orElse(attributeDto.toEntity(attributeDto))
        );
    }

}
