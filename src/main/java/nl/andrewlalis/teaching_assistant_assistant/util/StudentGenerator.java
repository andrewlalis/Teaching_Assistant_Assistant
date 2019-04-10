package nl.andrewlalis.teaching_assistant_assistant.util;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;

public class StudentGenerator extends PersonGenerator<Student> {

    public StudentGenerator(long seed) {
        super(seed);
    }

    @Override
    public Student generate() {
        String firstName = this.getRandomFirstName();
        String lastName = this.getRandomLastName();
        return new Student(firstName, lastName, this.getRandomEmailAddress(firstName, lastName));
    }
}
