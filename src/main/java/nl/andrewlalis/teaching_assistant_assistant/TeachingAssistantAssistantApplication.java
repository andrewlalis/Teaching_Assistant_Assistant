package nl.andrewlalis.teaching_assistant_assistant;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.PersonRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.CourseGenerator;
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

		// Generate some example courses.
		CourseGenerator courseGenerator = new CourseGenerator(0, 3, 2, 10, 3, this.courseRepository, teamRepository, personRepository);

		Course c1 = courseGenerator.generate();
		Course c2 = courseGenerator.generate();

		this.courseRepository.save(c1);
		System.out.println(c1.toString());
		this.courseRepository.save(c2);
		System.out.println(c2.toString());

		System.out.println("Course count: " + courseRepository.count());

	}
}
