package kr.org.booklog.domain.club.service;

import kr.org.booklog.config.auth.dto.SessionUser;
import kr.org.booklog.domain.club.dto.ClubCreateRequestDto;
import kr.org.booklog.domain.club.dto.ClubResponseDto;
import kr.org.booklog.domain.club.dto.TotalClubResponseDto;
import kr.org.booklog.domain.club.entity.Club;
import kr.org.booklog.domain.club.repository.ClubRepository;
import kr.org.booklog.domain.memberRegister.MemberRegister;
import kr.org.booklog.domain.memberRegister.MemberRegisterRepository;
import kr.org.booklog.domain.user.entity.User;
import kr.org.booklog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final MemberRegisterRepository memberRegisterRepository;

    @Transactional
    public Long create(SessionUser sessionUser, ClubCreateRequestDto requestDto) {
        User user = findUser(sessionUser);
        requestDto.setLeader(user);
        Club club = clubRepository.save(requestDto.toEntity());

        MemberRegister memberRegister = MemberRegister.register(club, user);
        memberRegisterRepository.save(memberRegister);
        return club.getId();
    }

    @Transactional
    public Long join(SessionUser sessionUser, Long id) {
        Club club = clubRepository.findByIdWithPessimisticLock(id)
                .orElseThrow(IllegalArgumentException::new);

        club.checkCapacity();
        User user = findUser(sessionUser);
        MemberRegister memberRegister = MemberRegister.register(club, user);

        return memberRegisterRepository.save(memberRegister).getId();
    }

    private User findUser(SessionUser sessionUser) {
        Long userId = sessionUser.getId();
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<TotalClubResponseDto> findAll() {
        List<Club> clubs = clubRepository.findAll();

        List<TotalClubResponseDto> responseDtos = new ArrayList<>();
        clubs.forEach(club -> responseDtos.add(new TotalClubResponseDto(club)));

        return responseDtos;
    }

    public ClubResponseDto findById(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return new ClubResponseDto(club);
    }
}