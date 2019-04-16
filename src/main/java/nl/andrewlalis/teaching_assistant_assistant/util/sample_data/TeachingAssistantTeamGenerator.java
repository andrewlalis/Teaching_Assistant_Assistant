package nl.andrewlalis.teaching_assistant_assistant.util.sample_data;

import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;

public class TeachingAssistantTeamGenerator extends TeamGenerator<TeachingAssistantTeam, TeachingAssistant> {

    public TeachingAssistantTeamGenerator(long seed, int groupSize, boolean groupSizeVariable) {
        super(seed, groupSize, groupSizeVariable);
    }

    @Override
    protected TeachingAssistantTeam getNewTeam() {
        return new TeachingAssistantTeam();
    }

    @Override
    protected PersonGenerator<TeachingAssistant> getPersonGenerator(long seed) {
        return new TeachingAssistantGenerator(seed);
    }
}
