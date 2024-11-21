package az.evoacademy.backend.repository.user;

import az.evoacademy.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
