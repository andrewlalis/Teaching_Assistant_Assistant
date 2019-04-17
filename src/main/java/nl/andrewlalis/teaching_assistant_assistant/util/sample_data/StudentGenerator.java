package nl.andrewlalis.teaching_assistant_assistant.util.sample_data;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;

public class StudentGenerator extends PersonGenerator<Student> {

    public StudentGenerator(long seed) {
        super(seed);
    }

    @Override
    public Student generate() {
        String firstName = this.getRandomFirstName();
        String lastName = this.getRandomLastName();
        return new Student(firstName, lastName, this.getRandomEmailAddress(firstName, lastName), null, this.getRandomInteger(0, 100000000));
    }
}
