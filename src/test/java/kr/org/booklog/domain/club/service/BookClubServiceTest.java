package kr.org.booklog.domain.club.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.dto.ClubCreateRequestDto;
import kr.org.booklog.domain.club.dto.ClubResponseDto;
import kr.org.booklog.domain.club.dto.TotalClubResponseDto;
import kr.org.booklog.domain.club.entity.Club;
import kr.org.booklog.domain.club.repository.ClubRepository;
import kr.org.booklog.domain.memberRegister.MemberRegister;
import kr.org.booklog.domain.memberRegister.MemberRegisterRepository;
import kr.org.booklog.domain.user.entity.Role;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import kr.org.booklog.exception.NotEnoughCapacityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BookClubServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired MemberRegisterRepository memberRegisterRepository;

    @Autowired ClubRepository clubRepository;
    @Autowired ClubService clubService;

    @Test
    @Transactional
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
        assertThat(bookClub.getMemberCount()).isEqualTo(1);
        assertThat(bookClub.getLeader().getNickname()).isEqualTo(sessionUser.getNickname());

        List<MemberRegister> memberRegisters = memberRegisterRepository.findByMemberId(user.getId());
        assertThat(memberRegisters.size()).isEqualTo(1);
        assertThat(memberRegisters.get(0).getClub()).isEqualTo(bookClub);
        assertThat(memberRegisters.get(0).getMember().getId()).isEqualTo(user.getId());
    }

    @Test
    @Transactional
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
    @Transactional
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

    @RepeatedTest(10)
    @DisplayName("두 명의 유저가 동시에 가입하여 정원 초과 시, 오류 발생")
    void 동시_가입_정원_초과() throws InterruptedException {

        //given
        User leader = newUser("leader");
        User member1 = newUser("member1");
        User member2 = newUser("member2");
        List<User> members = new ArrayList<>(List.of(member1, member2));

        Long clubId = newClub(leader, 2);

        //then
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        //when
        for (User member: members) {
            executorService.submit(() -> {
                try {
                    clubService.join(new SessionUser(member), clubId);
                } catch (Exception e) {
                    assertThat(e.getClass()).isEqualTo(NotEnoughCapacityException.class);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        Club club = clubRepository.findById(clubId).orElseThrow();
        assertThat(club.getMemberCount()).isEqualTo(2);
        List<MemberRegister> memberRegistersOfClub = memberRegisterRepository.findByClubId(club.getId());
        assertThat(memberRegistersOfClub.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("전체 모임 조회")
    void findAll() {

        //given
        User leader = newUser("leader");
        Long club1 = newClub(leader, 3);
        Long club2 = newClub(leader, 4);

        //when
        List<TotalClubResponseDto> result = clubService.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
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