package ru.cherepanov.FirstProjectCherepanov.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность заказ
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "orders")
public class Order {
    /**
     * Идентификатор заказа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Связь пользователя и его заказов
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * Описание заказа
     */
    private String description;
    /**
     * Статус заказа
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    /**
     * Время создания заказа
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
