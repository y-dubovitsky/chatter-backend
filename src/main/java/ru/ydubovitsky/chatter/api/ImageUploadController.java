package ru.ydubovitsky.chatter.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ydubovitsky.chatter.entity.ImageModel;
import ru.ydubovitsky.chatter.service.ImageUploadService;

import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/image")
public class ImageUploadController {

    private ImageUploadService imageUploadService;

    public ImageUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadImageUserProfile(
            @RequestParam(name = "file") MultipartFile file,
            Principal principal
    ) throws IOException {
        imageUploadService.uploadUserProfileImage(file, principal);

        return ResponseEntity.ok("Profile image uploaded successfully!");
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<Object> uploadPostImage(
            @RequestParam(name = "file") MultipartFile file,
            @PathVariable(name = "postId") String postId,
            Principal principal
    ) throws IOException {
        imageUploadService.uploadPostImage(file, Long.parseLong(postId), principal);

        return ResponseEntity.ok("Post image uploaded successfully!");
    }

    @GetMapping("/profile")
    public ResponseEntity<ImageModel> getProfileImage(Principal principal) {
        ImageModel userProfileImage = imageUploadService.getUserProfileImage(principal);

        return ResponseEntity.ok(userProfileImage);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModel> getPostImage(@PathVariable(name = "postId") String postId) {
        ImageModel postImage = imageUploadService.getPostImage(Long.parseLong(postId));

        return ResponseEntity.ok(postImage);
    }
}
