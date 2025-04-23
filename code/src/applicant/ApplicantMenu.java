package applicant;

import enquiry.Enquiry;
import helper.Color;
import helper.TablePrinter;
import interfaces.AbstractMenu;
import project.FlatType;
import project.Project;
import system.SessionManager;
import user.User;
import user.UserFilterSettings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

         } catch (NoSuchElementException e) {
            Color.println("Error: Input stream closed", Color.RED);
            return;
         } catch (IllegalStateException e) {
            Color.println("Error: Scanner is closed", Color.RED);
            return;
         }
      }
   }

   public void handleViewAvailableProjects() {
      try {
         List<Project> allProjects = applicantController.getEligibleProjects();
         if (allProjects.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            return;
         }

         User currentUser = sessionManager.getCurrentUser();
         UserFilterSettings filters = currentUser.getFilterSettings();

         // Step 1: Inform if filters are currently set
         boolean hasFilters = filters.getProjectName() != null || filters.getNeighbourhood() != null ||
                 filters.getFlatType() != null || filters.getDate() != null;

         if (hasFilters) {
            Color.println("Active filters are applied.", Color.YELLOW);
         }

         // Step 2: Ask if user wants to update/reset
         Color.println("\nWould you like to update/reset the filters? (yes/no/reset)", Color.CYAN);
         String filterChoice = scanner.nextLine().trim().toLowerCase();

         if (filterChoice.equals("reset")) {
            filters.reset();
         } else if (filterChoice.equals("yes")) {
            while (true) {
               Color.println("\nChoose a filter to set/update:", Color.CYAN);
               Color.println("1. Project Name", Color.YELLOW);
               Color.println("2. Neighbourhood", Color.YELLOW);
               Color.println("3. Flat Type", Color.YELLOW);
               Color.println("4. Date", Color.YELLOW);
               Color.println("5. Done", Color.YELLOW);
               Color.print("Enter your choice (1-5): ", Color.CYAN);
               int choice = Integer.parseInt(scanner.nextLine().trim());

               switch (choice) {
                  case 1 -> {
                     Color.print("Enter project name: ", Color.CYAN);
                     filters.setProjectName(scanner.nextLine().trim().toLowerCase());
                  }
                  case 2 -> {
                     Color.print("Enter neighbourhood: ", Color.CYAN);
                     filters.setNeighbourhood(scanner.nextLine().trim().toLowerCase());
                  }
                  case 3 -> {
                     try {
                        Color.print("Enter flat type (TWO_ROOM / THREE_ROOM): ", Color.CYAN);
                        filters.setFlatType(FlatType.valueOf(scanner.nextLine().trim().toUpperCase()));
                     } catch (IllegalArgumentException e) {
                        Color.println("Invalid flat type.", Color.RED);
                     }
                  }
                  case 4 -> {
                     try {
                        Color.print("Enter date (yyyy-mm-dd): ", Color.CYAN);
                        filters.setDate(LocalDate.parse(scanner.nextLine().trim()));
                     } catch (Exception e) {
                        Color.println("Invalid date format.", Color.RED);
                     }
                  }
                  case 5 -> {
                  }
                  default -> {
                     Color.println("Invalid choice.", Color.RED);
                  }
               }
               if (choice == 5) break;
            }
         }

         // Step 3: Apply filters
         Predicate<Project> combinedFilter = getProjectPredicate(filters);

         List<Project> filtered = allProjects.stream().filter(combinedFilter).collect(Collectors.toList());

         if (filtered.isEmpty()) {
            Color.println("No projects match your filter.", Color.RED);
         } else {
            Color.println("--- Filtered Projects ---", Color.YELLOW);
            printProjectsTable(filtered);
         }

      } catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private static Predicate<Project> getProjectPredicate(UserFilterSettings filters) {
      Predicate<Project> combinedFilter = p -> true;

      if (filters.getProjectName() != null)
         combinedFilter = combinedFilter.and(p -> p.getProjectName().toLowerCase().contains(filters.getProjectName()));

      if (filters.getNeighbourhood() != null)
         combinedFilter = combinedFilter.and(p -> p.getNeighborhood().toLowerCase().contains(filters.getNeighbourhood()));

      if (filters.getFlatType() != null)
         combinedFilter = combinedFilter.and(p -> p.getAvailableFlats().getOrDefault(filters.getFlatType(), 0) > 0);

      if (filters.getDate() != null)
         combinedFilter = combinedFilter.and(p -> !filters.getDate().isBefore(p.getApplicationOpeningDate()) &&
                 !filters.getDate().isAfter(p.getApplicationClosingDate()));
      return combinedFilter;
   }


   public void printProjectsTable(List<Project> projects) {
      Integer COLUMN_WIDTH = 15;
      List<List<String>> tableData = new ArrayList<>();
      tableData.add(List.of(
              "Project ID", "Project Name", "Neighbourhood", "Visibility",
              "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price",
              "Opening Date", "Closing Date"
      ));
      for (Project p : projects) {
         tableData.add(p.toPreviewTableRow());
      }
      tablePrinter.printTable(COLUMN_WIDTH, tableData);
   }

   public void handleApplyForBTOProject() {
      try {
         if (applicantController.hasSuccessfulOrBookedApplication()) {
            Color.println("You already have an application.", Color.RED);
            return;
         }

         List<Project> eligibleProjects = applicantController.getEligibleProjects();
         if (eligibleProjects.isEmpty()) {
            Color.println("No eligible projects available at the moment.", Color.RED);
            return;
         }

         // Show projects with numbering
         Integer COLUMN_WIDTH = 15;
         List<List<String>> tableData = new ArrayList<>();
         tableData.add(List.of("Index", "Project ID", "Project Name", "Neighbourhood", "Two Room Units", "Three Room Units"));
         for (int i = 0; i < eligibleProjects.size(); i++) {
            Project p = eligibleProjects.get(i);
            tableData.add(List.of(
                    String.valueOf(i + 1),
                    String.valueOf(p.getId()),
                    p.getProjectName(),
                    p.getNeighborhood(),
                    String.valueOf(p.getTwoRoomUnits()),
                    String.valueOf(p.getThreeRoomUnits())
            ));
         }
         tablePrinter.printTable(COLUMN_WIDTH, tableData);

         // Ask user to select project
         Color.print("Enter the index number of the project to apply for: ", Color.CYAN);
         int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

         if (choice < 0 || choice >= eligibleProjects.size()) {
            Color.println("Invalid project selection.", Color.RED);
            return;
         }

         Project selectedProject = eligibleProjects.get(choice);
         FlatType selectedFlatType = null;

         int twoRoomUnits = selectedProject.getTwoRoomUnits();
         int threeRoomUnits = selectedProject.getThreeRoomUnits();

         if (twoRoomUnits > 0 && threeRoomUnits > 0) {
            Color.println("This project has both TWO-ROOM and THREE-ROOM flats available.", Color.CYAN);
            Color.print("Enter your choice (1 = TWO_ROOM, 2 = THREE_ROOM): ", Color.CYAN);
            int flatChoice = Integer.parseInt(scanner.nextLine().trim());
            if (flatChoice == 1) selectedFlatType = FlatType.TWO_ROOM;
            else if (flatChoice == 2) selectedFlatType = FlatType.THREE_ROOM;
            else {
               Color.println("Invalid flat type selected.", Color.RED);
               return;
            }
         } else if (twoRoomUnits > 0) {
            Color.println("Only TWO-ROOM flats are available for this project.", Color.YELLOW);
            selectedFlatType = FlatType.TWO_ROOM;
         } else if (threeRoomUnits > 0) {
            Color.println("Only THREE-ROOM flats are available for this project.", Color.YELLOW);
            selectedFlatType = FlatType.THREE_ROOM;
         } else {
            Color.println("No flats are available in this project.", Color.RED);
            return;
         }

         // Apply
         applicantController.applyForProject(selectedProject.getId(), selectedFlatType);
         Color.println("Application submitted successfully!", Color.GREEN);

      } catch (NumberFormatException e) {
         Color.println("Invalid number entered. Please try again.", Color.RED);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      } catch (Exception e) {
         Color.println("Unexpected error occurred: " + e.getMessage(), Color.RED);
      }
   }


   public void handleViewApplicationStatus() {
      try {
         List<Application> myApplications = applicantController.getMyApplications();

         if (myApplications.isEmpty()) {
            Color.println("You have not submitted any applications.", Color.RED);
            return;
         }

         List<List<String>> tableData = new ArrayList<>();
         tableData.add(List.of("Project Name", "Date Applied", "Application Status"));

         for (Application app : myApplications) {
            tableData.add(List.of(
                    app.getProject().getProjectName(),
                    app.getDateApplied().toString(),
                    app.getApplicationStatus().toString()
            ));
         }

         Integer COLUMN_WIDTH = 20;
         Color.println("--- My Applications ---", Color.YELLOW);
         tablePrinter.printTable(COLUMN_WIDTH, tableData);

      } catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   public void handleRequestWithdrawal() {
      try {
         Application application = applicantController.getPendingApplication();

         if (application == null) {
            Color.println("You do not have any pending application to withdraw.", Color.RED);
            return;
         }

         // Show application details
         Color.println("--- Your Current Application ---", Color.YELLOW);
         Color.println("Project Name      : " + application.getProject().getProjectName(), Color.CYAN);
         Color.println("Flat Type         : " + application.getFlatType(), Color.CYAN);
         Color.println("Date Applied      : " + application.getDateApplied(), Color.CYAN);
         Color.println("Application Status: " + application.getApplicationStatus(), Color.CYAN);
         Color.println("-------------------------------------------", Color.YELLOW);

         // Ask for confirmation
         Color.print("Are you sure you want to withdraw this application? (Y/N): ", Color.CYAN);
         String confirm = scanner.nextLine().trim().toUpperCase();

         if (confirm.equals("Y")) {
            applicantController.requestWithdrawal(application.getId());
            Color.println("Withdrawal request submitted.", Color.GREEN);
         } else {
            Color.println("Withdrawal cancelled.", Color.YELLOW);
         }

      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      } catch (Exception e) {
         Color.println("Unexpected error: " + e.getMessage(), Color.RED);
      }
   }


   public void handleSubmitEnquiry() {
      try {
         List<Project> eligibleProjects = applicantController.getEligibleProjects();
         if (eligibleProjects.isEmpty()) {
            Color.println("No eligible projects available at the moment.", Color.RED);
            return;
         }

         // Show projects with numbering
         Integer COLUMN_WIDTH = 15;
         List<List<String>> tableData = new ArrayList<>();
         tableData.add(List.of("Index", "Project ID", "Project Name", "Neighbourhood", "Two Room Units", "Three Room Units"));
         for (int i = 0; i < eligibleProjects.size(); i++) {
            Project p = eligibleProjects.get(i);
            tableData.add(List.of(
                    String.valueOf(i + 1),
                    String.valueOf(p.getId()),
                    p.getProjectName(),
                    p.getNeighborhood(),
                    String.valueOf(p.getTwoRoomUnits()),
                    String.valueOf(p.getThreeRoomUnits())
            ));
         }
         tablePrinter.printTable(COLUMN_WIDTH, tableData);

         // Ask user to select project
         Color.print("Enter the index number of the project to apply for: ", Color.CYAN);
         int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

         if (choice < 0 || choice >= eligibleProjects.size()) {
            Color.println("Invalid project selection.", Color.RED);
            return;
         }

         Project selectedProject = eligibleProjects.get(choice);
         Color.print("Enter your enquiry: ", Color.CYAN);
         String enquiryText = scanner.nextLine();
         applicantController.submitEnquiry(selectedProject.getId(), enquiryText);
         Color.println("Enquiry submitted successfully.", Color.GREEN);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   public void handleViewMyEnquiries() {
      try {
         List<List<String>> enquiryData = applicantController.getMyEnquiriesAsTableData();

         if (enquiryData == null || enquiryData.size() <= 1) {
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


   public void handleEditEnquiry() {
      try {
         List<Enquiry> enquiries = applicantController.getMyEnquiries();
         if (enquiries.isEmpty()) {
            Color.println("You have no enquiries to edit.", Color.RED);
            return;
         }

         // Display list with enumeration
         Color.println("--- Your Enquiries ---", Color.YELLOW);
         int index = 1;
         for (Enquiry e : enquiries) {
            Color.println(index + ". " + e.getProject().getProjectName() + " | " +
                    e.getDateEnquired() + " | " +
                    (e.getEnquiry() != null ? e.getEnquiry() : "-") + " | " +
                    (e.getReply() != null ? "Reply Received" : "No Reply"), Color.CYAN);
            index++;
         }

         // Choose enquiry
         Color.print("Enter the number of the enquiry to edit: ", Color.CYAN);
         int choice = Integer.parseInt(scanner.nextLine());

         if (choice < 1 || choice > enquiries.size()) {
            throw new IllegalArgumentException("Invalid enquiry selection.");
         }

         Enquiry selected = enquiries.get(choice - 1);
         Color.println("Current Enquiry: " + selected.getEnquiry(), Color.YELLOW);
         Color.print("Enter new enquiry text: ", Color.CYAN);
         String newEnquiryText = scanner.nextLine();

         applicantController.editEnquiry(selected, newEnquiryText);
         Color.println("Enquiry updated successfully.", Color.GREEN);

      } catch (NumberFormatException e) {
         Color.println("Invalid input. Please enter a number.", Color.RED);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      } catch (Exception e) {
         Color.println("Unexpected error: " + e.getMessage(), Color.RED);
      }
   }


   public void handleDeleteEnquiry() {
      try {
         List<Enquiry> myEnquiries = applicantController.getMyEnquiries();

         if (myEnquiries.isEmpty()) {
            Color.println("You have no enquiries to delete.", Color.RED);
            return;
         }

         Color.println("--- Your Enquiries ---", Color.YELLOW);
         for (int i = 0; i < myEnquiries.size(); i++) {
            Enquiry e = myEnquiries.get(i);
            Color.println((i + 1) + ". " +
                    e.getProject().getProjectName() + " | " +
                    e.getDateEnquired() + " | " +
                    (e.getEnquiry() != null ? e.getEnquiry() : "-") + " | " +
                    (e.getReply() != null ? "Reply Received" : "No Reply"), Color.CYAN);
         }

         Color.print("Enter the number of the enquiry to delete: ", Color.CYAN);
         int choice = Integer.parseInt(scanner.nextLine());

         if (choice < 1 || choice > myEnquiries.size()) {
            throw new IllegalArgumentException("Invalid enquiry selection.");
         }

         Enquiry selected = myEnquiries.get(choice - 1);

         Color.print("Are you sure you want to delete this enquiry? (y/n): ", Color.YELLOW);
         String confirm = scanner.nextLine();
         if (!confirm.equalsIgnoreCase("y")) {
            Color.println("Deletion cancelled.", Color.YELLOW);
            return;
         }

         boolean success = applicantController.deleteEnquiryIfAllowed(selected);
         if (success) {
            Color.println("Enquiry deleted successfully.", Color.GREEN);
         } else {
            Color.println("Enquiry could not be deleted. Enquiry may have been replied to already.", Color.RED);
         }

      } catch (NumberFormatException e) {
         Color.println("Invalid input. Please enter a number.", Color.RED);
      } catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      } catch (Exception e) {
         Color.println("Unexpected error: " + e.getMessage(), Color.RED);
      }
   }

   public void handleChangePassword() {
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