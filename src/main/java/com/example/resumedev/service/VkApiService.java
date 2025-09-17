package com.example.resumedev.service;

import com.example.resumedev.dto.UserDto;

public interface VkApiService {
    boolean isValidVkUser(Long vkId);

    UserDto getUserInfo(Long userId);
}
