package ru.cherepanov.FirstProjectCherepanov.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthRequest {

    @NotEmpty(message = "Поле не должно быть пустым")
    private String username;
    @NotEmpty(message = "Поле не должно быть пустым")
    private String password;
}
