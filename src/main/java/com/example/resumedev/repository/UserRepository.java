package com.example.resumedev.repository;


import com.example.resumedev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id = :id")
    Optional<User> findById(Long id);

    boolean existsById(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.achievements WHERE u.id = :id")
    Optional<User> findByIdWithAchievements(@Param("id") Long id);


    @Query("SELECT u FROM User u LEFT JOIN FETCH u.awards WHERE u.id = :id")
    Optional<User> findByIdWithAwards(@Param("id") Long id);

    @Query("SELECT u.level FROM User u WHERE u.id = :Id")
    Optional<Integer> findLevelById(@Param("id") Long id);

}

