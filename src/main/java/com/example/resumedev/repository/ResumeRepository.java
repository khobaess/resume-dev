package com.example.resumedev.repository;

import com.example.resumedev.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByIdAndUserIdAndAchievementId(
            Long id,
            Long userId,
            Long achievementId);

    Page<Resume> findByUserId(
            Long userId,
            Pageable pageable);

    List<Resume> findByUserId(
            Long userId);
}
