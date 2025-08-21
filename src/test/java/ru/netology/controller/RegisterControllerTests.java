package ru.netology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import ru.netology.model.Person;
import ru.netology.model.Role;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
//@ExtendWith(MockitoExtension.class)
public class RegisterControllerTests {
   @Autowired
    private MockMvc mockMvc;

   @Autowired
    private ObjectMapper objectMapper;


    @Test
   public void registerTest () throws Exception {
        ResultHandler print;
        Person person = new Person(1,"emailTest","passwordTest",Role.USER);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("emailTest"))
                .andExpect(jsonPath("$.password").value("passwordTest"));
    }

}
