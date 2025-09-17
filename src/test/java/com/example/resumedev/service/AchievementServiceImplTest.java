package com.example.resumedev.service;

import com.example.resumedev.dto.AchievementDto;
import com.example.resumedev.entity.Achievement;
import com.example.resumedev.entity.Award;
import com.example.resumedev.entity.User;
import com.example.resumedev.exception.ResourceNotFoundException;
import com.example.resumedev.mapper.AchievementMapper;
import com.example.resumedev.repository.AchievementRepository;
import com.example.resumedev.repository.AwardRepository;
import com.example.resumedev.repository.UserRepository;
import com.example.resumedev.service.impl.AchievementServiceImpl;
import com.example.resumedev.service.impl.AwardServiceImpl;
import com.example.resumedev.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceImplTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private AwardRepository awardRepository;

    @Mock
    private AwardServiceImpl awardServiceImpl;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AchievementServiceImpl achievementServiceImpl;

    @Test
    void getAchievement_ShouldReturnDto_WhenAchievementExists() {

        Long achievementId = 1L;
        Long userId = 10L;

        Achievement mockAchievement = new Achievement();
        mockAchievement.setId(achievementId);
        mockAchievement.setTitle("Тестовое достижение");
        mockAchievement.setUser(new User());

        when(achievementRepository.findByIdAndUserId(achievementId, userId))
                .thenReturn(java.util.Optional.of(mockAchievement));

        AchievementDto mockDto = new AchievementDto();
        mockDto.setTitle("Тестовое достижение");
        when(achievementMapper.toDto(mockAchievement)).thenReturn(mockDto);

        AchievementDto result = achievementServiceImpl.getAchievement(achievementId, userId);

        assertNotNull(result);
        assertEquals("Тестовое достижение", result.getTitle());

        verify(achievementRepository).findByIdAndUserId(achievementId, userId);
        verify(achievementMapper).toDto(mockAchievement);
    }

    @Test
    void getAchievement_ShouldThrowException_WhenAchievementNotFound() {
        Long achievementId = 999L;
        Long userId = 10L;

        when(achievementRepository.findByIdAndUserId(achievementId, userId))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> achievementServiceImpl.getAchievement(achievementId, userId)
        );

        assertEquals("Достижение не найдено", exception.getMessage());

        verify(achievementRepository).findByIdAndUserId(achievementId, userId);
    }

    @Test
    void createAchievement_ShouldSaveAndReturnDto_WhenUserExists() {

        Long userId = 1L;
        Long achievementId = 1L;
        Long awardId = 1L;
        LocalDate now = LocalDate.now();

        AchievementDto dto = new AchievementDto();
        dto.setTitle("Новое достижение");
        dto.setCategory("Разработка");
        dto.setDateStart(now);
        dto.setDateEnd(now);
        dto.setDescription("description");
        dto.setUserId(userId);

        User user = new User();
        user.setId(userId);
        user.setFirstName("Qwerty");
        user.setLastName("Wasn");

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setUser(user);
        achievement.setDateStart(now);
        achievement.setDateEnd(now);
        achievement.setDescription("description");
        achievement.setTitle("Новое достижение");


        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(achievementMapper.toEntity(dto)).thenReturn(achievement);
        when(achievementMapper.toDto(achievement)).thenReturn(dto);
        when(achievementRepository.save(any(Achievement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        doNothing().when(userService).createLevel(user);

        Award award = new Award();
        award.setId(awardId);
        award.setUser(user);
        award.setTitle("Награда за первое достижение");
        when(awardServiceImpl.getAward(user)).thenReturn(award);
        when(awardRepository.save(any(Award.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AchievementDto result = achievementServiceImpl.createAchievement(userId, dto);

        assertNotNull(result);
        assertEquals("Новое достижение", result.getTitle());

        verify(userRepository).findById(userId);
        verify(achievementMapper).toEntity(dto);
        verify(achievementRepository).save(achievement);
        verify(userService).createLevel(user);
        verify(awardServiceImpl).getAward(user);
        verify(awardRepository).save(award);
    }
}
