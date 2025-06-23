package ru.netology.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.exception.RegistrationException;
import ru.netology.model.Person;
import ru.netology.repository.RegisterRepository;

@Service
@AllArgsConstructor
public class RegisterService {

    private RegisterRepository userRepository;
    private PasswordEncoder encoder;

    public Person register(Person person) {
        userRepository.findByUsername(person.getUsername())
                .ifPresent(s -> {
                    throw new RegistrationException(String
                            .format("Пользователь с логином %s уже зарегистрирован!", person.getUsername()));
                });
        person.setPassword(encoder.encode(person.getPassword()));
        return userRepository.save(person);
    }
}
