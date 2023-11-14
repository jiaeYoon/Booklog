package kr.org.booklog.domain.club.dto;

import kr.org.booklog.domain.club.entity.Club;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreateRequestDto {

    private User user;
    private String clubName;
    private int capacity;
    private String introduction;

    @Builder
    public ClubCreateRequestDto(String clubName, int capacity, String introduction) {
        this.clubName = clubName;
        this.capacity = capacity;
        this.introduction = introduction;
    }

    public Club toEntity() {
        return Club.builder()
                .user(user)
                .clubName(clubName)
                .capacity(capacity)
                .introduction(introduction)
                .build();
    }
}