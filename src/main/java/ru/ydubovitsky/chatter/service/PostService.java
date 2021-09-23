package ru.ydubovitsky.chatter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ydubovitsky.chatter.dto.PostDto;
import ru.ydubovitsky.chatter.entity.ImageModel;
import ru.ydubovitsky.chatter.entity.Post;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.exceptions.ImageNotFoundException;
import ru.ydubovitsky.chatter.exceptions.PostNotFoundException;
import ru.ydubovitsky.chatter.repository.ImageModelRepository;
import ru.ydubovitsky.chatter.repository.PostRepository;
import ru.ydubovitsky.chatter.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageModelRepository imageModelRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageModelRepository imageModelRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageModelRepository = imageModelRepository;
    }

    public Post createPost(PostDto postDto, Principal principal) { //PostDto - это сущность, которую мы принимаем от пользователя
        User user = getUserFromPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setName(postDto.getName());
        post.setDescription(postDto.getDescription());
        post.setLikes(0);

        LOGGER.info("Save new post " + post.getName() + " by " + user.getEmail());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostByIdForCurrentUser(Long id, Principal principal) {
        User user = getUserFromPrincipal(principal);

        return postRepository.findPostByIdAndUser(id, user)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    public List<Post> getAllPostsForCurrentUser(Principal principal) {
        User user = getUserFromPrincipal(principal);

        return postRepository.findAllByUserOrderByCreateTimeDesc(user);
    }

    public Post likePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!"));

        Optional<String> isUserLike = post.getLikedUsers() // Если пользователь уже лайкнул пост
                .stream()
                .filter(user -> user.equals(username)).findFirst();

        if (isUserLike.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        User user = getUserFromPrincipal(principal);

        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Optional<ImageModel> imageModel = imageModelRepository.findByPostId(post.getId()); // Если у поста есть картинка! Но можно на уровне БД настроить Каскадирование!

        postRepository.delete(post);

        imageModel.ifPresent(im -> imageModelRepository.delete(im));
    }

    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name  " + username + " not found!"));
    }
}
