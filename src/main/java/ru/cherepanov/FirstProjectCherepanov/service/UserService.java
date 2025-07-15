package ru.cherepanov.FirstProjectCherepanov.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthRequest;
import ru.cherepanov.FirstProjectCherepanov.dto.AuthResponse;
import ru.cherepanov.FirstProjectCherepanov.entity.Role;
import ru.cherepanov.FirstProjectCherepanov.entity.User;
import ru.cherepanov.FirstProjectCherepanov.repository.UserRepository;
import ru.cherepanov.FirstProjectCherepanov.util.RegisterUserException;
import ru.cherepanov.FirstProjectCherepanov.util.UserAlreadyExistsException;

import java.util.List;
import java.util.UUID;

/**
 * Сервис сущности Пользователь
 */
@Builder
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServicesImpl jwtServicesImpl;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServices userDetailsService;

    /**
     * Получение всех пользователей
     */
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    /**
     * Удаление пользователя
     */
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    /**
     * Регистрация пользователя
     */
    public AuthResponse register(AuthRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        try {
            repository.save(user);
            var userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            var jwtToken = jwtServicesImpl.generateToken(userDetails);
            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            throw new RegisterUserException();
        }
    }

    /**
     * Авторизация пользователя
     */
    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            var user = userDetailsService.loadUserByUsername(request.getUsername());


            var jwtToken = jwtServicesImpl.generateToken(user);
            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            throw new BadCredentialsException(null);
        }
    }
}
