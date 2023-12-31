package kr.org.booklog.domain.post.dto;

import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private Long postWriterId;
    private String nickname;
    private String postTitle;
    private String bookTitle;
    private String bookWriter;
    private LocalDate readStart;
    private LocalDate readEnd;
    private String postAt;
    private Integer rating;
    private String content;
    private Long likeId;
    private Boolean isLike;
    private Integer likesCnt;
    private Integer commentsCnt;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.postWriterId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.postTitle = post.getPostTitle();
        this.bookTitle = post.getBookTitle();
        this.bookWriter = post.getBookWriter();
        this.readStart = post.getReadStart();
        this.readEnd = post.getReadEnd();
        this.postAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(post.getPostAt());
        this.rating = post.getRating();
        this.content = post.getContent();
        this.likesCnt = post.getLikesCnt();
        this.commentsCnt = post.getCommentsCnt();
    }
}
