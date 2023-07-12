package kr.org.booklog.domain.comment;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.post.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public Long save(@RequestBody CommentRequestDto requestDto) {
        return commentService.save(requestDto);
    }

}
