package nl.andrewlalis.teaching_assistant_assistant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeachingAssistantAssistantApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TeachingAssistantAssistantApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Running startup...");
	}
}
