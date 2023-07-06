package kr.org.booklog.domain.user.repository;

import kr.org.booklog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
