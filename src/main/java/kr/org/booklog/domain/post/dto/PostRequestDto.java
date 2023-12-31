package kr.org.booklog.domain.post.dto;

import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime postAt;
    private Integer rating;
    private String content;

    @Builder
    public PostRequestDto(Long userId, String postTitle, String bookTitle, String bookWriter,
                          LocalDate readStart, LocalDate readEnd, LocalDateTime postAt,
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
                .build();
    }
}
