package com.example.resumedev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AchievementDto {

    @NotBlank(message = "Название достижения обязательно")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Категория обязательна")
    @Size(max = 50)
    private String category;

    @JsonProperty("date_start")
    private LocalDate dateStart;

    @JsonProperty("date_end")
    @NotNull(message = "Дата достижения обязательна")
    private LocalDate dateEnd;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @JsonProperty("user_id")
    private Long userId;
}