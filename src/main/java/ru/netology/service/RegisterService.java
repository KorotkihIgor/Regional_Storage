package ru.netology.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.dto.AuthRequest;
import ru.netology.exception.RegistrationException;
import ru.netology.model.Person;
import ru.netology.repository.RegisterRepository;

@Service
@AllArgsConstructor
public class RegisterService {

    private RegisterRepository registerRepository;
    private PasswordEncoder encoder;

    public Person register(Person person) {
       registerRepository.findByEmail(person.getEmail())
                .ifPresent(s -> {
                    throw new RegistrationException(String
                            .format("Пользователь с логином %s уже зарегистрирован!", person.getEmail()));
                });
       person.setPassword(encoder.encode(person.getPassword()));
        return registerRepository.save(person);
    }
}
