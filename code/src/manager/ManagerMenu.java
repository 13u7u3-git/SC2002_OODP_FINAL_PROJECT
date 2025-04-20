package manager;

import helper.Color;
import interfaces.Menu;
import system.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ManagerMenu implements Menu {
   private final Scanner scanner;
   private final SessionManager sessionManager;
   private final ManagerController managerController;

   public ManagerMenu(Scanner scanner, SessionManager sessionManager, ManagerController managerController) {
      this.scanner = scanner;
      this.sessionManager = sessionManager;
      this.managerController = managerController;
   }

   @Override
   public void display() {
      Color.print("========== HDB Manager Menu ==========\n" +
              "1. Create New BTO Project\n" +
              "2. Edit Existing BTO Project\n" +
              "3. Delete BTO Project\n" +
              "4. Toggle Project Visibility\n" +
              "5. View All Projects\n" +
              "6. Filter My Projects\n" +
              "7. View Pending Officer Registrations\n" +
              "8. Approve/Reject Officer Registrations\n" +
              "9. View Applicant Applications\n" +
              "10. Approve/Reject Applicant Applications\n" +
              "11. Approve/Reject Withdrawal Requests\n" +
              "12. View All Project Enquiries\n" +
              "13. Reply to Project Enquiries\n" +
              "14. Generate Applicant/Booking Reports\n" +
              "15. Change Password\n" +
              "0. Logout\n" +
              "======================================\n" +
              "Please enter your choice:", Color.CYAN);
   }

   @Override
   public void handleInput() {
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
            //handleViewPendingOfficerRegistrations();
         }
         case "8" -> {
            //handleApproveRejectOfficerRegistrations();
         }
         case "9" -> {
            //handleViewApplicantApplications();
         }
         case "10" -> {
            //handleApproveRejectApplicantApplications();
         }
         case "11" -> {
            //handleApproveRejectWithdrawalRequests();
         }
         case "12" -> {
            //handleViewAllProjectEnquiries();
         }
         case "13" -> {
            //handleReplyToProjectEnquiries();
         }
         case "14" -> {
            //handleGenerateApplicantBookingReports();
         }
         case "15" -> {
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
      managerController.viewAllProjects();
   }

   private void handleFilterMyProjects() {
      managerController.viewMyProjects();
   }

   private void handleToggleVisibility() {
      Color.println("My Projects:", Color.GREEN);
      managerController.viewMyProjects();//prints my projects on screen
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
      managerController.viewMyProjects();//prints my projects on screen
      Color.print("Enter Project ID to edit:", Color.GREEN);
      Integer projectId = Integer.parseInt(scanner.nextLine());

      if (projectId == null) {
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
               managerController.editProject(projectId, "1", newName);
            }
            case "2" -> {
               Color.print("Enter New Project Neighbourhood:", Color.GREEN);
               String newNeighbourhood = scanner.nextLine();
               managerController.editProject(projectId, "2", newNeighbourhood);
            }
            case "3" -> {
               Color.print("Enter New Application Opening Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationOpeningDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerController.editProject(projectId, "3", newApplicationOpeningDate);
            }
            case "4" -> {
               Color.print("Enter New Application Closing Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationClosingDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerController.editProject(projectId, "4", newApplicationClosingDate);
            }
            case "5" -> {
               Color.print("Enter New Visibility (true/false):", Color.GREEN);
               boolean newVisibility = Boolean.parseBoolean(scanner.nextLine());
               managerController.editProject(projectId, "5", newVisibility);
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

}//Menu ends here

