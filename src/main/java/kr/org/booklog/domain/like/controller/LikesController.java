package kr.org.booklog.domain.like.controller;

import kr.org.booklog.domain.like.dto.LikesSaveRequestDto;
import kr.org.booklog.domain.like.service.LikesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LikesController {

    private final LikesService likesService;

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("/posts/{post_id}/likes")
    public Long save(@PathVariable Long postId, @RequestBody LikesSaveRequestDto requestDto) {
        return likesService.saveLikes(postId, requestDto);
    }
}
