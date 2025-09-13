package com.example.resumedev.service;

import com.example.resumedev.dto.ResumeDto;
import com.example.resumedev.entity.Achievement;
import com.example.resumedev.entity.Resume;
import com.example.resumedev.entity.User;
import com.example.resumedev.exception.ResourceNotFoundException;
import com.example.resumedev.mapper.ResumeMapper;
import com.example.resumedev.repository.AchievementRepository;
import com.example.resumedev.repository.ResumeRepository;
import com.example.resumedev.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeMapper resumeMapper;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;


    public ResumeDto getResume(Long id, Long userId, Long achievementId) {
        log.debug("Creating resume for resume ID: {}", id );

        Resume resume = resumeRepository.findByIdAndUserIdAndAchievementId(id, userId, achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Резюме не найдено"));

        return resumeMapper.toDto(resume);
    }

    public Page<ResumeDto> getResumes(Long userId,  Pageable pageable) {
        log.debug("Getting resumes for user ID: {}", userId );

        Page<Resume> resumes;
        resumes = resumeRepository.findByUserId(userId, pageable);

        return resumes.map(resumeMapper::toDto);
    }

    @Transactional
    public ResumeDto createResume(Long userId, Long achievementId, ResumeDto achievementDto) {
        log.debug("Creating achievement for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Achievement achievement = achievementRepository.findByIdAndUserId(achievementId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение не найдено"));

        Resume resume = resumeMapper.toEntity(achievementDto);
        resume.setUser(user);
        resume.setAchievement(achievement);

        resumeRepository.save(resume);

        log.info("Resume created successfully with ID: {}  for user ID: {}", resume.getId(), userId);
        return resumeMapper.toDto(resume);
    }

    public void deleteResume(Long id, Long userId, Long achievementId) {
        log.debug("Deleting achievement for user ID: {}", id);

        Resume resume = resumeRepository.findByIdAndUserIdAndAchievementId(id, userId, achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение не найдено"));

        resumeRepository.delete(resume);
        log.info("Resume deleted successfully with ID: {} for user ID: {} for achievement ID: {}",
                id, userId, achievementId);

    }

    public String resumeGenerateTxt(Long userId){
        List<Resume> resumes = resumeRepository.findByUserId(userId);

        StringBuilder sb = new StringBuilder();

        for (Resume resume : resumes) {
            String line = String.format(
                    "ID: %d, Достижение: %s, Пользователь: %s%n",
                    resume.getId(),
                    resume.getAchievement(),
                    resume.getUser()
            );
            sb.append(line);
        }

        return sb.toString();
    }
}