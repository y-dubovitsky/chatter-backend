package ru.ydubovitsky.chatter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ydubovitsky.chatter.entity.ImageModel;

import java.util.Optional;

@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByUserId(Long userId);

    Optional<ImageModel> findByPostId(Long postId);

}
