package kr.org.booklog.domain.bookClub.dto;

import kr.org.booklog.domain.bookClub.entity.BookClub;
import kr.org.booklog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookClubCreateRequestDto {

    private User user;
    private String clubName;
    private int capacity;
    private String introduction;

    @Builder
    public BookClubCreateRequestDto(String clubName, int capacity, String introduction) {
        this.clubName = clubName;
        this.capacity = capacity;
        this.introduction = introduction;
    }

    public BookClub toEntity() {
        return BookClub.builder()
                .user(user)
                .clubName(clubName)
                .capacity(capacity)
                .introduction(introduction)
                .build();
    }
}