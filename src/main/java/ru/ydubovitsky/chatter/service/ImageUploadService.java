package ru.ydubovitsky.chatter.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.ydubovitsky.chatter.entity.ImageModel;
import ru.ydubovitsky.chatter.entity.Post;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.exceptions.PostNotFoundException;
import ru.ydubovitsky.chatter.repository.ImageModelRepository;
import ru.ydubovitsky.chatter.repository.PostRepository;
import ru.ydubovitsky.chatter.repository.UserRepository;
import ru.ydubovitsky.chatter.utils.ImageUtils;

import java.io.IOException;
import java.security.Principal;

@Service
public class ImageUploadService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadService.class);

    private ImageModelRepository imageModelRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    public ImageUploadService(ImageModelRepository imageModelRepository, UserRepository userRepository, PostRepository postRepository) {
        this.imageModelRepository = imageModelRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ImageModel uploadUserProfileImage(MultipartFile multipartFile, Principal principal) throws IOException{
        User user = getUserFromPrincipal(principal);
        LOGGER.info("Uploading image");

        ImageModel userProfileImage = imageModelRepository.findByUserId(user.getId())
                .orElse(null);

        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageModelRepository.delete(userProfileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());

        imageModel.setByteImage(ImageUtils.compressImage(multipartFile.getBytes()));
        imageModel.setName(multipartFile.getOriginalFilename());

        return imageModelRepository.save(imageModel);
    }

    public ImageModel getUserProfileImage(Principal principal) {
        User user = getUserFromPrincipal(principal);

        ImageModel imageModel = imageModelRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setByteImage(ImageUtils.deCompressImage(imageModel.getByteImage()));
        }

        return imageModel;
    }

    public ImageModel uploadPostImage(MultipartFile multipartFile, Long postId, Principal principal) throws IOException {
        User user = getUserFromPrincipal(principal);
        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new FileUploadException("You can`t upload image to outher post"));

        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(post.getId());
        imageModel.setByteImage(ImageUtils.compressImage(multipartFile.getBytes()));
        imageModel.setName(multipartFile.getOriginalFilename());
        LOGGER.info("Uploading post image");

        return imageModelRepository.save(imageModel);
    }

    public ImageModel getPostImage(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found!"));

        ImageModel imageModel = imageModelRepository.findByPostId(post.getId())
                .orElse(null);

        if(!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setByteImage(ImageUtils.deCompressImage(imageModel.getByteImage()));
        }

        return imageModel;
    }

    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name  " + username + " not found!"));
    }
}
