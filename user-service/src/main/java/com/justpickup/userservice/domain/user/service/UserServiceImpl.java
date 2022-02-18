package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.jwt.utils.JwtTokenProvider;
import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.OAuthAttributeDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.entity.StoreOwner;
import com.justpickup.userservice.domain.user.entity.User;
import com.justpickup.userservice.domain.user.exception.NotExistUserException;
import com.justpickup.userservice.domain.user.repository.CustomerRepository;
import com.justpickup.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;



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


        Collection<? extends GrantedAuthority> authorities = loadUserByUsername(userEmail).getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String accessToken = jwtTokenProvider.createJwtAccessToken(userEmail, request.getRequestURI(), roles);
        String refreshToken = jwtTokenProvider.createJwtRefreshToken();

        updateRefreshToken(customer.getId(), jwtTokenProvider.getRefreshTokenId(refreshToken));

        customer.changeRefreshToken(refreshToken);

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


    @Transactional
    @Override
    public void updateRefreshToken(Long id, String refreshToken) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotExistUserException("사용자 고유번호 : " + id + "는 없는 사용자입니다."));

        user.changeRefreshToken(refreshToken);
    }

}
