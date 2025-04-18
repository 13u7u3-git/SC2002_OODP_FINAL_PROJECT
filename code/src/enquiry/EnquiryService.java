package enquiry;

import applicant.Applicant;
import helper.Color;
import helper.UniqueId;
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

    public void viewEnquiry(List<Enquiry> enquiries) {
        System.out.println("Your enquiries:");
        enquiries.forEach(System.out::println);
    }

    // manager wont even have the option to edit enquiry in menu
    public void editEnquiry(Enquiry enquiry, String newMessage) {
        if (newMessage == null) {
            Color.println("You must write something.", Color.RED);
        }

        enquiry.setEnquiry(newMessage);
        Color.println("Enquiry edited successfully.", Color.GREEN);
    }

    // app / off wont even have the option to delete in menu. They will never touch this function.
    public void deleteEnquiry(Enquiry enquiry) {
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

    public void viewEnquiriesFrom(Project project) {
        List<Enquiry> enquiries = projectService.getAllEnquiriesFrom(project);

        // Print project header once
        Color.println("\nProject: " + project.getName() +
                " (ID: " + project.getId() + ")", Color.YELLOW);

        if (enquiries.isEmpty()) {
            Color.println("  No enquiries for this project.", Color.RED);
            return;
        }

        // Print all enquiries for this project
        for (Enquiry enquiry : enquiries) {
            Color.print("  Enquiry ID: " + enquiry.getId(), Color.CYAN);
            Color.println("  From: " + enquiry.getApplicant().getName(), Color.CYAN);
            Color.println("  Message: " + enquiry.getEnquiry(), Color.CYAN);

            // Print reply if exists
            if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
                Color.println("  Reply: " + enquiry.getReply(), Color.GREEN);
                Color.println("  Responded by: " + enquiry.getRespondent().getName() +
                        " on " + enquiry.getDateReplied(), Color.GREEN);
            } else {
                Color.println("  Status: Pending reply", Color.RED);
            }

            Color.println("  -----------------------------", Color.CYAN);
        }
    }

    public void viewEnquiresFromAllProjects(ProjectRegistry projectRegistry) {
        List<Enquiry> enquiries = projectService.getAllEnquiriesFromAllProjects(projectRegistry);
        Integer currentProjectId = null;

        for(Enquiry enquiry : enquiries){
            // If we're starting a new project, print the project header
            if (!enquiry.getProject().getId().equals(currentProjectId)) {
                currentProjectId = enquiry.getProject().getId();
                Color.println("\nProject: " + enquiry.getProject().getName() +
                        " (ID: " + currentProjectId + ")", Color.YELLOW);
                Color.println("  -----------------------------", Color.CYAN);
            }

            // Print enquiry details
            Color.println("  Enquiry ID: " + enquiry.getId(), Color.CYAN);
            Color.println("  From: " + enquiry.getApplicant().getName(), Color.CYAN);
            Color.println("  Message: " + enquiry.getEnquiry(), Color.CYAN);

            // Print reply if exists
            if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
                Color.println("  Reply: " + enquiry.getReply(), Color.GREEN);
                Color.println("  Responded by: " + enquiry.getRespondent().getName() +
                        " on " + enquiry.getDateReplied(), Color.GREEN);
            } else {
                Color.println("  Status: Pending reply", Color.RED);
            }

            Color.println("  -----------------------------", Color.CYAN);
        }
    }
}