package ru.netology.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.exception.UserNotFoundException;
import ru.netology.model.Person;
import ru.netology.model.Role;
import ru.netology.repository.RegisterRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private RegisterRepository registerRepository;

    private Person person;

    @BeforeEach
    void setUp() {
        person = Person.builder().Id(1)
                .login("emailTest@com")
                .password("passwordTest")
                .role(Role.USER)
                .build();
    }

    @Test
    void loadUserByUsername() {
        Mockito.when(registerRepository.findByLogin(person.getLogin())).thenReturn(Optional.of(person));

        var personTest = myUserDetailsService.loadUserByUsername(person.getLogin());

        assertNotNull(personTest);
        assertEquals("emailTest@com", person.getLogin());
    }

    @DisplayName("Отсутствие person в БД.")
    @Test
    void loadUserByUsernameThrows() {
        Mockito.when(registerRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> myUserDetailsService.loadUserByUsername(person.getLogin()),
                "Пользователь %s не найден!");
    }
}