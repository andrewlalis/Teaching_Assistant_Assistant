package nl.andrewlalis.teaching_assistant_assistant.model.security;

import nl.andrewlalis.teaching_assistant_assistant.model.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A custom user details service to supply database persistent user information to Spring Security.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    protected UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        return new nl.andrewlalis.teaching_assistant_assistant.model.security.UserDetails(optionalUser.get());
    }
}
