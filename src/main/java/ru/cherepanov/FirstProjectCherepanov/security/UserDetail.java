package ru.cherepanov.FirstProjectCherepanov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.cherepanov.FirstProjectCherepanov.entity.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Создаем кастомный класс
 * имплементирующий UserDetails
 * для Защиты данных и распределения ролей
 */
@RequiredArgsConstructor
public class UserDetail implements UserDetails {

    private final User user;

    /**
     * Устанвливаем роль пользваотелю
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Получаем имя пользователя
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Получаем пароль пользователя
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }
}
