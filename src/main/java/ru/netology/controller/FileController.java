package ru.netology.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.dto.ResponseFile;
import ru.netology.service.FileService;

import java.io.IOException;
import java.util.List;

@RestController
public class FileController {

    private static final String URL = "/file";
    private static final String PARAM_FILE = "filename";

    @Autowired
    private FileService fileService;

    //  Добавления файла.
    @PostMapping(URL)
    @Secured("ADMIN")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam(PARAM_FILE) String filename) throws IOException {
        fileService.fileSave(file, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //  Изменения файла.
    @PutMapping(URL)
    @Secured("ADMIN")
    public ResponseEntity<?> updateFile(@RequestParam(PARAM_FILE) String filename,
                                        @RequestParam("newFilename") String newFilename) {
        fileService.updateFile(filename, newFilename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //  Скачивания файла.
    @GetMapping(URL)
    @Secured({"ADMIN", "USER"})
    public ResponseEntity<byte[]> downloadFile(@RequestParam(PARAM_FILE) String filename) {
        ResponseFile responseFile = fileService.downloadFile(filename);
        if (responseFile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(responseFile.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + responseFile.getFilename() + "\"")
                .body(responseFile.getData());
    }

    // Удаление файла по имени файла.
    @DeleteMapping(URL)
    @Secured("ADMIN")
    public ResponseEntity<?> deleteFile(@RequestParam(PARAM_FILE) String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //    Получение списка файлов.
    @GetMapping("/list")
    @Secured({"ADMIN", "USER"})
    public ResponseEntity<List<ResponseFile>> getFiles(@RequestParam("limit") int limit) {
        return ResponseEntity.ok(fileService.getFile(limit));
    }
}
