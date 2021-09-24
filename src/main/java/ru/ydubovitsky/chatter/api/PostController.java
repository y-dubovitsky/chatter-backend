package ru.ydubovitsky.chatter.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ydubovitsky.chatter.dto.PostDto;
import ru.ydubovitsky.chatter.entity.Post;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.facade.PostFacade;
import ru.ydubovitsky.chatter.service.ImageUploadService;
import ru.ydubovitsky.chatter.service.PostService;
import ru.ydubovitsky.chatter.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostFacade postFacade;

    public PostController(PostService postService, UserService userService, PostFacade postFacade) {
        this.postService = postService;
        this.userService = userService;
        this.postFacade = postFacade;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addPost(@Valid @RequestBody PostDto postDto, Principal principal) {
        User user = userService.getCurrentUser(principal);

        Post post = postService.createPost(postDto, principal);
        PostDto createdPostDto = postFacade.postToPostDto(post);

        return ResponseEntity.ok(createdPostDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> allPostDto = postService.getAllPosts()
                .stream()
                .map(post -> postFacade.postToPostDto(post))
                .collect(Collectors.toList());

        return ResponseEntity.ok(allPostDto);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDto>> getAllUserPosts(Principal principal) {
        List<PostDto> allUserPosts = postService.getAllPostsForCurrentUser(principal)
                .stream()
                .map(post -> postFacade.postToPostDto(post))
                .collect(Collectors.toList());

        return ResponseEntity.ok(allUserPosts);
    }

    @GetMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDto> likePost(
            @PathVariable(name = "postId") String postId,
            @PathVariable(name = "username") String username,
            Principal principal
    ) {
        User user = userService.getCurrentUser(principal);
        if(!user.getName().equals(username)) {
            return ResponseEntity.badRequest().build();
        }

        Post post = postService.likePost(Long.parseLong(postId), principal.getName());
        PostDto postDto = postFacade.postToPostDto(post);

        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/{postId}/{username}/delete") //TODO Переделать запросы согласно выполняемым функциям! @Get -> @Delete
    public ResponseEntity<Object> deletePost(
            @PathVariable(name="postId") String postId,
            @PathVariable(name="username") String username,
            Principal principal
    ) {
        if(!username.equals(principal.getName())) {
            return ResponseEntity.badRequest().build();
        }

        postService.deletePost(Long.parseLong(postId), principal);
        return ResponseEntity.ok().build();
    }
}
