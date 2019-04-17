package nl.andrewlalis.teaching_assistant_assistant.util.sample_data;

import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;

public class TeachingAssistantGenerator extends PersonGenerator<TeachingAssistant> {

    public TeachingAssistantGenerator(long seed) {
        super(seed);
    }

    @Override
    public TeachingAssistant generate() {
        String firstName = this.getRandomFirstName();
        String lastName = this.getRandomLastName();
        return new TeachingAssistant(firstName, lastName, null, this.getRandomEmailAddress(firstName, lastName));
    }
}
