package kr.org.booklog.domain.bookClub.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.bookClub.dto.BookClubCreateRequestDto;
import kr.org.booklog.domain.bookClub.entity.BookClub;
import kr.org.booklog.domain.bookClub.repository.BookClubRepository;
import kr.org.booklog.domain.user.entity.Role;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookClubServiceTest {

    @Autowired EntityManager em;
    @Autowired UserRepository userRepository;

    @Autowired BookClubRepository bookClubRepository;
    @Autowired BookClubService bookClubService;

    @Test
    @DisplayName("독서 모임 생성")
    void save() {

        //given
        SessionUser sessionUser = newSessionUser();
        BookClubCreateRequestDto requestDto = BookClubCreateRequestDto.builder()
                .clubName("추리 소설 클럽")
                .capacity(4)
                .introduction("추리 소설을 읽습니다. 장소는 강남역, 2주에 한 번 만나서 같이 책 읽어요!")
                .build();

        //when
        Long id = bookClubService.save(sessionUser, requestDto);

        //then
        BookClub bookClub = bookClubRepository.findById(id).get();
        assertThat(bookClub.getClubName()).isEqualTo("추리 소설 클럽");
        assertThat(bookClub.getCapacity()).isEqualTo(4);
        assertThat(bookClub.getUser().getNickname()).isEqualTo(sessionUser.getNickname());
    }

    private SessionUser newSessionUser() {
        User user = User.builder()
                .name("유저A")
                .nickname("닉네임")
                .email("email@gmail.com")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return new SessionUser(user);
    }
}