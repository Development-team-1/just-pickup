package com.justpickup.userservice.domain.user.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.dto.OAuthAttributeDto;
import com.justpickup.userservice.domain.user.dto.OAuthAttributeDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.entity.StoreOwner;
import com.justpickup.userservice.domain.user.entity.User;
import com.justpickup.userservice.domain.user.exception.NotExistUserException;
import com.justpickup.userservice.domain.user.repository.CustomerRepository;
import lombok.*;
import com.justpickup.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final HttpServletResponse response;
    private final Environment env;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getDtype()));
        return new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getPassword(), authorities);
    }

    @Override
    public CustomerDto findCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new NotExistUserException("존재하지 않는 사용자 입니다."));

        return new CustomerDto(customer);
    }

    @Override
    @Transactional
    public StoreOwner saveStoreOwner(StoreOwnerDto storeOwnerDto) {
        String encode = passwordEncoder.encode(storeOwnerDto.getPassword());

        StoreOwner storeOwner = new StoreOwner(storeOwnerDto.getEmail(), encode, storeOwnerDto.getName(),
                storeOwnerDto.getPhoneNumber(), storeOwnerDto.getBusinessNumber(), storeOwnerDto.getRefreshTokenId());

        return userRepository.save(storeOwner);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthResponse{
        private String id;
        private String name;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class AuthRequest {
        private String code;
        private String client_id;
        private String client_secret;
    }




    @Override
    @Transactional(readOnly = false)
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

        Customer customer = customerRepository.save(
                customerRepository.findByEmail(attributeDto.getEmail())
                        .orElse(attributeDto.toEntity(attributeDto))
        );

        // TODO: 2022/02/16 Response에 token 담아 보내기

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(customer.getRole().getKey()))
                , attributeDto.getAttributes()
                , attributeDto.getNameAttributeKey());

    }

    @Getter
    public static class UserPayload implements Serializable {
        private String name;
        private String email;

        public UserPayload(Customer user){
            this.name = user.getName();
            this.email = user.getEmail();
        }
    }
}
