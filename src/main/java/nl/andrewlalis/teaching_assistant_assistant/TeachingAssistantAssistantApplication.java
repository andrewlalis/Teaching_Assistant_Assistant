package nl.andrewlalis.teaching_assistant_assistant;

import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.PersonRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeachingAssistantAssistantApplication implements CommandLineRunner {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(TeachingAssistantAssistantApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Running startup...");
//
//		String exampleDate = "2019/04/15 4:13:41 PM GMT+2 ";
//		// Parse the timestamp.
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm:ss a O ");
//		ZonedDateTime dateTime = ZonedDateTime.parse(exampleDate, formatter);
//		System.out.println("Read time: " + dateTime.toString());
	}
}
