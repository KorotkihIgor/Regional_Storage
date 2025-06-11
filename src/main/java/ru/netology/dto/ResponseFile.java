package ru.netology.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseFile {
    private String filename;
    private String type;
    private byte[] data;

    public ResponseFile(String filename, String type) {
        this.filename = filename;
        this.type = type;
    }
}
