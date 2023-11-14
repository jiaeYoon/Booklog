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
        User user = findUser(sessionUser);
        requestDto.setLeader(user);
        return clubRepository.save(requestDto.toEntity()).getId();
    }

    private User findUser(SessionUser sessionUser) {
        Long userId = sessionUser.getId();
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}