package kr.org.booklog.domain.bookClub.controller;

import kr.org.booklog.config.auth.LoginUser;
import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.bookClub.dto.BookClubCreateRequestDto;
import kr.org.booklog.domain.bookClub.service.BookClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BookClubController {

    private final BookClubService bookClubService;

    @PostMapping("/clubs")
    public Long create(@LoginUser SessionUser user, BookClubCreateRequestDto requestDto) {
        return bookClubService.save(user, requestDto);
    }
}