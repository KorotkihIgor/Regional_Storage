package ru.netology.testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.netology.configTest.MyTestConfiguration;
import ru.netology.dto.AuthRequest;
import ru.netology.model.Person;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
public class IntegrationTest {

    private static final String LOGIN = "emailTest";
    private static final String PASSWORD = "passwordTest";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerTest() throws Exception {
        Person person = Person.builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void authTest() throws Exception {
        AuthRequest authRequest = AuthRequest.builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
