package nl.andrewlalis.teaching_assistant_assistant.util.team_importing;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Provides some methods to streamline the process of transforming a CSV file of student data into a list of valid teams
 * which can be used for a course.
 */
public class StudentTeamImporter {

    /**
     * Imports data from the given File Input Stream into the course.
     * @param fileInputStream An input stream representing the contents of a CSV file.
     * @param course The course to add the students to.
     * @return A list of valid teams that the given file describes.
     */
    public static List<StudentTeam> importFromCSV(InputStream fileInputStream, Course course) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withSkipHeaderRecord()
                .withHeader("timestamp", "email", "name", "number", "github_username", "has_partner", "partner_name", "partner_number", "partner_email", "partner_github_username")
                .parse(new InputStreamReader(fileInputStream));

        List<StudentRecordEntry> studentEntries = extractStudentsFromRecords(records);

        for (StudentRecordEntry entry : studentEntries) {
            System.out.println(entry.toString());
        }

        return new ArrayList<>();

    }

    /**
     * Extracts all student data from a list of records, and automatically discards outdated responses (those where the
     * same student submitted more than once).
     * @param records The list of records in the CSV file.
     * @return A mapping for each timestamp to
     */
    private static List<StudentRecordEntry> extractStudentsFromRecords(Iterable<CSVRecord> records) {
        List<StudentRecordEntry> studentEntries = new ArrayList<>();

        for (CSVRecord record : records) {
            // Parse the actual student.
            Student s = parseStudentRecordData(record.get("name"), record.get("email"), record.get("github_username"), record.get("number"));

            // Parse the student's preferred partner, if they exist.
            Student preferredPartner = null;
            if (record.get("has_partner").equalsIgnoreCase("yes")) {
                preferredPartner = parseStudentRecordData(record.get("partner_name"), record.get("partner_email"), record.get("partner_github_username"), record.get("partner_number"));
            }

            // Parse the timestamp.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm:ss a O ");
            ZonedDateTime dateTime = ZonedDateTime.parse(record.get("timestamp") + ' ', formatter);
            // A space is added because of a java bug: https://stackoverflow.com/questions/37287103/why-does-gmt8-fail-to-parse-with-pattern-o-despite-being-copied-straight-ou

            studentEntries.add(new StudentRecordEntry(dateTime, s, preferredPartner));
        }

        studentEntries = removeDuplicateEntries(studentEntries);

        return studentEntries;
    }


    private static List<StudentRecordEntry> removeDuplicateEntries(List<StudentRecordEntry> studentEntries) {
        List<StudentRecordEntry> uniqueStudentEntries = new ArrayList<>();

        for (StudentRecordEntry entry : studentEntries) {
            // Check for if the current entry's student already exists.
            boolean duplicateFound = false;
            for (StudentRecordEntry existingEntry : uniqueStudentEntries) {
                if (entry.getStudent().equals(existingEntry.getStudent())) {
                    duplicateFound = true;
                    // Check if the existing entry is older than the new one; it should be overwritten.
                    if (existingEntry.getDateTime().isBefore(entry.getDateTime())) {
                        uniqueStudentEntries.remove(existingEntry);
                        uniqueStudentEntries.add(entry);
                        break;
                    }
                }
            }

            if (!duplicateFound) {
                uniqueStudentEntries.add(entry);
            }
        }

        return uniqueStudentEntries;
    }

    /**
     * Creates a student object from given entries in a record obtained from a CSV file.
     * @param name The name value.
     * @param email The email value.
     * @param githubUsername The github_username value.
     * @param studentNumber The number value.
     * @return A student object constructed from the given data.
     */
    private static Student parseStudentRecordData(String name, String email, String githubUsername, String studentNumber) {
        // Extract a sensible first and last name.
        String[] nameSegments = name.split(" ");
        String firstName = "No First Name Given";
        String lastName = "No Last Name Given";

        if (nameSegments.length > 0) {
            firstName = nameSegments[0];
        }

        if (nameSegments.length > 1) {
            lastName = String.join(" ", Arrays.copyOfRange(nameSegments, 1, nameSegments.length));
        }

        // Extract a sensible github username.
        String githubURL = "https://github.com/";

        if (githubUsername.startsWith(githubURL)) {
            githubUsername = githubUsername.substring(githubURL.length());
        }

        // Create the student.
        return new Student(firstName, lastName, email, githubUsername, Integer.parseInt(studentNumber));
    }

}
