package kr.org.booklog.domain.comment.service;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.entity.Comment;
import kr.org.booklog.domain.comment.repository.CommentRepository;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Long save(CommentRequestDto requestDto) {

        Long userId = requestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다. id = " + userId));
        requestDto.setUser(user);

        Long postId = requestDto.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 감상평이 없습니다. id = " + postId));
        requestDto.setPost(post);

        post.updateCommentsCnt(post.getCommentsCnt(), Boolean.TRUE);    // 게시글의 댓글 수 업데이트(+1)

        return commentRepository.save(requestDto.toEntity()).getId();
    }

    public List<CommentResponseDto> findAll(Long id) {
        List<Comment> comments = commentRepository.findByPostId(id);
        List<CommentResponseDto> responseDtos = new ArrayList<>();
        for (Comment comment:comments) {
            responseDtos.add(new CommentResponseDto(comment));
        }
        return responseDtos;
    }

    public Long update(Long id, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다. id = " + id));
        comment.update(requestDto);
        return id;
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다. id = " + id));

        Post post = comment.getPost();
        post.updateCommentsCnt(post.getCommentsCnt(), Boolean.FALSE);   // 게시글의 댓글 수 업데이트(-1)

        commentRepository.delete(comment);
    }
}
