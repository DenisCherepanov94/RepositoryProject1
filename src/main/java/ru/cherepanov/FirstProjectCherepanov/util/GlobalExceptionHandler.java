package ru.cherepanov.FirstProjectCherepanov.util;

import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный Отлов Событий со всего приложения
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RegisterUserException.class)
    private ResponseEntity<ResponseError> registerUserException(RegisterUserException e) {
        ResponseError responseError = new ResponseError("Ошибка регистрации пользователя");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<ResponseError> userAlreadyExistsException(UserAlreadyExistsException e) {
        ResponseError responseError = new ResponseError("Пользователь уже существует,выполните аутентификацию");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<ResponseError> badCredentialsException(BadCredentialsException e) {
        ResponseError responseError = new ResponseError("Неверные логин или пароль");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(ServletException.class)
    private ResponseEntity<ResponseError> servletException(ServletException e) {
        ResponseError responseError = new ResponseError("Error token");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ResponseError> userNotFoundException(UserNotFoundException e) {
        ResponseError responseError = new ResponseError("Пользователь не существует");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    private ResponseEntity<ResponseError> orderNotFoundException(OrderNotFoundException e) {
        ResponseError responseError = new ResponseError("Заказ не существует");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(NotAuthorizedDeleteException.class)
    private ResponseEntity<ResponseError> notAuthorizedDeleteException(NotAuthorizedDeleteException e) {
        ResponseError responseError = new ResponseError("Недостаточно прав для совершения операции");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

}
