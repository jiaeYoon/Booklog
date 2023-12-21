package kr.org.booklog.domain.post.controller;

import kr.org.booklog.config.auth.LoginUser;
import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static kr.org.booklog.config.guest.GuestInfo.guestId;

//@RequestMapping("/api/v1")
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // TODO : Api Response 추가

    @GetMapping("/posts/save")
    public String loadForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "post-save";
    }

    @PostMapping("/posts/save")
    public String save(@LoginUser SessionUser user, @Valid PostForm form, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("form error");
            return "post-save";
        }

        PostRequestDto dto = PostRequestDto.builder()
                .userId(user.getId())
                .postTitle(form.getPostTitle())
                .bookTitle(form.getBookTitle())
                .bookWriter(form.getBookWriter())
                .readStart(form.getReadStart())
                .readEnd(form.getReadEnd())
                .postAt(LocalDateTime.now())
                .rating(form.getRating())
                .content(form.getContent())
                .build();

        postService.save(dto);
        return "redirect:/";
    }

    @GetMapping("/posts/{id}")
    public String findById(@LoginUser SessionUser user, @PathVariable Long id, Model model) {

        Long userId = guestId;
        if (user != null) {
             userId = user.getId();
        }
        PostResponseDto post = postService.findById(id, userId);
        model.addAttribute("userId", userId);
        model.addAttribute("post_", post);
        model.addAttribute("commentForm", new CommentResponseDto());
        return "post-detail";
    }

    @GetMapping("/posts/update/{id}")
    public String getUpdatePost(@LoginUser SessionUser user, @PathVariable Long id, Model model) {

        PostResponseDto post = postService.findById(id, user.getId());
//        model.addAttribute("userId", userId);
        model.addAttribute("post_", post);
        return "post-update";
    }


    @PutMapping("posts/update/{id}")
    public String updatePost(@PathVariable Long id, PostForm form, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("form error");
            return "post-update";
        }

        PostRequestDto requestDto = PostRequestDto.builder()
                .postTitle(form.getPostTitle())
                .bookTitle(form.getBookTitle())
                .bookWriter(form.getBookWriter())
                .readStart(form.getReadStart())
                .readEnd(form.getReadEnd())
                .postAt(form.getPostAt())
                .rating(form.getRating())
                .content(form.getContent())
                .build();

        postService.update(id, requestDto);

        return "redirect:/";
    }
}
