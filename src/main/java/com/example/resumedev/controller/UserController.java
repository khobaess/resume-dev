package com.example.resumedev.controller;

import com.example.resumedev.dto.UserDto;
import com.example.resumedev.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "API для работы с пользователем")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @GetMapping()
    @Operation(summary = "Получить пользователя", description = "Получает пользователя")
    public ResponseEntity<UserDto> getUser(@RequestParam Long userId) {
        log.info("Getting User with id: {}", userId);

        UserDto user = userService.getUserProfile(userId);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update")
    @Operation(summary = "Обновить пользователя", description = "Обновляет доп поля у пользователя")
    public ResponseEntity<UserDto> updateUser( @Parameter(description = "Айди пользователя") @RequestParam Long userId,
                                              @RequestBody UserDto userDto) {
        log.info("Updating User with id: {}", userId);

        UserDto updatedUser = userService.updateUserProfile(userId, userDto);

        return ResponseEntity.ok(updatedUser);

    }
}
