package ru.ydubovitsky.chatter.facade;

import org.springframework.stereotype.Component;
import ru.ydubovitsky.chatter.dto.PostDto;
import ru.ydubovitsky.chatter.dto.UserDto;
import ru.ydubovitsky.chatter.entity.Post;
import ru.ydubovitsky.chatter.entity.User;

@Component
public class PostFacade {

    public PostDto postToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setName(post.getName());
        postDto.setDescription(post.getDescription());
        postDto.setLikes(post.getLikes());
        postDto.setLocation(post.getLocation());
        postDto.setUsername(post.getUser().getUsername()); //FIXME ?
        postDto.setUsersLikes(post.getLikedUsers());

        return postDto;
    }

}
