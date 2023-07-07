package kr.org.booklog.domain.post.dto;

import kr.org.booklog.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String writerNickname;
    private String postTitle;
    private String bookTitle;
    private String bookWriter;
    private LocalDate readStart;
    private LocalDate readEnd;
    private LocalDate postAt;
    private Integer rating;
    private String content;
    private Boolean isLike;
    private Integer likesCnt;
    private Integer commentsCnt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.writerNickname = post.getUser().getNickname();
        this.postTitle = post.getPostTitle();
        this.bookTitle = post.getBookTitle();
        this.bookWriter = post.getBookWriter();
        this.readStart = post.getReadStart();
        this.readEnd = post.getReadEnd();
        this.postAt = post.getPostAt();
        this.rating = post.getRating();
        this.content = post.getContent();
        this.likesCnt = post.getLikesCnt();
        this.commentsCnt = post.getCommentsCnt();
    }
}
