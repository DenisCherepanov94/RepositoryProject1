package ru.cherepanov.FirstProjectCherepanov.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ДТО аутентификации полученное от пользователя
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthRequest {
    /**
     * Имя пользователя
     */
    @NotEmpty(message = "Поле не должно быть пустым")
    private String username;
    /**
     * Пароль пользователя
     */
    @NotEmpty(message = "Поле не должно быть пустым")
    private String password;
}
