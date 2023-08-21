package kr.org.booklog.domain.likes.service;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        List<Long> userIds = newUsers(2);
        Long userId1 = userIds.get(0);
        Long userId2 = userIds.get(1);
        Long postId = newPosts(userId1, 3);

        em.flush(); // post 엔티티의 default value 적용을 위한 플러시 및 영속성 컨텍스트 초기화
        em.clear();

        // when : 유저 2명이 한 포스트에 좋아요 등록
        Long likesId = likesService.save(postId, userId1);
        Long likesId2 = likesService.save(postId, userId2);

        //then
        assertThat(likesRepository.findByPostId(postId).size()).isEqualTo(2);
        assertThat(postRepository.findById(postId).get().getLikesCnt()).isEqualTo(2);
    }

    @Test
    void 좋아요_취소() {

        //given
        Long userId = newUsers(1).get(0);
        Long postId = newPosts(userId,1);

        em.flush(); // post 엔티티의 default value 적용을 위한 플러시 및 영속성 컨텍스트 초기화
        em.clear();

        Long likesId = likesService.save(postId, userId);

        //when
        likesService.delete(postId, likesId);

        //then
        assertThat(likesRepository.findByPostId(postId).size()).isEqualTo(0);
        assertThat(postRepository.findById(postId).get().getLikesCnt()).isEqualTo(0);
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
                    .postAt(LocalDateTime.now())
                    .rating(4)
                    .content("후기" + i)
                    .build();
            id = postRepository.save(post).getId();
        }
        return id;
    }
}