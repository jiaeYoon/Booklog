package kr.org.booklog.domain.club.controller;

import kr.org.booklog.config.auth.LoginUser;
import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.dto.ClubCreateRequestDto;
import kr.org.booklog.domain.club.dto.ClubResponseDto;
import kr.org.booklog.domain.club.dto.TotalClubResponseDto;
import kr.org.booklog.domain.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/clubs")
    public Long create(@LoginUser SessionUser user, ClubCreateRequestDto requestDto) {
        return clubService.create(user, requestDto);
    }

    @PostMapping("/clubs/{id}/join")
    public Long join(@LoginUser SessionUser user, @PathVariable Long id) {
        return clubService.join(user, id);
    }

    @GetMapping("/clubs")
    public List<TotalClubResponseDto> findAll() {
        return clubService.findAll();
    }

    @GetMapping("/clubs/{id}")
    public ClubResponseDto findById(@PathVariable("id") Long id) {
        return clubService.findById(id);
    }
}