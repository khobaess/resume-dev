package com.example.resumedev.repository;

import com.example.resumedev.model.Achievement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query("SELECT a FROM Achievement a WHERE a.user.vkId = :userId " +
            "ORDER BY a.date DESC")
    Page<Achievement> findByUserIdOrderByDateDesc(@Param("userId") Long userId,
                                                  Pageable pageable);

    @Query("SELECT a FROM Achievement a WHERE a.user.vkId = :userId AND a.id = :id")
    Optional<Achievement> findByIdAndUserId(Long id, Long userId);


    @Query("SELECT a.category FROM Achievement a WHERE a.user.vkId = :userId " +
            "GROUP BY a.category ORDER BY COUNT(a) DESC LIMIT 1")
    Optional<String> findMostActiveCategoryByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(a) FROM Achievement a WHERE a.user.vkId = :userId")
    int countAchievementsByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Achievement a WHERE a.user.vkId = :userId AND a.category = :category ORDER BY a.date DESC")
    Page<Achievement> findByUserIdAndCategoryOrderByDateDesc(
            @Param("userId") Long userId,
            @Param("category") String category,
            Pageable pageable
    );

}
