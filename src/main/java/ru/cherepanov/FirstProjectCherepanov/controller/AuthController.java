package ru.cherepanov.FirstProjectCherepanov.controller;

import io.swagger.annotations.Api;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthRequest;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthResponse;
import ru.cherepanov.FirstProjectCherepanov.security.UserDetail;
import ru.cherepanov.FirstProjectCherepanov.service.UserService;
import ru.cherepanov.FirstProjectCherepanov.util.RegisterUserException;
import ru.cherepanov.FirstProjectCherepanov.util.RequestEmptyException;
import ru.cherepanov.FirstProjectCherepanov.util.ResponseError;
import ru.cherepanov.FirstProjectCherepanov.util.UserAlreadyExistsException;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestEmptyException();
        }
        return ResponseEntity.ok(userService.register(request));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @GetMapping("/me")
    public String getCurrentUser() {
        StringBuilder msg = new StringBuilder();
        msg.append("User info: ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) auth.getPrincipal();
        msg.append(userDetail.getUsername());
        return msg.toString();
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> registerUserException(RegisterUserException e) {
        ResponseError responseError = new ResponseError("Ошибка регистрации пользователя");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> requestEmptyException(RequestEmptyException e) {
        ResponseError responseError = new ResponseError("Пустые поля недопустимы");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> userAlreadyExistsException(UserAlreadyExistsException e) {
        ResponseError responseError = new ResponseError("Пользователь уже существует,выполните аутентификацию");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> badCredentialsException(BadCredentialsException e) {
        ResponseError responseError = new ResponseError("Неверные логин или пароль");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> servletException(ServletException e) {
        ResponseError responseError = new ResponseError("Error token");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> servletException(IOException e) {
        ResponseError responseError = new ResponseError("Error token");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

}
