package kr.org.booklog.domain.comment.dto;

import kr.org.booklog.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentResponseDto {

    private Long id;
    private String nickname;
    private String content;
    private LocalDate commentAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.commentAt = LocalDate.from(comment.getCreatedAt());
    }
}
