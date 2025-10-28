package dat250.projects.pollapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import dat250.projects.pollapp.model.User;
import jakarta.persistence.LockModeType;

public interface UserRepository extends CrudRepository<User, Integer> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<User> findByEmail(String email);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<User> findByUsernameAndPassword(String username, String password);
}
