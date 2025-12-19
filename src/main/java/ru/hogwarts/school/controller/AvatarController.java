package ru.hogwarts.school.controller;

import org.springframework.http.MediaType;
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Avatar> uploadAvatar(@RequestParam Long studentId, @RequestParam MultipartFile file) throws IOException {
        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok(avatar);
    }

    @GetMapping(value = "/{studentId}/from-db", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long studentId) {
        Avatar avatar = avatarService.findAvatar(studentId);
        return ResponseEntity.ok().contentType(MediaType.valueOf(avatar.getMediaType())).body(avatar.getData());
    }

    @GetMapping(value = "/{studentId}/from-file", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getAvatarFromFile(@PathVariable Long studentId) throws IOException {
        Avatar avatar = avatarService.findAvatar(studentId);
        byte[] data = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(avatar.getFilePath()));
        return ResponseEntity.ok().contentType(MediaType.valueOf(avatar.getMediaType())).body(data);
    }
}