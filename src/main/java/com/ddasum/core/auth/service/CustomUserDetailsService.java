package com.ddasum.core.auth.service;

import com.ddasum.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // 이메일로 먼저 찾고, 없으면 username으로 찾기
        return userRepository.findByEmail(usernameOrEmail)
            .or(() -> userRepository.findByUsername(usernameOrEmail))
            .map(user -> org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // 인증 주체를 email로 통일
                .password(user.getPassword())
                .roles(user.getRole())
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + usernameOrEmail));
    }
} 