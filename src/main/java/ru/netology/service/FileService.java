package ru.netology.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.dto.ResponseFile;
import ru.netology.exception.FileNotFoundException;
import ru.netology.model.File;
import ru.netology.repository.FileRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public File updateFile(String filename, String newFilename) {
        File file = fileRepository.findByFilename(filename)
                .orElseThrow(() -> new FileNotFoundException(String.format("Файл  %s не найден!", filename)));
        file.setFilename(newFilename);
        return fileRepository.save(file);
    }

    @Transactional
    public void deleteFile(String filename) {
        File file = fileRepository.findByFilename(filename)
                .orElseThrow(() -> new FileNotFoundException(String.format("Файл  %s не найден!", filename)));
        fileRepository.deleteById(file.getId());

    }

    @Transactional
    public ResponseFile downloadFile(String filename) {
        File file = fileRepository.findByFilename(filename)
                .orElseThrow(() -> new FileNotFoundException(String.format("Файл  %s не найден!", filename)));

        return ResponseFile.builder()
                .filename(file.getFilename())
                .type(file.getType())
                .data(file.getData())
                .build();
    }

    @Transactional
    public File fileSave(String filename, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileNotFoundException("Файл не загружен!");
        }
        if (fileRepository.findByFilename(filename).isPresent()) {
            throw new FileNotFoundException(String.format("Файл с именем %s уже существует!", filename));
        }
        return fileRepository.save(new File(filename, file.getContentType(), file.getBytes()));
    }

    public List<ResponseFile> getFile(int limit) {
        Optional<List<File>> listFile = Optional.of(fileRepository.findAll());
        return listFile.get().stream().map(fr -> new ResponseFile(fr.getFilename(), fr.getType()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}