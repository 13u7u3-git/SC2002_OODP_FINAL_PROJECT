package enquiry;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import UniqueID.UniqueIdService;
import helper.Color;
import project.ProjectRegistry;
import project.ProjectService;
import project.String;
import system.ServiceRegistry;
import system.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnquiryService {
   private final ProjectService projectService;
   private final IUniqueIdService uniqueIdService;
   private final SessionManager sessionManager;

   public EnquiryService(final ProjectService projectService, UniqueIdService uniqueIdService) {
      this.projectService = projectService;
      this.uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
      this.sessionManager = ServiceRegistry.get(SessionManager.class);
   }

   public Enquiry createEnquiry(Integer projectId, java.lang.String applicantName, Integer applicantNric, java.lang.String message)
           throws IllegalArgumentException {
      try {
         String project = projectService.getProjectById(projectId).isVisibility() ? projectService.getProjectById(projectId) : null;
         if (project == null) {
            throw new IllegalArgumentException("You cannot create an enquiry for that Project ID. Please try again.");
         }
         Enquiry enquiry = new Enquiry(uniqueIdService.generateUniqueId(IdType.ENQUIRY_ID), project.getProjectName(), projectId, applicantName, applicantNric, LocalDate.now());
         if (enquiry == null) {
            throw new IllegalArgumentException("Something went wrong while creating enquiry. Try again.");
         }
         enquiry.setEnquiry(message);
         return enquiry;//success
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Something went wrong while creating enquiry. Try again.");
      }


   }

   public void submitEnquiry(Enquiry enquiry) throws IllegalArgumentException {
      try {
         projectService.addEnquiryToProject(enquiry);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Something went wrong while submitting enquiry. Try again.");
      }
   }

   // manager wont even have the option to edit enquiry in menu
   // have to make sure that they edit their own enquiry.
   public void editEnquiry(Enquiry enquiry, java.lang.String newMessage) throws IllegalArgumentException {
      if (!enquiry.getApplicantNric().equals(sessionManager.getCurrentUser().getNric())) {
         throw new IllegalArgumentException("You are not allowed to edit this enquiry.");
      }

      if (newMessage == null || newMessage.isBlank()) {
         throw new IllegalArgumentException("Your message is Empty.");
      }
      try {
         enquiry.setEnquiry(newMessage);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Something went wrong while editing enquiry. Try again.");
      }

   }

   // manager wont even have the option to delete. Need to confirm that current user deletes their own enquiry.
   // user cannot put random enquiry id and then it deletes that enquiry.
   public void deleteEnquiry(Enquiry enquiry) throws IllegalArgumentException {

      // Safety check: make sure the applicant owns the enquiry
      if (!enquiry.getApplicantNric().equals(sessionManager.getCurrentUser().getNric())) {
         throw new IllegalArgumentException("You are not allowed to delete this enquiry.");
      }

      // Check if the enquiry has a reply (can't delete)
      if (enquiry.getReply() != null && !enquiry.getReply().isBlank()) {
         throw new IllegalArgumentException("Cannot delete enquiry with a reply.");
      }
      projectService.removeEnquiryFromProject(enquiry);
   }

   public void replyToEnquiry(Enquiry enquiry, java.lang.String reply) {
      if (enquiry.getReply() == null || enquiry.getReply().isEmpty()) {
         enquiry.setReply(reply, sessionManager.getCurrentUser());
         Color.println("Enquiry replied successfully.", Color.GREEN);
      }
      else {
         Color.println("Enquiry already has reply.", Color.RED);
      }
   }

   public List<List<java.lang.String>> getEnquiriesFor(java.lang.String projectId) {
      List<List<java.lang.String>> enquiries = projectService.getEnquiriesFrom(projectId);

      // Define headers without "Project Name" since we're viewing a single project
      List<java.lang.String> headers = List.of(
              "Enquiry ID",
              "Applicant Name",
              "Date Enquired",
              "Enquiry",
              "Reply",
              "Respondent",
              "Date Replied"
      );

      // Initialize empty list if null
      if (enquiries == null) {
         enquiries = new ArrayList<>();
      }

      // Remove "Project Name" column (index 1) from each row if there are enquiries
      if (!enquiries.isEmpty()) {
         for (List<java.lang.String> row : enquiries) {
            if (row.size() > 1) {
               row.remove(1); // Remove the "Project Name" column
            }
         }
      }

      // Add headers as the first row
      if (enquiries.isEmpty()) {
         enquiries.add(headers);
      }
      else {
         enquiries.add(0, headers);
      }

      return enquiries;
   }

   public List<List<java.lang.String>> getEnquiresFromAllProjects(ProjectRegistry projectRegistry) {
      List<List<java.lang.String>> enquiries = projectService.getAllEnquiriesFromAllProjects();

      // Add headers based on Enquiry.toStringList() fields
      List<java.lang.String> headers = List.of(
              "Enquiry ID",
              "Project Name",
              "Applicant Name",
              "Date Enquired",
              "Enquiry",
              "Reply",
              "Respondent",
              "Date Replied"
      );

      // Insert headers as the first row
      if (enquiries == null) {
         enquiries = new ArrayList<>();
      }

      if (enquiries.isEmpty()) {
         enquiries.add(headers);
      }
      else {
         enquiries.add(0, headers);
      }

      return enquiries;
   }
}