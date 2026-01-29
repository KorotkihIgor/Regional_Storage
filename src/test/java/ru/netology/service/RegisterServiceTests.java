package ru.netology.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.netology.exception.RegistrationException;
import ru.netology.model.Person;
import ru.netology.model.Role;
import ru.netology.repository.RegisterRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTests {
    @InjectMocks
    private RegisterService registerService;

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private PasswordEncoder encoder;

    private Person personTest;

    @BeforeEach
    void setUp() {
        personTest = Person.builder()
                .Id(1)
                .email("emailTest")
                .password(encoder.encode("password"))
                .role(Role.USER)
                .build();
    }

    @Test
    void registerTest() {

        when(encoder.encode(personTest.getPassword())).thenReturn("passwordTest");
        when(registerRepository.save(any(Person.class))).thenReturn(personTest);

        var newPersonsTest = registerService.register(personTest);

        assertNotNull(newPersonsTest);
        Mockito.verify(registerRepository, Mockito.times(1)).save(personTest);
        assertEquals("emailTest", personTest.getEmail());
        assertEquals("passwordTest", personTest.getPassword());
    }

    @DisplayName("Проверяем работу исключения при совпадении email, которое уже существует в БД.")
    @Test
    void registerThrows() {

        when(registerRepository.findByEmail(personTest.getEmail())).thenReturn(Optional.of(personTest));

        Assertions.assertThrows(RegistrationException.class, () -> registerService.register(personTest),
                String.format("Пользователь с логином %s уже зарегистрирован!", personTest.getEmail()));
    }

}
