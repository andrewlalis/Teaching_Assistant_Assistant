package nl.andrewlalis.teaching_assistant_assistant.util;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;

/**
 * Generates random people for testing purposes.
 */
public abstract class PersonGenerator<P extends Person> extends TestDataGenerator<P> {

    private static final String[] FIRST_NAMES = {
            "Andrew",
            "John",
            "Klaus",
            "William",
            "Bruce",
            "James",
            "Kyle",
            "Michael",
            "Connor",
            "Kate",
            "Sarah",
            "Alex",
            "George",
            "Anna",
            "Bianca"
    };

    private static final String[] LAST_NAMES = {
            "Lalis",
            "Smith",
            "Williams",
            "Willis",
            "Peterson",
            "van Dijk",
            "Rakshiev",
            "Jordan",
            "Kiel",
            "Salamanca",
            "Rockwell",
            "Rockefeller",
            "Armstrong"
    };

    private static final String[] EMAIL_DOMAINS = {
            "gmail.com",
            "hotmail.com",
            "mail.ru",
            "student.rug.nl",
            "yahoo.com"
    };

    public PersonGenerator(long seed) {
        super(seed);
    }

    protected String getRandomFirstName() {
        return (String) this.getRandomObjectFromArray(FIRST_NAMES);
    }

    protected String getRandomLastName() {
        return (String) this.getRandomObjectFromArray(LAST_NAMES);
    }

    protected String getRandomEmailAddress(String firstName, String lastName) {
        return firstName + '.' + lastName + this.getRandom().nextInt(1000) + '@' + this.getRandomObjectFromArray(EMAIL_DOMAINS);
    }
}
