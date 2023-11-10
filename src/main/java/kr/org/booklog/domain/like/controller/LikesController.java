package kr.org.booklog.domain.like.controller;

import kr.org.booklog.config.auth.LoginUser;
import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.like.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/posts/{id}/likes")
    public String save(@LoginUser SessionUser user, @PathVariable Long id, HttpServletRequest request) {

        likesService.save(user.getId(), id);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @DeleteMapping("/posts/{id}/likes")
    public String delete(@PathVariable Long id, @RequestParam Long likeId, HttpServletRequest request) {
        likesService.delete(id, likeId);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
