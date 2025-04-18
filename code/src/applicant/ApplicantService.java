package applicant;

import java.util.List;

import enquiry.Enquiry;
import enums.ApplicationStatus;
import enums.FlatType;
import enums.UserType;
import helper.Color;
import project.ProjectService;
import user.User;
import project.Project;
import enums.BookingStatus;

public class ApplicantService {
    private static ApplicantService instance = null;
    ProjectService projectService = ProjectService.getInstance();

    private ApplicantService() {
        // Private constructor for singleton
    }

    public static ApplicantService getInstance() {
        if (instance == null) {
            instance = new ApplicantService();
        }
        return instance;
    }

    public void viewEligibleProjects(User user) {
        // TODO: Implement logic to display eligible projects for the user
    }

    public Application createApplication(User applicant, Project project, FlatType flatType) {
        return new Application( applicant, project, flatType);
    }

    // applicant service is like: i'll just give u the application and u being project service will sort out which project it belongs to yourself
    public void submitApplication(Application application) {
        if(!projectService.addApplicationToProject(application)){
            Color.println("Application not submitted. Applications are not open for this project.", Color.RED);
        }
        else{
            Color.println("Application submitted successfully.", Color.GREEN);
        }
    }

    public void setApplicationStatus(Application application, ApplicationStatus status) {
        application.setApplicationStatus(status);
    }

    public void viewApplicationStatus(Application application) {
        Color.println("Application Status: " + application.getApplicationStatus(), Color.BLUE);
    }

    public void viewProjectAppliedFor(Application application) {

    }

    public void withdrawApplication(Application application) {
        // TODO: Implement logic to withdraw the application
    }

    public void sendBookingRequest(Application application) {
        // TODO: Implement logic to send booking request
    }

    public void updateBookingStatus(Application application, BookingStatus status) {
        // TODO: Implement logic to update booking status
    }

    public void viewAllProjects() {
        // TODO: Implement logic to display all available projects
    }

    //validate current applicant
    public boolean validateApplicant(Applicant applicant, Application application){
        return applicant.equals(application.getApplicant());
    }
    public boolean validateApplicant(Applicant applicant, Enquiry enquiry){
        return applicant.equals(enquiry.getApplicant());
    }
}
