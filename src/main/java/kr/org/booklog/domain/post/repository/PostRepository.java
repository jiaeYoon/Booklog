package kr.org.booklog.domain.post.repository;

import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.user order by post_at desc")
    List<Post> findAllPosts();

    @Query("select p from Post p join fetch p.user where p.id = :postId")
    Optional<PostResponseDto> findPostById(@Param("postId") Long id);
}
