package com.example.resumedev.service;

import com.example.resumedev.dto.UserDto;
import com.example.resumedev.exception.ResourceNotFoundException;
import com.example.resumedev.mapper.UserMapper;
import com.example.resumedev.entity.User;
import com.example.resumedev.repository.AchievementRepository;
import com.example.resumedev.repository.AwardRepository;
import com.example.resumedev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final AchievementRepository achievementRepository;

    private final AwardRepository awardRepository;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserDto getUserProfile(Long userId) {
        log.debug("Getting user profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        UserDto userDto = userMapper.toDto(user);

        userDto.setAchievementsCount(achievementRepository.countAchievementsByUserId(userId));
        userDto.setAwardsCount(awardRepository.countAwardsByUserId(userId));
        userDto.setMostActiveCategory(achievementRepository.findMostActiveCategoryByUserId(userId).orElse(null));

        return userDto;
    }

    public UserDto createOrUpdateVkUser(Long vkId, String firstName, String lastName) {
        log.debug("Creating or updating VK user with VK ID: {}", vkId);

        User user = userRepository.findById(vkId).orElse(new User());

        user.setId(vkId);

        user.setFirstName(firstName);
        user.setLastName(lastName);

        User savedUser = userRepository.save(user);
        log.info("VK user created/updated successfully with VK ID: {}", vkId);

        return userMapper.toDto(savedUser);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "'vk_' + #vkId")
    public UserDto findByVkId(Long vkId) {
        log.debug("Finding user by VK ID: {}", vkId);

        User user = userRepository.findById(vkId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с VK ID " + vkId + " не найден"));

        return userMapper.toDto(user);
    }

    public Optional<Integer> getLevel(Long vkId){
        return userRepository.findLevelById(vkId);
    }

    public boolean existsByVkId(Long vkId) {
        return userRepository.existsById(vkId);
    }

    public void createLevel(User user){
        int awardCounter = achievementRepository.countAchievementsByUserId(user.getId());

        if (awardCounter <=9){ user.setLevel(1);}
        else if (awardCounter <=19){ user.setLevel(2);}
        else if (awardCounter <=29){ user.setLevel(3);}
        else if (awardCounter <=39){ user.setLevel(4);}
        else if (awardCounter <=49){ user.setLevel(5);}
        else if (awardCounter <=59){ user.setLevel(6);}
        else if (awardCounter <=69){ user.setLevel(7);}
        else if (awardCounter <=79){ user.setLevel(8);}
        else if (awardCounter <=89){ user.setLevel(9);}
        else { user.setLevel(10);}

    }
}
