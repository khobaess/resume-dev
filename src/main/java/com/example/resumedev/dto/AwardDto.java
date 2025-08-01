package com.example.resumedev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwardDto {

    private Long id;
    private String title;
    private String user_id;
}