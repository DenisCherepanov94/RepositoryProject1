package ru.cherepanov.FirstProjectCherepanov.dto;

import lombok.*;
import ru.cherepanov.FirstProjectCherepanov.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ДТО ответ в виде Заказа
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    /**
     * Идентификатор пользователя
     */
    private UUID id;
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Описание заказа пользователя
     */
    private String description;
    /**
     * Статус заказа пользователя
     */
    private OrderStatus status;
    /**
     * Время заказа пользователя
     */
    private LocalDateTime createdAt;
}
