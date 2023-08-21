package kr.org.booklog.domain.post.controller;

import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.service.CommentService;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.dto.PostTotalResponseDto;
import kr.org.booklog.domain.post.service.PostService;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

//@RequestMapping("/api/v1")
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    // TODO : Api Response 추가
//    @PostMapping("/posts")
//    public Long savePost(@RequestBody PostRequestDto requestDto) {
//        return postService.save(requestDto);
//    }
//
//    @GetMapping("/posts")
//    public List<PostTotalResponseDto> findAll() {
//        return postService.findAll();
//    }
//
//    @GetMapping("/posts/{id}")
//    public PostResponseDto findById(@PathVariable Long id, Long userId) {
//        return postService.findById(id, userId);
//    }
//
//    @PatchMapping("posts/{id}")
//    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
//        return postService.update(id, requestDto);
//    }
//
//    @DeleteMapping("/posts/{id}")
//    public Long delete(@PathVariable Long id) {
//        postService.delete(id);
//        return id;
//    }

//    @GetMapping("/home")
//    public String home(Model model) {
//        User user = User.builder().name("userA").build();
//        model.addAttribute(user);
//        return "home";
//    }

    @GetMapping("/posts/save")
    public String loadForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "post-save";
    }

    @PostMapping("/posts/save")
    public String save(@Valid PostForm form, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("form error");
            return "post-save";
        }

        Long userId = 1L;
        PostRequestDto dto = PostRequestDto.builder()
                .userId(userId)
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

    @GetMapping("/home")
    public String findAll(Model model) {
        User user = userRepository.findById(1L).get();
        List<PostTotalResponseDto> posts = postService.findAll(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "home";

    }

    @GetMapping("/posts/{id}")
    public String findById(@PathVariable Long id, Model model) {

        Long userId = 1L;
        PostResponseDto post = postService.findById(id, userId);
        List<CommentResponseDto> commenets = commentService.findAll(id);
        model.addAttribute("post_", post);
        model.addAttribute("comments", commenets);
        model.addAttribute("commentForm", new CommentResponseDto());
        return "post-update";
    }

    @PutMapping("posts/{id}")
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
