package manager;

import project.IProjectService;
import project.Project;

import java.time.LocalDate;
import java.util.List;

public class ManagerController {
   private final IManagerService managerService;
   private final IProjectService projectService;

   public ManagerController(IManagerService managerService, IProjectService projectService) {
      this.managerService = managerService;
      this.projectService = projectService;
   }

   public boolean createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                                Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                                LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                                List<String> officers) {
      try {
         managerService.createProject(projectName, neighbourhood,
                 twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);
         return true;
      }
      catch (Exception e) {
         return false;
      }
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

   public void viewAllProjects() {
      projectService.printProjectsToTable(projectService.getAllProjects());
   }

   public void viewMyProjects() {
      List<Project> myProjects = managerService.getMyProjects();
      projectService.printProjectsToTable(myProjects);
   }


   public Project getProjectById(Integer projectId) {
      return projectService.getProjectById(projectId);
   }

   public boolean toggleVisibility(Integer projectId) {
      Project project = projectService.getProjectById(projectId);
      project.setVisibility(!project.isVisibility());
      return true;
   }

   public void editProject(Integer projectId, String option, Object T) {
      Project project = projectService.getProjectById(projectId);
      switch (option) {
         case "1" -> {
            project.setProjectName((String) T);
         }
         case "2" -> {
            project.setNeighbourhood((String) T);
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

}