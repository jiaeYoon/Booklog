package kr.org.booklog.domain.post.controller;

import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.dto.PostTotalResponseDto;
import kr.org.booklog.domain.post.service.PostService;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    // TODO : Api Response 추가
    @PostMapping("/posts")
    public Long savePost(@RequestBody PostRequestDto requestDto) {
        return postService.save(requestDto);
    }

    @GetMapping("/posts")
    public List<PostTotalResponseDto> findAll() {
        return postService.findAll();
    }

    @GetMapping("/posts/{id}")
    public PostResponseDto findById(@PathVariable Long id, Long userId) {
        return postService.findById(id, userId);
    }

    @PatchMapping("posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.update(id, requestDto);
    }

    @DeleteMapping("/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }

    @GetMapping("/home")
    public String home(Model model) {
        User user = User.builder().name("userA").build();
        model.addAttribute(user);
        return "home";
    }

    @GetMapping("/posts/save")
    public String loadForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "post-save";
    }

    @PostMapping("/posts/save")
    public String create(@Valid PostForm form, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("form error");
            return "post-save";
        }

        User user = userRepository.findById(1L).get();
        PostRequestDto dto = PostRequestDto.builder()
                .userId(user.getId())
                .postTitle(form.getPostTitle())
                .bookTitle(form.getBookTitle())
                .bookWriter(form.getBookWriter())
                .readStart(form.getReadStart())
                .postAt(LocalDate.now())
                .build();

        postService.save(dto);
        return "redirect:/";
    }
}
