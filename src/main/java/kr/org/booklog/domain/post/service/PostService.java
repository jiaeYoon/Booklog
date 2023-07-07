package kr.org.booklog.domain.post.service;

import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.post.dto.PostRequestDto;
import kr.org.booklog.domain.post.dto.PostResponseDto;
import kr.org.booklog.domain.post.dto.PostTotalResponseDto;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, LikesRepository likesRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likesRepository = likesRepository;
    }

    public Long save(PostRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException());
        requestDto.setUser(user);
        return postRepository.save(requestDto.toEntity()).getId();
    }

    public List<PostTotalResponseDto> findAll() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "postAt"));
        List<PostTotalResponseDto> responseDto = new ArrayList<>();
        for (Post post : posts) {
            responseDto.add(new PostTotalResponseDto(post));
        }
        return responseDto;
    }

    public PostResponseDto findById(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        PostResponseDto responseDto = new PostResponseDto(post);
        
        // 게시글의 좋아요 여부 조회
        Likes likes = likesRepository.findByUserIdAndPostId(userId, post.getId())
                .orElse(new Likes(Boolean.FALSE));
        responseDto.setIsLike(likes.getIsLike());

        return responseDto;
    }

    public Long update(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        post.updatePost(requestDto);
        return id;
    }
}
