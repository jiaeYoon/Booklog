package kr.org.booklog.domain.post.service;

import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.entity.Comment;
import kr.org.booklog.domain.comment.repository.CommentRepository;
import kr.org.booklog.domain.like.entity.Likes;
import kr.org.booklog.domain.like.repository.LikesRepository;
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
    private final CommentRepository commentRepository;

    @Transactional
    public Long save(PostRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException());
        requestDto.setUser(user);
        return postRepository.save(requestDto.toEntity()).getId();
    }

    public List<PostTotalResponseDto> findAll(Long userId) {

        List<PostTotalResponseDto> dtos = new ArrayList<>();

        List<Post> posts = postRepository.findAllPosts();
        List<Long> userLikePosts = findUserLikePosts(userId);

        for (Post post : posts) {
            Boolean isLike = userLikePosts.contains(post.getId()) ? Boolean.TRUE : Boolean.FALSE;
            dtos.add(new PostTotalResponseDto(post, isLike));
        }

        return dtos;
    }

    private List<Long> findUserLikePosts(Long userId) {
        List<Long> userLikePosts = new ArrayList<>();
        List<Likes> userLikes = likesRepository.findByUserId(userId);
        for (Likes userLike : userLikes) {
            userLikePosts.add(userLike.getPost().getId());
        }
        return userLikePosts;
    }

    public PostResponseDto findById(Long id, Long userId) {
        PostResponseDto post = postRepository.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        
        // 게시글의 좋아요 여부 조회
        Likes like = likesRepository.isLike(userId, post.getId());
        Long likeId = like != null ? like.getId() : null;
        Boolean isLike = like != null;
        post.setLikeId(likeId);
        post.setIsLike(isLike);

        // 게시글의 댓글 조회
        List<Comment> comments = commentRepository.findByPostId(id);
        List<CommentResponseDto> dtos = new ArrayList<>();
        for (Comment comment:comments) {
            dtos.add(new CommentResponseDto(comment));
        }
        post.setComments(dtos);

        return post;
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
