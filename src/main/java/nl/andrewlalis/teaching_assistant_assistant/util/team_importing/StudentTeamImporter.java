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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

        List<StudentTeam> studentTeams = generateTeamsFromStudentEntries(studentEntries, 0);

        return studentTeams;
    }

    /**
     * Extracts all student data from a list of records, and automatically discards outdated responses (those where the
     * same student submitted more than once).
     * @param records The list of records in the CSV file.
     * @return A list of all student entries. A student entry is an intermediate object used to organize the parsed data
     * from each record in the CSV file.
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

    /**
     * Generates a list of teams from a list of unique student entries. The algorithm is described as follows:
     *
     * 1. Select the next student from the list of entries.
     * 2. If the student (A) has specified a partner (B), search for that partner (B) in the list of entries.
     * 3. If the partner (B) also specified the student (A) as their preferred partner, a team is created from the two.
     *      a. Pop both student entries from the list of entries.
     * 4. Else, place student (A) in a queue of single students.
     *      a. Pop student (A) from the list of entries.
     * 5. If the list of entries is not empty, go to 1.
     * 6. The list of entries is now empty, and zero or more entries exist in a queue of single students.
     * 7. Pop two (or as many as possible) students randomly from the queue, and form a team from them.
     * 8. Repeat until the queue of single students is empty.
     * 9. There is now a list of student teams and no student should remain unprocessed.
     *
     * @param entries A list of record entries.
     * @param seed A seed to use for randomization of single student teams.
     * @return A list of student teams.
     */
    private static List<StudentTeam> generateTeamsFromStudentEntries(List<StudentRecordEntry> entries, long seed) {
        List<StudentTeam> teams = new ArrayList<>();
        List<StudentRecordEntry> singleStudentsEntryQueue = new ArrayList<>();
        Random random = new Random(seed);

        // Realize step 5. Loop until there are no more processed entries.
        while (!entries.isEmpty()) {
            // Step 1. Select the next student from the list of entries.
            StudentRecordEntry entry = entries.remove(0);

            // Step 2. If the student has a partner, search for that partner.
            if (entry.hasPartner()) {
                boolean partnerFound = false; // Use this to keep track of if a partner is actually found.
                for (int i = 0; i < entries.size(); i++) {
                    StudentRecordEntry secondEntry = entries.get(i);
                    // Step 3. If the partner specifies their partner as the student (both want each other), then create a team.
                    if (
                            entry.getPartnerStudent().equals(secondEntry.getStudent())
                            && secondEntry.hasPartner()
                            && secondEntry.getPartnerStudent().equals(entry.getStudent())
                    ) {
                        partnerFound = true;

                        entries.remove(i); // Step 3a. The first student has been popped, so pop this one.

                        StudentTeam team = new StudentTeam();
                        team.addMember(entry.getStudent());
                        team.addMember(secondEntry.getStudent());
                        teams.add(team);

                        break;
                    }
                }

                // Step 4. If no partner was found, then add this entry to the queue of single students.
                if (!partnerFound) {
                    singleStudentsEntryQueue.add(entry);
                }
            } else {
                // Also Step 4. The student chose to have no partner, so add this entry to the queue of single students.
                singleStudentsEntryQueue.add(entry);
            }
        }

        // We are now at step 6.
        while (!singleStudentsEntryQueue.isEmpty()) {
            StudentTeam team = new StudentTeam();
            StudentRecordEntry firstRandomEntry = singleStudentsEntryQueue.remove(random.nextInt(singleStudentsEntryQueue.size()));
            team.addMember(firstRandomEntry.getStudent());

            // Check if there's another student in the queue.
            if (!singleStudentsEntryQueue.isEmpty()) {
                StudentRecordEntry secondRandomEntry = singleStudentsEntryQueue.remove(random.nextInt(singleStudentsEntryQueue.size()));
                team.addMember(secondRandomEntry.getStudent());
            }

            teams.add(team);
        }

        return teams;
    }

    /**
     * Scans a list of student entries and removes entries which are outdated by newer entries by the same student, such
     * that the resulting list contains only one unique entry per student.
     * @param studentEntries The list of student entries, possibly containing duplicates.
     * @return A list of student entries in which all outdated duplicates have been removed.
     */
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
