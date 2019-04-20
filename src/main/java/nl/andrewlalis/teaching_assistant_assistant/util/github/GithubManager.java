package nl.andrewlalis.teaching_assistant_assistant.util.github;

import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import org.kohsuke.github.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Encapsulates much of the github functionality that is needed.
 */
public class GithubManager {

    private GitHub github;

    public GithubManager(String apiKey) throws IOException {
        this.github = GitHub.connectUsingOAuth(apiKey);
    }

    public GithubManager(String username, String password) throws IOException {
        this.github = GitHub.connectUsingPassword(username, password);
    }

    public void generateStudentTeamRepository(StudentTeam team) throws IOException {
        GHOrganization organization = this.github.getOrganization(team.getCourse().getGithubOrganizationName());
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        String repositoryName = year + "_Team_" + team.getId();

        // Get the TA team which manages this repository.
        GHTeam teachingAssistantGithubTeam = organization.getTeamByName(team.getAssignedTeachingAssistantTeam().getGithubTeamName());

        // Create the repo
        GHCreateRepositoryBuilder repositoryBuilder = organization.createRepository(repositoryName);
        repositoryBuilder.wiki(false);
        repositoryBuilder.issues(false);
        repositoryBuilder.description("University of Groningen OOP Student Repository");
        repositoryBuilder.private_(false);
        repositoryBuilder.autoInit(false);
        repositoryBuilder.team(teachingAssistantGithubTeam);
        GHRepository repository = repositoryBuilder.create();

        // Add getting started file.
        GHContentBuilder contentBuilder = repository.createContent();
        contentBuilder.branch("master");
        contentBuilder.message("Initial Commit");
        File f = new File(getClass().getClassLoader().getResource("program_resources/getting_started.md").getFile());
        contentBuilder.content(Files.readAllBytes(f.toPath()));
        contentBuilder.path("getting_started.md");
        contentBuilder.commit();

        // Create the development branch.
        try {
            String sha1 = repository.getBranch(repository.getDefaultBranch()).getSHA1();
            repository.createRef("refs/heads/development", sha1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Protect master branch.
        GHBranchProtectionBuilder protectionBuilder = repository.getBranch("master").enableProtection();
        protectionBuilder.includeAdmins(false);
        protectionBuilder.restrictPushAccess();
        protectionBuilder.teamPushAccess(teachingAssistantGithubTeam);
        protectionBuilder.addRequiredChecks("ci/circleci");
        protectionBuilder.enable();

        List<GHUser> studentUsers = new ArrayList<>(team.getMembers().size());
        team.getStudents().forEach(student -> {
            try {
                studentUsers.add(this.github.getUser(student.getGithubUsername()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //repository.addCollaborators(studentUsers);

    }

}
