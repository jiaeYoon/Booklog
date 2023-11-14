package kr.org.booklog.domain.memberRegister;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRegisterRepository extends JpaRepository<MemberRegister, Long> {

}
