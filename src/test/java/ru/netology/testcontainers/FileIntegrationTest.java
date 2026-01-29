package ru.netology.testcontainers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.netology.configTest.MyTestConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
public class FileIntegrationTest {

    private static final String URL = "/file";
    private static final String FILENAME = "filename";
    private static final String TEST_ONE = "filenameTest";
    private static final String TEST_TWO = "filenameTestTwo";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private MockMultipartFile mockFile;
    private MockMultipartFile mockFileTwo;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockFile = new MockMultipartFile("file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test".getBytes());

        mockFileTwo = new MockMultipartFile("file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "testTwo".getBytes());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void uploadFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/file")
                        .file(mockFile)
                        .param(FILENAME, TEST_ONE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void uploadUserFileTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL)
                        .file(mockFile)
                        .param(FILENAME, TEST_ONE))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void updateUserFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .param(FILENAME, TEST_ONE)
                        .param("newFilename", "newFilenameTest"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL)
                        .file(mockFile)
                        .param(FILENAME, TEST_TWO))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .param(FILENAME, TEST_TWO)
                        .param("newFilename", "newFilenameTest"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    void getFiles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    void downloadFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .param(FILENAME, TEST_ONE))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL)
                        .param(FILENAME, TEST_ONE))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
