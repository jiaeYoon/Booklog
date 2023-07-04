package kr.org.booklog.domain.user.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.config.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

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
}
