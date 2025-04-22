package applicant;

import helper.Color;
import helper.TablePrinter;
import interfaces.Menu;
import system.SessionManager;

import java.util.List;
import java.util.Scanner;

public class ApplicantMenu extends Menu {
   private final Scanner scanner;
   private final TablePrinter tablePrinter;
   private final SessionManager sessionManager;
   private final ApplicantController applicantController;

   //   private final ApplicantController applicantController;
   public ApplicantMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager, ApplicantController applicantController) {
      super(scanner);
      this.scanner = scanner;
      this.tablePrinter = tablePrinter;
      this.sessionManager = sessionManager;
      this.applicantController = applicantController;
   }

   @Override
   protected void display() {
      Color.print("========== Applicant Menu ==========\n" +
              "1. View Available Projects\n" +
              "2. Apply for BTO Project\n" +
              "3. View My Application Status\n" +
              "4. Request Withdrawal\n" +
              "5. Submit Project Enquiry\n" +
              "6. View My Enquiries\n" +
              "7. Edit My Enquiry\n" +
              "8. Delete My Enquiry\n" +
              "9. Change Password\n" +
              "0. Logout\n" +
              "===================================\n" +
              "Please enter your choice:", Color.CYAN);
   }

   @Override
   protected void handleInput() {
      String input = scanner.nextLine();
      switch (input) {
         case "1" -> handleViewAvailableProjects(); // TODO: Show projects relevant to applicant's status
         case "2" -> handleApplyForBTOProject(); // TODO: Implement application logic
         case "3" -> handleViewApplicationStatus(); // TODO: Show current application status
         case "4" -> handleRequestWithdrawal(); // TODO: Implement withdrawal request
         case "5" -> handleSubmitEnquiry(); // TODO: Implement enquiry submission
         case "6" -> handleViewMyEnquiries(); // TODO: Show applicant's enquiries
         case "7" -> handleEditEnquiry(); // TODO: Implement edit functionality
         case "8" -> handleDeleteEnquiry(); // TODO: Implement delete functionality
         case "9" -> handleChangePassword(); // TODO: Implement password change
         case "0" -> sessionManager.logout();
         default -> Color.println("Invalid choice", Color.RED);
      }
   }


   private void handleDeleteEnquiry() {
   }

   private void handleEditEnquiry() {
   }

   private void handleViewMyEnquiries() {
   }

   private void handleSubmitEnquiry() {
   }

   private void handleRequestWithdrawal() {
   }

   private void handleViewApplicationStatus() {
   }

   private void handleApplyForBTOProject() {
   }

   private void handleViewAvailableProjects() {
      try {
         List<List<String>> tableData = applicantController.getEligibleProjectsTableData();
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

   @Override
   public void run() {
      while (sessionManager.isLoggedIn()) {
         display();
         handleInput();
      }
   }

   private void handleChangePassword() {
      List<String> inputs = super.getInputsChangePassword();
      try {
         applicantController.changePassword(inputs.get(0), inputs.get(1), inputs.get(2));
         Color.println("Password changed successfully.", Color.GREEN);
      }
      catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
   }
}
