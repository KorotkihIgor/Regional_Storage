package ru.netology.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.model.Person;
import ru.netology.service.RegisterService;

@RestController
@AllArgsConstructor
public class RegisterController {

    private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Person person) {
        registerService.register(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
