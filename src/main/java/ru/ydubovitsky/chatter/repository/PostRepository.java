package ru.ydubovitsky.chatter.repository;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ydubovitsky.chatter.entity.Post;
import ru.ydubovitsky.chatter.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByCreateTimeDesc(User user);

    List<Post> findAllByOrderByCommentsDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);

}
