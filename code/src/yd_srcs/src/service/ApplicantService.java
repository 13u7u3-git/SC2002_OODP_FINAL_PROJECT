package service;

import enums.ApplicationStatus;
import enums.FlatType;
import enums.WithdrawalStatus;
import model.form.Application;
import model.project.Project;
import model.user.Applicant;
import util.UniqueId;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicantService {
    private ApplicantService() {}

    private static class Holder {
        private static final ApplicantService INSTANCE = new ApplicantService();
    }

    public static ApplicantService getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Returns the list of projects that are visible and open to the applicant’s user group.
     * (For example, you may add additional filtering based on marital status, etc.)
     */
    public List<Project> viewOpenProjects(Applicant applicant, List<Project> allProjects) {
        // Filter projects by visibility.
        return allProjects.stream()
                .filter(Project::isVisible)
                .collect(Collectors.toList());
    }

    /**
     * Submits an application for a project.
     * - Single applicants (35+ years) can only apply for 2-Room flats.
     * - Married applicants (21+ years) can apply for either 2-Room or 3-Room flats.
     * - Cannot apply if already applied.
     */
    public boolean applyForProject(Applicant applicant, Project project, FlatType requestedFlatType) {
        // Check if the applicant has already applied.
        if (applicant.getApplication() != null) {
            System.out.println("You have already applied for a project.");
            return false;
        }

        // Eligibility check based on marital status and age.
        String maritalStatus = applicant.getMaritalStatus().toString().toUpperCase();
        if ("SINGLE".equals(maritalStatus)) {
            if (applicant.getAge() < 35) {
                System.out.println("Single applicants must be 35 or older.");
                return false;
            }
            if (requestedFlatType != FlatType.TWO_ROOM) {
                System.out.println("Single applicants can only apply for 2-Room flats.");
                return false;
            }
        } else { // Assume Married
            if (applicant.getAge() < 21) {
                System.out.println("Married applicants must be 21 or older.");
                return false;
            }
        }

        // Create the application.
        UniqueId idGen = new UniqueId(); // (Ideally, UniqueId should be a singleton.)
        int applicationId = idGen.getNextApplicationId();
        Application application = new Application(applicationId,
                applicant, // Linking applicant to the application.
                project,
                requestedFlatType,
                ApplicationStatus.PENDING,
                WithdrawalStatus.NONE,
                LocalDate.now());
        // For simplicity, assume the applicant’s Application field is set externally.
        // In a real design, you might recreate the Applicant with the new application,
        // or manage it via a mutable field.
        // Here, we simply add the application to the project.
        project.addApplication(application);
        System.out.println("Application submitted successfully.");
        return true;
    }

    /**
     * Displays the applied project and current application status.
     */
    public void viewAppliedProject(Applicant applicant) {
        Application app = applicant.getApplication();
        if (app == null) {
            System.out.println("No application found.");
        } else {
            System.out.println("Application Status: " + app.getApplicationStatus());
            System.out.println("Applied Project: " + app.getProject().getName());
        }
    }

    /**
     * Requests withdrawal for the applicant's BTO application.
     */
    public boolean requestWithdrawal(Applicant applicant) {
        Application app = applicant.getApplication();
        if (app == null) {
            System.out.println("No application to withdraw.");
            return false;
        }
        app.setWithdrawalRequestStatus(WithdrawalStatus.REQUESTED);
        app.getProject().addWithdrawalRequest(app);
        System.out.println("Withdrawal request submitted.");
        return true;
    }
}
