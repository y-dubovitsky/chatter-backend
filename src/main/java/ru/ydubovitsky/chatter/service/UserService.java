package ru.ydubovitsky.chatter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ydubovitsky.chatter.entity.User;
import ru.ydubovitsky.chatter.entity.enums.ERole;
import ru.ydubovitsky.chatter.payload.request.SingUpRequest;
import ru.ydubovitsky.chatter.repository.UserRepository;

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
        user.setEmail(userFromReq.getEmail());
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
}
