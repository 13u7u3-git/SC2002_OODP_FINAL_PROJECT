package officer;

import helper.Color;
import helper.TablePrinter;
import interfaces.Menu;
import project.String;
import system.SessionManager;

import java.util.List;
import java.util.Scanner;

public class OfficerMenu extends Menu {
   private final Scanner scanner;
   private final SessionManager sessionManager;
   private final OfficerController officerController;
   private final TablePrinter tablePrinter;

   public OfficerMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager,
                      OfficerController officerController) {
      super(scanner);
      this.scanner = scanner;
      this.sessionManager = sessionManager;
      this.officerController = officerController;
      this.tablePrinter = tablePrinter;
   }


   protected void display() {
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

   protected void handleInput() {
      java.lang.String input = scanner.nextLine();
      switch (input) {
         case "1" -> handleViewAllProjects();
         case "2" -> handleApplyForBTOProject(); // TODO: not yet
         case "3" -> handleRegisterAsOfficer();
         case "4" -> handleViewOfficerRegistrationStatus();
         case "5" -> handleViewHandledProjectDetails();
         case "6" -> handleViewAndReplyToEnquiries(); // TODO: after applicant enquiry logic
         case "7" -> handleManageFlatSelection(); // TODO: after applicant flat selection logic
         case "8" -> handleChangePassword();
         case "0" -> {
            sessionManager.logout();
         }
         default -> Color.println("Invalid choice", Color.RED);
      }
   }

   @Override
   public void run() {
      while (sessionManager.isLoggedIn()) {
         display();
         handleInput();
      }
   }

   private void handleViewAllProjects() {
      try {
         List<List<java.lang.String>> tableData = officerController.getAllProjectsTableData();
         if (tableData.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            return;
         }
         Color.println("---  All Projects ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 15;
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
      }
      catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleApplyForBTOProject() {
      Color.println("Applying for BTO Project", Color.GREEN);
      // Implement logic to apply for a BTO project
   }

   private void handleRegisterAsOfficer() {
      if (officerController.getOfficerStatus() != OfficerStatus.INACTIVE) {
         Color.println("You have either already registered as an officer or your registration is pending.", Color.RED);
         return;
      }

      try {
         List<List<java.lang.String>> table = officerController.getOfficerEligibleProjectsTableData();
         if (table == null) {
            Color.println("No projects found.", Color.RED);
            return;
         }

         Integer COLUMN_WIDTH = 15;
         TablePrinter tablePrinter = new TablePrinter();
         for (List<java.lang.String> row : table) {
            tablePrinter.printRow(COLUMN_WIDTH, row);
         }

         Color.print("Enter the project name or ID you want to register for (0 to exit): ", Color.GREEN);
         java.lang.String projectName = scanner.nextLine();
         if (projectName.equals("0")) {
            Color.println("Returning to Officer Menu.", Color.RED);
            return;
         }

         RegistrationForm form = officerController.CreateRegistrationForm(projectName);
         if (form == null) {
            return;
         }

         Color.print("Do you want to send the registration request? (y/n): ", Color.GREEN);
         java.lang.String confirm = scanner.nextLine();
         if (confirm.equals("y")) {
            officerController.sendRegistrationRequest(form);
            Color.println("Registration Form sent successfully.", Color.CYAN);
         }
         else {
            Color.println("Registration Form is discarded. Returning to Officer Menu.", Color.RED);
         }

      }
      catch (IllegalArgumentException | IllegalStateException e) {
         Color.println("Registration Error: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) {
         Color.println("Unexpected error occurred: " + e.getMessage(), Color.RED);
      }
   }


   private void handleViewOfficerRegistrationStatus() {
      OfficerStatus status = officerController.getOfficerStatus();

      Color.println("===============================================", Color.YELLOW);

      switch (status) {
         case ACTIVE -> {
            Color.println("You are currently an active officer", Color.GREEN);
            try {
               String project = officerController.getCurrentProject();
               Color.println(project.toString(), Color.YELLOW);
            }
            catch (Exception e) {
               Color.println("Error in the System: You have no current registration form. " + e.getMessage(), Color.RED);
            }
         }

         case INACTIVE -> {
            Color.println("You are currently inactive", Color.RED);
            Color.println("===============================================", Color.YELLOW);
            try {
               RegistrationForm form = officerController.getCurrentRegistrationForm();
               Color.println(form.toString(), Color.YELLOW);
            }
            catch (Exception e) {
               Color.println("Error in the System: You have no current registration form", Color.RED);
            }
         }

         case PENDING -> {
            Color.println("You are currently pending for registration", Color.YELLOW);
            try {
               RegistrationForm form = officerController.getCurrentRegistrationForm();
               Color.println(form.toString(), Color.YELLOW);
            }
            catch (Exception e) {
               Color.println("Error in the System: You have no current registration form", Color.RED);
            }
         }
      }
   }

   private void handleViewHandledProjectDetails() {
      Color.println("Viewing Details of Handled Project", Color.GREEN);

      try {
         String project = officerController.getCurrentProject();
         Color.println(project.toString(), Color.YELLOW);
      }
      catch (Exception e) {
         Color.println("Error in the System: You have no current registration form. :" + e.getMessage(), Color.RED);
      }

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
      List<java.lang.String> inputs = super.getInputsChangePassword();
      try {
         officerController.changePassword(inputs.get(0), inputs.get(1), inputs.get(2));
         Color.println("Password changed successfully.", Color.GREEN);
      }
      catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
   }
}
