package com.example.resumedev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "achievements", indexes = {
        @Index(name = "idx_achievements_user_id", columnList = "user_id"),
        @Index(name = "idx_achievements_category", columnList = "category"),
        @Index(name = "idx_achievements_date", columnList = "date"),
})
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @NotBlank(message = "Название достижения обязательно")
    @Size(max = 200)
    @Setter
    private String title;

    @NotBlank(message = "Категория обязательна")
    @Size(max = 50)
    @Setter
    private String category;

    @NotNull(message = "Дата достижения обязательна")
    @Setter
    private LocalDate date;

    @NotBlank(message = "Описание обязательно")
    @Column(columnDefinition = "TEXT")
    @Setter
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
