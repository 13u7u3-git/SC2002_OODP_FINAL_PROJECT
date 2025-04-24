package manager;

import officer.RegistrationForm;
import officer.RegistrationStatus;
import project.IProjectService;
import project.String;
import system.ServiceRegistry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManagerController {
   private final IManagerService managerService;
   private final IProjectService projectService;

   public ManagerController() {
      this.managerService = ServiceRegistry.get(IManagerService.class);
      this.projectService = ServiceRegistry.get(IProjectService.class);
   }

   public void createProject(java.lang.String projectName, java.lang.String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                             Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                             LocalDate applicationClosingDate, java.lang.String manager, Integer availableOfficerSlots,
                             List<java.lang.String> officers) throws IllegalArgumentException {

      managerService.createProject(projectName, neighbourhood,
              twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);

   }


   public boolean deleteProject(Integer projectId) {
      try {
         managerService.deleteProject(projectId);
         return true;
      }
      catch (Exception e) {
         return false;
      }
   }

   public List<List<java.lang.String>> getAllProjectsTableData() throws Exception {
      List<String> projects = projectService.getAllProjects();
      if (projects == null || projects.isEmpty()) {
         return null;
      }
      else {
         List<java.lang.String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
         List<List<java.lang.String>> tableData = new ArrayList<>();
         tableData.add(headerRow);
         for (String p : projects) {
            List<java.lang.String> fromEachProject = p.toStringAsList();
            tableData.add(fromEachProject);
         }
         return tableData;
      }
   }

   public List<List<java.lang.String>> getMyProjectsTableData() {

      List<String> myProjects = managerService.getMyProjects();

      if (myProjects == null || myProjects.isEmpty()) {
         return null;
      }
      else {
         List<java.lang.String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
         List<List<java.lang.String>> tableData = new ArrayList<>();
         tableData.add(headerRow);
         for (String p : myProjects) {
            List<java.lang.String> fromRegistration = p.toStringAsList();
            tableData.add(fromRegistration);
         }
         return tableData;
      }
   }


   public String getProjectById(Integer projectId) {
      return projectService.getProjectById(projectId);
   }

   public boolean toggleVisibility(Integer projectId) {
      String project = projectService.getProjectById(projectId);
      project.setVisibility(!project.isVisibility());
      return true;
   }

   public void editProject(Integer projectId, java.lang.String option, Object T) {
      String project = projectService.getProjectById(projectId);
      switch (option) {
         case "1" -> {
            project.setProjectName((java.lang.String) T);
         }
         case "2" -> {
            project.setNeighborhood((java.lang.String) T);
         }
         case "3" -> {
            project.setApplicationOpeningDate((LocalDate) T);
         }
         case "4" -> {
            project.setApplicationClosingDate((LocalDate) T);
         }
         case "5" -> {
            project.setVisibility((boolean) T);
         }
         default -> {
            System.out.println("Invalid option");
         }
      }
   }

   public List<List<java.lang.String>> getPendingRegistrationTableData() throws Exception {

      try {
         List<RegistrationForm> pendingRegistrations = managerService.getPendingOfficerRegistrations();
         if (pendingRegistrations.isEmpty()) {
            return null;
         }
         List<java.lang.String> headerRow = List.of("ID", "Officer", "NRIC", "Date Applied", "Status", "Project ID", "Project Name");
         List<List<java.lang.String>> tableData = new ArrayList<>();
         tableData.add(headerRow.subList(0, 4));
         for (RegistrationForm form : pendingRegistrations) {
            List<java.lang.String> fromRegistration = form.toStringAsList().subList(0, 4);
            tableData.add(fromRegistration);
         }
         return tableData;
      }
      catch (Exception e) {
         throw new Exception("No pending registrations found");
      }
   }

   public Boolean processOfficerRegistration(java.lang.String identifier, RegistrationStatus status) throws Exception {
      try {

         java.lang.String officerStr = managerService.setRegistrationStatus(identifier, status);
         if (officerStr != null) {
            managerService.addToOfficersList(officerStr);

            return true;
         }
      }
      catch (Exception e) {
         throw e;
      }
      return false;
   }

   public java.lang.String getCurrentProjectData() {
      return managerService.getCurrentProject() != null ? managerService.getCurrentProject().toString() : null;
   }
}