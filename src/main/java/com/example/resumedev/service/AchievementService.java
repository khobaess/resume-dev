package com.example.resumedev.service;

import com.example.resumedev.dto.AchievementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AchievementService {
    Page<AchievementDto> getUserAchievements(
        Long userId,
        String category,
        String description,
        Pageable pageable
    );

    AchievementDto getAchievement(Long achievementId, Long userId);

    AchievementDto createAchievement(Long userId, AchievementDto achievementDto);

    AchievementDto updateAchievement(
            Long achievementId,
            Long userId,
            AchievementDto achievementDto
    );

    void deleteAchievement(Long achievementId, Long userId);

    int getUserAchievementsCount(Long userId);
}
