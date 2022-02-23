package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.entity.StoreOwner;
import com.justpickup.userservice.domain.user.entity.User;
import com.justpickup.userservice.domain.user.exception.NotExistUserException;
import com.justpickup.userservice.domain.user.repository.CustomerRepository;
import com.justpickup.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



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
                storeOwnerDto.getPhoneNumber(), storeOwnerDto.getBusinessNumber());

        return userRepository.save(storeOwner);
    }
}
