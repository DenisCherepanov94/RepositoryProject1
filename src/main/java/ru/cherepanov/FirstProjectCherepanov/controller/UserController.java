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
import ru.cherepanov.FirstProjectCherepanov.util.UserAlreadyExistsException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
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
