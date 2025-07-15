package ru.cherepanov.FirstProjectCherepanov.controller;


import jakarta.servlet.ServletException;
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
import ru.cherepanov.FirstProjectCherepanov.util.NotAuthorizedDeleteException;
import ru.cherepanov.FirstProjectCherepanov.util.OrderNotFoundException;
import ru.cherepanov.FirstProjectCherepanov.util.ResponseError;
import ru.cherepanov.FirstProjectCherepanov.util.UserNotFoundException;

import java.io.IOException;
import java.util.UUID;

/**
 * Контроллер заказов
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Получение всех заказов пользователя
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(pageable));
    }

    /**
     * Создание заказа пользователя
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    /**
     * Просмотр всех заказов с ролью Админ
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    /**
     * Изменение статуса заказа с ролью Админ
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable("id") UUID id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    /**
     * Удаление заказа
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}
