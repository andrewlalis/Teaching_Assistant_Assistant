package nl.andrewlalis.teaching_assistant_assistant.model.people;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Represents a student, or someone enrolled and submitting assignments for a course.
 */
@Entity
public class Student extends Person {

    /**
     * The student's unique student number, as given by the university.
     */
    @Column(unique = true, nullable = false)
    private int studentNumber;

    /**
     * Default constructor for JPA.
     */
    protected Student() {}

    /**
     * Constructs a new student with all the properties of a Person, and any extra properties.
     * @param firstName The student's first name.
     * @param lastName The student's last name.
     * @param emailAddress The student's email address.
     * @param githubUsername The student's Github username.
     * @param studentNumber The student's unique student number.
     */
    public Student(String firstName, String lastName, String emailAddress, String githubUsername, int studentNumber) {
        super(firstName, lastName, emailAddress, githubUsername);

        this.studentNumber = studentNumber;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    /**
     * Determines if two students are equal. They are considered equal if their person attributes are the same, or
     * their student-specific attributes are equal.
     * @param o The other object.
     * @return True if the other object is the same student.
     */
    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        }

        if (!(o instanceof Student)) {
            return false;
        }

        Student s = (Student) o;

        return this.getStudentNumber() == s.getStudentNumber();
    }

    @Override
    public String toString() {
        return super.toString() + ", Student Number: " + this.getStudentNumber();
    }
}
