package ru.cherepanov.FirstProjectCherepanov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.cherepanov.FirstProjectCherepanov.dto.OrderRequest;
import ru.cherepanov.FirstProjectCherepanov.dto.OrderResponse;
import ru.cherepanov.FirstProjectCherepanov.entity.Order;
import ru.cherepanov.FirstProjectCherepanov.entity.OrderStatus;
import ru.cherepanov.FirstProjectCherepanov.entity.User;
import ru.cherepanov.FirstProjectCherepanov.repository.OrderRepository;
import ru.cherepanov.FirstProjectCherepanov.repository.UserRepository;
import ru.cherepanov.FirstProjectCherepanov.util.NotAuthorizedDeleteException;
import ru.cherepanov.FirstProjectCherepanov.util.OrderNotFoundException;
import ru.cherepanov.FirstProjectCherepanov.util.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderResponse createOrder(OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);//todo

        Order order = Order.builder()
                .user(user)
                .description(request.getDescription())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    public Page<OrderResponse> getUserOrders(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return orderRepository.findByUser(user,pageable)
                .map(this::mapToOrderResponse);
    }

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::mapToOrderResponse);
    }

    public OrderResponse updateOrderStatus(UUID id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        order.setStatus(status);
        order = orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    public void deleteOrder(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUser().getId().equals(user.getId()) &&
                authentication.getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            throw new NotAuthorizedDeleteException();
        }

        orderRepository.delete(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .username(order.getUser().getUsername())
                .description(order.getDescription())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
