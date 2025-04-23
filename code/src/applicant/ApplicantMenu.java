package applicant;

import enquiry.Enquiry;
import helper.Color;
import helper.TablePrinter;
import interfaces.AbstractMenu;
import system.SessionManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ApplicantMenu extends AbstractMenu {
   private final TablePrinter tablePrinter;
   private final SessionManager sessionManager;
   private final ApplicantController applicantController;

   public ApplicantMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager, ApplicantController applicantController) {
      super(scanner);
      this.tablePrinter = tablePrinter;
      this.sessionManager = sessionManager;
      this.applicantController = applicantController;
   }

   @Override
   public void display() {
      Color.print("""
        ================================ Applicant Menu =================================
 1. View Available Projects     |     2. Apply for BTO Project     |     3. View My Application Status
 4. Request Withdrawal          |     5. Submit Project Enquiry    |     6. View My Enquiries
 7. Edit My Enquiry             |     8. Delete My Enquiry         |     9. Change Password
 0. Logout
=======================================================================================
Please enter your choice:""", Color.CYAN);
   }


   @Override
   public void handleInput() {
      while (true) {  // Keep asking until valid input
         try {
            String input = scanner.nextLine().trim();

            // Check if input is a single digit (0-9)
            if (!input.matches("[0-9]")) {
               Color.println("Error: Please enter a digit (0-9)", Color.RED);
               continue;  // Skip to next iteration (ask again)
            }

            // Process valid input
            switch (input) {
               case "1" -> handleViewAvailableProjects();
               case "2" -> handleApplyForBTOProject();
               case "3" -> handleViewApplicationStatus();
               case "4" -> handleRequestWithdrawal();
               case "5" -> handleSubmitEnquiry();
               case "6" -> handleViewMyEnquiries();
               case "7" -> handleEditEnquiry();
               case "8" -> handleDeleteEnquiry();
               case "9" -> handleChangePassword();
               case "0" -> {
                  sessionManager.logout();
                  return;  // Exit the loop after logout
               }
            }
            return;

         } catch (NoSuchElementException e) {
            Color.println("Error: Input stream closed", Color.RED);
            return;
         } catch (IllegalStateException e) {
            Color.println("Error: Scanner is closed", Color.RED);
            return;
         }
      }
   }

   private void handleViewAvailableProjects() {
      try {
         List<List<String>> tableData = applicantController.getEligibleProjectsTableData();
         if (tableData.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            return;
         }
         Color.println("--- All Projects ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 15;
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
      } catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleApplyForBTOProject() {
      try {
         Color.print("Enter Project ID to apply for: ", Color.CYAN);
         String projectId = scanner.nextLine();
         Color.print("Enter Flat Type (e.g., TWO_ROOM, THREE_ROOM): ", Color.CYAN);
         String flatTypeStr = scanner.nextLine();
         FlatType flatType = FlatType.valueOf(flatTypeStr.toUpperCase());
         applicantController.applyForProject(projectId, flatType);
         Color.println("Application submitted successfully.", Color.GREEN);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleViewApplicationStatus() {
      try {
         List<List<String>> applicationData = applicantController.getApplicationStatusTableData();
         if (applicationData.isEmpty()) {
            Color.println("No applications found.", Color.RED);
            return;
         }
         Color.println("--- My Applications ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 15;
         tablePrinter.printTable(COLUMN_WIDTH, applicationData);
      } catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleRequestWithdrawal() {
      try {
         Color.print("Enter Application ID to withdraw: ", Color.CYAN);
         String applicationId = scanner.nextLine();
         applicantController.requestWithdrawal(applicationId);
         Color.println("Withdrawal request submitted.", Color.GREEN);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleSubmitEnquiry() {
      try {
         Color.print("Enter Project ID for enquiry: ", Color.CYAN);
         String projectId = scanner.nextLine();
         Color.print("Enter your enquiry: ", Color.CYAN);
         String enquiryText = scanner.nextLine();
         applicantController.submitEnquiry(projectId, enquiryText);
         Color.println("Enquiry submitted successfully.", Color.GREEN);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleViewMyEnquiries() {
      try {
         List<List<String>> enquiryData = applicantController.getMyEnquiriesTableData();
         if (enquiryData.isEmpty()) {
            Color.println("No enquiries found.", Color.RED);
            return;
         }
         Color.println("--- My Enquiries ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 15;
         tablePrinter.printTable(COLUMN_WIDTH, enquiryData);
      } catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleEditEnquiry() {
      try {
         Color.print("Enter Enquiry ID to edit: ", Color.CYAN);
         String enquiryId = scanner.nextLine();
         Color.print("Enter new enquiry text: ", Color.CYAN);
         String newEnquiryText = scanner.nextLine();
         applicantController.editEnquiry(enquiryId, newEnquiryText);
         Color.println("Enquiry updated successfully.", Color.GREEN);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   public void handleDeleteEnquiry() {
      List<Enquiry> myEnquiries = applicantController.getMyEnquiries();

      if (myEnquiries.isEmpty()) {
         Color.println("You have no enquiries to delete.", Color.RED);
         return;
      }

      tablePrinter.printTable(20, EnquiryFormatter.formatEnquiries(myEnquiries));

      Color.print("Enter the ID of the enquiry to delete: ", Color.GREEN);
      String input = scanner.nextLine();

      try {
         int enquiryId = Integer.parseInt(input);
         boolean success = applicantController.deleteEnquiryIfAllowed(enquiryId);
         if (success) {
            Color.println("Enquiry deleted successfully.", Color.GREEN);
         } else {
            Color.println("Enquiry could not be deleted (might be replied to or not yours).", Color.RED);
         }
      } catch (NumberFormatException e) {
         Color.println("Invalid input. Please enter a valid number.", Color.RED);
      }
   }


   private void handleChangePassword() {
      List<String> inputs = super.getInputsChangePassword();
      try {
         applicantController.changePassword(inputs.get(0), inputs.get(1), inputs.get(2));
         Color.println("Password changed successfully.", Color.GREEN);
      } catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
   }

   @Override
   public void run() {
      while (sessionManager.isLoggedIn()) {
         display();
         handleInput();
      }
   }
}