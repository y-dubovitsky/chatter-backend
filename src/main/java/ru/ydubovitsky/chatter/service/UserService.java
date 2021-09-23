package ru.ydubovitsky.chatter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ydubovitsky.chatter.dto.UserDto;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.entity.enums.ERole;
import ru.ydubovitsky.chatter.payload.request.SingUpRequest;
import ru.ydubovitsky.chatter.repository.UserRepository;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SingUpRequest userFromReq) throws Exception { // Поступает dto (SingUpRequest)
        User user = new User();
        user.setName(userFromReq.getName());
        user.setEmail(userFromReq.getEmail());
        user.setFirstname(userFromReq.getFirstname());
        user.setLastname(userFromReq.getLastname());
        user.setUsername(userFromReq.getUsername());
        user.setPassword(passwordEncoder.encode(userFromReq.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            LOGGER.info("Saving user " + user.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Error registration!" + e.getMessage());
            //FIXME Выбросить свое исключение
            throw new Exception();
        }
    }

    public User updateUser(UserDto userDto, Principal principal) { // Principal - это объект из security, содержит данные о пользователе
        User user = getUserFromPrincipal(principal);
        user.setName(userDto.getName());
        user.setFirstname(userDto.getLastname());
        user.setLastname(userDto.getLastname());
        user.setBio(userDto.getBio());

        userRepository.save(user);
        return user;
    }

    public User getCurrentUser(Principal principal) {
        return getUserFromPrincipal(principal);
    }

    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name  " + username + " not found!"));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}
