package officer;

import helper.Color;
import helper.TablePrinter;
import project.IProjectService;
import project.Project;

import java.util.List;

public class OfficerController {

   private final IOfficerService officerService;
   private final IProjectService projectService;

   public OfficerController(IOfficerService officerService, IProjectService projectService) {
      this.officerService = officerService;
      this.projectService = projectService;
   }

   public void viewAllProjects() {
      Integer COLUMN_WIDTH = 15;
      Color.println("--- All Projects ---", Color.YELLOW);
      String[] headers = {"Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers"};
      TablePrinter tablePrinter = new TablePrinter();
      tablePrinter.PrintTableRow(COLUMN_WIDTH, List.of(headers));

      List<Project> projects = projectService.getAllProjects();
      if (projects == null || projects.isEmpty()) {
         Color.println("No projects found.", Color.RED);
         return;
      }//else
      projects.forEach(project -> {
         String[] row = {
                 project.getId().toString(),
                 project.getProjectName(),
                 project.getNeighborhood(),
                 project.isVisibility() ? "Visible" : "Hidden",
                 project.getTwoRoomUnits().toString(),
                 project.getTwoRoomPrice().toString(),
                 project.getThreeRoomUnits().toString(),
                 project.getThreeRoomPrice().toString(),
                 project.getApplicationOpeningDate().toString(),
                 project.getApplicationClosingDate().toString(),
                 project.getManager(),
                 project.getAvailableOfficerSlots().toString(),
                 project.getOfficers().toString()
         };
         tablePrinter.PrintTableRow(COLUMN_WIDTH, List.of(row));
      });
   }

   public void viewOfficerEligibleProjects() {

      if (officerService.getOfficerStatus() != OfficerStatus.INACTIVE) {
         Color.println("You are already registered as an officer for the project", Color.RED);
         return;
      }

      Integer COLUMN_WIDTH = 15;
      Color.println("--- Viewing Officer Eligible Projects ---", Color.YELLOW);

      List<Project> projects = projectService.getFilteredProjects(project ->
              project.getOfficers().size() < project.getAvailableOfficerSlots()
      );

      String[] headers = {"Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers"};
      TablePrinter tablePrinter = new TablePrinter();
      tablePrinter.PrintTableRow(COLUMN_WIDTH, List.of(headers));
      if (projects == null || projects.isEmpty()) {
         Color.println("No projects found.", Color.RED);
         return;
      }//else
      projects.forEach(project -> {
         String[] row = {
                 project.getId().toString(),
                 project.getProjectName(),
                 project.getNeighborhood(),
                 project.isVisibility() ? "Visible" : "Hidden",
                 project.getTwoRoomUnits().toString(),
                 project.getTwoRoomPrice().toString(),
                 project.getThreeRoomUnits().toString(),
                 project.getThreeRoomPrice().toString(),
                 project.getApplicationOpeningDate().toString(),
                 project.getApplicationClosingDate().toString(),
                 project.getManager(),
                 project.getAvailableOfficerSlots().toString(),
                 project.getOfficers().toString()
         };
         tablePrinter.PrintTableRow(COLUMN_WIDTH, List.of(row));
      });
   }

   public RegistrationForm CreateRegistrationForm(String project) {
      // TODO : check if officer is already an applicant for the project

      String projectStr = null;
      projectStr = projectService.returnNameIfProjectExists(project.trim());
      if (projectStr == null) {
         Color.println("Form not Created. Project not found or Error Creating Form. Try Again", Color.RED);
         return null;
      }
      RegistrationForm registrationForm = officerService.createRegistrationForm(projectStr);
      Color.println(registrationForm.toString(), Color.YELLOW);
      return registrationForm;
   }

   public void sendRegistrationRequest(RegistrationForm form) throws IllegalArgumentException {
      officerService.sendRegistrationRequest(form);
      officerService.setOfficerStatus(OfficerStatus.PENDING);
      officerService.setCurrentRegistrationForm(form);
      officerService.addToMyRegistrations(form);
   }

   public void viewOfficerRegistrationStatus() {
      OfficerStatus status = officerService.getOfficerStatus();
      Color.println("===============================================", Color.YELLOW);
      switch (status) {
         case ACTIVE -> {
            Color.println("You are currently an active officer", Color.GREEN);
            Color.println("===============================================", Color.YELLOW);
         }
         case INACTIVE -> {
            Color.println("You are currently inactive", Color.RED);
            Color.println("===============================================", Color.YELLOW);
         }
         case PENDING -> {
            Color.println("You are currently pending for registration", Color.YELLOW);
            try {
               RegistrationForm form = officerService.getCurrentRegistrationForm();
               Color.println(form.toString(), Color.YELLOW);
            }
            catch (Exception e) {
               Color.println("Error in the System: You have no current registration form", Color.RED);
            }
         }
      }
   }

   public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
      officerService.changePassword(oldPassword, newPassword, confirmPassword);
   }
}

