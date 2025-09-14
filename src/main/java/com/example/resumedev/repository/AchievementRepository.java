package com.example.resumedev.repository;

import com.example.resumedev.entity.Achievement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Page<Achievement> findByUserIdOrderByDateEndDesc(@Param("userId") Long userId,
                                                  Pageable pageable);
    
    Optional<Achievement> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT a.category FROM Achievement a WHERE a.user.id = :userId " +
            "GROUP BY a.category ORDER BY COUNT(a) DESC LIMIT 1")
    Optional<String> findMostActiveCategoryByUserId(Long userId);

    int countAchievementsByUserId(@Param("userId") Long userId);

    Page<Achievement> findByUserIdAndCategoryOrderByDateEndDesc(
            @Param("userId") Long userId,
            @Param("category") String category,
            Pageable pageable
    );

    Page<Achievement> findByUserIdAndCategoryAndDescriptionContainsOrderByDateEndDesc(
            Long userId,
            String category,
            String description,
            Pageable pageable
    );

    Page<Achievement> getAchievementByUserIdAndDescriptionContainsOrderByDateEndDesc(@Param("userId") Long userId,
                                              @Param("description") String description,
                                                           Pageable pageable);

}
