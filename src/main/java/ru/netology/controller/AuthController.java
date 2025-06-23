package ru.netology.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.dto.AuthRequest;
import ru.netology.dto.AuthResponseToken;
import ru.netology.service.AuthService;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseToken> login(@RequestBody AuthRequest authRequest) {
        var token =  authService.authLogin(authRequest);
        return ResponseEntity.ok(token);
    }
}
