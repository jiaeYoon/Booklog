package kr.org.booklog.domain.post.repository;

import kr.org.booklog.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
