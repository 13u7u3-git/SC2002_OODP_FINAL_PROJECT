package enquiry;

import applicant.Applicant;
import helper.Color;
import UniqueID.UniqueId;
import project.Project;
import project.ProjectRegistry;
import project.ProjectService;
import system.SessionManager;

import java.util.List;

public class EnquiryService {
    private final ProjectService projectService;

    public EnquiryService(final ProjectService projectService) {
        this.projectService = projectService;
    }

    public Enquiry createEnquiry(UniqueId uniqueId, Project project, Applicant applicant, String message) {
        return new Enquiry(uniqueId.getNextEnquiryId(), project, applicant, message);
    }

    public void submitEnquiry(Enquiry enquiry) {
        // Enquiry service tells project enquiry: here's your enquiry, figure out which project it belongs to yourself
        projectService.addEnquiryToProject(enquiry);
        Color.println("Enquiry submitted successfully.", Color.GREEN);
    }

    // manager wont even have the option to edit enquiry in menu
    // have to make sure that they edit their own enquiry.
    public void editEnquiry(Applicant currentUser, Enquiry enquiry, String newMessage) {
        if (!enquiry.getApplicant().equals(currentUser)) {
            Color.println("You are not allowed to edit this enquiry.", Color.RED);
            return;
        }

        if (newMessage == null || newMessage.isBlank()) {
            Color.println("You must write something.", Color.RED);
            return;
        }

        enquiry.setEnquiry(newMessage);
        Color.println("Enquiry edited successfully.", Color.GREEN);
    }


    // manager wont even have the option to delete. Need to confirm that current user deletes their own enquiry.
    // user cannot put random enquiry id and then it deletes that enquiry.

    public void deleteEnquiry(Applicant currentUser, int enquiryId) {
        Applicant currentUser = sessionManager.getLoggedInApplicant();
        return applicantService.deleteEnquiryIfAllowed(currentUser.getId(), enquiryId);

        Enquiry enquiry = enquiryRepository.findById(enquiryId);

        if (enquiry == null) return false;
        if (enquiry.getApplicantId() != applicantId) return false;
        if (enquiry.getReply() != null && !enquiry.getReply().isBlank()) return false;

        enquiryRepository.delete(enquiryId);
        return true;

        if (!enquiry.getApplicant().equals(currentUser)) {
            Color.println("You are not allowed to delete this enquiry.", Color.RED);
            return;
        }


        projectService.removeEnquiryFromProject(enquiry);
        Color.println("Enquiry deleted successfully.", Color.GREEN);
    }



    public void replyToEnquiry(Enquiry enquiry, String reply, SessionManager sessionManager) {
        if (enquiry.getReply().isEmpty()) {
            enquiry.setReply(reply, sessionManager.getCurrentUser());
            Color.println("Enquiry replied successfully.", Color.GREEN);
        }
        Color.println("Enquiry already has reply.", Color.RED);

    }

    public List<Enquiry> getEnquiriesForApplicant() {
        return null;
    }

    public void getEnquiriesFrom(Project project) {
        List<Enquiry> enquiries = projectService.getAllEnquiriesFrom(project);

        // Print project header once
        Color.println("\nProject: " + project.getProjectName() +
                " (ID: " + project.getId() + ")", Color.YELLOW);

        if (enquiries.isEmpty()) {
            Color.println("  No enquiries for this project.", Color.RED);
            return;
        }

        // Print all enquiries for this project
        for (Enquiry enquiry : enquiries) {
            Color.print("  Enquiry ID: " + enquiry.getId(), Color.CYAN);
            Color.println("  From: " + enquiry.getApplicant().getProjectName(), Color.CYAN);
            Color.println("  Message: " + enquiry.getEnquiry(), Color.CYAN);

            // Print reply if exists
            if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
                Color.println("  Reply: " + enquiry.getReply(), Color.GREEN);
                Color.println("  Responded by: " + enquiry.getRespondent().getProjectName() +
                        " on " + enquiry.getDateReplied(), Color.GREEN);
            } else {
                Color.println("  Status: Pending reply", Color.RED);
            }

            Color.println("  -----------------------------", Color.CYAN);
        }
    }

    public void getEnquiresFromAllProjects(ProjectRegistry projectRegistry) {
        List<Enquiry> enquiries = projectService.getAllEnquiriesFromAllProjects(projectRegistry);
        Integer currentProjectId = null;

        for(Enquiry enquiry : enquiries){
            // If we're starting a new project, print the project header
            if (!enquiry.getProject().getId().equals(currentProjectId)) {
                currentProjectId = enquiry.getProject().getId();
                Color.println("\nProject: " + enquiry.getProject().getProjectName() +
                        " (ID: " + currentProjectId + ")", Color.YELLOW);
                Color.println("  -----------------------------", Color.CYAN);
            }

            // Print enquiry details
            Color.println("  Enquiry ID: " + enquiry.getId(), Color.CYAN);
            Color.println("  From: " + enquiry.getApplicant().getProjectName(), Color.CYAN);
            Color.println("  Message: " + enquiry.getEnquiry(), Color.CYAN);

            // Print reply if exists
            if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
                Color.println("  Reply: " + enquiry.getReply(), Color.GREEN);
                Color.println("  Responded by: " + enquiry.getRespondent().getProjectName() +
                        " on " + enquiry.getDateReplied(), Color.GREEN);
            } else {
                Color.println("  Status: Pending reply", Color.RED);
            }

            Color.println("  -----------------------------", Color.CYAN);
        }
    }
}