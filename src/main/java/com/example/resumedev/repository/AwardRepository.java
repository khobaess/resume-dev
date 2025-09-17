package com.example.resumedev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.resumedev.entity.Award;

import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    Long countAwardsByUserId(
            @Param("userId") Long userId);

    List<Award> getByUserId(
            @Param("userId") Long userId);

    void deleteByTitleAndUserId(
            String title, @Param("id") Long userId);

    List<Award> id(
            Long id);
}
