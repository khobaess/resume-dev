package com.example.resumedev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("achievement_id")
    private Long achievementId;
}
