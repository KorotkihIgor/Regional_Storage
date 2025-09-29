package ru.netology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.netology.dto.ResponseFile;
import ru.netology.model.File;
import ru.netology.service.FileService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @InjectMocks
    private FileController fileController;

    @Mock
    private FileService fileService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private ResponseFile responseFile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
        objectMapper = new ObjectMapper();
        responseFile = ResponseFile.builder()
                .filename("filenameTest")
                .type(MediaType.TEXT_PLAIN_VALUE)
                .data("test".getBytes()).build();
    }

    @Test
    void updateFile() throws Exception {
        File fileTest = File.builder()
                .id(1)
                .filename("testFile")
                .type(MediaType.TEXT_PLAIN_VALUE)
                .data("test".getBytes())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/file")
                        .param("filename", fileTest.getFilename())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(objectMapper.writeValueAsString(fileTest)))
                .andExpect(status().isOk());

    }

    @Test
    void downloadFile() throws Exception {

        when(fileService.downloadFile(Mockito.anyString())).thenReturn(responseFile);

        mockMvc.perform(MockMvcRequestBuilders.get("/file")
                        .param("filename", responseFile.getFilename())
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE));
    }

    @Test
    void uploadFile() throws Exception {
        var mockFile = new MockMultipartFile("filename",
                "testFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/file")
                        .file("file", mockFile.getBytes())
                        .param("filename", mockFile.getName()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/file")
                        .param("filename", "filenameTest"))
                .andExpect(status().isOk());
    }

    @Test
    void getFiles() throws Exception {

        ResponseFile responseFileTwo = ResponseFile.builder()
                .filename("filenameTestTwo")
                .type(MediaType.TEXT_PLAIN_VALUE)
                .data("testTwo".getBytes())
                .build();
        List<ResponseFile> listTest = List.of(responseFile, responseFileTwo);

        Mockito.when(fileService.getFile(2)).thenReturn(listTest);

        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(content().json(objectMapper.writeValueAsString(listTest)));
    }
}