package com.example.resumedev.mapper;

import com.example.resumedev.dto.AwardDto;
import com.example.resumedev.model.Award;
import org.springframework.stereotype.Component;

@Component
public class AwardMapper {
    public AwardDto toDto(Award award) {
        if (award == null) return null;

        AwardDto dto = new AwardDto();
        dto.setTitle(award.getTitle());
        dto.setUser_id(dto.getUser_id());
        return dto;
    }

}
