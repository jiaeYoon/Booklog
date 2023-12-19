package kr.org.booklog.domain.club.dto;

import kr.org.booklog.domain.club.entity.Club;
import lombok.Getter;

@Getter
public class TotalClubResponseDto {

    private final Long id;
    private final String clubName;
    private final int capacity;
    private final int memberCount;

    public TotalClubResponseDto(Club club) {
        this.id = club.getId();
        this.clubName = club.getClubName();
        this.capacity = club.getCapacity();
        this.memberCount = club.getMemberCount();
    }
}