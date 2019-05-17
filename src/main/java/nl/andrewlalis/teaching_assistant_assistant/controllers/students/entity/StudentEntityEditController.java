package nl.andrewlalis.teaching_assistant_assistant.controllers.students.entity;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * Controller for editing a student entity.
 */
@Controller("/students/{id}/edit")
public class StudentEntityEditController extends UserPageController {

    private StudentRepository studentRepository;
    private StudentService studentService;

    protected StudentEntityEditController(StudentRepository studentRepository, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    /**
     * Get the data of the student whose information is going to be edited, and add that to the model to be rendered.
     * @param id The id of the student to edit.
     * @param model The view model.
     * @return The edit template which will be populated with the student's data.
     */
    @GetMapping("/students/{id}/edit")
    public String getEdit(@PathVariable long id, Model model) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);
        optionalStudent.ifPresent(student -> model.addAttribute("student", student));
        return "students/entity/edit";
    }

    /**
     * Receives edited data about a student and saves it.
     * @param editedStudent A temporary <code>Student</code> object containing the edited information.
     * @param id The id of the student to edit the information of.
     * @return A redirect to the entity page for the student whose information was just edited.
     */
    @PostMapping(
            value = "/students/{id}/edit",
            consumes = "application/x-www-form-urlencoded"
    )
    public String post(@ModelAttribute Student editedStudent, @PathVariable long id) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);
        optionalStudent.ifPresent(student -> this.studentService.editStudent(student, editedStudent));

        return "redirect:/students/{id}";
    }
}
