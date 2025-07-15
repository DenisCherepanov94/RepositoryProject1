package ru.cherepanov.FirstProjectCherepanov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthRequest;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthResponse;
import ru.cherepanov.FirstProjectCherepanov.security.UserDetail;
import ru.cherepanov.FirstProjectCherepanov.service.UserService;

import java.util.Objects;

/**
 * Контроллер аутентификации
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final UserService userService;

    /**
     * Регистрация пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String error= Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return new ResponseEntity<>(new AuthResponse(error), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.register(request));

    }

    /**
     * Авторизация пользователя
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String error= Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return new ResponseEntity<>(new AuthResponse(error), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.authenticate(request));
    }

    /**
     * Home страница
     */
    @GetMapping("/me")
    public String getCurrentUser() {
        StringBuilder msg = new StringBuilder();
        msg.append("User info: ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) auth.getPrincipal();
        msg.append(userDetail.getUsername());
        return msg.toString();
    }


}
