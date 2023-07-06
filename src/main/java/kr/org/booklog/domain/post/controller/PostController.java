package kr.org.booklog.domain.post.controller;

import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // TODO : Api Response 추가
    @PostMapping("/posts")
    public Long savePost(@RequestBody PostRequestDto requestDto) {
        return postService.save(requestDto);
    }
}
