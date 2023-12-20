package kr.org.booklog.domain.user.entity;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.service.ClubService;
import kr.org.booklog.domain.user.dto.JoinedClubResponseDto;
import kr.org.booklog.domain.user.service.UserService;
import kr.org.booklog.util.Util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@Transactional
class UserTest {

    @Autowired UserService userService;
    @Autowired ClubService clubService;

    @Autowired Util util;

    @Test
    @DisplayName("가입한 클럽 목록 조회")
    void getJoinedClubs() {

        //given
        User leader = util.newUser("leader");
        Long club1Id = util.newClub(leader, 3);
        Long club2Id = util.newClub(leader, 4);
        Long club3Id = util.newClub(leader, 5);

        User member = util.newUser("member");
        clubService.join(new SessionUser(member), club1Id);
        clubService.join(new SessionUser(member), club2Id);

        //when
        List<JoinedClubResponseDto> leaderJoinClubs = userService.findJoinClubs(new SessionUser(leader));
        List<JoinedClubResponseDto> memberJoinClubs = userService.findJoinClubs(new SessionUser(member));

        //then
        assertThat(leaderJoinClubs.size()).isEqualTo(3);
        assertThat(memberJoinClubs.size()).isEqualTo(2);
        assertThatNoException()
                .isThrownBy(() -> memberJoinClubs.get(0).getClubName());
    }
}