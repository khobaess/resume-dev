package com.example.resumedev.repository;


import com.example.resumedev.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByVkId(Long vkId);

    boolean existsByVkId(Long vkId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.achievements WHERE u.vkId = :id")
    Optional<User> findByIdWithAchievements(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.achievements WHERE u.vkId = :vkId")
    Optional<User> findByVkIdWithAchievements(@Param("vkId") Long vkId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.awards WHERE u.vkId = :vkId")
    Optional<User> findByVkIdWithAwards(@Param("vkId") Long vkId);

    @Query("SELECT u.level FROM User u WHERE u.vkId = :vkId")
    Optional<Integer> findLevelByVkId(@Param("vkId") Long vkId);

}

