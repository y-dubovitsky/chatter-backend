package ru.ydubovitsky.chatter.facade;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.ydubovitsky.chatter.dto.UserDto;
import ru.ydubovitsky.chatter.entity.User;

@Component
public class UserFacade {

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setBio(user.getBio());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());

        return userDto;
    }

}
