package com.example.resumedev.service;

import com.example.resumedev.dto.ResumeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeService {
    ResumeDto getResume(
            Long id,
            Long userId,
            Long achievementId
    );

    Page<ResumeDto> getResumes(Long userId, Pageable pageable);

    ResumeDto createResume(
            Long userId,
            Long achievementId,
            ResumeDto resumeDto
    );

    void deleteResume(
            Long id,
            Long userId,
            Long achievementId
    );

    String resumeGenerateTxt(Long userId);
}
