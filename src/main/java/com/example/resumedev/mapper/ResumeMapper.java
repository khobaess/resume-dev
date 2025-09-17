package com.example.resumedev.mapper;

import com.example.resumedev.dto.ResumeDto;
import com.example.resumedev.entity.Resume;
import org.springframework.stereotype.Component;

@Component
public class ResumeMapper {
    public ResumeDto toDto(
            Resume resume
    ) {
        ResumeDto dto = new ResumeDto();
        dto.setUserId(dto.getUserId());
        dto.setAchievementId(dto.getAchievementId());

        return dto;
    }

    public Resume toEntity(
            ResumeDto dto
    ) {
        if (dto == null) return null;

        return new Resume();
    }
}
