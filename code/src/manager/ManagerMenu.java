//updated managerMenu, with all the methods implemented 
package manager;

import applicant.ApplicantController;
import enquiry.Enquiry;
import helper.Color;
import helper.TablePrinter;
import interfaces.AbstractMenu;
import officer.RegistrationStatus;
import system.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ManagerMenu extends AbstractMenu {
   private final TablePrinter tablePrinter;
   private final SessionManager sessionManager;
   private final ManagerService managerService;

   public ManagerMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager, ManagerService managerService) {
      super(scanner);
      this.tablePrinter = tablePrinter;
      this.sessionManager = sessionManager;
      this.managerService = managerService;
   }

   @Override
   public void display() {
      Color.print("""           
         ========================================================================== Manager Menu ==================================================================================
 1. Create New BTO Project                 |     2. Edit Existing BTO Project     |     3. Delete BTO Project                       |    4. Toggle Project Visibility
 5. View All Projects                      |     6. Filter My Projects            |     7. View Current Project                     |    8. View Pending Officer Registrations
 9. Approve/Reject Officer Registrations   |     10. View Applicant Applications  |     11. Approve/Reject Applicant Applications   |    12. Approve/Reject Withdrawal Requests
 13. View All Project Enquiries            |     14. Reply to Project Enquiries   |     15. Generate Applicant/Booking Reports      |    16. Change Password
 0. Logout
=========================================================================================================================================================================================
Please enter your choice:""", Color.CYAN);
   }
   @Override
   public  void handleInput() {
      while (true) { 
         try {
            String input = scanner.nextLine().trim();

            //input ranges from 0-16
            if (!input.matches("^(1[0-6]|[0-9])$")){
               Color.println("Error: Please enter a digit (0-9)", Color.RED);
               continue;  // Skip to next iteration (ask again)
            }

            //process the valid inputs      
            switch (input) {
               case "1" -> handleCreateProject();
               case "2" -> handleEditProject();         
               case "3" -> handleDeleteProject();         
               case "4" -> handleToggleVisibility();         
               case "5" -> handleViewAllProjects();
               case "6" -> handleFilterMyProjects();
               case "7" -> handleViewCurrentProject();         
               case "8" -> handleViewPendingOfficerRegistrations();         
               case "9" -> handleApproveRejectOfficerRegistrations();         
               case "10" -> handleViewApplicantApplications();         
               case "11" -> handleApproveRejectApplicantApplications();         
               case "12" -> handleApproveRejectWithdrawalRequests();         
               case "13" -> handleViewAllProjectEnquiries();         
               case "14" -> handleReplyToProjectEnquiries();         
               case "15" -> handleGenerateApplicantBookingReports();         
               case "16" -> handleChangePassword();         
               case "0" -> {
                  sessionManager.logout();
                  return; //Exit the loop after logout          
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
            managerService.createProject(name, neighbourhood, twoRoomFlatCount, twoRoomPrice, threeRoomFlatCount, threeRoomPrice,
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

      if (managerService.deleteProject(projectId)) {
         Color.println("Project deleted successfully!", Color.GREEN);
      }
      else {
         Color.println("Project deletion failed.", Color.RED);
      }
   }

   private void handleViewAllProjects() {

      try {
         List<List<String>> tableData = managerService.getAllProjectsTableData();
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

   private void handleFilterMyProjects() {
      managerService.getMyProjectsTableData();
   }

   private void handleViewCurrentProject() {
      try {
         Color.println(managerService.getCurrentProjectData(), Color.YELLOW);
      }
      catch (Exception e) {
         Color.println("Error viewing current project: " + e.getMessage(), Color.RED);
      }
   }

   private void handleToggleVisibility() {
      Color.println("My Projects:", Color.GREEN);
      managerService.getMyProjectsTableData();//prints my projects on screen
      Color.print("Enter Project ID to toggle visibility:", Color.GREEN);
      Integer projectId = Integer.parseInt(scanner.nextLine());
      if (projectId == null) {
         Color.println("Invalid Project ID.", Color.RED);
         return;
      }
      if (managerService.toggleVisibility(projectId)) {
         Color.println("Visibility toggled successfully!", Color.GREEN);
      }
      else {
         Color.println("Visibility toggle failed.", Color.RED);
      }

   }

   private void handleEditProject() {
      Color.println("My Projects:", Color.GREEN);
      managerService.getMyProjectsTableData();//prints my projects on screen
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
               managerService.editProject(projectName, "1", newName);
            }
            case "2" -> {
               Color.print("Enter New Project Neighbourhood:", Color.GREEN);
               String newNeighbourhood = scanner.nextLine();
               managerService.editProject(projectName, "2", newNeighbourhood);
            }
            case "3" -> {
               Color.print("Enter New Application Opening Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationOpeningDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerService.editProject(projectName, "3", newApplicationOpeningDate);
            }
            case "4" -> {
               Color.print("Enter New Application Closing Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationClosingDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerService.editProject(projectName, "4", newApplicationClosingDate);
            }
            case "5" -> {
               Color.print("Enter New Visibility (true/false):", Color.GREEN);
               boolean newVisibility = Boolean.parseBoolean(scanner.nextLine());
               managerService.editProject(projectName, "5", newVisibility);
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
         List<List<String>> tableData = managerService.getPendingRegistrationTableData();
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
               boolean success = managerService.processOfficerRegistration(identifier, isApproved);

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

   private void handleViewApplicantApplications() {
      List<List<String>> tableData = managerService.getApplicantApplications();
      if (tableData == null || tableData.isEmpty()) {
         Color.println("No applicant applications available.", Color.RED);
         return;
      }
      Color.println("--- Applicant Applications ---", Color.YELLOW);
      tablePrinter.printTable(15, tableData);
   }

   private void handleApproveRejectApplicantApplications() {
      handleViewApplicantApplications();
      Color.print("Enter Application ID to approve/reject: ", Color.GREEN);
      String appId = scanner.nextLine();
      Color.print("Enter 'A' to approve or 'R' to reject: ", Color.GREEN);
      String action = scanner.nextLine().toUpperCase();

      boolean result = managerService.processApplicantApplication(appId, action);
      if (result) {
         Color.println("Application " + (action.equals("A") ? "approved" : "rejected") + " successfully!", Color.GREEN);
      } else {
         Color.println("Failed to process application.", Color.RED);
      }
   }

   private void handleApproveRejectWithdrawalRequests() {
      List<List<String>> withdrawalData = managerService.getWithdrawalRequests();
      if (withdrawalData == null || withdrawalData.isEmpty()) {
         Color.println("No withdrawal requests.", Color.RED);
         return;
      }
      tablePrinter.printTable(15, withdrawalData);
      Color.print("Enter Request ID to approve/reject: ", Color.GREEN);
      String id = scanner.nextLine();
      Color.print("Enter 'A' to approve or 'R' to reject: ", Color.GREEN);
      String action = scanner.nextLine().toUpperCase();
      boolean result = managerService.processWithdrawalRequest(id, action);
      if (result) {
         Color.println("Withdrawal request " + (action.equals("A") ? "approved" : "rejected") + ".", Color.GREEN);
      } else {
         Color.println("Failed to process withdrawal.", Color.RED);
      }
   }

   private void handleViewAllProjectEnquiries() {
      List<Enquiry> enquiries = managerService.getAllEnquiries();
      if (enquiries == null || enquiries.isEmpty()) {
         Color.println("No enquiries found.", Color.RED);
         return;
      }
      tablePrinter.printTable(15, managerService.formatEnquiries(enquiries));
   }

   private void handleReplyToProjectEnquiries() {
      handleViewAllProjectEnquiries();
      Color.print("Enter Enquiry ID to reply to: ", Color.GREEN);
      String enquiryId = scanner.nextLine();
      Color.print("Enter your reply: ", Color.GREEN);
      String reply = scanner.nextLine();
      boolean success = managerService.replyToEnquiry(enquiryId, reply);
      if (success) {
         Color.println("Reply sent successfully.", Color.GREEN);
      } else {
         Color.println("Failed to send reply.", Color.RED);
      }
   }

   private void handleGenerateApplicantBookingReports() {
      String report = managerService.generateBookingReport();
      if (report == null || report.isEmpty()) {
         Color.println("No data to generate report.", Color.RED);
      } else {
         Color.println("--- Applicant/Booking Report ---\n" + report, Color.YELLOW);
      }
   }

   private void handleChangePassword() {
      List<String> inputs = super.getInputsChangePassword();
      try {
         managerService.changePassword(inputs.get(0), inputs.get(1), inputs.get(2));
         Color.println("Password changed successfully.", Color.GREEN);
      } catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
   }

}//Menu ends here
