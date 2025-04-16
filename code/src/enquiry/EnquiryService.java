package enquiry;

import java.util.List;

import applicant.Applicant;
import applicant.ApplicantService;
import helper.Color;
import project.Project;
import project.ProjectService;
import system.SessionManager;
import user.User;

public class EnquiryService {
    private static EnquiryService instance;
    ProjectService projectService = ProjectService.getInstance();
    ApplicantService applicantService = ApplicantService.getInstance();
    SessionManager sessionManager = SessionManager.getInstance();

    private EnquiryService() {
    }

    public static EnquiryService getInstance() {
        if (instance == null) {
            instance = new EnquiryService();
        }
        return instance;
    }

    public Enquiry createEnquiry(Project project, String message) {
        return new Enquiry(project, (Applicant) sessionManager.getCurrentUser(), message);
    }

    public void submitEnquiry(Enquiry enquiry) {
        //enquiry.getProject().getEnquiries().add(enquiry);//illegal > encapsulation violation
        projectService.addEnquiryToProject(enquiry); // correct way
    }

    public void viewEnquiry(Enquiry enquiry) {
        Color.println(enquiry.toString(), Color.CYAN);
    }

    public void editEnquiry(Enquiry enquiry, String newMessage) {
        if(!applicantService.validateApplicant((Applicant) sessionManager.getCurrentUser(),enquiry))
            Color.println("You are not authorized to delete this enquiry.", Color.RED);
        else {
            Color.println("Enquiry edited successfully.", Color.GREEN);
            enquiry.setEnquiry(newMessage);
        }
    }

    public void deleteEnquiry(Enquiry enquiry) {
        //enquiry.getProject().getEnquiries().remove(enquiry);
        //enquiry.getApplicant().removeEnquiry(enquiry); //illegal > encapsulation violation
        if(!applicantService.validateApplicant((Applicant) sessionManager.getCurrentUser(),enquiry))
            Color.println("You are not authorized to delete this enquiry.", Color.RED);
        else{
            Color.println("Enquiry deleted successfully.", Color.GREEN);
            projectService.removeEnquiryFromProject(enquiry); // correct way
        }
    }

    public void replyToEnquiry(Enquiry enquiry, String reply) {
        if (projectService.isProjectStaff(sessionManager.getCurrentUser(), enquiry.getProject())) {
            enquiry.setReply(reply, sessionManager.getCurrentUser());
            Color.println("Enquiry replied successfully.", Color.GREEN);
        }
        else {
            Color.println("You are not authorized to reply to this enquiry.", Color.RED);
        }
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

    public void viewEnquiresFromAllProject() {
        List<Enquiry> enquiries = projectService.getAllEnquiresFromAllProjects();
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
