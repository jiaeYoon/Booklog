package kr.org.booklog.domain.comment.repository;

import kr.org.booklog.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
