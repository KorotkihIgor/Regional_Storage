package ru.netology.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.dto.AuthRequest;
import ru.netology.dto.AuthResponseToken;
import ru.netology.exception.PasswordBadRequest;
import ru.netology.jwt.JwtToken;
import ru.netology.model.Token;
import ru.netology.repository.RegisterRepository;
import ru.netology.repository.TokenRepository;

@Service
@AllArgsConstructor
public class AuthService {

    private RegisterRepository registerRepository;
    private PasswordEncoder passwordEncoder;
    private JwtToken jwtToken;
    private AuthenticationManager authenticationManager;
    private TokenRepository tokenRepository;

    public AuthResponseToken authLogin(AuthRequest authRequest) {
        var authPerson = registerRepository.findByLogin(authRequest.getLogin()).orElseThrow();
        if (passwordEncoder.matches(authRequest.getPassword(), authPerson.getPassword())) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtToken.generateToken(authentication);
            tokenRepository.save(new Token(authPerson.getLogin(), token));
            return new AuthResponseToken(token);
        } else {
            throw new PasswordBadRequest("Неправильный пароль!");
        }
    }
}
