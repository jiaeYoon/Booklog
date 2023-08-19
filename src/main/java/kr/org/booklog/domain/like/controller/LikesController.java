package kr.org.booklog.domain.like.controller;

import kr.org.booklog.domain.like.dto.LikesSaveRequestDto;
import kr.org.booklog.domain.like.service.LikesService;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//@RequestMapping("/api/v1")
//@RestController
@Controller
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/posts/{id}/likes")
    public String save(@PathVariable Long id, HttpServletRequest request) {
        Long userId = 1L;
        likesService.save(id, userId);
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
