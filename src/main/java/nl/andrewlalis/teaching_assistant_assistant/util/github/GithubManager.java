package nl.andrewlalis.teaching_assistant_assistant.util.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.kohsuke.github.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Calendar;

/**
 * Encapsulates much of the github functionality that is needed.
 */
public class GithubManager {

    private GitHub github;
    private String apiKey;

    /**
     * Constructs this manager with an API key.
     * @param apiKey The api key to use.
     * @throws IOException if the key is invalid.
     */
    public GithubManager(String apiKey) throws IOException {
        this.github = GitHub.connectUsingOAuth(apiKey);
        this.apiKey = apiKey;
    }

    /**
     * Constructs this manager with a username/password login combination.
     * @param username The username to use.
     * @param password The password for the above username.
     * @throws IOException if the username or password are invalid.
     */
    public GithubManager(String username, String password) throws IOException {
        this.github = GitHub.connectUsingPassword(username, password);
    }

    /**
     * Generates a new Github Repository for the given student team.
     * @param team The team to create a repository for.
     * @return The name of the created repository.
     */
    public String generateStudentTeamRepository(StudentTeam team) {
        GHOrganization organization;

        try {
            organization = this.github.getOrganization(team.getCourse().getGithubOrganizationName());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not get Github organization with name: " + team.getCourse().getGithubOrganizationName());
            return null;
        }

        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        String repositoryName = year + "_Team_" + team.getId();

        // Get the TA team which manages this repository.
        GHTeam teachingAssistantGithubTeam;

        try {
            teachingAssistantGithubTeam = organization.getTeamByName(team.getAssignedTeachingAssistantTeam().getGithubTeamName());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not get team by name: " + team.getAssignedTeachingAssistantTeam().getGithubTeamName());
            return null;
        }

        // Create the repo
        GHCreateRepositoryBuilder repositoryBuilder = organization.createRepository(repositoryName);
        repositoryBuilder.wiki(false);
        repositoryBuilder.issues(true);
        repositoryBuilder.description("University of Groningen OOP Student Repository");
        repositoryBuilder.private_(true);
        repositoryBuilder.autoInit(false);
        GHRepository repository;
        try {
             repository = repositoryBuilder.create();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not create repository: " + repositoryName);
            return null;
        }

        try {
            this.addRepositoryToTeam(teachingAssistantGithubTeam, repository);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not add repository " + repositoryName + " to team " + teachingAssistantGithubTeam.getName());
            return null;
        }

        try {
            this.addStarterFile(repository, "program_resources/getting_started.md", "getting_started.md");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not add the starter file to the repository: " + repositoryName);
            return null;
        }

        try {
            this.createDevelopmentBranch(repository);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not create development branch for repository: " + repositoryName);
            return null;
        }

        try {
            this.protectMasterBranch(repository, teachingAssistantGithubTeam);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not add protections to the master branch of " + repositoryName);
            return null;
        }

        try {
            this.addStudentsAsCollaborators(repository, team);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not add students as collaborators to " + repositoryName);
            return null;
        }


        return repositoryName;
    }

    private void addStarterFile(GHRepository repository, String filePath, String fileName) throws IOException {
        GHContentBuilder contentBuilder = repository.createContent();
        contentBuilder.branch("master");
        contentBuilder.message("Initial Commit");

        URL resource = getClass().getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new IOException("Could not get resource identified by " + filePath);
        }
        File f = new File(resource.getFile());

        contentBuilder.content(Files.readAllBytes(f.toPath()));
        contentBuilder.path(fileName);
        contentBuilder.commit();
    }

    private void protectMasterBranch(GHRepository repository, GHTeam adminTeam) throws IOException {
        GHBranchProtectionBuilder protectionBuilder = repository.getBranch("master").enableProtection();
        protectionBuilder.includeAdmins(false);
        protectionBuilder.restrictPushAccess();
        protectionBuilder.teamPushAccess(adminTeam);
        protectionBuilder.addRequiredChecks("ci/circleci");
        protectionBuilder.enable();
    }

    private void addStudentsAsCollaborators(GHRepository repository, StudentTeam studentTeam) throws IOException {
        for (Student student : studentTeam.getStudents()) {
            this.addCollaborator(repository.getFullName(), student.getGithubUsername(), "push");
        }

    }

    private void createDevelopmentBranch(GHRepository repository) throws IOException {
        String sha1 = repository.getBranch(repository.getDefaultBranch()).getSHA1();
        repository.createRef("refs/heads/development", sha1);
    }

    public void addCollaborator(String repositoryName, String githubUsername, String permission) throws IOException {
        try {
            String url = "https://api.github.com/repos/" + repositoryName + "/collaborators/" + githubUsername + "?access_token=" + this.apiKey;
            HttpPut put = new HttpPut(url);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("permission", permission);
            String json = mapper.writeValueAsString(root);
            put.setEntity(new StringEntity(json));
            HttpResponse response = client.execute(put);

            if (response.getStatusLine().getStatusCode() != 201) {
                throw new IOException("Error adding collaborator via url " + url + " : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
            }
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void addRepositoryToTeam(GHTeam team, GHRepository repository) throws IOException {
        team.add(repository, GHOrganization.Permission.ADMIN);
    }

}
