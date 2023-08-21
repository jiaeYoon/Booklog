package kr.org.booklog.domain.post.service;

import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.like.service.LikesService;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.dto.PostTotalResponseDto;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public Long save(PostRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException());
        requestDto.setUser(user);
        return postRepository.save(requestDto.toEntity()).getId();
    }

    public List<PostTotalResponseDto> findAll(Long userId) {
        List<Post> posts = postRepository.findAllPosts();
        List<PostTotalResponseDto> responseDto = new ArrayList<>();
        for (Post post : posts) {
            Likes like = likesRepository.isLike(userId, post.getId());
            Boolean isLike = like != null;
            responseDto.add(new PostTotalResponseDto(post, isLike));
        }
        return responseDto;
    }

    public PostResponseDto findById(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        PostResponseDto responseDto = new PostResponseDto(post);
        
        // 게시글의 좋아요 여부 조회
        Likes like = likesRepository.isLike(userId, post.getId());
        Boolean isLike = like != null;
        post.setLikeId(like.getId());
        post.setIsLike(isLike);

        return responseDto;
    }

    @Transactional
    public Long update(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        post.updatePost(requestDto);
        return id;
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = "+ id));
        postRepository.delete(post);
    }
}
