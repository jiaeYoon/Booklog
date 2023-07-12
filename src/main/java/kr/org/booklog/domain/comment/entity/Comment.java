package kr.org.booklog.domain.comment.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.config.BaseTimeEntity;
import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Column(columnDefinition = "text")
    private String content;

    @Builder
    Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
