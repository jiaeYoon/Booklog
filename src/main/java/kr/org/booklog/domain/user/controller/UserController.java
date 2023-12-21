package kr.org.booklog.domain.user.controller;

import kr.org.booklog.config.auth.LoginUser;
import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.user.dto.JoinedClubResponseDto;
import kr.org.booklog.domain.user.dto.SetNicknameForm;
import kr.org.booklog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/users")
    public RedirectView updateUserInfo(@LoginUser SessionUser sessionUser, SetNicknameForm nicknameForm, BindingResult result) {

        String nickname = nicknameForm.getNickname();

        if (result.hasErrors()) {
            System.out.println("form error");
            return new RedirectView("/home");
        }

        userService.updateUserInfo(sessionUser.getId(), nickname);
        sessionUser.updateNickname(nickname);

        userService.reloadSessionUserInfo(sessionUser);

        return new RedirectView("/home");
    }

    @GetMapping("/users/clubs")
    public List<JoinedClubResponseDto> findJoinClubs(@LoginUser SessionUser sessionUser) {
        return userService.findJoinClubs(sessionUser);
    }
}
