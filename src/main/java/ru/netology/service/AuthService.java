package ru.netology.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.dto.AuthRequest;
import ru.netology.dto.AuthResponseToken;
import ru.netology.jwt.JwtToken;
import ru.netology.repository.RegisterRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
//    @Autowired
  private RegisterRepository registerRepository;
//    @Autowired
  private PasswordEncoder passwordEncoder;
//    @Autowired
  private JwtToken jwtToken;

    private Map<String,String> storageToken = new HashMap<>();

    public AuthResponseToken authLogin(AuthRequest authRequest) {
       var authPerson = registerRepository.findByUsername(authRequest.getUsername()).orElseThrow();
       if(passwordEncoder.matches(authRequest.getPassword(),authPerson.getPassword())){
           String token = jwtToken.generateToken(authPerson);
           storageToken.put(authPerson.getUsername(),token);
           return new AuthResponseToken(token);
       }else {
           throw new RuntimeException("Неправельный пароль");
       }
    }

}
