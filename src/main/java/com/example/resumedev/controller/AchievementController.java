package com.example.resumedev.controller;

import com.example.resumedev.dto.AchievementDto;
import com.example.resumedev.service.AchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Achievements", description = "API для работы с достижениями")
@CrossOrigin(origins = "http://localhost:5173")
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    @Operation(summary = "Получить достижения пользователя", description = "Возвращает список достижений с фильтрацией")
    public ResponseEntity<Page<AchievementDto>> getAchievements(
            @Parameter(description = "Айди пользователя") @RequestParam(required = false) Long userId,
            @Parameter(description = "Категория") @RequestParam(required = false) String category,
            @Parameter(description = "Поиск по слову") @RequestParam(required = false) String description,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size){

        log.info("Getting achievements for user: {}, category: {}", userId, category);

        Pageable pageable = PageRequest.of(page, size);
        Page<AchievementDto> achievements = achievementService.getUserAchievements(userId, category, description, pageable);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить достижение по ID", description = "Возвращает конкретное достижение")
    public ResponseEntity<AchievementDto> getAchievement(
            @Parameter(description = "ID достижения") @PathVariable Long id, Long userId) {

        log.info("Getting achievement: {} for user: {}", id, userId);

        AchievementDto achievement = achievementService.getAchievement(id, userId);
        return ResponseEntity.ok(achievement);
    }

    @PostMapping
    @Operation(summary = "Создать достижение", description = "Создает новое достижение")
    public ResponseEntity<AchievementDto> createAchievement(
            @Parameter(description = "Данные достижения") @Valid @RequestBody AchievementDto achievementDto) {

        log.info("Creating achievement for user: {}", achievementDto.getUser_id());

        AchievementDto createdAchievement = achievementService.createAchievement(achievementDto.getUser_id(), achievementDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAchievement);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить достижение", description = "Обновляет существующее достижение")
    public ResponseEntity<AchievementDto> updateAchievement(
            @Parameter(description = "ID достижения") @PathVariable Long id,
            @Parameter(description = "Обновленные данные") @Valid @RequestBody AchievementDto achievementDto) {

        log.info("Updating achievement: {} for user: {}", id, achievementDto.getUser_id());

        AchievementDto updatedAchievement = achievementService.updateAchievement(id, achievementDto.getUser_id(), achievementDto);
        return ResponseEntity.ok(updatedAchievement);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить достижение", description = "Удаляет достижение")
    public ResponseEntity<Void> deleteAchievement(
            @Parameter(description = "ID достижения") @PathVariable Long id,
          Long userId ) {

        log.info("Deleting achievement: {} for user: {}", id, userId);

        achievementService.deleteAchievement(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Получить количество достижений", description = "Возвращает общее количество достижений пользователя")
    public ResponseEntity<Integer> getAchievementsCount(Long userId) {
        log.info("Getting achievements count for user: {}", userId);

        int count = achievementService.getUserAchievementsCount(userId);
        return ResponseEntity.ok(count);
    }
}
