package nl.andrewlalis.teaching_assistant_assistant.util.sample_data;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;

public class StudentTeamGenerator extends TeamGenerator<StudentTeam, Student> {

    public StudentTeamGenerator(long seed, int groupSize, boolean groupSizeVariable) {
        super(seed, groupSize, groupSizeVariable);
    }

    @Override
    protected StudentTeam getNewTeam() {
        return new StudentTeam();
    }

    @Override
    protected PersonGenerator<Student> getPersonGenerator(long seed) {
        return new StudentGenerator(seed);
    }
}
