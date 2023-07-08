package kr.org.booklog.domain.post.dto;

import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {

    private Long userId;
    private User user;
    private String postTitle;
    private String bookTitle;
    private String bookWriter;
    private LocalDate readStart;
    private LocalDate readEnd;
    private LocalDate postAt;
    private Integer rating;
    private String content;
    private Integer likesCnt;
    private Integer commentsCnt;

    @Builder
    public PostRequestDto(Long userId, String postTitle, String bookTitle, String bookWriter,
                          LocalDate readStart, LocalDate readEnd, LocalDate postAt,
                          Integer rating, String content) {
        this.userId = userId;
        this.postTitle = postTitle;
        this.bookTitle = bookTitle;
        this.bookWriter = bookWriter;
        this.readStart = readStart;
        this.readEnd = readEnd;
        this.postAt = postAt;
        this.rating = rating;
        this.content = content;
    }

    public PostRequestDto(String postTitle, String bookTitle, String bookWriter,
                          LocalDate readStart, LocalDate readEnd, LocalDate postAt,
                          Integer rating, String content) {
        this.postTitle = postTitle;
        this.bookTitle = bookTitle;
        this.bookWriter = bookWriter;
        this.readStart = readStart;
        this.readEnd = readEnd;
        this.postAt = postAt;
        this.rating = rating;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .user(user)
                .postTitle(postTitle)
                .bookTitle(bookTitle)
                .bookWriter(bookWriter)
                .readStart(readStart)
                .readEnd(readEnd)
                .postAt(postAt)
                .rating(rating)
                .content(content)
                .likesCnt(0)
                .commentsCnt(0)
                .build();
    }
}
