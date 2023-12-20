package kr.org.booklog.domain.user.dto;

import kr.org.booklog.domain.club.entity.Club;
import lombok.Getter;

@Getter
public class JoinedClubResponseDto {

    private final Long ClubId;
    private final String clubName;

    public JoinedClubResponseDto(Club club) {
        this.ClubId = club.getId();
        this.clubName = club.getClubName();
    }
}