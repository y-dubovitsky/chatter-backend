package ru.ydubovitsky.chatter.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ydubovitsky.chatter.dto.UserDto;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.facade.UserFacade;
import ru.ydubovitsky.chatter.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin //TODO Позволяет обращаться с локальной машины к серверу
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;

    public UserController(UserService userService, UserFacade userFacade) {
        this.userService = userService;
        this.userFacade = userFacade;
    }

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userFacade.userToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfileById(@PathVariable("userId")  String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDto userDto = userFacade.userToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);

        if(currentUser.getId().equals(userDto.getId())) {
            User userFromDb = userService.updateUser(userDto, principal);
            UserDto updatedUser = userFacade.userToUserDto(userFromDb);

            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

}
