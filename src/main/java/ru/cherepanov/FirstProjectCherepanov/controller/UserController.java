package ru.cherepanov.FirstProjectCherepanov.controller;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cherepanov.FirstProjectCherepanov.entity.User;
import ru.cherepanov.FirstProjectCherepanov.service.UserService;
import ru.cherepanov.FirstProjectCherepanov.util.ResponseError;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Контроллер для Админа
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    /**
     * Получение всех пользователей для Админа
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Удаление пользователя для Админа
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
    }


}
