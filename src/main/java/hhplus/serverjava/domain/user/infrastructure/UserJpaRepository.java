package hhplus.serverjava.domain.user.infrastructure;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hhplus.serverjava.domain.user.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	List<User> findUsersByStatus(User.State state);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("select u from User u where u.id = :id")
	Optional<User> findById(@Param("id") Long id);
}
