package manager;

import helper.Color;
import helper.TablePrinter;
import interfaces.Menu;
import officer.RegistrationStatus;
import system.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerMenu extends Menu {
   private final Scanner scanner;
   private final TablePrinter tablePrinter;
   private final SessionManager sessionManager;
   private final ManagerController managerController;

   public ManagerMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager, ManagerController managerController) {
      super(scanner);
      this.scanner = scanner;
      this.tablePrinter = tablePrinter;
      this.sessionManager = sessionManager;
      this.managerController = managerController;
   }

   protected void display() {
      Color.print("========== HDB Manager Menu ==========\n" +
              "1. Create New BTO Project\n" +
              "2. Edit Existing BTO Project\n" +
              "3. Delete BTO Project\n" +
              "4. Toggle Project Visibility\n" +
              "5. View All Projects\n" +
              "6. Filter My Projects\n" +
              "7. View Current Project\n" +
              "8. View Pending Officer Registrations\n" +
              "9. Approve/Reject Officer Registrations\n" +
              "10. View Applicant Applications\n" +
              "11. Approve/Reject Applicant Applications\n" +
              "12. Approve/Reject Withdrawal Requests\n" +
              "13. View All Project Enquiries\n" +
              "14. Reply to Project Enquiries\n" +
              "15. Generate Applicant/Booking Reports\n" +
              "16. Change Password\n" +
              "0. Logout\n" +
              "======================================\n" +
              "Please enter your choice:", Color.CYAN);
   }

   protected void handleInput() {
      String input = scanner.nextLine();

      switch (input) {
         case "1" -> {
            handleCreateProject();
         }
         case "2" -> {
            handleEditProject();
         }
         case "3" -> {
            handleDeleteProject();
         }
         case "4" -> {
            handleToggleVisibility();
         }
         case "5" -> {
            handleViewAllProjects();
         }
         case "6" -> {
            handleFilterMyProjects();
         }
         case "7" -> {
            handleViewCurrentProject();
         }
         case "8" -> {
            handleViewPendingOfficerRegistrations();
         }
         case "9" -> {
            handleApproveRejectOfficerRegistrations();
         }
         case "10" -> {
            //handleViewApplicantApplications();
         }
         case "11" -> {
            //handleApproveRejectApplicantApplications();
         }
         case "12" -> {
            //handleApproveRejectWithdrawalRequests();
         }
         case "13" -> {
            //handleViewAllProjectEnquiries();
         }
         case "14" -> {
            //handleReplyToProjectEnquiries();
         }
         case "15" -> {
            //handleGenerateApplicantBookingReports();
         }
         case "16" -> {
            //handleChangePassword();
         }
         case "0" -> {
            sessionManager.logout();
         }
         default -> {
            Color.println("Invalid choice", Color.RED);
         }
      }
   }

   @Override
   public void run() {
      while (sessionManager.isLoggedIn()) {
         display();
         handleInput();
      }
   }


   private void handleCreateProject() {
      try {
         Color.print("Enter Project Name:", Color.GREEN);
         String name = scanner.nextLine();

         Color.print("Enter Project Neighbourhood:", Color.GREEN);
         String neighbourhood = scanner.nextLine();

         Color.print("Enter Two Room Flat Count:", Color.GREEN);
         int twoRoomFlatCount = Integer.parseInt(scanner.nextLine());

         Color.print("Enter Two Room Flat Price:", Color.GREEN);
         double twoRoomPrice = Double.parseDouble(scanner.nextLine());

         Color.print("Enter Three Room Flat Count:", Color.GREEN);
         int threeRoomFlatCount = Integer.parseInt(scanner.nextLine());

         Color.print("Enter Three Room Flat Price:", Color.GREEN);
         double threeRoomPrice = Double.parseDouble(scanner.nextLine());

         Color.print("Enter Project Application Opening Date (yyyy-MM-dd):", Color.GREEN);
         LocalDate applicationOpeningDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

         Color.print("Enter Project Application Closing Date (yyyy-MM-dd):", Color.GREEN);
         LocalDate applicationClosingDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);


         Color.print("Enter Number of Officer Slots Available:", Color.GREEN);
         int officerSlots = Integer.parseInt(scanner.nextLine());

         try {
            managerController.createProject(name, neighbourhood, twoRoomFlatCount, twoRoomPrice, threeRoomFlatCount, threeRoomPrice,
                    applicationOpeningDate, applicationClosingDate, sessionManager.getCurrentUser().getName(), officerSlots, new ArrayList<>());
            Color.println("Project created successfully!", Color.GREEN);
         }
         catch (IllegalArgumentException e) {
            Color.println("Project Creation Failed, An error occurred: " + e.getMessage(), Color.RED);
         }
      }
      catch (NumberFormatException e) {
         Color.println("Invalid number format. Please try again.", Color.RED);
      }
      catch (DateTimeParseException e) {
         Color.println("Invalid date format. Please use yyyy-MM-dd format.", Color.RED);
      }
      catch (Exception e) {
         Color.println("Project Creation Failed. An error occurred." + e.getMessage(), Color.RED);
      }
   }


   private void handleDeleteProject() {
      Color.print("Enter Project ID to delete:", Color.GREEN);
      int projectId = Integer.parseInt(scanner.nextLine());

      if (managerController.deleteProject(projectId)) {
         Color.println("Project deleted successfully!", Color.GREEN);
      }
      else {
         Color.println("Project deletion failed.", Color.RED);
      }
   }

   private void handleViewAllProjects() {

      try {
         List<List<String>> tableData = managerController.getAllProjectsTableData();
         if (tableData.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            return;
         }
         Color.println("---  All Projects ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 15;
         tablePrinter.printTable(COLUMN_WIDTH, tableData);


         // TODO : user filter
      }
      catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleFilterMyProjects() {
      managerController.getMyProjectsTableData();
   }

   private void handleViewCurrentProject() {
      try {
         Color.println(managerController.getCurrentProjectData(), Color.YELLOW);
      }
      catch (Exception e) {
         Color.println("Error viewing current project: " + e.getMessage(), Color.RED);
      }
   }

   private void handleToggleVisibility() {
      Color.println("My Projects:", Color.GREEN);
      managerController.getMyProjectsTableData();//prints my projects on screen
      Color.print("Enter Project ID to toggle visibility:", Color.GREEN);
      Integer projectId = Integer.parseInt(scanner.nextLine());
      if (projectId == null) {
         Color.println("Invalid Project ID.", Color.RED);
         return;
      }
      if (managerController.toggleVisibility(projectId)) {
         Color.println("Visibility toggled successfully!", Color.GREEN);
      }
      else {
         Color.println("Visibility toggle failed.", Color.RED);
      }

   }

   private void handleEditProject() {
      Color.println("My Projects:", Color.GREEN);
      managerController.getMyProjectsTableData();//prints my projects on screen
      Color.print("Enter Project ID to edit (0 to exit):", Color.GREEN);
      Integer projectName = Integer.parseInt(scanner.nextLine());
      if (projectName.equals(0)) {
         Color.println("Returning to Manager Menu.", Color.RED);
         return;
      }

      if (projectName == null) {
         Color.println("Invalid Project ID.", Color.RED);
         return;
      }

      while (true) {
         Color.print("Editing Options:\n 1. Name, 2. Neighbourhood, " +
                 "3. Application Opening Date, 4. Application Closing Dates, " +
                 "5. Visibility, 6. Exit\n" +
                 "Enter your choice:", Color.GREEN);
         String editOption = scanner.nextLine();
         switch (editOption) {
            case "1" -> {
               Color.print("Enter New Project Name:", Color.GREEN);
               String newName = scanner.nextLine();
               managerController.editProject(projectName, "1", newName);
            }
            case "2" -> {
               Color.print("Enter New Project Neighbourhood:", Color.GREEN);
               String newNeighbourhood = scanner.nextLine();
               managerController.editProject(projectName, "2", newNeighbourhood);
            }
            case "3" -> {
               Color.print("Enter New Application Opening Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationOpeningDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerController.editProject(projectName, "3", newApplicationOpeningDate);
            }
            case "4" -> {
               Color.print("Enter New Application Closing Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationClosingDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerController.editProject(projectName, "4", newApplicationClosingDate);
            }
            case "5" -> {
               Color.print("Enter New Visibility (true/false):", Color.GREEN);
               boolean newVisibility = Boolean.parseBoolean(scanner.nextLine());
               managerController.editProject(projectName, "5", newVisibility);
            }
            case "6" -> {
               Color.println("Exiting Edit Menu.", Color.BLUE);
               return;
            }
            default -> {
               Color.println("Invalid option. Edit cancelled.", Color.RED);
            }
         }
      }

   }

   private Boolean handleViewPendingOfficerRegistrations() {
      try {
         List<List<String>> tableData = managerController.getPendingRegistrationTableData();
         if (tableData == null || tableData.isEmpty()) {
            Color.println("No pending officer registrations.", Color.RED);
            return false;
         }
         Integer COLUMN_WIDTH = 15;
         Color.println("--- Viewing Pending Officer Registrations ---", Color.YELLOW);
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
         return true;
      }
      catch (Exception e) {
         Color.println("Error viewing pending officer registrations.\nPossible Reason:" + e.getMessage(), Color.RED);
      }
      return false;
   }

   private void handleApproveRejectOfficerRegistrations() {
      try {
         if (handleViewPendingOfficerRegistrations()) {

            Color.print("Enter Registration ID or Officer Name to approve/reject:", Color.GREEN);
            String identifier = scanner.nextLine();

            Color.print("Enter 'A' to approve or 'R' to reject:", Color.GREEN);
            String action = scanner.nextLine().toUpperCase();

            if (action.equals("A") || action.equals("R")) {

               RegistrationStatus isApproved = action.equals("A") ? RegistrationStatus.APPROVED : RegistrationStatus.REJECTED;

               // Let the controller handle the type checking and processing
               boolean success = managerController.processOfficerRegistration(identifier, isApproved);

               if (success) {
                  Color.println("Registration " + isApproved + " successfully!", Color.GREEN);
               }
               else {
                  Color.println("Failed to process registration. Registration may not exist or already processed. Check the registration ID or Officer Name.", Color.RED);
               }
            }
            else {
               Color.println("Invalid action. Please enter 'A' or 'R'.", Color.RED);
            }
         }

      }
      catch (Exception e) {
         Color.println("Error processing registration: " + e.getMessage(), Color.RED);
      }
   }
}//Menu ends here