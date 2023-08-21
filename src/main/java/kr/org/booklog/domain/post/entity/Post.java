package kr.org.booklog.domain.post.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.domain.comment.entity.Comment;
import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

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

    @Builder
    public Post(User user, String postTitle, String bookTitle, String bookWriter,
                LocalDate readStart, LocalDate readEnd, LocalDateTime postAt, Integer rating, String content, Integer likesCnt, Integer commentsCnt) {
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

    public void addLike() {
        this.likesCnt += 1;
    }

    public void deleteLike() {
        this.likesCnt -= 1;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.commentsCnt += 1;
    }

    public void deleteComment(Comment comment) {
        this.getComments().remove(comment);
        this.commentsCnt = commentsCnt - 1;
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
