package kr.org.booklog.domain.like.service;

import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.like.repository.LikesRepository;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Long save(Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 유저가 없습니다. id = " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 감상평이 없습니다. id = " + postId));

        Likes like = Likes.builder()
                .user(user)
                .post(post)
                .build();

        post.addLike();    // 게시글의 좋아요 수 업데이트(+1)

        return likesRepository.save(like).getId();
    }

    public void delete(Long postId, Long likeId) {
        Likes entity = likesRepository.findById(likeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 감상평에 등록한 좋아요가 없습니다. id = " + likeId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 감상평이 없습니다. id = " + postId));

        post.deleteLike();    // 게시글의 좋아요 수 업데이트(-1)

        likesRepository.delete(entity);
    }
}
