package kr.org.booklog.domain.post.service;

import kr.org.booklog.domain.like.dto.LikesSaveRequestDto;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.like.service.LikesService;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.dto.PostTotalResponseDto;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;
    @Autowired LikesRepository likesRepository;
    @Autowired PostService postService;
    @Autowired LikesService likesService;

    @Test
    void 게시글_저장() {

        //given
        User user = new User("tname", "tpw", "tnickname", "temail@gmail.com", OAuthType.GOOGLE);
        userRepository.save(user);

        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setUserId(user.getId());
        requestDto.setPostTitle("이방인을 읽고...");
        requestDto.setBookTitle("이방인");
        requestDto.setBookWriter("알베르 카뮈");
        requestDto.setReadStart(LocalDate.of(2023, 5, 14));
        requestDto.setReadEnd(LocalDate.of(2023, 6, 30));
        requestDto.setPostAt(LocalDate.of(2023, 6, 30));
        requestDto.setRating(4);
        requestDto.setContent("읽고싶었던 책이었는데 드디어 읽었다! 흥미롭게 읽음");

        //when
        Long id = postService.save(requestDto);

        //then
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(post.getBookTitle()).isEqualTo("이방인");
    }

    @Test
    void 전체_게시글_조회() {

        //given
        User user = new User("tname", "tpw", "tnickname", "temail@gmail.com", OAuthType.GOOGLE);
        userRepository.save(user);

        for (int i = 0; i < 3; i++) {
            postService.save(PostRequestDto.builder()
                .userId(user.getId())
                .postTitle("테스트를 읽고...")
                .bookTitle("테스트")
                .bookWriter("zzyoon")
                .readStart(LocalDate.of(2023, 5, 14))
                .readEnd(LocalDate.of(2023, 6, 14))
                .postAt(LocalDate.of(2023, 6, 14))
                .rating(4)
                .content("테스트 감상평")
                .build());
        }

        //when
        List<PostTotalResponseDto> responseDto = postService.findAll();

        //then
        assertThat(responseDto.size()).isEqualTo(3);
    }

    @Test
    void 특정_게시글_조회() {
        //given
        User user1 = new User("tname", "tpw", "tnickname", "temail@gmail.com", OAuthType.GOOGLE);
        userRepository.save(user1);

        User user2 = new User("tname2", "tpw2", "tnickname2", "temail2@gmail.com", OAuthType.GOOGLE);
        userRepository.save(user2);

        Post post = new Post(user1, "테스트를 읽고...", "테스트", "zzyoon",
                LocalDate.of(2023, 5, 14), LocalDate.of(2023, 6, 14), LocalDate.of(2023, 6, 14),
                4, "감상평 테스트", 0, 0);
        postRepository.save(post);

        //when
        likesService.save(post.getId(), new LikesSaveRequestDto(user1.getId(), post.getId()));
        PostResponseDto responseDto1 = postService.findById(post.getId(), user1.getId());
        PostResponseDto responseDto2 = postService.findById(post.getId(), user2.getId());

        //then
        assertThat(responseDto1.getIsLike()).isEqualTo(Boolean.TRUE);   // 좋아요 누른 사람의 좋아요 여부가 TRUE인지 확인
        assertThat(responseDto1.getLikeId()).isNotEqualTo(null);
        assertThat(responseDto1.getLikesCnt()).isEqualTo(1);
        assertThat(responseDto1.getPostTitle()).isEqualTo("테스트를 읽고...");

        assertThat(responseDto2.getIsLike()).isEqualTo(Boolean.FALSE);  // 좋아요 누르지 않은 사람의 좋아요 여부가 FALSE인지 확인
        assertThat(responseDto2.getLikeId()).isEqualTo(null);
    }

    @Test
    void 게시글_수정() throws Exception {

        //given
        User user1 = new User("tname", "tpw", "tnickname", "temail@gmail.com", OAuthType.GOOGLE);
        userRepository.save(user1);

        Post post = new Post(user1, "테스트를 읽고...", "테스트", "zzyoon",
                LocalDate.of(2023, 5, 14), LocalDate.of(2023, 6, 14), LocalDate.of(2023, 6, 14),
                4, "감상평 테스트", 0, 0);
        postRepository.save(post);

        //when
        Long updatedId = postService.update(post.getId(), new PostRequestDto("인간실격을 읽고...", "테스트", "zzyoon",
                LocalDate.of(2023, 5, 14), LocalDate.of(2023, 6, 14), LocalDate.of(2023, 6, 14),
                1, "감상평 테스트"));

        //then
        assertThat(postRepository.findById(updatedId).get().getPostTitle()).isEqualTo("인간실격을 읽고...");
        assertThat(postRepository.findById(updatedId).get().getBookTitle()).isEqualTo("테스트");
        assertThat(postRepository.findById(updatedId).get().getRating()).isEqualTo(1);
    }

    @Test
    void 게시글_삭제() {

        //given
        User user1 = new User("tname", "tpw", "tnickname", "temail@gmail.com", OAuthType.GOOGLE);
        userRepository.save(user1);

        Long post1Id = postService.save(new PostRequestDto(user1.getId(), "테스트를 읽고...", "테스트", "zzyoon",
                LocalDate.of(2023, 5, 14), LocalDate.of(2023, 6, 14), LocalDate.of(2023, 6, 14),
                4, "감상평 테스트1"));

        Long post2Id = postService.save(new PostRequestDto(user1.getId(), "테스트를 읽고...", "테스트", "zzyoon",
                LocalDate.of(2023, 5, 14), LocalDate.of(2023, 6, 14), LocalDate.of(2023, 6, 14),
                4, "감상평 테스트2"));

        // when
        postService.delete(post1Id);

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(1);
    }
}