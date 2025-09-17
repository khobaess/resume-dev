package com.example.resumedev.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_user_id", columnList = "userId"),
})
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private int level;

    private String description;

    private LocalDate birthDate;

    private String city;

    private String jobTitle;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Achievement> achievements = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Award> awards = new ArrayList<>();

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", level=" + level +
                ", description='" + description + '\'' +
                ", birthDate=" + birthDate +
                ", city='" + city + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }

    public User() {}

    public User(String firstName, String lastName, String description,
                LocalDate birthDate, String city, String jobTitle) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.birthDate = birthDate;
        this.city = city;
        this.jobTitle = jobTitle;
    }

}
