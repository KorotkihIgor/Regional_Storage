package ru.netology.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ru.netology.jwt.JwtAuthFilter;
import ru.netology.repository.TokenRepository;

@Service
@AllArgsConstructor
public class LogoutService implements LogoutHandler {
    private TokenRepository tokenRepository;
    private JwtAuthFilter jwtAuthFilter;

    @Override
    @Transactional
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String token = jwtAuthFilter.parseToken(request);
        tokenRepository.findByToken(token)
                .ifPresent(storedToken -> tokenRepository.deleteByEmail(storedToken.getEmail()));
    }
}
