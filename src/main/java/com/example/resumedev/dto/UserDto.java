package com.example.resumedev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 100)
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Фамилия обязательно")
    @Size(max = 100)
    @JsonProperty("last_name")
    private String lastName;

    private String description;

    private String city;

    @Size(max = 100)
    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    // Статистика
    private int achievementsCount;
    private String mostActiveCategory;
    private Long awardsCount;
    private int level;
}