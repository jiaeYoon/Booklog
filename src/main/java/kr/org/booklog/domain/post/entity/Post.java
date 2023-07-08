package kr.org.booklog.domain.post.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String postTitle;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String bookTitle;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String bookWriter;

    @NotNull
    private LocalDate readStart;

    @NotNull
    private LocalDate readEnd;

    @NotNull
    private LocalDate postAt;

    @NotNull
    @Column(columnDefinition = "tinyint")
    private Integer rating;

    @NotNull
    @Column(columnDefinition = "text")
    private String content;

    @NotNull
    @ColumnDefault("0")
    private Integer likesCnt;

    @NotNull
    @ColumnDefault("0")
    private Integer commentsCnt;

    @Builder
    public Post(User user, String postTitle, String bookTitle, String bookWriter,
                LocalDate readStart, LocalDate readEnd, LocalDate postAt, Integer rating, String content, Integer likesCnt, Integer commentsCnt) {
        this.user = user;
        this.postTitle = postTitle;
        this.bookTitle = bookTitle;
        this.bookWriter = bookWriter;
        this.readStart = readStart;
        this.readEnd = readEnd;
        this.postAt = postAt;
        this.rating = rating;
        this.content = content;
        this.likesCnt = likesCnt;
        this.commentsCnt = commentsCnt;
    }

    public void updateLikesCnt(Integer likesCnt) {
        this.likesCnt = likesCnt + 1;
    }

    public void updatePost(PostRequestDto requestDto) {
        this.postTitle = requestDto.getPostTitle();
        this.bookTitle = requestDto.getBookTitle();
        this.bookWriter = requestDto.getBookWriter();
        this.readStart = requestDto.getReadStart();
        this.readEnd = requestDto.getReadEnd();
        this.postAt = requestDto.getPostAt();
        this.rating = requestDto.getRating();
        this.content = requestDto.getContent();
    }

}
