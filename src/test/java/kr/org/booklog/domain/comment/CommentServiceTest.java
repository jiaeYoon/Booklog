package kr.org.booklog.domain.comment;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.repository.CommentRepository;
import kr.org.booklog.domain.comment.service.CommentService;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.util.Util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired EntityManager em;
    @Autowired Util util;

    @Autowired CommentRepository commentRepository;
    @Autowired PostRepository postRepository;

    @Autowired CommentService commentService;

    @Test
    void 댓글_등록() {

        // given
        Long userId = util.newUser();
        Long postId = util.newPost(userId);

        em.clear();

        // when
        CommentRequestDto requestDto = new CommentRequestDto(userId, postId, "댓글1");
        Long commentId = commentService.save(requestDto);

        // then
        assertThat(commentRepository.findById(commentId).get().getContent()).isEqualTo("댓글1");
        assertThat(commentRepository.findByPostId(commentId).size()).isEqualTo(1);
        assertThat(postRepository.findById(postId).get().getCommentsCnt()).isEqualTo(1);
        assertThat(postRepository.findById(postId).get().getComments().get(0).getContent()).isEqualTo("댓글1");
    }

    @Test
    void 댓글_조회() {

        // given
        Long userId = util.newUser();
        Long postId = util.newPost(userId);

        em.clear();

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
        Long userId = util.newUser();
        Long postId = util.newPost(userId);

        em.clear();

        Long commentId = commentService.save(new CommentRequestDto(userId, postId, "댓글1"));

        // when
        CommentRequestDto dto = CommentRequestDto.builder().content("댓글2").build();
        commentService.update(commentId, dto);

        // then
        assertThat(commentRepository.findById(commentId).get().getContent()).isEqualTo("댓글2");
        assertThat(commentRepository.findById(commentId).get().getContent()).isNotEqualTo("댓글1");
        assertThat(postRepository.findById(postId).get().getComments().get(0).getContent()).isEqualTo("댓글2");
    }

    @Test
    void 댓글_삭제() {
        // given
        Long userId = util.newUser();
        Long postId = util.newPost(userId);

        em.clear();

        Long commentId = commentService.save(new CommentRequestDto(userId, postId, "댓글1"));

        em.flush();
        em.clear();

        // when
        commentService.delete(commentId);

        em.flush();
        em.clear();

        // then
        assertThat(commentRepository.findByPostId(postId).size()).isEqualTo(0);
        assertThat(postRepository.findById(postId).get().getCommentsCnt()).isEqualTo(0);
    }
}