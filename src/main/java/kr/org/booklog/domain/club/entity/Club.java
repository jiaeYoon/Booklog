package kr.org.booklog.domain.club.entity;

import kr.org.booklog.domain.memberRegister.MemberRegister;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.exception.NotEnoughCapacityException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_club_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User leader;

    @OneToMany(mappedBy = "club")
    private List<MemberRegister> memberRegisters = new ArrayList<>();

    @NotNull
    private String clubName;

    @NotNull
    private int capacity;

    @NotNull
    private int memberCount = 1;

    @NotNull
    private String introduction;

    @Builder
    public Club(User leader, String clubName, int capacity, String introduction) {
        this.leader = leader;
        this.clubName = clubName;
        this.capacity = capacity;
        this.introduction = introduction;
    }

    public void addMemberRegister(MemberRegister memberRegister) {
        memberRegisters.add(memberRegister);
        ++memberCount;
    }

    public void checkCapacity() {
        if (memberCount + 1 > capacity) {
            throw new NotEnoughCapacityException("member count is over capacity");
        }
    }
}