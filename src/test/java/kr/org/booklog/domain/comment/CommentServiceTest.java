package kr.org.booklog.domain.comment;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.repository.CommentRepository;
import kr.org.booklog.domain.post.CommentService;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.post.service.PostService;
import kr.org.booklog.domain.user.entity.OAuthType;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired CommentRepository commentRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentService commentService;
    @Autowired PostService postService;

    @Test
    void 댓글_등록() {

        // given
        User user = new User("tname", "tpw", "tnickname", "temail@gmail.com", OAuthType.GOOGLE);
        userRepository.save(user);
        Long userId = userRepository.findTop1ByOrderByIdDesc().getId();

        postService.save(new PostRequestDto(userId, "테스트를 읽고...", "테스트", "zzyoon",
                LocalDate.of(2023, 5, 14), LocalDate.of(2023, 6, 14), LocalDate.of(2023, 6, 14),
                4, "감상평 테스트1"));
        Long postId = postRepository.findTop1ByOrderByIdDesc().getId();

        // when
        CommentRequestDto requestDto = new CommentRequestDto(userId, postId, "테스트 댓글");
        Long commentId = commentService.save(requestDto);

        // then
        assertThat(commentRepository.findById(commentId).get().getContent()).isEqualTo("테스트 댓글");
    }
}