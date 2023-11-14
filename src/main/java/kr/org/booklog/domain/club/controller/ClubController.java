package kr.org.booklog.domain.club.controller;

import kr.org.booklog.config.auth.LoginUser;
import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.dto.ClubCreateRequestDto;
import kr.org.booklog.domain.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/clubs")
    public Long create(@LoginUser SessionUser user, ClubCreateRequestDto requestDto) {
        return clubService.save(user, requestDto);
    }

    @PatchMapping("/clubs/{id}")
    public Long join(@LoginUser SessionUser user, @PathVariable Long id) {
        return clubService.join(user, id);
    }
}