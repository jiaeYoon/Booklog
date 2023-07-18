package kr.org.booklog.domain.user.repository;

import kr.org.booklog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findTop1ByOrderByIdDesc();

    Optional<User> findByEmail(String email);
}
