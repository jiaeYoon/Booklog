package kr.org.booklog.domain.comment.controller;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public Long save(@RequestBody CommentRequestDto requestDto) {
        return commentService.save(requestDto);
    }

    @GetMapping("/posts/{id}/comments")
    public List<CommentResponseDto> findAll(@PathVariable Long id) {
        return commentService.findAll(id);
    }

    @PatchMapping("/comments/{id}")
    public Long update(@PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        return commentService.update(id, requestDto);
    }

    @DeleteMapping("/comments/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
