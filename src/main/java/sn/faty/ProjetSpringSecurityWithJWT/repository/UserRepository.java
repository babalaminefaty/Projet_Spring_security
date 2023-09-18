package sn.faty.ProjetSpringSecurityWithJWT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.faty.ProjetSpringSecurityWithJWT.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
