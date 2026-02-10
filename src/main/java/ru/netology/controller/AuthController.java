package ru.netology.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.dto.AuthRequest;
import ru.netology.dto.AuthResponseToken;
import ru.netology.service.AuthService;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseToken> login(@RequestBody AuthRequest authRequest) {
        AuthResponseToken token = authService.authLogin(authRequest);
        return ResponseEntity.ok(token);
    }
}
