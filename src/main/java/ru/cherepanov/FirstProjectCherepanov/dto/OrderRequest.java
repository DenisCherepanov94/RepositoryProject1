package ru.cherepanov.FirstProjectCherepanov.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * ДТО заказ полученное от пользователя
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {
    /**
     * Описание заказа полученное от пользователя
     */
    @NotEmpty(message = "Поле не должно быть пустым")
    private String description;
}
