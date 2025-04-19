package manager;

import enquiry.Enquiry;
import interfaces.StaffService;
import project.FlatType;
import project.ImmutableProjectView;
import project.Project;
import project.ProjectService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ManagerService implements IManagerService, StaffService {
   private final ProjectService projectService;
   private final Manager manager;

   public ManagerService(ProjectService projectService, Manager manager) {
      this.projectService = projectService;
      this.manager = manager;
   }


   public void setCurrentProject(Project currentProject) {
      this.manager.setCurrentProject(currentProject);
   }

   @Override
   public ImmutableProjectView getCurrentProjectDetails() {
      //return immutable version of the current project
      return new ImmutableProjectView(this.manager.getCurrentProject());
   }//not in this format , to string format is better

   @Override
   public List<Enquiry> getCurrentProjectEnquiries() {
      // Implementation
      return null;
   }

   public void createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                             Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                             LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                             List<String> officers) {

      Project newProject = projectService.createProject(projectName, neighbourhood,
              twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);
   }

   public void updateProject(Project project, String name, String neighbourhood,
                             Map<FlatType, Integer> availableFlats, LocalDate openingDate,
                             LocalDate closingDate, boolean visibility) {


   }


   @Override
   public void deleteProject(Integer projectId) {
      Project proj = projectService.getProjectById(projectId);
      projectService.deleteProject(proj);
   }

//   @Override
//   public List<Project> getAllProjects() {
//      return projectService.getAllProjects();
//   }

   @Override
   public List<Project> getMyProjects() {
      //System.out.println("Manager: " + manager.getNric());
      return projectService.getFilteredProjects(project -> project.getManager().equals(this.manager.getName()));
   }

   @Override
   public void viewAllEnquiries() {

   }

}