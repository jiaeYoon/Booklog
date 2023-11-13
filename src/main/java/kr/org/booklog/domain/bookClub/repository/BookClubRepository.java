package kr.org.booklog.domain.bookClub.repository;

import kr.org.booklog.domain.bookClub.entity.BookClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookClubRepository extends JpaRepository<BookClub, Long> {

}