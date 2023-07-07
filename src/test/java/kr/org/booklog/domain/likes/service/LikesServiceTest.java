package kr.org.booklog.domain.likes.service;

import kr.org.booklog.domain.like.dto.LikesSaveRequestDto;
import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.like.service.LikesService;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.OAuthType;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LikesServiceTest {

    @Autowired LikesRepository likesRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired LikesService likesService;

    @Test
    void saveLikes() {
        
        //given : users, posts 미리 세팅
        for (int i = 0; i < 3; i++) {
            User user = new User("tname", "tpw", "tnickname", "temail@gmail.com", OAuthType.GOOGLE);
            userRepository.save(user);
        }

        User user = userRepository.findAll().get(0);
        Long userId = user.getId();
        
        for (int i = 0; i < 4; i++) {
            Post post = new Post(user, "테스트를 읽고...", "테스트", "zzyoon",
                    LocalDate.of(2023, 5, 14), LocalDate.of(2023, 6, 14), LocalDate.of(2023, 6, 14),
                    4, "감상평 테스트", 0, 0);
            postRepository.save(post);
        }

        Long postId = postRepository.findAll().get(1).getId();
        
        // 유저 2명이 한 포스트에 좋아요 등록
        LikesSaveRequestDto requestDto1 = new LikesSaveRequestDto();
        requestDto1.setUserId(userId);
        requestDto1.setPostId(postId);

        LikesSaveRequestDto requestDto2 = new LikesSaveRequestDto();
        requestDto2.setUserId(userRepository.findAll().get(1).getId());
        requestDto2.setPostId(postId);

        //when
        Long likesId = likesService.saveLikes(postId, requestDto1);
        Long likesId2 = likesService.saveLikes(postId, requestDto2);

        //then
        Likes like = likesRepository.findById(likesId).get();

        assertThat(like.getUser().getId()).isEqualTo(userId);
        assertThat(like.getPost().getId()).isEqualTo(postRepository.findAll().get(0).getId()+1L);
        assertThat(like.getIsLike()).isEqualTo(Boolean.TRUE);
        assertThat(postRepository.findById(postId).get().getLikesCnt()).isEqualTo(2);   // 두 명이 좋아요한 게시글의 좋아요 수 확인

        assertThat(postRepository.findAll().get(0).getLikesCnt()).isEqualTo(0);     // 좋아요하지 않은 게시글의 좋아요 수 확인
    }
}