package com.example.resumedev.service;

import com.example.resumedev.dto.UserDto;

import java.util.Optional;

public interface UserService {
    UserDto getUserProfile(Long userId);

    UserDto createOrUpdateVkUser(
            Long vkId,
            String firstName,
            String lastName
    );

    UserDto updateUserProfile(Long userId, UserDto userDto);

    Optional<Integer> getLevel(Long userId);

    boolean existsByVkId(Long userId);
}
