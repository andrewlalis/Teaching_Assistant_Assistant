package nl.andrewlalis.teaching_assistant_assistant.util.team_importing;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;

import java.time.ZonedDateTime;

/**
 * Represents a pair of students, one of which has indicated that the other is their partner.
 */
public class StudentRecordEntry {

    private Student student;

    private Student partnerStudent;

    private ZonedDateTime dateTime;

    public StudentRecordEntry(ZonedDateTime dateTime, Student student, Student partnerStudent) {
        this.student = student;
        this.partnerStudent = partnerStudent;
        this.dateTime = dateTime;
    }

    public Student getStudent() {
        return this.student;
    }

    public Student getPartnerStudent() {
        return this.partnerStudent;
    }

    public ZonedDateTime getDateTime() {
        return this.dateTime;
    }

    public boolean hasPartner() {
        return this.partnerStudent != null;
    }

    @Override
    public String toString() {
        return "Entry at: " + this.dateTime.toString() + "\n\tStudent: " + this.getStudent() + "\n\tPreferred partner: " + this.getPartnerStudent();
    }
}
