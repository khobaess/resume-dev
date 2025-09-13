package com.example.resumedev.mapper;

import com.example.resumedev.dto.ResumeDto;
import com.example.resumedev.entity.Resume;
import org.springframework.stereotype.Component;

@Component
public class ResumeMapper {
    public ResumeDto toDto(Resume resume) {
        ResumeDto dto = new ResumeDto();
        dto.setUser_id(dto.getUser_id());
        dto.setAchievement_id(dto.getAchievement_id());

        return dto;
    }

    public Resume toEntity(ResumeDto dto) {
        if (dto == null) return null;

        Resume resume = new Resume();

        return resume;
    }

}
