package kr.org.booklog.domain.user.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.config.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String name;

    @NotNull
    @Column(columnDefinition = "varchar(255)")
    private String password;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String nickname;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private OAuthType join_type;

    @Builder
    public User(String name, String password, String nickname, String email, OAuthType join_type) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.join_type = join_type;
    }
}
