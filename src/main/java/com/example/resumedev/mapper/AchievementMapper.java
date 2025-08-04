package com.example.resumedev.mapper;

import com.example.resumedev.dto.AchievementDto;
import com.example.resumedev.model.Achievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {

    public AchievementDto toDto(Achievement achievement) {
        if (achievement == null) return null;

        AchievementDto dto = new AchievementDto();
        dto.setId(achievement.getId());
        dto.setTitle(achievement.getTitle());
        dto.setCategory(achievement.getCategory());
        dto.setDate(achievement.getDate());
        dto.setDescription(achievement.getDescription());
        dto.setUser_id(dto.getUser_id());
        return dto;
    }

    public Achievement toEntity(AchievementDto dto) {
        if (dto == null) return null;

        Achievement achievement = new Achievement();
        achievement.setId(dto.getId());
        achievement.setTitle(dto.getTitle());
        achievement.setCategory(dto.getCategory());
        achievement.setDate(dto.getDate());
        achievement.setDescription(dto.getDescription());
        dto.setUser_id(dto.getUser_id());

        return achievement;
    }
}