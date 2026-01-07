package ru.hogwarts.school.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Avatar> uploadAvatar(@RequestParam Long studentId, @RequestParam MultipartFile file) throws IOException {
        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok(avatar);
    }

    @GetMapping("/{studentId}/from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long studentId) {
        Avatar avatar = avatarService.findAvatar(studentId);
        return ResponseEntity.ok().header("Content-Type", avatar.getMediaType()).body(avatar.getData());
    }

    @GetMapping("/{studentId}/from-file")
    public ResponseEntity<byte[]> getAvatarFromFile(@PathVariable Long studentId) throws IOException {
        Avatar avatar = avatarService.findAvatar(studentId);
        byte[] data = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(avatar.getFilePath()));
        return ResponseEntity.ok().header("Content-Type", avatar.getMediaType()).body(data);
    }

    @GetMapping
    public ResponseEntity<Page<Avatar>> getAllAvatars(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        Page<Avatar> avatars = avatarService.getAllAvatars(page, size);
        return ResponseEntity.ok(avatars);
    }
}