package ru.cherepanov.FirstProjectCherepanov.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {

    @NotEmpty(message = "Поле не должно быть пустым")
    private String description;
}
