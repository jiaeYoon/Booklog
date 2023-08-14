package kr.org.booklog.domain.comment;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.repository.CommentRepository;
import kr.org.booklog.domain.comment.service.CommentService;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.user.entity.Role;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired CommentRepository commentRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentService commentService;

    @Test
    void 댓글_등록() {

        // given
        User user = newUser();
        Long userId = userRepository.save(user).getId();

        Post post = newPost(user);
        Long postId = postRepository.save(post).getId();

        // when
        CommentRequestDto requestDto = new CommentRequestDto(userId, postId, "댓글1");
        Long commentId = commentService.save(requestDto);

        // then
        assertThat(commentRepository.findById(commentId).get().getContent()).isEqualTo("댓글1");
    }

    @Test
    void 댓글_조회() {

        // given
        User user = newUser();
        Long userId = userRepository.save(user).getId();

        Post post = newPost(user);
        Long postId = postRepository.save(post).getId();

        commentService.save(new CommentRequestDto(userId, postId, "댓글1"));
        commentService.save(new CommentRequestDto(userId, postId, "댓글2"));

        // when
        List<CommentResponseDto> comments = commentService.findAll(postId);

        // then
        assertThat(comments.size()).isEqualTo(2);
    }

    @Test
    void 댓글_수정() {
        // given
        User user = newUser();
        Long userId = userRepository.save(user).getId();

        Post post = newPost(user);
        Long postId = postRepository.save(post).getId();

        Long commentId = commentService.save(new CommentRequestDto(userId, postId, "댓글 수정 테스트의 댓글"));

        // when
        CommentRequestDto dto = CommentRequestDto.builder().content("댓글2").build();
        commentService.update(commentId, dto);

        // then
        assertThat(commentRepository.findById(commentId).get().getContent()).isEqualTo("댓글2");
        assertThat(commentRepository.findById(commentId).get().getContent()).isNotEqualTo("댓글1");
    }

    @Test
    void 댓글_삭제() {
        // given
        User user = newUser();
        Long userId = userRepository.save(user).getId();

        Post post = newPost(user);
        Long postId = postRepository.save(post).getId();

        int before_delete_size = commentRepository.findAll().size();
        Integer before_delete_comments_cnt = postRepository.findById(postId).get().getCommentsCnt();

        Long commentId = commentService.save(new CommentRequestDto(userId, postId, "댓글1"));

        // when
        commentService.delete(commentId);

        // then
        assertThat(commentRepository.findAll().size()).isEqualTo(before_delete_size);
        assertThat(postRepository.findById(postId).get().getCommentsCnt()).isEqualTo(before_delete_comments_cnt);
    }
    
    private User newUser() {
        return User.builder()
                .name("유저A")
                .nickname("닉네임")
                .email("email@gmail.com")
                .role(Role.USER)
                .build();
    }

    private Post newPost(User user) {
        return Post.builder()
                .user(user)
                .postTitle("게시글 제목")
                .bookTitle("책 제목")
                .bookWriter("zzyoon")
                .readStart(LocalDate.of(2023, 5, 14))
                .readEnd(LocalDate.of(2023, 6, 14))
                .postAt(LocalDate.of(2023, 6, 14))
                .rating(4)
                .content("후기")
                .build();
    }
}