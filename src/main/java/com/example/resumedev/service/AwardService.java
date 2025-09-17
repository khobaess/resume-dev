package com.example.resumedev.service;


import com.example.resumedev.dto.AwardDto;
import com.example.resumedev.entity.Award;
import com.example.resumedev.entity.User;

import java.util.List;

public interface AwardService {
    List<AwardDto> getUserAwards(Long userId);

    Award getAward(User user);
}
