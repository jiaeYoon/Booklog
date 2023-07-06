package kr.org.booklog.domain.post.service;

import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostRepository postRepository;
    @Autowired PostService postService;

    @Test
    void 게시글_저장() {

        //given
        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setUserId(1L);
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
        Assertions.assertThat(post.getBookTitle()).isEqualTo("이방인");
    }
}