package ru.netology.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.exception.RegistrationException;
import ru.netology.model.Person;
import ru.netology.model.Role;
import ru.netology.repository.RegisterRepository;

@Service
public class RegisterService {
    @Autowired
    private RegisterRepository registerRepository;
    @Autowired
    private PasswordEncoder encoder;

    private int count;

    public Person register(Person person) {
        registerRepository.findByLogin(person.getLogin())
                .ifPresent(s -> {
                    throw new RegistrationException(String
                            .format("Пользователь с логином %s уже зарегистрирован!", person.getLogin()));
                });
        person.setPassword(encoder.encode(person.getPassword()));
        if (count == 0) {
            person.setRole(Role.ADMIN);
            count++;
            return registerRepository.save(person);
        }
        person.setRole(Role.USER);
        return registerRepository.save(person);
    }
}
