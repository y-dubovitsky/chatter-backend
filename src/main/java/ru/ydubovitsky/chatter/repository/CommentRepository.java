package ru.ydubovitsky.chatter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ydubovitsky.chatter.entity.Comment;
import ru.ydubovitsky.chatter.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    Optional<Comment> findByUserId(Long userId);

    List<Comment> findAllByPost(Post post);

}
