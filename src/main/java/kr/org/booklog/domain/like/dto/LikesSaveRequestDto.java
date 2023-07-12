package kr.org.booklog.domain.like.dto;

import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LikesSaveRequestDto {

    private User user;
    private Long userId;
    private Post post;
    private Boolean isLike;

    @Builder
    public LikesSaveRequestDto(Long userId) {
        this.userId = userId;
        this.isLike = Boolean.TRUE;
    }

    public Likes toEntity(){
        return Likes.builder()
                .user(user)
                .post(post)
                .isLike(Boolean.TRUE)
                .build();
    }
}
