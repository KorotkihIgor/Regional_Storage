package ru.netology.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.exception.UserNotFoundException;
import ru.netology.model.Person;
import ru.netology.model.UserPrincipal;
import ru.netology.repository.RegisterRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private RegisterRepository registerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person persons = registerRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь %s не найден!", email)));
        return new UserPrincipal(persons);
    }
}
