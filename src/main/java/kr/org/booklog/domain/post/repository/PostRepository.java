package kr.org.booklog.domain.post.repository;

import kr.org.booklog.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.user order by post_at desc")
    List<Post> findAllPosts();
}
