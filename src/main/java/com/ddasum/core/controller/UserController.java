package com.ddasum.core.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.annotation.LoginRequired;
import com.ddasum.core.annotation.TraceLog;
import com.ddasum.domain.user.entity.User;
import com.ddasum.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@LoginRequired
@TraceLog
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserRepository userRepository;

    // 전체 사용자 목록 (페이징)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(Pageable pageable) {
        return successPage(userRepository.findAll(pageable));
    }

    // 단일 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::success)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 생성
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        // 비밀번호는 반드시 암호화해서 저장해야 하지만, 테스트용이므로 생략
        User saved = userRepository.save(user);
        return success(saved, "사용자가 생성되었습니다");
    }

    // 사용자 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(user.getUsername());
                    existing.setPassword(user.getPassword());
                    existing.setEmail(user.getEmail());
                    existing.setRole(user.getRole());
                    User updated = userRepository.save(existing);
                    return success(updated, "사용자 정보가 수정되었습니다");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return success(null, "사용자가 삭제되었습니다");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 