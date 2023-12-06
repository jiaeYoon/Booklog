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
import kr.org.booklog.exception.NotEnoughCapacityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        User user = newUser("A");
        SessionUser sessionUser = new SessionUser(user);
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

        Long clubId = newClub(leader, 4);

        //when
        Long bookClubMemberId = clubService.join(new SessionUser(member), clubId);

        //then
        MemberRegister memberRegister = memberRegisterRepository.findById(bookClubMemberId).get();
        Club club = clubRepository.findById(clubId).get();
        User user = userRepository.findById(member.getId()).get();

        assertThat(club.getMemberCount()).isEqualTo(2);
        assertThat(user.getMemberRegisters().size()).isEqualTo(1);
        assertThat(memberRegister.getClub().getId()).isEqualTo(clubId);
        assertThat(memberRegister.getMember().getName()).isEqualTo("B");
    }

    @Test
    @DisplayName("모임 정원 초과")
    void overcapacity() {

        //given
        User leader = newUser("A");
        User member1 = newUser("B");
        User member2 = newUser("C");
        Long clubId = newClub(leader, 2);

        //when, then
        clubService.join(new SessionUser(member1), clubId);
        assertThatThrownBy(() -> clubService.join(new SessionUser(member2), clubId))
                .isInstanceOf(NotEnoughCapacityException.class);
    }

    private Long newClub(User leader, int capacity) {
        ClubCreateRequestDto requestDto = ClubCreateRequestDto.builder()
                .clubName("추리 소설 클럽")
                .capacity(capacity)
                .introduction("소개글")
                .build();
        return clubService.save(new SessionUser(leader), requestDto);
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