package ru.ydubovitsky.chatter.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "INVALID USERNAME";
        this.password = "INVALID PASSWORD";
    }
}
