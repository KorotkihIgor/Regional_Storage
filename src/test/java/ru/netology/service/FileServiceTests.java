package ru.netology.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.netology.exception.FileBadRequestException;
import ru.netology.model.File;
import ru.netology.repository.FileRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceTests {
    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    private MockMultipartFile fileTest;
    private File file;
    private File fileTwo;

    @BeforeEach
    void init() throws IOException {
        fileTest = new MockMultipartFile("filename",
                "testFile.txt",
                "text/play",
                "test".getBytes());

        file = File.builder()
                .filename(fileTest.getName())
                .type(fileTest.getContentType())
                .data(fileTest.getBytes()).build();

        fileTwo = File.builder()
                .id(1)
                .filename("filenameTwo")
                .type(MediaType.TEXT_PLAIN_VALUE)
                .build();
    }

    @Test
    void fileSaveTest() throws IOException {
        when(fileRepository.save(Mockito.any(File.class))).thenReturn(file);
        file.setFilename("file");

        fileService.fileSave(fileTest, "file");

        Mockito.verify(fileRepository, Mockito.times(1)).save(file);
        assertEquals("file", file.getFilename());
        assertEquals(fileTest.getBytes(), file.getData());
        assertEquals(fileTest.getContentType(), file.getType());
    }

    @DisplayName("File с таким именем существует.")
    @Test
    void fileSaveThrows() {
        when(fileRepository.findByFilename(file.getFilename())).thenReturn(Optional.of(file));

        assertThrows(FileBadRequestException.class, () -> fileService.fileSave(fileTest, file.getFilename()),
                "Файл c таким именем уже существует!");
    }

    @Test
    void downloadFileTest() throws JsonProcessingException {
        when(fileRepository.findByFilename(Mockito.anyString())).thenReturn(Optional.of(file));

        var newFileTest = fileService.downloadFile(file.getFilename());

        assertNotNull(newFileTest);
        assertEquals("filename", newFileTest.getFilename());
    }

    @DisplayName("Отсутствие file в БД.")
    @Test
    void downloadFileThrows() {
        when(fileRepository.findByFilename(file.getFilename())).thenReturn(Optional.empty());

        assertThrows(FileBadRequestException.class, () -> fileService.downloadFile(file.getFilename()),
                String.format("Файл  %s не найден!", file.getFilename()));

    }

    @Test
    void updateFileTest() {

        when(fileRepository.findByFilename(Mockito.anyString())).thenReturn(Optional.of(file));
        when(fileRepository.save(Mockito.any(File.class))).thenReturn(file);
        file.setFilename("newFilename");

        var newTest = fileService.updateFile("filename", "newFilename");

        assertNotNull(newTest);
        assertEquals("newFilename", newTest.getFilename());
    }

    @DisplayName("File не найден в БД.")
    @Test
    void updateFileThrows() {
        when(fileRepository.findByFilename(file.getFilename())).thenReturn(Optional.empty());

        assertThrows(FileBadRequestException.class, () -> fileService.updateFile(file.getFilename(), Mockito.anyString()),
                (String.format("Файл  %s не найден!", file.getFilename())));
    }

    @Test
    void getFileTest() {

        when(fileRepository.findAll()).thenReturn(List.of(file, fileTwo));

        var testFile = fileService.getFile(2);

        assertNotNull(testFile);
        Assertions.assertThat(testFile.size()).isEqualTo(2);
    }

    @Test
    void deleteFileTest() throws IOException {

        when(fileRepository.findByFilename(Mockito.anyString())).thenReturn(Optional.of(fileTwo));
        Mockito.doNothing().when(fileRepository).deleteById(Mockito.anyInt());

        fileService.deleteFile(file.getFilename());

        Mockito.verify(fileRepository).deleteById(1);
        Mockito.verify(fileRepository, Mockito.times(1)).deleteById(1);
    }

}
