package officer;

import helper.Color;
import interfaces.Menu;
import system.SessionManager;

import java.util.Scanner;

public class OfficerMenu implements Menu {
   private final Scanner scanner;
   private final SessionManager sessionManager;
   private final OfficerController officerController;

   public OfficerMenu(Scanner scanner, SessionManager sessionManager, OfficerController officerController) {
      this.scanner = scanner;
      this.sessionManager = sessionManager;
      this.officerController = officerController;
   }

   @Override
   public void display() {
      Color.print("========== HDB Officer Menu ==========\n" +
              "1. View All Projects\n" +
              "2. Apply for BTO Project\n" +
              "3. Register as Officer for Project\n" +
              "4. View Officer Registration Status\n" +
              "5. View Details of Handled Project\n" +
              "6. View and Reply to Project Enquiries\n" +
              "7. Manage Flat Selection/Booking for Applicants\n" +
              "8. Change Password\n" +
              "0. Logout\n" +
              "======================================\n" +
              "Please enter your choice:", Color.CYAN);
   }

   @Override
   public void handleInput() {
      String input = scanner.nextLine();
      switch (input) {
         case "1" -> handleViewAllProjects();// TODO: Half Baked
         case "2" -> handleApplyForBTOProject(); // TODO: not yet
         case "3" -> handleRegisterAsOfficer(); // TODO: done
         case "4" -> handleViewOfficerRegistrationStatus(); // TODO: done
         case "5" -> handleViewHandledProjectDetails(); // TODO: after officer registration acceptance logic
         case "6" -> handleViewAndReplyToEnquiries(); // TODO: after applicant enquiry logic
         case "7" -> handleManageFlatSelection(); // TODO: after applicant flat selection logic
         case "8" -> handleChangePassword(); // TODO: not yet
         case "0" -> sessionManager.logout();
         default -> Color.println("Invalid choice", Color.RED);
      }
   }

   private void handleViewAllProjects() {
      Color.println("Viewing All Projects", Color.GREEN);
      officerController.viewAllProjects();
   }

   private void handleApplyForBTOProject() {
      Color.println("Applying for BTO Project", Color.GREEN);
      // Implement logic to apply for a BTO project
   }

   private void handleRegisterAsOfficer() {
      // Implement logic to register as an officer
      officerController.viewOfficerEligibleProjects();
      Color.print("Enter the project name or ID you want to register for (0 to exit): ", Color.GREEN);
      String projectName = scanner.nextLine();
      if (projectName.equals("0")) {
         Color.println("Returning to Officer Menu.", Color.RED);
         return;
      }
      //create registration form
      try {
         RegistrationForm form = officerController.CreateRegistrationForm(projectName);
         if (form == null) {
            return;
         }
         //confirm officer whether to send registration request
         Color.print("Do you want to send the registration request? (y/n): ", Color.GREEN);
         String confirm = scanner.nextLine();
         if (confirm.equals("y")) {
            //validation is done in when it gets added to the project
            officerController.sendRegistrationRequest(form);
            Color.println("Registration Form sent successfully.", Color.GREEN);
         }
         else {
            Color.println("Registration Form is discarded. Returning to Officer Menu.", Color.RED);
         }
      }
      catch (IllegalArgumentException e) {
         Color.println("Registration Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleViewOfficerRegistrationStatus() {
      Color.println("Viewing Officer Registration Status", Color.GREEN);
      // Implement logic to view officer registration status
      officerController.viewOfficerRegistrationStatus();
   }

   private void handleViewHandledProjectDetails() {
      Color.println("Viewing Details of Handled Project", Color.GREEN);
      // Implement logic to view details of a handled project
   }

   private void handleViewAndReplyToEnquiries() {
      Color.println("Viewing and Replying to Project Enquiries", Color.GREEN);
      // Implement logic to view and reply to project enquiries
   }

   private void handleManageFlatSelection() {
      Color.println("Managing Flat Selection/Booking for Applicants", Color.GREEN);
      // Implement logic to manage flat selection/booking for applicants
   }

   private void handleChangePassword() {
      Color.println("Changing Password", Color.GREEN);
      //get old password and new passwords confirm password
      Color.print("Enter your old password: ", Color.GREEN);
      String oldPassword = scanner.nextLine();
      Color.print("Enter your new password: ", Color.GREEN);
      String newPassword = scanner.nextLine();
      Color.print("Confirm your new password: ", Color.GREEN);
      String confirmPassword = scanner.nextLine();
      try {
         officerController.changePassword(oldPassword, newPassword, confirmPassword);
         Color.println("Password changed successfully.", Color.GREEN);
      }
      catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
   }
}
