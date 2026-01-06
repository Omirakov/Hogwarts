package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final Path avatarDir;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, @Value("${app.avatar.dir}") String avatarPath) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.avatarDir = Paths.get(avatarPath);
        try {
            Files.createDirectories(avatarDir);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для аватаров: " + avatarDir, e);
        }
    }

    @Transactional
    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Студент с ID " + studentId + " не найден"));

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Файл должен быть изображением");
        }

        byte[] bytes = file.getBytes();

        Optional<Avatar> existingAvatar = avatarRepository.findByStudentId(studentId);
        existingAvatar.ifPresent(avatar -> {
            try {
                Files.deleteIfExists(avatarDir.resolve(avatar.getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            avatarRepository.delete(avatar);
        });

        String fileName = "avatar-" + studentId + "." + getFileExtension(file.getOriginalFilename());
        Path filePath = avatarDir.resolve(fileName);
        Files.write(filePath, bytes);

        Avatar avatar = existingAvatar.orElse(new Avatar());
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(bytes);
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("Аватар для студента с ID " + studentId + " не найден"));
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "png" : filename.substring(lastDot + 1);
    }
}