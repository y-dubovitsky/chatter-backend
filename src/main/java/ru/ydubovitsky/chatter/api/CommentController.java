package ru.ydubovitsky.chatter.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ydubovitsky.chatter.dto.CommentDto;
import ru.ydubovitsky.chatter.dto.PostDto;
import ru.ydubovitsky.chatter.entity.Comment;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.facade.CommentFacade;
import ru.ydubovitsky.chatter.service.CommentService;
import ru.ydubovitsky.chatter.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final UserService userService;

    public CommentController(CommentService commentService, CommentFacade commentFacade, UserService userService) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.userService = userService;
    }

    @PostMapping("/{postId}/add")
    public ResponseEntity<CommentDto> addComment(
            @Valid @RequestBody CommentDto commentDto,
            @PathVariable(name = "postId") String postId,
            Principal principal
    ) {
        User user = userService.getCurrentUser(principal);

        if (!commentDto.getUsername().equals(user.getName())) { //TODO Убрать логику из контроллера!
            return ResponseEntity.badRequest().build();
        }

        Comment comment = commentService.saveComment(commentDto, Long.parseLong(postId), principal);
        CommentDto createdComment = commentFacade.commentToCommentDto(comment);

        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentsToPost(@PathVariable(name = "postId") String postId) {
        List<CommentDto> allCommentToPost = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(comment -> commentFacade.commentToCommentDto(comment))
                .collect(Collectors.toList());

        return ResponseEntity.ok(allCommentToPost);
    }

    @PostMapping("/{commentId}/delete") //FIXME -> @DeleteMapping
    public ResponseEntity<Object> deletePost(
            @PathVariable(name = "commentId") String commentId,
            Principal principal
    ) {
        commentService.deleteComment(Long.parseLong(commentId), principal);

        return ResponseEntity.ok("Post was deleted");
    }
}
