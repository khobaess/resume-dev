package com.example.resumedev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeDto {

    private Long user_id;

    private Long achievement_id;

}
