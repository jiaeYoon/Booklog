package kr.org.booklog.domain.likes.service;

import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.like.service.LikesService;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.util.Util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LikesServiceTest {

    @Autowired EntityManager em;
    @Autowired Util util;
    
    @Autowired LikesRepository likesRepository;
    @Autowired PostRepository postRepository;
    
    @Autowired LikesService likesService;

    @Test
    void 좋아요_등록() {

        //given : users, posts 미리 세팅
        Long userId1 = util.newUser("user1").getId();
        Long userId2 = util.newUser("user2").getId();
        Long postId = util.newPost(userId1);

        em.flush(); // post 엔티티의 default value 적용을 위한 플러시 및 영속성 컨텍스트 초기화
        em.clear();

        // when : 유저 2명이 한 포스트에 좋아요 등록
        Long likesId = likesService.save(userId1, postId);
        Long likesId2 = likesService.save(userId2, postId);

        //then
        assertThat(likesRepository.findByPostId(postId).size()).isEqualTo(2);
        assertThat(postRepository.findById(postId).get().getLikesCnt()).isEqualTo(2);
    }

    @Test
    void 좋아요_취소() {

        //given
        Long userId = util.newUser();
        Long postId = util.newPost(userId);

        em.flush(); // post 엔티티의 default value 적용을 위한 플러시 및 영속성 컨텍스트 초기화
        em.clear();

        Long likesId = likesService.save(userId, postId);

        //when
        likesService.delete(postId, likesId);

        //then
        assertThat(likesRepository.findByPostId(postId).size()).isEqualTo(0);
        assertThat(postRepository.findById(postId).get().getLikesCnt()).isEqualTo(0);
    }
}