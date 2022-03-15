package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.entity.StoreOwner;
import com.justpickup.userservice.domain.user.entity.User;
import com.justpickup.userservice.domain.user.exception.DuplicateUserEmail;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
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
    public void saveStoreOwner(StoreOwnerDto storeOwnerDto) {
        String email = storeOwnerDto.getEmail();
        boolean exists = userRepository.existsByEmail(email);

        if (exists) throw new DuplicateUserEmail(email + "은 중복된 이메일입니다.");

        String encode = passwordEncoder.encode(storeOwnerDto.getPassword());

        StoreOwner storeOwner = new StoreOwner(email, encode, storeOwnerDto.getName(),
                storeOwnerDto.getPhoneNumber(), storeOwnerDto.getBusinessNumber());

        userRepository.save(storeOwner);
    }

    @Override
    public List<CustomerDto> findCustomerByUserIds(List<Long> userIds) {
        return customerRepository.findAllById(userIds)
                .stream()
                .map(CustomerDto::new)
                .collect(Collectors.toList());
    }

}
