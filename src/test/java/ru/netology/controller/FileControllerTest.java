package ru.netology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.netology.dto.ResponseFile;
import ru.netology.service.FileService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    private static final String URL = "/file";
    private static final String PARAM_FILE = "filename";

    @InjectMocks
    private FileController fileController;

    @Mock
    private FileService fileService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ResponseFile responseFile;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
        objectMapper = new ObjectMapper();
        responseFile = ResponseFile.builder()
                .filename("filenameTest")
                .type(MediaType.TEXT_PLAIN_VALUE)
                .data("test".getBytes()).build();

        mockFile = new MockMultipartFile("file",
                "testFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test".getBytes());
    }

    @Test
    void updateFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .param(PARAM_FILE, mockFile.getName())
                        .param("newFilename", "newFilenameTest"))
                .andExpect(status().isOk());
    }

    @Test
    void downloadFile() throws Exception {

        when(fileService.downloadFile(Mockito.anyString())).thenReturn(responseFile);

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .param(PARAM_FILE, responseFile.getFilename())
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE));
    }


    @Test
    void uploadFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL)
                        .file(mockFile)
                        .param(PARAM_FILE, "fileTest"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL)
                        .param(PARAM_FILE, "filenameTest"))
                .andExpect(status().isOk());
    }

    @Test
    public void getFiles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}