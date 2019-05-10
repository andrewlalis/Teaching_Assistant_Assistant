package nl.andrewlalis.teaching_assistant_assistant.util.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Encapsulates much of the github functionality that is needed.
 */
public class GithubManager {

    Logger logger = LogManager.getLogger(GithubManager.class);

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
        logger.info("Generating repository for student team " + team.getId());

        GHOrganization organization;
        try {
            organization = this.github.getOrganization(team.getCourse().getGithubOrganizationName());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not get Github organization with name: " + team.getCourse().getGithubOrganizationName());
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
            logger.error("Could not get team by name: " + team.getAssignedTeachingAssistantTeam().getGithubTeamName());
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
            logger.debug("Creating empty repository " + repositoryName);
            repository = repositoryBuilder.create();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not create repository: " + repositoryName);
            return null;
        }

        try {
            logger.debug("Assigning teaching assistant team " + teachingAssistantGithubTeam.getName() + " to repository.");
            this.addRepositoryToTeam(teachingAssistantGithubTeam, repository);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not add repository " + repositoryName + " to team " + teachingAssistantGithubTeam.getName());
            return null;
        }

        try {
            logger.debug("Adding starting file to the repository.");
            this.addStarterFile(repository, "program_resources/getting_started.md", "getting_started.md");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not add the starter file to the repository: " + repositoryName);
            return null;
        }

        try {
            logger.debug("Creating development branch.");
            this.createDevelopmentBranch(repository);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not create development branch for repository: " + repositoryName);
            return null;
        }

        try {
            logger.debug("Adding protections to the master branch.");
            this.protectMasterBranch(repository, teachingAssistantGithubTeam);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not add protections to the master branch of " + repositoryName);
            return null;
        }

        try {
            logger.debug("Adding students as collaborators.");
            this.addStudentsAsCollaborators(repository, team);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not add students as collaborators to " + repositoryName);
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
        protectionBuilder.addRequiredChecks("ci/circleci: build");
        protectionBuilder.enable();
    }

    private void addStudentsAsCollaborators(GHRepository repository, StudentTeam studentTeam) throws IOException {
        for (Student student : studentTeam.getStudents()) {
            this.addCollaborator(repository.getOwnerName(), repository.getName(), student.getGithubUsername(), "push");
        }

    }

    private void createDevelopmentBranch(GHRepository repository) throws IOException {
        String sha1 = repository.getBranch(repository.getDefaultBranch()).getSHA1();
        repository.createRef("refs/heads/development", sha1);
    }

    public void addCollaborator(String organizationName, String repositoryName, String githubUsername, String permission) throws IOException {
        try {
            String url = "https://api.github.com/repos/" + organizationName + '/' + repositoryName + "/collaborators/" + githubUsername + "?access_token=" + this.apiKey;
            HttpPut put = new HttpPut(url);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("permission", permission);
            String json = mapper.writeValueAsString(root);
            put.setEntity(new StringEntity(json));
            HttpResponse response = client.execute(put);

            if (response.getStatusLine().getStatusCode() != 201) {
                String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                        .lines().collect(Collectors.joining("\n"));
                throw new IOException("Error adding collaborator via url " + url + " : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase() + "\n" + content);
            }
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void removeCollaborator(StudentTeam studentTeam, Student student) throws IOException {
        GHOrganization organization = this.github.getOrganization(studentTeam.getCourse().getGithubOrganizationName());
        GHRepository repository = organization.getRepository(studentTeam.getGithubRepositoryName());
        GHUser user = this.github.getUser(student.getGithubUsername());

        repository.removeCollaborators(user);
    }

    /**
     * Deactivates a repository by removing all collaborator students, unassigning the repository from the TA team that
     * was responsible for it, and archiving it.
     * @param studentTeam The student team for which to archive.
     * @throws IOException If an io exception occurred, duh!
     */
    public void deactivateRepository(StudentTeam studentTeam) throws IOException {
        GHOrganization organization = this.github.getOrganization(studentTeam.getCourse().getGithubOrganizationName());
        GHRepository repository = organization.getRepository(studentTeam.getGithubRepositoryName());
        List<GHUser> users = new ArrayList<>();
        for (Student s : studentTeam.getStudents()) {
            users.add(this.github.getUser(s.getGithubUsername()));
        }

        //repository.removeCollaborators(users);

        GHTeam taTeam = organization.getTeamByName(studentTeam.getAssignedTeachingAssistantTeam().getGithubTeamName());
        taTeam.remove(repository);

        this.archiveRepository(repository);
    }

    private void addRepositoryToTeam(GHTeam team, GHRepository repository) throws IOException {
        team.add(repository, GHOrganization.Permission.ADMIN);
    }

    /**
     * Updates branch protection for a given repository. That is, removes old branch protection and reinstates it to
     * follow updated circleci conventions.
     * @param organizationName The name of the organization.
     * @param repositoryName The name of the repository.
     * @param teamName The name of the team responsible for this repository.
     * @throws IOException If an error occurs with any actions.
     */
    public void updateBranchProtection(String organizationName, String repositoryName, String teamName) throws IOException {
        GHOrganization organization = this.github.getOrganization(organizationName);
        GHRepository repository = organization.getRepository(repositoryName);
        GHTeam team = organization.getTeamByName(teamName);

        repository.getBranch("master").disableProtection();
        GHBranchProtectionBuilder builder = repository.getBranch("master").enableProtection();
        builder.includeAdmins(false);
        builder.restrictPushAccess();
        builder.teamPushAccess(team);
        builder.addRequiredChecks("ci/circleci: build");
        builder.enable();
    }

    /**
     * Archives a repository so that it can no longer be manipulated.
     * TODO: Change to using Github API instead of Apache HttpUtils.
     * @param repo The repository to archive.
     */
    private void archiveRepository(GHRepository repo) throws IOException {
        HttpPatch patch = new HttpPatch("https://api.github.com/repos/" + repo.getFullName() + "?access_token=" + this.apiKey);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("archived", true);
        String json = mapper.writeValueAsString(root);
        patch.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(patch);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Could not archive repository: " + repo.getName() + ". Code: " + response.getStatusLine().getStatusCode());
        }
    }

}
