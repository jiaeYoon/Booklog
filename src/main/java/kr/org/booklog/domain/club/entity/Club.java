package kr.org.booklog.domain.club.entity;

import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_club_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String clubName;

    @NotNull
    private int capacity;

    @NotNull
    private String introduction;

    @Builder
    public Club(User user, String clubName, int capacity, String introduction) {
        this.user = user;
        this.clubName = clubName;
        this.capacity = capacity;
        this.introduction = introduction;
    }
}