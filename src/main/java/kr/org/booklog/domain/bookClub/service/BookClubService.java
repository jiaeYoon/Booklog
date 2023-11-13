package kr.org.booklog.domain.bookClub.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.bookClub.dto.BookClubCreateRequestDto;
import kr.org.booklog.domain.bookClub.repository.BookClubRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookClubService {

    private final UserRepository userRepository;
    private final BookClubRepository bookClubRepository;

    @Transactional
    public Long save(SessionUser sessionUser, BookClubCreateRequestDto requestDto) {
        setHost(sessionUser, requestDto);
        return bookClubRepository.save(requestDto.toEntity()).getId();
    }

    private void setHost(SessionUser sessionUser, BookClubCreateRequestDto requestDto) {
        Long userId = sessionUser.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        requestDto.setUser(user);
    }
}