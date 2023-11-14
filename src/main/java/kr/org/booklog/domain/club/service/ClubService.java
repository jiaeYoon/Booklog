package kr.org.booklog.domain.club.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.dto.ClubCreateRequestDto;
import kr.org.booklog.domain.club.repository.ClubRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Long save(SessionUser sessionUser, ClubCreateRequestDto requestDto) {
        setHost(sessionUser, requestDto);
        return clubRepository.save(requestDto.toEntity()).getId();
    }

    private void setHost(SessionUser sessionUser, BookClubCreateRequestDto requestDto) {
        Long userId = sessionUser.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        requestDto.setUser(user);
    }
}