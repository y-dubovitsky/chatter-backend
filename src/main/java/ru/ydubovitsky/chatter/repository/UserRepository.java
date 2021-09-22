package ru.ydubovitsky.chatter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ydubovitsky.chatter.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username); // Optional - обертка над null

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

}
