package kr.org.booklog.domain.likes.service;

import kr.org.booklog.domain.like.dto.LikesSaveRequestDto;
import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.like.service.LikesService;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.Role;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LikesServiceTest {

    @Autowired EntityManager em;
    @Autowired LikesRepository likesRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired LikesService likesService;

    @Test
    void 좋아요_등록() {

        //given : users, posts 미리 세팅
        Long userId = newUsers(2);
        Long postId = newPosts(userId, 3);

        em.clear();

        // when
        // 유저 2명이 한 포스트에 좋아요 등록
        LikesSaveRequestDto requestDto1 = new LikesSaveRequestDto();
        requestDto1.setUserId(userId);

        LikesSaveRequestDto requestDto2 = new LikesSaveRequestDto();
        requestDto2.setUserId(userRepository.findAll().get(1).getId());

        Long likesId = likesService.save(postId, requestDto1);
        Long likesId2 = likesService.save(postId, requestDto2);

        em.flush();
        em.clear();

        //then
        Likes like = likesRepository.findById(likesId).get();

        assertThat(like.getUser().getId()).isEqualTo(userId);
        assertThat(like.getIsLike()).isEqualTo(Boolean.TRUE);
        assertThat(postRepository.findById(postId).get().getLikesCnt()).isEqualTo(2);   // 두 명이 좋아요한 게시글의 좋아요 수 확인
    }

    @Test
    void 좋아요_취소() {

        //given
        Long userId = newUsers(1);
        Long postId = newPosts(userId,1);
        em.clear();

        Long likesId = likesService.save(postId, new LikesSaveRequestDto(userId));
        int repoSize = likesRepository.findAll().size();

        //when
        likesService.delete(postId, likesId);

        em.flush();
        em.clear();

        //then
        assertThat(likesRepository.findAll().size()).isEqualTo(repoSize - 1);
        assertThat(postRepository.findById(postId).get().getLikesCnt()).isEqualTo(0);
    }

    private Long newUsers(int num) {
        Long id = null;
        for (int i = 0; i < num; i++) {
            User user = User.builder()
                    .name("이름" + i)
                    .nickname("닉네임" + i)
                    .email("email" + i + "@gmail.com")
                    .role(Role.USER)
                    .build();
            id = userRepository.save(user).getId();
        }
        return id;
    }

    private Long newPosts(Long userId, int num) {

        User user = userRepository.findById(userId).get();

        Long id = null;
        for (int i = 0; i < num; i++) {
            Post post = Post.builder()
                    .user(user)
                    .postTitle("게시글 제목" + i)
                    .bookTitle("책 제목" + i)
                    .bookWriter("zzyoon")
                    .readStart(LocalDate.of(2023, 5, 14))
                    .readEnd(LocalDate.of(2023, 6, 14))
                    .postAt(LocalDate.of(2023, 6, 14))
                    .rating(4)
                    .content("후기" + i)
                    .build();
            id = postRepository.save(post).getId();
        }
        return id;
    }
}