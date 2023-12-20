package kr.org.booklog.util;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.dto.ClubCreateRequestDto;
import kr.org.booklog.domain.club.service.ClubService;
import kr.org.booklog.domain.post.entity.Post;
import kr.org.booklog.domain.post.repository.PostRepository;
import kr.org.booklog.domain.user.entity.Role;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Util {

    @Autowired ClubService clubService;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    public Long newUser() {
        User user = User.builder()
                .name("유저A")
                .nickname("닉네임")
                .email("email@gmail.com")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return user.getId();
    }

    public User newUser(String name) {
        User user = User.builder()
                .name(name)
                .nickname("닉네임" + name)
                .email(name + "@gmail.com")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return user;
    }

    public Long newPost(Long userId) {
        User user = userRepository.findById(userId).get();
        Post post = Post.builder()
                .user(user)
                .postTitle("게시글 제목")
                .bookTitle("책 제목")
                .bookWriter("zzyoon")
                .readStart(LocalDate.of(2023, 5, 14))
                .readEnd(LocalDate.of(2023, 6, 14))
                .postAt(LocalDateTime.now())
                .rating(4)
                .content("후기")
                .build();
        postRepository.save(post);
        return post.getId();
    }

    public List<Long> newPosts(Long userId, int num) {
        User user = userRepository.findById(userId).get();

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Post post = Post.builder()
                    .user(user)
                    .postTitle("게시글 제목" + i)
                    .bookTitle("책 제목" + i)
                    .bookWriter("zzyoon")
                    .readStart(LocalDate.of(2023, 5, 14))
                    .readEnd(LocalDate.of(2023, 6, 14))
                    .postAt(LocalDateTime.now())
                    .rating(4)
                    .content("후기" + i)
                    .build();
            ids.add(postRepository.save(post).getId());
        }
        return ids;
    }

    public Long newClub(User leader, int capacity) {
        ClubCreateRequestDto requestDto = ClubCreateRequestDto.builder()
                .clubName("추리 소설 클럽")
                .capacity(capacity)
                .introduction("소개글")
                .build();
        return clubService.create(new SessionUser(leader), requestDto);
    }
}