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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

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
    public ResumeDto createResume(Long userId, Long achievementId, ResumeDto resumeDto) {
        log.debug("Creating achievement for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Achievement achievement = achievementRepository.findByIdAndUserId(achievementId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение не найдено"));

        Resume resume = resumeMapper.toEntity(resumeDto);
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

        final String LINE = "_".repeat(80);

        List<Resume> resumes = resumeRepository.findByUserId(userId);

        if (resumes.isEmpty()) {
            return "Резюме не найдены.";
        }

        StringBuilder sb = new StringBuilder();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        String userName = user.getFirstName() + " " + user.getLastName();
        sb.append(userName).append("\n\n");

        long totalDays = 0;
        for (Resume resume : resumes) {
            Achievement achievement = resume.getAchievement();
            if (achievement.getDateStart() != null) {
                LocalDate endDate = achievement.getDateEnd() != null ? achievement.getDateEnd() : LocalDate.now();
                long days = ChronoUnit.DAYS.between(achievement.getDateStart(), endDate);
                totalDays += days;
            }
        }

        String totalExperience = formatTotalExperience(totalDays);
        sb.append("Общий опыт: ").append(totalExperience).append("\n\n");

        for (Resume resume : resumes) {
            Achievement achievement = resume.getAchievement();

            sb.append(LINE).append("\n");

            formatDate(achievement.getDateStart());
            String startDate;
            String endDate;
            String duration;

            if (achievement.getDateStart() == null) {
                startDate = "не указана";
                endDate = formatDate(achievement.getDateEnd());;
                duration = "";
            } else if (achievement.getDateEnd() == null) {
                startDate = formatDate(achievement.getDateStart());
                endDate = "по настоящее время";
                duration = "Текущее место работы";
            } else {
                startDate = formatDate(achievement.getDateStart());
                endDate = formatDate(achievement.getDateEnd());
                long daysBetween = ChronoUnit.DAYS.between(achievement.getDateStart(), achievement.getDateEnd());
                int months = (int) Math.round(daysBetween / 30.44);
                duration = formatMonthString(months);
            }

            String title = achievement.getTitle() != null ? achievement.getTitle() : "[без названия]";
            String description = achievement.getDescription() != null ? achievement.getDescription() : "[без описания]";

            sb.append(startDate).append(" — ").append(endDate).append("\n\n");
            if (!duration.isEmpty()) {
                sb.append(duration).append("\n\n");
            }
            sb.append(title).append("\n\n");

            String[] lines = description.split("\n");
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    sb.append("- ").append(trimmed).append("\n");
                }
            }
            sb.append("\n");
        }

        return sb.toString().trim();
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "не указана";
        return date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru")));
    }

    private String formatMonthString(int months) {
        if (months <= 0) return "";

        int lastDigit = months % 10;
        int lastTwoDigits = months % 100;

        String suffix = switch (lastDigit) {
            case 1 -> (lastTwoDigits == 11) ? "месяцев" : "месяц";
            case 2, 3, 4 -> (lastTwoDigits >= 12 && lastTwoDigits <= 14) ? "месяцев" : "месяца";
            default -> "месяцев";
        };

        return months + " " + suffix;
    }

    private String formatTotalExperience(long totalDays) {
        if (totalDays <= 0) return "0 месяцев";

        int totalMonths = (int) Math.round(totalDays / 30.44);
        int years = totalMonths / 12;
        int remainingMonths = totalMonths % 12;

        if (years == 0) {
            return formatMonthString(remainingMonths);
        } else if (remainingMonths == 0) {
            String yearSuffix = switch (years % 10) {
                case 1 -> (years == 11) ? "лет" : "год";
                case 2, 3, 4 -> (years >= 12 && years <= 14) ? "лет" : "года";
                default -> "лет";
            };
            return years + " " + yearSuffix;
        } else {
            String yearPart = switch (years % 10) {
                case 1 -> (years == 11) ? "лет" : "год";
                case 2, 3, 4 -> (years >= 12 && years <= 14) ? "лет" : "года";
                default -> "лет";
            };
            String monthPart = formatMonthString(remainingMonths);

            return years + " " + yearPart + " " + monthPart;
        }
    }
}