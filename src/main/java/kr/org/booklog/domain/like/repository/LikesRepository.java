package kr.org.booklog.domain.like.repository;

import kr.org.booklog.domain.like.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("select l from Likes l where post_id = :postId")
    List<Likes> findByPostId(@Param("postId") Long postId);

    Optional<Likes> findByUserIdAndPostId(Long userId, Long postId);

    @Query("select l from Likes l where user_id = :userId and post_id = :postId")
    Likes isLike(@Param("userId") Long userId, @Param("postId") Long postId);
}
