package kr.org.booklog.domain.post.service;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.repository.CommentRepository;
import kr.org.booklog.domain.comment.service.CommentService;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.like.service.LikesService;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.dto.PostTotalResponseDto;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.repository.UserRepository;
import kr.org.booklog.util.Util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired EntityManager em;
    @Autowired Util util;
    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;
    @Autowired LikesRepository likesRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired PostService postService;
    @Autowired LikesService likesService;
    @Autowired CommentService commentService;

    @Test
    void 게시글_저장() {

        //given
        Long userId = util.newUser();

        PostRequestDto requestDto = PostRequestDto.builder()
                .userId(userId)
                .postTitle("게시글 제목")
                .bookTitle("책 제목")
                .bookWriter("zzyoon")
                .readStart(LocalDate.of(2023, 5, 14))
                .readEnd(LocalDate.of(2023, 6, 14))
                .postAt(LocalDateTime.now())
                .rating(4)
                .content("후기")
                .build();

        //when
        Long id = postService.save(requestDto);

        //then
        Post post = postRepository.findById(id).get();
        assertThat(post.getBookTitle()).isEqualTo("책 제목");
    }

    @Test
    void 전체_게시글_조회() {

        //given
        Long userId1 = util.newUser();
        List<Long> postIds = util.newPosts(userId1, 2);
        Long postId1 = postIds.get(0);
        Long postId2 = postIds.get(1);

        em.flush();
        em.clear();

        likesService.save(userId1, postId1);

        //when
        List<PostTotalResponseDto> posts = postService.findAll(userId1); // postAt 기준 내림차순 정렬

        //then
        assertThat(posts.size()).isEqualTo(2);

        assertThat(posts.get(1).getLikesCnt()).isEqualTo(1);
        assertThat(posts.get(1).getCommentsCnt()).isEqualTo(0);
        assertThat(posts.get(1).getIsLike()).isEqualTo(true);

        assertThat(posts.get(0).getLikesCnt()).isEqualTo(0);
        assertThat(posts.get(0).getIsLike()).isEqualTo(false);
    }

    @Test
    void 특정_게시글_조회() {
        //given
        Long userId1 = util.newUser();
        Long postId = util.newPosts(userId1, 2).get(0);

        em.flush();
        em.clear();

        likesService.save(userId1, postId);

        //when
        PostResponseDto responseDto = postService.findById(postId, userId1);

        //then
        assertThat(responseDto.getPostTitle()).isEqualTo("게시글 제목0");
        assertThat(responseDto.getLikesCnt()).isEqualTo(1);
    }

    @Test
    void 게시글_수정() {

        //given
        Long userId = util.newUser();
        Long postId = util.newPost(userId);

        em.flush();
        em.clear();

        //when
        PostRequestDto dto = PostRequestDto.builder()
                .postTitle("게시글 제목100")
                .bookTitle("책 제목100")
                .bookWriter("zzyoon")
                .readStart(LocalDate.of(2023, 5, 14))
                .readEnd(LocalDate.of(2023, 6, 14))
                .postAt(LocalDateTime.now())
                .rating(1)
                .content("후기100")
                .build();
        postService.update(postId, dto);

        //then
        PostResponseDto responseDto = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + postId));
        assertThat(responseDto.getPostTitle()).isEqualTo("게시글 제목100");
        assertThat(responseDto.getBookTitle()).isEqualTo("책 제목100");
        assertThat(responseDto.getRating()).isEqualTo(1);
    }

    @Test
    void 게시글_삭제() {

        //given
        Long userId = util.newUser();
        Long postId = util.newPosts(userId, 2).get(0);

        em.flush();
        em.clear();

        likesService.save(userId, postId);
        commentService.save(new CommentRequestDto(userId, postId, "댓글1"));
        commentService.save(new CommentRequestDto(userId, postId, "댓글2"));

        // when
        postService.delete(postId);

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        assertThat(likesRepository.findByPostId(postId).size()).isEqualTo(0);
        assertThat(commentRepository.findByPostId(postId).size()).isEqualTo(0);
    }
}