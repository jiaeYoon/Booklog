package kr.org.booklog.domain.like.controller;

import kr.org.booklog.domain.like.dto.LikesSaveRequestDto;
import kr.org.booklog.domain.like.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/posts/{id}/likes")
    public Long save(@PathVariable Long id, @RequestBody LikesSaveRequestDto requestDto) {
        return likesService.save(id, requestDto);
    }

    @DeleteMapping("/posts/{id}/likes")
    public void delete(@PathVariable Long id, @RequestParam Long likeId) {
        likesService.delete(id, likeId);
    }
}
