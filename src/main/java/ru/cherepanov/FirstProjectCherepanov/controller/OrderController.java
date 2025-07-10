package ru.cherepanov.FirstProjectCherepanov.controller;


import jakarta.servlet.ServletException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cherepanov.FirstProjectCherepanov.dto.OrderRequest;
import ru.cherepanov.FirstProjectCherepanov.dto.OrderResponse;
import ru.cherepanov.FirstProjectCherepanov.entity.OrderStatus;
import ru.cherepanov.FirstProjectCherepanov.service.OrderService;
import ru.cherepanov.FirstProjectCherepanov.util.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(pageable));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable("id") UUID id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> userNotFoundException(UserNotFoundException e) {
        ResponseError responseError = new ResponseError("Пользователь не существует");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> orderNotFoundException(OrderNotFoundException e) {
        ResponseError responseError = new ResponseError("Заказ не существует");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseError> notAuthorizedDeleteException(NotAuthorizedDeleteException e) {
        ResponseError responseError = new ResponseError("Недостаточно прав для совершения операции");
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
