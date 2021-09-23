package ru.ydubovitsky.chatter.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDto {

    private Long id;
    private String name;
    private String description;
    private String location;
    private String username;
    private Integer likes;
    private Set<String> usersLikes;

}
