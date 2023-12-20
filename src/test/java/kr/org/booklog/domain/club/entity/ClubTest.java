package kr.org.booklog.domain.club.entity;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.repository.ClubRepository;
import kr.org.booklog.domain.club.service.ClubService;
import kr.org.booklog.domain.memberRegister.MemberRegister;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.exception.NotEnoughCapacityException;
import kr.org.booklog.util.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ClubTest {

    @Autowired ClubService clubService;
    @Autowired ClubRepository clubRepository;
    @Autowired Util util;

    @Test
    @DisplayName("클럽에 멤버 추가 등록")
    void addMemberRegister() {

        //given
        User leader = util.newUser("leader");
        User member = util.newUser("member");

        Long clubId = util.newClub(leader, 3);
        Club club = clubRepository.findById(clubId).get();

        //when
        club.addMemberRegister(new MemberRegister(member, club));

        //then
        assertThat(club.getMemberCount()).isEqualTo(2);
        assertThat(club.getMemberRegisters().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("클럽 정원 초과 시, 예외 발생")
    void checkCapacity() {

        //given
        User leader = util.newUser("leader");
        Long clubId = util.newClub(leader, 2);
        Club club = clubRepository.findById(clubId).get();

        User member = util.newUser("member");
        clubService.join(new SessionUser(member), clubId);

        //when, then
        assertThatThrownBy(club::checkCapacity)
                .isInstanceOf(NotEnoughCapacityException.class)
                .hasMessage("member count is over capacity");
    }

    @Test
    @DisplayName("클럽에 가입한 멤버 리스트 조회")
    void getMembers() {

        //given
        User leader = util.newUser("leader");
        User member1 = util.newUser("member1");
        User member2 = util.newUser("member2");

        Long clubId = util.newClub(leader, 3);
        Club club = clubRepository.findById(clubId).get();

        MemberRegister.register(club, member1);
        MemberRegister.register(club, member2);

        //when
        assertThatNoException().isThrownBy(club::getMembers);
        List<User> members = club.getMembers();

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(members).contains(leader, member1, member2);
    }
}