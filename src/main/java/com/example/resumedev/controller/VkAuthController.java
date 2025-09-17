package com.example.resumedev.controller;

import com.example.resumedev.dto.UserDto;
import com.example.resumedev.service.impl.UserServiceImpl;
import com.example.resumedev.service.impl.VkApiServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "VK Authentication", description = "API для работы с пользователями VK")
public class VkAuthController {

    private final UserServiceImpl userService;

    private final VkApiServiceImpl vkApiServiceImpl;

    @Autowired
    public VkAuthController(UserServiceImpl userService, VkApiServiceImpl vkApiServiceImpl) {

        this.userService = userService;
        this.vkApiServiceImpl = vkApiServiceImpl;
    }

    @PostMapping("/vk-user")
    @Operation(summary = "Создать/обновить пользователя из VK", description = "Создает или обновляет пользователя на основе VK ID")
    public ResponseEntity<UserDto> createOrUpdateVkUser(
            @RequestBody UserDto userDto
    ) {
        try {
            Long vkId = userDto.getId();

            if (!vkApiServiceImpl.isValidVkUser(vkId)) {
                return ResponseEntity.badRequest().build();
            }

            UserDto user = userService.createOrUpdateVkUser(
                    vkId,
                    userDto.getFirstName(),
                    userDto.getLastName()
            );

            return ResponseEntity.ok(user);
        } catch (Exception e) {

            log.error("Failed to create/update VK user", e);

            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/vk-user/{vkId}")
    @Operation(summary = "Получить пользователя по VK ID", description = "Возвращает данные пользователя по VK ID")
    public ResponseEntity<UserDto> getVkUser(
            @Parameter(description = "VK ID пользователя") @PathVariable Long vkId
    ) {

        log.info("Getting user by VK ID: {}", vkId);

        try {
            UserDto user = userService.getUserProfile(vkId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.warn("User not found for VK ID: {}", vkId);

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vk-user/{vkId}/exists")
    @Operation(summary = "Проверить существование пользователя", description = "Проверяет, существует ли пользователь с данным VK ID")
    public ResponseEntity<Map<String, Boolean>> checkVkUserExists(
            @Parameter(description = "VK ID пользователя") @PathVariable Long vkId
    ) {

        log.info("Checking if user exists for VK ID: {}", vkId);

        try {
            boolean exists = userService.existsByVkId(vkId);

            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            log.error("Error checking user existence for vkId {}: ", vkId, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
