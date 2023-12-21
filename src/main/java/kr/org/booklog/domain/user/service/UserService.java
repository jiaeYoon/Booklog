package kr.org.booklog.domain.user.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.user.dto.JoinedClubResponseDto;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

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
        user.addNicknameAndGrantRole(nickname);
    }

    public void reloadSessionUserInfo(SessionUser sessionUser) {
        httpSession.removeAttribute("user");
        httpSession.setAttribute("user", sessionUser);
    }

    public List<JoinedClubResponseDto> findJoinClubs(SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(IllegalArgumentException::new);
        return user.getJoinedClubs();
    }
}
