package ru.cherepanov.FirstProjectCherepanov.dto;

import lombok.*;

/**
 * ДТО ответ в виде токена пользователю
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {
    /**
     * Токен
     */
    private String token;
}
