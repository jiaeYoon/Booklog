package kr.org.booklog.domain.like.controller;

import kr.org.booklog.domain.UserInfo;
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
    public String save(@PathVariable Long id, HttpServletRequest request) {

        Long userId = UserInfo.userId;
        likesService.save(userId, id);

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
