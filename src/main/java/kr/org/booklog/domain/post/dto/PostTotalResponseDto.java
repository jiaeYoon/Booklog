package kr.org.booklog.domain.post.dto;

import kr.org.booklog.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class PostTotalResponseDto {
    private Long Id;
    private String nickname;
    private String postTitle;
    private String bookTitle;
    private String postAt;
    private Integer rating;
    private String content;
    private Boolean isLike;
    private Integer likesCnt;
    private Integer commentsCnt;

    public PostTotalResponseDto(Post post, Boolean isLike) {
        this.Id = post.getId();
        this.nickname = post.getUser().getNickname();
        this.postTitle = post.getPostTitle();
        this.bookTitle = post.getBookTitle();
        this.postAt = DateTimeFormatter.ofPattern("yyyy.MM.dd").format(post.getPostAt());
        this.rating = post.getRating();
        this.content = post.getContent();
        this.isLike = isLike;
        this.likesCnt = post.getLikesCnt();
        this.commentsCnt = post.getCommentsCnt();
    }
}
