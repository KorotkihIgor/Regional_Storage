package ru.netology.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.dto.AuthRequest;
import ru.netology.dto.AuthResponseToken;
import ru.netology.jwt.JwtToken;
import ru.netology.repository.RegisterRepository;

import java.util.HashMap;
import java.util.Map;

@Service
//@AllArgsConstructor
public class AuthService {
    @Autowired
    private RegisterRepository registerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtToken jwtToken;

    @Autowired
   private AuthenticationManager authenticationManager;

    private Map<String, String> storageToken = new HashMap<>();

    public AuthResponseToken authLogin(AuthRequest authRequest) {
        var authPerson = registerRepository.findByEmail(authRequest.getEmail()).orElseThrow();
        if (passwordEncoder.matches(authRequest.getPassword(), authPerson.getPassword())) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtToken.generateToken(authentication);
           storageToken.put(authPerson.getEmail(),token);
//            storageToken.put(token, authPerson.getEmail());
            return new AuthResponseToken(token);
        } else {
            throw new RuntimeException("Неправельный пароль");
        }
    }

    public void authLogout(String token) {
        storageToken.remove(token);
    }

}
