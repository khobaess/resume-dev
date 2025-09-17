package com.example.resumedev.mapper;

import com.example.resumedev.dto.AchievementDto;
import com.example.resumedev.entity.Achievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {

    public AchievementDto toDto(
            Achievement achievement
    ) {
        if (achievement == null) return null;

        AchievementDto dto = new AchievementDto();
        dto.setTitle(achievement.getTitle());
        dto.setCategory(achievement.getCategory());
        dto.setDateStart(achievement.getDateStart());
        dto.setDateEnd(achievement.getDateEnd());
        dto.setDescription(achievement.getDescription());
        dto.setUserId(dto.getUserId());
        return dto;
    }

    public Achievement toEntity(
            AchievementDto dto
    ) {
        if (dto == null) return null;

        Achievement achievement = new Achievement();
        achievement.setTitle(dto.getTitle());
        achievement.setCategory(dto.getCategory());
        achievement.setDateStart(dto.getDateStart());
        achievement.setDateEnd(dto.getDateEnd());
        achievement.setDescription(dto.getDescription());

        return achievement;
    }
}