package com.example.resumedev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.resumedev.model.Award;

import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    @Query("SELECT COUNT(a) FROM Award a WHERE a.user.vkId = :userId")
    Long countAwardsByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Award a WHERE a.user.vkId = :userId")
    List<Award> getByUserId(@Param("userId") Long userId);

}
