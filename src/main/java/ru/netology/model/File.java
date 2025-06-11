package ru.netology.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(name = "Files")
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;
    private String type;
    @Lob
    private byte[] data;

    @CreationTimestamp
    @Column(name = "upload_time")
    LocalDateTime uploadTime;


    public File(String filename, String type, byte[] data) {
        this.filename = filename;
        this.type = type;
        this.data = data;
    }


}
