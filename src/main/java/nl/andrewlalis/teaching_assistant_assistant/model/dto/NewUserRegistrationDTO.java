package nl.andrewlalis.teaching_assistant_assistant.model.dto;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;

import java.util.List;

/**
 * A data transfer object to aid in the registration of a new authenticated user. This object therefore contains all the
 * data that potential users enter prior to the actual creation of a new user.
 */
public class NewUserRegistrationDTO {

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String username;

    private String password;

    private String passwordConfirm;

    private String selectedPersonType;

    private List<String> availablePersonTypes;

    private Iterable<Course> availableCourses;

    public NewUserRegistrationDTO(List<String> availablePersonTypes, Iterable<Course> availableCourses) {
        this.availablePersonTypes = availablePersonTypes;
        this.availableCourses = availableCourses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<String> getAvailablePersonTypes() {
        return availablePersonTypes;
    }

    public Iterable<Course> getAvailableCourses() {
        return availableCourses;
    }

    public String getSelectedPersonType() {
        return this.selectedPersonType;
    }
}
