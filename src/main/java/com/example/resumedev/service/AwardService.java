package com.example.resumedev.service;

import com.example.resumedev.dto.AwardDto;
import com.example.resumedev.mapper.AwardMapper;
import com.example.resumedev.model.Award;
import com.example.resumedev.model.User;
import com.example.resumedev.repository.AchievementRepository;
import com.example.resumedev.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AwardService {
    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;
    private final AchievementRepository achievementRepository;

    public List<AwardDto> getUserAwards(Long userId){
        log.debug("Getting achievements for user ID: {}", userId);

        List<Award> awards = awardRepository.getByUserId(userId);
        return awards.stream().map(awardMapper::toDto).collect(Collectors.toList());
    }

    public Award getAward(User user){

        int awardCounter = achievementRepository.countAchievementsByUserId(user.getVkId());
        Award award = new Award();
        award.setUser(user);
        switch (awardCounter){
            case 1:
                award.setTitle("Награда за первое добавленное достижение");
                break;

            case 10:
                award.setTitle("Награда за 10 достижений");
                break;

            case 20:
                award.setTitle("Награда за 20 достижений");
                break;

            case 50:
                award.setTitle("Награда за 50 достижений");
                break;

            case 100:
                award.setTitle("Награда за 100 достижений");
                break;

            default:
                return null;
        }

        return award;
    }
}
