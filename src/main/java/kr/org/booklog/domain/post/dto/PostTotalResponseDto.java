package kr.org.booklog.domain.post.dto;

import kr.org.booklog.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostTotalResponseDto {
    private Long Id;
    private String nickname;
    private String postTitle;
    private String bookTitle;
    private LocalDate postAt;
    private Integer rating;
    private String content;
    private Integer likesCnt;
    private Integer commentsCnt;

    public PostTotalResponseDto(Post post) {
        this.Id = post.getId();
        this.nickname = post.getUser().getNickname();
        this.postTitle = post.getPostTitle();
        this.bookTitle = post.getBookTitle();
        this.postAt = post.getPostAt();
        this.rating = post.getRating();
        this.content = post.getContent();
        this.likesCnt = post.getLikesCnt();
        this.commentsCnt = post.getCommentsCnt();
    }
}
