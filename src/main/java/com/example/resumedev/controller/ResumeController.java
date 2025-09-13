package com.example.resumedev.controller;

import com.example.resumedev.dto.ResumeDto;
import com.example.resumedev.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Resume", description = "API для работы с резюме")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "Экспорт резюме", description = "Возвращает резюме пользователя формата txt")
    @GetMapping("/export-txt")
    public ResponseEntity<InputStreamResource> exportToTxt(Long userId) throws Exception {

        String txtContent = resumeService.resumeGenerateTxt(userId);

        ByteArrayInputStream bis = new ByteArrayInputStream(txtContent.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=resume.txt");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(new InputStreamResource(bis));
    }

    @GetMapping
    @Operation(summary = "Получить все резюме пользователя", description = "Возвращает список резюме")
    public ResponseEntity<Page<ResumeDto>> getResumes(Long userId, Pageable pageable) {
        log.info("Getting resume for user: {}", userId);

        return ResponseEntity.ok(resumeService.getResumes(userId,  pageable));
    }

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
