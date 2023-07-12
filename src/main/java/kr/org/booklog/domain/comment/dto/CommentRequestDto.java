package kr.org.booklog.domain.comment.dto;

import kr.org.booklog.domain.comment.entity.Comment;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {

    private User user;
    private Long userId;
    private Post post;
    private Long postId;
    private String content;

    @Builder
    public CommentRequestDto(Long userId, Long postId, String content) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }

    public Comment toEntity() {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }
}
