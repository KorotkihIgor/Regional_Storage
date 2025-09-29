package ru.netology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.netology.model.Person;
import ru.netology.model.Role;
import ru.netology.service.RegisterService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RegisterService registerService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register() throws Exception {
        Person person = Person.builder()
                .Id(1)
                .email("emailTest")
                .password("test")
                .role(Role.ADMIN)
                .build();

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());
        verify(registerService, times(1)).register(person);
    }
}