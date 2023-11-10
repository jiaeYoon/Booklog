package kr.org.booklog.domain.user.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id값을 가지는 사용자가 없습니다."));
    }

    public void updateUserInfo(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id값을 가지는 사용자가 없습니다."));
        user.addInfo(nickname);
    }

    public void reloadSessionUserInfo(SessionUser sessionUser) {
        httpSession.removeAttribute("user");
        httpSession.setAttribute("user", sessionUser);
    }

    public Long setGuestSession() {
        Long guestId = 164L;
        SessionUser sessionUser = new SessionUser(findById(guestId));
        httpSession.setAttribute("user", sessionUser);
        return guestId;
    }
}
