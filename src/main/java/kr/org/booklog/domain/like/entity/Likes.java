package kr.org.booklog.domain.like.entity;

import kr.org.booklog.config.BaseTimeEntity;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Likes extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Likes(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public Likes(Boolean isLike) {
        this.user = new User();
        this.post = new Post();
    }
}
