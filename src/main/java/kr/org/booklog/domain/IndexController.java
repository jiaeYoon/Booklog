package kr.org.booklog.domain;

import kr.org.booklog.config.auth.LoginUser;
import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.post.dto.PostTotalResponseDto;
import kr.org.booklog.domain.post.service.PostService;
import kr.org.booklog.domain.user.dto.SetNicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostService postService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, @LoginUser SessionUser user) {

        List<PostTotalResponseDto> posts;

        if (user != null) {

            boolean setNickname = user.getNickname() == null || user.getNickname().equals("");
            posts = postService.findAll(user.getId());

            model.addAttribute("user", user);
            model.addAttribute("setNickname", setNickname);
            model.addAttribute("nicknameForm", new SetNicknameForm());
        } else {
            Long guest_id = 164L;
            posts = postService.findAll(guest_id);
        }

        model.addAttribute("posts", posts);

        return "home";

    }
}
