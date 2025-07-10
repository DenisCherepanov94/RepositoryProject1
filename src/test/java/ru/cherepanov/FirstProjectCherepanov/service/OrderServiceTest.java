package ru.cherepanov.FirstProjectCherepanov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.cherepanov.FirstProjectCherepanov.dto.OrderRequest;
import ru.cherepanov.FirstProjectCherepanov.dto.OrderResponse;
import ru.cherepanov.FirstProjectCherepanov.entity.Order;
import ru.cherepanov.FirstProjectCherepanov.entity.OrderStatus;
import ru.cherepanov.FirstProjectCherepanov.entity.Role;
import ru.cherepanov.FirstProjectCherepanov.entity.User;
import ru.cherepanov.FirstProjectCherepanov.repository.OrderRepository;
import ru.cherepanov.FirstProjectCherepanov.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .password("password")
                .role(Role.USER)
                .build();

        testOrder = Order.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .description("Test order")
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

       // when(securityContext.getAuthentication()).thenReturn(authentication);//TODO
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createOrder_ShouldReturnOrderResponse() {
        // Arrange
        OrderRequest request = new OrderRequest();
        request.setDescription("Test order");

        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        OrderResponse response = orderService.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(testOrder.getDescription(), response.getDescription());
        assertEquals(testOrder.getStatus(), response.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getUserOrders_ShouldReturnPagedOrders() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(testOrder), pageable, 1);

        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(orderRepository.findByUser(testUser, pageable)).thenReturn(orderPage);

        // Act
        Page<OrderResponse> result = orderService.getUserOrders(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(testOrder.getDescription(), result.getContent().getFirst().getDescription());
    }

    @Test
    void getAllOrders_ShouldReturnPagedOrders() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(testOrder), pageable, 1);

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);

        // Act
        Page<OrderResponse> result = orderService.getAllOrders(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(testOrder.getDescription(), result.getContent().getFirst().getDescription());
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus() {
        // Arrange
        UUID orderId = testOrder.getId();
        OrderStatus newStatus = OrderStatus.IN_PROGRESS;
        testOrder.setStatus(newStatus);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        OrderResponse response = orderService.updateOrderStatus(orderId, newStatus);

        // Assert
        assertEquals(newStatus, response.getStatus());
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void deleteOrder_WhenUserIsOwner_ShouldDeleteOrder() {
        // Arrange
        UUID orderId = testOrder.getId();

        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        orderService.deleteOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).delete(testOrder);
    }

    @Test
    void deleteOrder_WhenUserIsNotOwner_ShouldThrowException() {
        // Arrange
        UUID orderId = testOrder.getId();
        User anotherUser = User.builder()
                .id(UUID.randomUUID())
                .username("anotheruser")
                .password("password")
                .role(Role.USER)
                .build();

        when(authentication.getName()).thenReturn("anotheruser");
        when(userRepository.findByUsername("anotheruser")).thenReturn(Optional.of(anotherUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(orderId));
        verify(orderRepository, never()).delete(any());
    }
}


