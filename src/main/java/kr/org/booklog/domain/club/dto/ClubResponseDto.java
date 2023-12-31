package kr.org.booklog.domain.club.dto;

import kr.org.booklog.domain.club.entity.Club;
import kr.org.booklog.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class ClubResponseDto {

    private final Long id;
    private final User leader;
    private final List<User> members;

    private final String clubName;
    private final int capacity;
    private final int memberCount;
    private final String introduction;


    public ClubResponseDto(Club club) {
        this.id = club.getId();
        this.leader = club.getLeader();
        this.members = club.getMembers();
        this.clubName = club.getClubName();
        this.capacity = club.getCapacity();
        this.memberCount = club.getMemberCount();
        this.introduction = club.getIntroduction();
    }
}