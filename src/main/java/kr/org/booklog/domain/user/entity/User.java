package kr.org.booklog.domain.user.entity;

import kr.org.booklog.config.BaseTimeEntity;
import kr.org.booklog.domain.memberRegister.MemberRegister;
import kr.org.booklog.domain.user.dto.JoinedClubResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    private final List<MemberRegister> memberRegisters = new ArrayList<>();

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

    public void addMemberRegister(MemberRegister memberRegister) {
        memberRegisters.add(memberRegister);
    }

    public List<JoinedClubResponseDto> getJoinedClubs() {
        List<JoinedClubResponseDto> clubs = new ArrayList<>();
        for (MemberRegister memberRegister : memberRegisters) {
            clubs.add(new JoinedClubResponseDto(memberRegister.getClub()));
        }
        return clubs;
    }
}
