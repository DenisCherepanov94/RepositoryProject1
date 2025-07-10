package ru.cherepanov.FirstProjectCherepanov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthRequest;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthResponse;
import ru.cherepanov.FirstProjectCherepanov.entity.Role;
import ru.cherepanov.FirstProjectCherepanov.entity.User;
import ru.cherepanov.FirstProjectCherepanov.repository.UserRepository;
import ru.cherepanov.FirstProjectCherepanov.security.UserDetail;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private AuthRequest authRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
        authRequest.setUsername("user2");
        authRequest.setPassword("password");

        testUser = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .password("encodedpassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void register_ShouldReturnAuthResponse() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(UserDetail.class))).thenReturn("testtoken");

        // Act
        AuthResponse response = userService.register(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals("testtoken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void authenticate_ShouldReturnAuthResponse() {
        // Arrange
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(any(UserDetail.class))).thenReturn("testtoken");

        // Act
        AuthResponse response = userService.authenticate(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals("testtoken", response.getToken());
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.authenticate(authRequest));
    }
}