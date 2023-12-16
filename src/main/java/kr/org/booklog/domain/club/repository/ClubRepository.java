package kr.org.booklog.domain.club.repository;

import kr.org.booklog.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Club c where id = :id")
    Optional<Club> findByIdWithPessimisticLock(@Param("id") Long id);
}