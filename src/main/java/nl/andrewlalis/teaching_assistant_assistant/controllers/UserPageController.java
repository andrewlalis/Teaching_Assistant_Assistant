package nl.andrewlalis.teaching_assistant_assistant.controllers;

import nl.andrewlalis.teaching_assistant_assistant.model.security.User;
import nl.andrewlalis.teaching_assistant_assistant.model.security.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * An abstract controller which simplifies the definition of controllers for pages that require a user to be signed in.
 */
public abstract class UserPageController {

    /**
     * A shortcut to get the current authenticated user.
     * @param auth The spring authentication.
     * @return The user that's logged in.
     */
    @ModelAttribute("user")
    protected User getUser(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails.getUser();
    }
}
