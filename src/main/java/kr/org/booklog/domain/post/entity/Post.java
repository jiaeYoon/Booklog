package kr.org.booklog.domain.post.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

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
    private LocalDateTime readStart;

    @NotNull
    private LocalDateTime readEnd;

    @NotNull
    private LocalDateTime postAt;

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

    public void setUser(User user) {
        this.user = user;
    }
}
