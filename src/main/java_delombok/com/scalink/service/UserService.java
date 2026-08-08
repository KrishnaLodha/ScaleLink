package com.scalink.service;

import com.scalink.dto.response.UserResponse;
import com.scalink.entity.User;
import com.scalink.exception.ResourceNotFoundException;
import com.scalink.repository.UserRepository;
import com.scalink.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        SecurityUser principal = getAuthenticatedUser();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserResponse.fromEntity(user);
    }

    public SecurityUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser;
        }
        throw new ResourceNotFoundException("Authenticated user not found");
    }
}
