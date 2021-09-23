package ru.ydubovitsky.chatter.facade;

import org.springframework.stereotype.Component;
import ru.ydubovitsky.chatter.dto.CommentDto;
import ru.ydubovitsky.chatter.dto.PostDto;
import ru.ydubovitsky.chatter.entity.Comment;
import ru.ydubovitsky.chatter.entity.Post;

@Component
public class CommentFacade {

    public CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setComment(comment.getComment());
        commentDto.setUsername(comment.getUsername());

        return commentDto;
    }

}
