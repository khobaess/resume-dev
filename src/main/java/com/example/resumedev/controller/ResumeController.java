package com.example.resumedev.controller;

import com.example.resumedev.dto.AchievementDto;
import com.example.resumedev.dto.ResumeDto;
import com.example.resumedev.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Resume", description = "API для работы с резюме")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить резюме по ID", description = "Возвращает конкретное резюме")
    public ResponseEntity<ResumeDto> getResume(
            @Parameter(description = "ID резюме") @PathVariable Long id, Long userId, Long achievementId)
    {
        log.info("Getting resume: {} for user: {}", id, userId);


        ResumeDto resume = resumeService.getResume(id, userId, achievementId);

        return ResponseEntity.ok(resume);
    }

    @PostMapping
    @Operation(summary = "Создать резюме", description = "Создает новое резюме")
    public ResponseEntity<ResumeDto> createResume( @Valid @RequestBody ResumeDto resumeDto) {

        log.info("Creating achievement for user: {}", resumeDto.getUser_id());

        ResumeDto createdResume = resumeService.createResume(resumeDto.getUser_id(), resumeDto.getAchievement_id(), resumeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResume);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить резюме", description = "Удаляет резюме")
    public ResponseEntity<Void> deleteResume(
            @Parameter(description = "ID достижения") @PathVariable Long id,
            Long userId, Long achievementId ) {

        log.info("Deleting achievement: {} for user: {} for achievement: {}", id, userId,  achievementId);

        resumeService.deleteResume(id, userId, achievementId );
        return ResponseEntity.noContent().build();
    }
}
