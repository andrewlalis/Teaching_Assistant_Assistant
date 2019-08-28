package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
