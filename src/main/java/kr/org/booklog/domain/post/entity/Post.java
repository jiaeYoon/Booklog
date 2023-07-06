package kr.org.booklog.domain.post.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
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
                LocalDate readStart, LocalDate readEnd, LocalDate postAt, Integer rating, String content) {
        this.user = user;
        this.postTitle = postTitle;
        this.bookTitle = bookTitle;
        this.bookWriter = bookWriter;
        this.readStart = readStart;
        this.readEnd = readEnd;
        this.postAt = postAt;
        this.rating = rating;
        this.content = content;
    }

}
