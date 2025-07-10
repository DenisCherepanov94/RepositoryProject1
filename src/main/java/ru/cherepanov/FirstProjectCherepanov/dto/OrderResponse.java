package ru.cherepanov.FirstProjectCherepanov.dto;

import lombok.*;
import ru.cherepanov.FirstProjectCherepanov.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {

    private UUID id;
    private String username;
    private String description;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
