package ru.ydubovitsky.chatter.payload.request;

import lombok.Data;
import ru.ydubovitsky.chatter.annotations.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SingUpRequest { //Объект, который мы передаем на сервер для регистрации

    @Email(message = "Invalid email value")
    @NotBlank(message = "User must have email")
//    @ValidEmail
    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @NotEmpty
    private String username;

    @NotEmpty
    @Size(min = 6)
    private String password;

    private String confirmPassword;
}

