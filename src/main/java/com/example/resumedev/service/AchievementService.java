package com.example.resumedev.service;

import com.example.resumedev.dto.AchievementDto;
import com.example.resumedev.exception.ResourceNotFoundException;
import com.example.resumedev.mapper.AchievementMapper;
import com.example.resumedev.model.Achievement;
import com.example.resumedev.model.Award;
import com.example.resumedev.model.User;
import com.example.resumedev.repository.AchievementRepository;
import com.example.resumedev.repository.AwardRepository;
import com.example.resumedev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AchievementService {
    private final AchievementRepository achievementRepository;

    private final UserRepository userRepository;

    private final AchievementMapper achievementMapper;

    private final AwardRepository awardRepository;

    private final AwardService awardService;

    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<AchievementDto> getUserAchievements(Long userId, String category, Pageable pageable) {
        log.debug("Getting achievements for user ID: {}, category: {}", userId, category);

        Page<Achievement> achievements;

        if (category != null && !category.equals("all")) {
            achievements = achievementRepository.findByUserIdAndCategoryOrderByDateDesc(
                    userId, category, pageable);
        } else {
            achievements = achievementRepository.findByUserIdOrderByDateDesc(userId, pageable);
        }

        return achievements.map(achievementMapper::toDto);
    }

    @Transactional(readOnly = true)
    public AchievementDto getAchievement(Long achievementId, Long userId) {
        log.debug("Getting achievement ID: {} for user ID: {}", achievementId, userId);
        Achievement achievement = achievementRepository.findByIdAndUserId(achievementId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение не найдено"));
        return achievementMapper.toDto(achievement);
    }

    @CacheEvict(value = {"achievements", "users"}, allEntries = true)
    public AchievementDto createAchievement(Long userId, AchievementDto achievementDto) {
        log.debug("Creating achievement for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Achievement achievement = achievementMapper.toEntity(achievementDto);
        achievement.setUser(user);

        Achievement savedAchievement = achievementRepository.save(achievement);

        userService.createLevel(user);
        userRepository.save(user);

        Award award = awardService.getAward(user);
        if (award != null) {
            awardRepository.save(award);
            log.info("Award granted: {} for user ID: {}", award.getTitle(), userId);
        }

        log.info("Achievement created successfully with ID: {} for user ID: {}", savedAchievement.getId(), userId);
        return achievementMapper.toDto(savedAchievement);
    }

    @CacheEvict(value = {"achievements", "users"}, allEntries = true)
    public AchievementDto updateAchievement(Long achievementId, Long userId, AchievementDto achievementDto) {
        log.debug("Updating achievement ID: {} for user ID: {}", achievementId, userId);

        Achievement achievement = achievementRepository.findByIdAndUserId(achievementId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение не найдено"));

        achievement.setTitle(achievementDto.getTitle());
        achievement.setCategory(achievementDto.getCategory());
        achievement.setDate(achievementDto.getDate());
        achievement.setDescription(achievementDto.getDescription());

        Achievement savedAchievement = achievementRepository.save(achievement);
        log.info("Achievement updated successfully with ID: {} for user ID: {}", achievementId, userId);
        return achievementMapper.toDto(savedAchievement);
    }

    @CacheEvict(value = {"achievements", "users"}, allEntries = true)
    public void deleteAchievement(Long achievementId, Long userId) {
        log.debug("Deleting achievement ID: {} for user ID: {}", achievementId, userId);

        Achievement achievement = achievementRepository.findByIdAndUserId(achievementId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение не найдено"));

        achievementRepository.delete(achievement);
        log.info("Achievement deleted successfully with ID: {} for user ID: {}", achievementId, userId);
    }

    @Transactional(readOnly = true)
    public int getUserAchievementsCount(Long userId) {
        return achievementRepository.countAchievementsByUserId(userId);
    }


}
