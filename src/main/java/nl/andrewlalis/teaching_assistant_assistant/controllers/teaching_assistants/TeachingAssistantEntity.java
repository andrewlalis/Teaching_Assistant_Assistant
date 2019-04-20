package nl.andrewlalis.teaching_assistant_assistant.controllers.teaching_assistants;

import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeachingAssistantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class TeachingAssistantEntity {

    private TeachingAssistantRepository teachingAssistantRepository;

    protected TeachingAssistantEntity(TeachingAssistantRepository teachingAssistantRepository) {
        this.teachingAssistantRepository = teachingAssistantRepository;
    }

    @GetMapping("/teaching_assistants/{id}")
    public String get(@PathVariable long id, Model model) {
        Optional<TeachingAssistant> optionalTeachingAssistant = this.teachingAssistantRepository.findById(id);
        optionalTeachingAssistant.ifPresent(teachingAssistant -> model.addAttribute("teachingAssistant", teachingAssistant));
        return "teaching_assistants/entity";
    }

    @GetMapping("/teaching_assistants/{id}/delete")
    public String delete(@PathVariable long id) {
        Optional<TeachingAssistant> optionalTeachingAssistant = this.teachingAssistantRepository.findById(id);
        optionalTeachingAssistant.ifPresent(teachingAssistant -> {
            teachingAssistant.getCourses().forEach(course -> course.removeParticipant(teachingAssistant));
            teachingAssistant.getTeams().forEach(team -> {
                team.removeMember(teachingAssistant);
            });
            this.teachingAssistantRepository.delete(teachingAssistant);
        });

        return "redirect:/teaching_assistants";
    }
}
