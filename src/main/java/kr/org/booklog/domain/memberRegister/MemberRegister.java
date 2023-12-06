package kr.org.booklog.domain.memberRegister;

import kr.org.booklog.domain.club.entity.Club;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MemberRegister {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_club_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boook_club_id")
    private Club club;

    @Builder
    public MemberRegister(User member, Club club) {
        this.member = member;
        this.club = club;
    }

    public static MemberRegister register(Club club, User member) {

        MemberRegister memberRegister = new MemberRegister();
        memberRegister.club = club;
        memberRegister.member = member;

        club.addMemberRegister(memberRegister);
        member.addMemberRegister(memberRegister);

        return memberRegister;
    }
}