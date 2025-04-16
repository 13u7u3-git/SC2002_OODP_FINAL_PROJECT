package officer;

import enums.FlatType;
import enums.RegistrationStatus;
import model.enquiry.Enquiry;
import model.form.Application;
import model.form.RegistrationForm;
import model.project.Project;
import model.user.Applicant;
import model.user.Officer;
import util.UniqueId;

import java.time.LocalDate;
import java.util.List;

public class OfficerService {

    private final ApplicantService applicantService = ApplicantService.getInstance();
    private OfficerService() {}

    private static class Holder {
        private static final OfficerService INSTANCE = new OfficerService();
    }

    public static OfficerService getInstance() {
        return OfficerService.Holder.INSTANCE;
    }
    /**
     * Registers an HDB Officer for a project.
     * - The officer should not have applied as an applicant for the project.
     * - The officer should not have an active registration for another project in the current application period.
     */
    public boolean registerAsOfficer(Officer officer, Project project) {
        if (officer.getApplication() != null && officer.getApplication().getProject().equals(project)) {
            System.out.println("Cannot register as officer because you have applied as an applicant for this project.");
            return false;
        }
        if (officer.getRegistrationFormList() != null) {
            LocalDate now = LocalDate.now();
            Project regProj = officer.getRegistrationFormList().getProject();
            if (!now.isBefore(regProj.getApplicationOpeningDate()) && !now.isAfter(regProj.getApplicationClosingDate())) {
                System.out.println("You already have a registration for a project in the current application period.");
                return false;
            }
        }
        UniqueId idGen = new UniqueId();
        int registrationId = idGen.getNextRegistrationId();
        RegistrationForm registrationForm = new RegistrationForm(registrationId, officer, project,
                RegistrationStatus.PENDING, LocalDate.now());
        officer.setRegistrationForm(registrationForm);
        project.addOfficerRegistrationForm(registrationForm);
        System.out.println("Officer registration form submitted.");
        return true;
    }

    /**
     * Returns the registration status of the officer.
     */
    public RegistrationForm viewRegistrationStatus(Officer officer) {
        return officer.getRegistrationFormList();
    }

    /**
     * Returns the project the officer is currently handling.
     */
    public Project viewHandlingProject(Officer officer) {
        return officer.getCurrentAssignedProject();
    }

    /**
     * Returns all enquiries for a given project.
     */
    public List<Enquiry> viewEnquiriesForProject(Officer officer, Project project) {
        // Officers can view enquiries regardless of the project's visibility.
        return project.getEnquiryList();
    }

    /**
     * Adds a reply to an enquiry.
     */
    public void replyToEnquiry(Officer officer, Enquiry enquiry, String replyMessage) {
        UniqueId idGen = new UniqueId();
        int replyId = idGen.getNextReplyId();
        model.enquiry.Reply reply = new model.enquiry.Reply(replyId, replyMessage, officer, LocalDate.now());
        enquiry.addReply(reply);
        System.out.println("Reply added.");
    }

    /**
     * Completes the flat booking process:
     * - Updates the applicant's application status from SUCCESSFUL to BOOKED.
     * - Records the flat booking form.
     */
    public boolean completeFlatBooking(Officer officer, Applicant applicant, FlatType flatType) {
        Application app = applicant.getApplication();
        if (app == null || app.getApplicationStatus() != enums.ApplicationStatus.SUCCESSFUL) {
            System.out.println("Applicant is not eligible for flat booking.");
            return false;
        }
        // For this example, we assume that booking is recorded only if no previous booking exists.
        // (A more complete design might check at the applicant level.)
        UniqueId idGen = new UniqueId();
        int bookingFormId = idGen.getNextApplicationId(); // Ideally use a dedicated method for booking IDs.
        FlatBookingForm bookingForm = new FlatBookingForm(bookingFormId, app, LocalDate.now());
        officer.getFlatBookingFormList().add(bookingForm);
        app.setApplicationStatus(enums.ApplicationStatus.BOOKED);
        System.out.println("Flat booking completed. Application status updated to BOOKED.");
        return true;
    }

    /**
     * Generates a receipt for the applicant’s flat booking.
     */
    public String generateReceipt(Officer officer, Applicant applicant) {
        Application app = applicant.getApplication();
        if (app == null || app.getApplicationStatus() != enums.ApplicationStatus.BOOKED) {
            return "No flat booking found for applicant.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Flat Booking Receipt:\n");
        sb.append("Applicant Name: ").append(applicant.getName()).append("\n");
        sb.append("NRIC: ").append(applicant.getNric()).append("\n");
        sb.append("Age: ").append(applicant.getAge()).append("\n");
        sb.append("Marital Status: ").append(applicant.getMaritalStatus()).append("\n");
        sb.append("Flat Type: ").append(app.getFlatType()).append("\n");
        sb.append("Project: ").append(app.getProject().getName()).append("\n");
        return sb.toString();
    }

    // Additional applicant functionalities can be delegated to ApplicantService:
    public List<Project> viewOpenProjects(Officer officer, List<Project> allProjects) {
        // Officers have all applicant capabilities.
        return applicantService.viewOpenProjects(officer, allProjects);
    }

    public boolean applyForProject(Officer officer, Project project, FlatType requestedFlatType) {
        return applicantService.applyForProject(officer, project, requestedFlatType);
    }
}
