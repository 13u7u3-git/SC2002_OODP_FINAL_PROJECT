package officer;

import java.util.List;
import applicant.Application;
import enums.ApplicationStatus;
import enums.FlatType;
import project.Project;
import officer.RegistrationForm;
import applicant.ApplicantService;

public class OfficerService {
    private static OfficerService instance = null;
    private ApplicantService applicantService = ApplicantService.getInstance();

    private OfficerService() {
        // TODO Private constructor for singleton pattern
    }

    public static OfficerService getInstance() {
        if (instance == null) {
            instance = new OfficerService();
        }
        return instance;
    }

    public RegistrationForm createRegistrationForm(Project project, Officer officer) {
        return new RegistrationForm(project, officer);
    }

    public Boolean sendRegistrationRequest(RegistrationForm registrationForm) {
        // TODO Implementation to send registration request
        return true;
    }

    public void viewRegistrationStatus(RegistrationForm registrationForm) {
        // TODO Implementation to view registration status
    }

    public void updateFlatAvailability(Project project, FlatType flatType, int count) {
        // TODO Implementation to update flat availability
    }

    public void processFlatBooking(Application application) {
        // TODO Implementation to process flat booking
    }

    public void retrieveApplicantsDetails(Application application) {
        // TODO Implementation to retrieve applicant details
    }

    public void updateApplicationStatus(Application application, ApplicationStatus status) {
        applicantService.setApplicationStatus(application, status);
    }

    public void updateApplicantProfile(FlatType flatType) {
        // TODO Implementation to update applicant profile
    }

    public void generateReceipt(Application application) {
        // TODO Implementation to generate receipt
    }

    public void viewAllProjects() {
        // TODO Implementation to view all projects
    }
}
