package kr.org.booklog.domain.like.service;

import kr.org.booklog.domain.like.dto.LikesSaveRequestDto;
import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikesService(LikesRepository likesRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likesRepository = likesRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Long save(Long postId, LikesSaveRequestDto requestDto) {

        Long userId = requestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 유저가 없습니다. id = " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 감상평이 없습니다. id = " + postId));

        requestDto.setUser(user);
        requestDto.setPost(post);
        
        post.updateLikesCnt(post.getLikesCnt());    // 게시글의 좋아요 수 업데이트

        return likesRepository.save(requestDto.toEntity()).getId();
    }

    public void delete(Long postId, Long likeId) {
        Likes entity = likesRepository.findById(likeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 감상평이 없습니다. id = " + postId));
        likesRepository.delete(entity);
    }
}
