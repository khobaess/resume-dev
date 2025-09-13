package com.example.resumedev.controller;

import com.example.resumedev.dto.AwardDto;
import com.example.resumedev.dto.UserDto;
import com.example.resumedev.service.AwardService;
import com.example.resumedev.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Awards", description = "API для работы с наградами и статистикой пользователя")
@CrossOrigin(origins = "http://localhost:5173")
public class AwardController {

    private final AwardService awardService;

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить награды", description = "Возвращает список наград")
    public ResponseEntity<List<AwardDto>> getAwards(
            @Parameter(description = "Айди пользователя") @RequestParam(required = false) Long userId){

        log.info("Getting awards for user: {}", userId);

        List<AwardDto> awards = awardService.getUserAwards(userId);
        return ResponseEntity.ok(awards);
    }

    @GetMapping("/level")
    @Operation(summary = "Получить уровень", description = "Возвращает уровень")
    public ResponseEntity<Optional<Integer>> getLevel(
            @Parameter(description = "Айди пользователя") @RequestParam(required = false) Long userId){

        log.info("Getting level for user: {}", userId);

        Optional<Integer> level = userService.getLevel(userId);
        return ResponseEntity.ok(level);
    }

    @GetMapping("/stats")
    @Operation(summary = "Получить статистику", description = "Возвращает статистику")
    public ResponseEntity<UserDto> getStats(
            @Parameter(description = "Айди пользователя") @RequestParam(required = false) Long userId){

        log.info("Getting stats for user: {}", userId);

        UserDto user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }
}
