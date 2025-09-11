package com.example.resumedev.mapper;

import com.example.resumedev.dto.UserDto;
import com.example.resumedev.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLevel(user.getLevel());

        return dto;
    }

}
