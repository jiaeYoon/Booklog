package kr.org.booklog.domain.comment.controller;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@RestController
//@RequestMapping("/api/v1")
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public String save(CommentRequestDto requestDto, BindingResult result, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        if (result.hasErrors()) {
            return "redirect:"+ referer;
        }

        requestDto.setUserId(1L);
        commentService.save(requestDto);
        return "redirect:"+ referer;
    }

    @DeleteMapping("/comments/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request) {

        commentService.delete(id);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

//    @PatchMapping("/comments/{id}")
//    public Long update(@PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
//        return commentService.update(id, requestDto);
//    }
}
