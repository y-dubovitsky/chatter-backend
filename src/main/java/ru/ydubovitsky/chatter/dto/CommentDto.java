package ru.ydubovitsky.chatter.dto;

import lombok.Data;

@Data
public class CommentDto {

    private Long id;
    private String comment;
    private String username;

}
