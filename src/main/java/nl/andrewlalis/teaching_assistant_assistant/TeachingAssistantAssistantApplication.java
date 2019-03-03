package nl.andrewlalis.teaching_assistant_assistant;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;

@SpringBootApplication
public class TeachingAssistantAssistantApplication implements CommandLineRunner {

	@Autowired
	CourseRepository courseRepository;

	public static void main(String[] args) {
		SpringApplication.run(TeachingAssistantAssistantApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Running startup...");
		courseRepository.save(new Course("Intro to Information Systems", "INF-1"));
		courseRepository.save(new Course("Program Correctness", "PC-002"));
		System.out.println("Saved two courses.");
		System.out.println("Course count: " + courseRepository.count());
	}
}
