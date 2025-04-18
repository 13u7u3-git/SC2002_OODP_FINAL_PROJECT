package applicant;

import enquiry.Enquiry;
import enquiry.EnquiryService;
import enums.FlatType;
import helper.UniqueId;
import project.Project;
import project.ProjectRegistry;
import project.ProjectService;

import java.util.List;

public class ApplicantController {
    private final ApplicantService applicantService;
    private final EnquiryService enquiryService;
    private final ProjectService projectService;
    private final ProjectRegistry projectRegistry;
    private final UniqueId uniqueId;
    private final Applicant applicant;

    public ApplicantController(ApplicantService applicantService, EnquiryService enquiryService,
                               ProjectService projectService, ProjectRegistry projectRegistry,
                               UniqueId uniqueId, Applicant applicant) {
        this.applicantService = applicantService;
        this.enquiryService = enquiryService;
        this.projectService = projectService;
        this.projectRegistry = projectRegistry;
        this.uniqueId = uniqueId;
        this.applicant = applicant;
    }

    /** Retrieves a list of projects the applicant is eligible for. */
    public List<Project> getEligibleProjects() {
        return applicantService.getEligibleProjects(projectService, projectRegistry, applicant);
    }

    /** Applies for a project with the specified flat type. */
    public void applyForProject(Project project, FlatType flatType) {
        Application application = applicantService.createApplication(uniqueId, applicant, project, flatType);
        if (application != null) {
            applicantService.submitApplication(projectService, application);
            applicant.addApplication(application); // Ensure the application is added to the applicant's list
        }
    }

    /** Returns the applicant's most recent application. */
    public Application getCurrentApplication() {
        return applicantService.getCurrentApplication(applicant);
    }

    /** Returns a list of all applications submitted by the applicant. */
    public List<Application> getAllApplications() {
        return applicantService.getAllApplications(applicant);
    }

    /** Sends a booking request for the specified application. */
    public void bookFlat(Application application) {
        applicantService.sendBookingRequest(application);
    }

    /** Requests withdrawal of the specified application. */
    public void requestWithdrawal(Application application) {
        applicantService.withdrawApplication(application);
    }

    /** Creates and submits an enquiry for a project with the given message. */
    public void createEnquiry(Project project, String message) {
        Enquiry enquiry = enquiryService.createEnquiry(uniqueId, project, applicant, message);
        enquiryService.submitEnquiry(enquiry);
        applicant.addEnquiry(enquiry); // Add to applicant's enquiry list
    }

    /** Retrieves a list of all enquiries made by the applicant. */
    public List<Enquiry> getEnquiries() {
        return applicant.getEnquiries(); // Directly access applicant's enquiry list
    }

    /** Edits an existing enquiry with a new message. */
    public void editEnquiry(Enquiry enquiry, String newMessage) {
        enquiryService.editEnquiry(applicant, enquiry, newMessage);
    }

    /** Deletes an existing enquiry. */
    public void deleteEnquiry(Enquiry enquiry) {
        enquiryService.deleteEnquiry(applicant, enquiry);
        applicant.removeEnquiry(enquiry); // Remove from applicant's enquiry list
    }

    /** Returns the applicant's name for display purposes. */
    public String getApplicantName() {
        return applicant.getName();
    }

    /** Retrieves a list of visible projects for enquiry purposes. */
    public List<Project> getProjectsForEnquiry() {
        return projectService.getVisibleProjects(projectRegistry.getProjects());
    }
}