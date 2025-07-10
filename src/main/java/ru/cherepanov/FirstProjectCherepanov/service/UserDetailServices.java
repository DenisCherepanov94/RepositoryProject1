package ru.cherepanov.FirstProjectCherepanov.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.cherepanov.FirstProjectCherepanov.entity.User;
import ru.cherepanov.FirstProjectCherepanov.repository.UserRepository;
import ru.cherepanov.FirstProjectCherepanov.security.UserDetail;
import ru.cherepanov.FirstProjectCherepanov.util.UserNotFoundException;

import java.util.Optional;

@Service
@Builder
@RequiredArgsConstructor
public class UserDetailServices implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return new UserDetail(byUsername.get());
        } else {
            throw new UserNotFoundException();
        }
    }
}
