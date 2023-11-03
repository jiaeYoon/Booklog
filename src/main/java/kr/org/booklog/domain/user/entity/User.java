package kr.org.booklog.domain.user.entity;

import com.sun.istack.NotNull;
import kr.org.booklog.config.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String name;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String nickname;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Builder
    public User(String name, String nickname, String email, Role role) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    public void addInfo(String nickname) {
        this.nickname = nickname;
        this.role = Role.USER;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
