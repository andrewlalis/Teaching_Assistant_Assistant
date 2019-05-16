package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.security.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * The repository for all roles to which users may be assigned.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Try to find a role by the given name.
     * @return An optional Role that has the given name.
     */
    public Optional<Role> findByName(String name);
}
