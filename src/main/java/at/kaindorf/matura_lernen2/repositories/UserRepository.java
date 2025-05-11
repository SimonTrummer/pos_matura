package at.kaindorf.matura_lernen2.repositories;

import at.kaindorf.matura_lernen2.pojos.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
