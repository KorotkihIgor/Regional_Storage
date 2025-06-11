package ru.netology.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.model.Person;
import ru.netology.repository.RegisterRepository;

@Service
public class RegisterService {

    @Autowired
    private RegisterRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public Person register(Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        return userRepository.save(person);
    }
}
