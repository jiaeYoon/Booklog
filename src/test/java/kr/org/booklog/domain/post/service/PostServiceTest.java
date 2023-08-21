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

import kr.org.booklog.domain.user.entity.Role;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired EntityManager em;
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
        Long userId = newUsers(1);

        PostRequestDto requestDto = PostRequestDto.builder()
                .userId(userId)
                .postTitle("게시글 제목")
                .bookTitle("책 제목")
                .bookWriter("zzyoon")
                .readStart(LocalDate.of(2023, 5, 14))
                .readEnd(LocalDate.of(2023, 6, 14))
                .postAt(LocalDate.of(2023, 6, 14))
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
        Long userId1 = newUsers(1).get(0);
        List<Long> postIds = newPosts(userId1, 2);
        Long postId1 = postIds.get(0);
        Long postId2 = postIds.get(1);

        em.flush();
        em.clear();

        likesService.save(postId1, userId1);

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
        User user1 = new User("user1", "nickname1", "email1@gmail.com", Role.USER);
        userRepository.save(user1);

        User user2 = new User("user2", "nickname2", "email2@gmail.com", Role.USER);
        userRepository.save(user2);

        Long postId = newPosts(user1.getId(), 1);

        //when
        PostResponseDto responseDto1 = postService.findById(postId, user1.getId());
        PostResponseDto responseDto2 = postService.findById(postId, user2.getId());

        //then
        assertThat(responseDto1.getPostTitle()).isEqualTo("게시글 제목0");
    }

    @Test
    void 게시글_수정() throws Exception {

        //given
        Long userId = newUsers(1);
        Long postId = newPosts(userId, 1);

        //when
        PostRequestDto dto = PostRequestDto.builder()
                .postTitle("게시글 제목100")
                .bookTitle("책 제목100")
                .bookWriter("zzyoon")
                .readStart(LocalDate.of(2023, 5, 14))
                .readEnd(LocalDate.of(2023, 6, 14))
                .postAt(LocalDate.of(2023, 6, 14))
                .rating(1)
                .content("후기100")
                .build();
        Long updatedId = postService.update(postId, dto);

        em.flush();
        em.clear();

        //then
        assertThat(postRepository.findById(updatedId).get().getPostTitle()).isEqualTo("게시글 제목100");
        assertThat(postRepository.findById(updatedId).get().getBookTitle()).isEqualTo("책 제목100");
        assertThat(postRepository.findById(updatedId).get().getRating()).isEqualTo(1);
    }

    @Test
    void 게시글_삭제() {

        //given
        Long userId = newUsers(1).get(0);
        Long postId = newPosts(userId, 2).get(0);

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

    private List<Long> newUsers(int num) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            User user = User.builder()
                    .name("이름" + i)
                    .nickname("닉네임" + i)
                    .email("email" + i + "@gmail.com")
                    .role(Role.USER)
                    .build();
            ids.add(userRepository.save(user).getId());
        }
        return ids;
    }

    private List<Long> newPosts(Long userId, int num) {
        User user = userRepository.findById(userId).get();

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Post post = Post.builder()
                    .user(user)
                    .postTitle("게시글 제목" + i)
                    .bookTitle("책 제목" + i)
                    .bookWriter("zzyoon")
                    .readStart(LocalDate.of(2023, 5, 14))
                    .readEnd(LocalDate.of(2023, 6, 14))
                    .postAt(LocalDateTime.now())
                    .rating(4)
                    .content("후기" + i)
                    .build();
            ids.add(postRepository.save(post).getId());
        }
        return ids;
    }
}