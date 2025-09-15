package com.example.resumedev.mapper;

import com.example.resumedev.dto.UserDto;
import com.example.resumedev.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLevel(user.getLevel());
        dto.setLastName(user.getLastName());
        dto.setDescription(user.getDescription());
        dto.setCity(user.getCity());
        dto.setJobTitle(user.getJobTitle());
        dto.setBirthDate(user.getBirthDate());

        return dto;
    }

    public User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDescription(dto.getDescription());
        user.setCity(dto.getCity());
        user.setJobTitle(dto.getJobTitle());
        user.setBirthDate(dto.getBirthDate());

        return user;
    }
}
