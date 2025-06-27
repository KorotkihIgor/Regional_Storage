package ru.netology.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.dto.ResponseFile;
import ru.netology.service.FileService;

import java.io.IOException;
import java.util.List;


@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    //    Обновление имени файла.
    @PutMapping("/file")
    public ResponseEntity<?> updateFile(@RequestParam("filename") String filename, @RequestBody String newFilename) {
        fileService.updateFile(filename, newFilename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //   Загрузка файлов в базу данных.
    @PostMapping("/file")
    public ResponseEntity<?> addFile(@RequestParam("filename") String filename,
                                     @RequestParam("file") MultipartFile file) throws IOException {
        fileService.fileSave(filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //    Просмотр файла.
    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename) {
        ResponseFile responseFile = fileService.downloadFile(filename);
        if (responseFile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(responseFile.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + responseFile.getFilename() + "\"")
                .body(responseFile.getData());
    }

    //    Удаление файла по имени файла.
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //    Получение списка файлов.
    @GetMapping("/list")
    public ResponseEntity<List<ResponseFile>> getAllFile(@RequestParam("limit") int limit) {
        return ResponseEntity.ok(fileService.getFile(limit));
    }

}
