package kr.org.booklog.domain.comment.repository;

import kr.org.booklog.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where post_id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);
}
