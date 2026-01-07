package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(nullable = false)
    private long fileSize;

    @Column(name = "media_type", nullable = false)
    private String mediaType;

    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    public Avatar() {
    }

    public Avatar(String filePath, long fileSize, String mediaType) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
    }

    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Avatar{" + "id=" + id + ", filePath='" + filePath + '\'' + ", fileSize=" + fileSize + ", mediaType='" + mediaType + '\'' + ", data=" + Arrays.toString(data) + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return fileSize == avatar.fileSize && Objects.equals(id, avatar.id) && Objects.equals(filePath, avatar.filePath) && Objects.equals(mediaType, avatar.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, fileSize, mediaType);
    }
}
