package kr.org.booklog.domain.comment.service;

import kr.org.booklog.domain.comment.dto.CommentRequestDto;
import kr.org.booklog.domain.comment.dto.CommentResponseDto;
import kr.org.booklog.domain.comment.entity.Comment;
import kr.org.booklog.domain.comment.repository.CommentRepository;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Long save(CommentRequestDto requestDto) {

        Long userId = requestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다. id = " + userId));
        requestDto.setUser(user);

        Long postId = requestDto.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 감상평이 없습니다. id = " + postId));
        requestDto.setPost(post);

        Comment comment = commentRepository.save(requestDto.toEntity());

        post.addComment(comment);    // 게시글의 댓글 목록, 수 업데이트(+1)

        return comment.getId();
    }

    @Transactional(readOnly = true)
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

    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다. id = " + commentId));

        Post post = comment.getPost();
        post.deleteComment(comment);   // 게시글의 댓글 목록, 수 업데이트(-1)

        commentRepository.delete(comment);
    }
}
