package kr.org.booklog.domain.like.repository;

import kr.org.booklog.domain.like.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserIdAndPostId(Long userId, Long postId);
}
