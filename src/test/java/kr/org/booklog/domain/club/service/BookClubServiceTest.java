package kr.org.booklog.domain.club.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.dto.ClubCreateRequestDto;
import kr.org.booklog.domain.club.entity.Club;
import kr.org.booklog.domain.club.repository.ClubRepository;
import kr.org.booklog.domain.memberRegister.MemberRegister;
import kr.org.booklog.domain.memberRegister.MemberRegisterRepository;
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

    @Autowired UserRepository userRepository;
    @Autowired MemberRegisterRepository memberRegisterRepository;

    @Autowired ClubRepository clubRepository;
    @Autowired ClubService clubService;

    @Test
    @DisplayName("독서 모임 생성")
    void save() {

        //given
        SessionUser sessionUser = new SessionUser(newUser("A"));
        ClubCreateRequestDto requestDto = ClubCreateRequestDto.builder()
                .clubName("추리 소설 클럽")
                .capacity(4)
                .introduction("추리 소설을 읽습니다. 장소는 강남역, 2주에 한 번 만나서 같이 책 읽어요!")
                .build();

        //when
        Long id = clubService.save(sessionUser, requestDto);

        //then
        Club bookClub = clubRepository.findById(id).get();
        assertThat(bookClub.getClubName()).isEqualTo("추리 소설 클럽");
        assertThat(bookClub.getCapacity()).isEqualTo(4);
        assertThat(bookClub.getLeader().getNickname()).isEqualTo(sessionUser.getNickname());
    }

    @Test
    @DisplayName("독서 모임 참가")
    void join() {

        //given
        User leader = newUser("A");
        User member = newUser("B");

        Long clubId = newClub(leader).getId();

        //when
        Long bookClubMemberId = clubService.join(new SessionUser(member), clubId);

        //then
        MemberRegister memberRegister = memberRegisterRepository.findById(bookClubMemberId).get();
        Club club = clubRepository.findById(clubId).get();
        User user = userRepository.findById(member.getId()).get();

        assertThat(user.getMemberRegisters().size()).isEqualTo(1);
        assertThat(memberRegister.getClub().getId()).isEqualTo(clubId);
        assertThat(memberRegister.getUser().getName()).isEqualTo("B");
    }

    private Club newClub(User leader) {
        Club club = Club.builder()
                .leader(leader)
                .clubName("추리 소설 클럽")
                .capacity(4)
                .introduction("소개글")
                .build();
        return clubRepository.save(club);
    }

    private User newUser(String name) {
        User user = User.builder()
                .name(name)
                .nickname("닉네임" + name)
                .email(name + "@gmail.com")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return user;
    }
}