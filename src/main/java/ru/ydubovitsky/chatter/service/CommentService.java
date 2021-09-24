package ru.ydubovitsky.chatter.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ydubovitsky.chatter.dto.CommentDto;
import ru.ydubovitsky.chatter.entity.Comment;
import ru.ydubovitsky.chatter.entity.Post;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.exceptions.CommentException;
import ru.ydubovitsky.chatter.exceptions.PostNotFoundException;
import ru.ydubovitsky.chatter.repository.CommentRepository;
import ru.ydubovitsky.chatter.repository.PostRepository;
import ru.ydubovitsky.chatter.repository.UserRepository;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(CommentDto commentDto, Long postId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new CommentException("Post not found!"));

        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());

//        post.getComments().add(comment); //FIXME Нужно ли?
        postRepository.save(post);

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not foun!"));

        List<Comment> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteComment(Long commentId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        commentRepository.findById(commentId)
                .ifPresent(comment -> {
                    if(comment.getUsername().equals(user.getName())) {
                        commentRepository.delete(comment);
                    }
                });
    }

    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name  " + username + " not found!"));
    }
}
