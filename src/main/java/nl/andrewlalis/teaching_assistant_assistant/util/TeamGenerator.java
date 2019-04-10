package nl.andrewlalis.teaching_assistant_assistant.util;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class TeamGenerator<T extends Team, P extends Person> extends TestDataGenerator<T> {

    private int groupSize;
    private boolean groupSizeVariable = false;
    private PersonGenerator<P> personGenerator;

    public TeamGenerator(long seed, int groupSize, boolean groupSizeVariable) {
        super(seed);
        this.groupSize = groupSize;
        this.groupSizeVariable = groupSizeVariable;
        this.personGenerator = this.getPersonGenerator(seed);
    }

    protected abstract T getNewTeam();

    protected abstract PersonGenerator<P> getPersonGenerator(long seed);

    @Override
    public T generate() {
        T team = this.getNewTeam();
        List<P> members = new ArrayList<>(this.groupSize);
        for (int i = 0; i < this.groupSize; i++) {
            members.add(this.personGenerator.generate());
        }
        //team.addMembers(members);
        return team;
    }
}
