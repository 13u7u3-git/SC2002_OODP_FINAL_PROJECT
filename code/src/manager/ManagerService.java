package manager;

import enquiry.Enquiry;
import interfaces.StaffService;
import officer.IOfficerService;
import officer.RegistrationForm;
import officer.RegistrationStatus;
import project.FlatType;
import project.IProjectService;
import project.Project;
import system.ServiceRegistry;
import user.IPasswordValidationService;
import user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ManagerService implements IManagerService, StaffService {
   private final IProjectService projectService;
   private final IPasswordValidationService passwordValidationService;
   private Manager manager;

   public ManagerService() {
      this.projectService = ServiceRegistry.get(IProjectService.class);
      this.passwordValidationService = ServiceRegistry.get(IPasswordValidationService.class);
   }

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

   @Override
   public Project getCurrentProject() {
      return manager.getCurrentProject();
   }

   public void setCurrentProject(Project currentProject) {
      this.manager.setCurrentProject(currentProject);
   }

   @Override
   public List<RegistrationForm> getPendingOfficerRegistrations() throws Exception {
      try {
         List<RegistrationForm> pendingRegistrations
                 = manager.getCurrentProject()
                 .getRegistrationForms().stream()
                 .filter(RegistrationForm::isPending).toList();//Lambda expression
         return pendingRegistrations;
      }
      catch (RuntimeException e) {
         if (manager.getCurrentProject() == null) {
            throw new Exception("You are not managing any project.");
         }
         else {
            throw new Exception("No pending registrations");
         }
      }
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

   @Override
   public User getUser() { //upcasting is done via return type definition
      return this.manager;
   }

   @Override
   public void setUser(Manager manager) {
      this.manager = manager;
   }

   @Override
   public IPasswordValidationService getPasswordValidationService() {
      return passwordValidationService;
   }

   @Override
   public String setRegistrationStatus(String identifier, RegistrationStatus status) throws Exception {
      Integer id = null;
      try {
         id = Integer.parseInt(identifier);
      }
      catch (NumberFormatException e) {
         // Identifier is not a number, assume it's a name
      }

      Project project = manager.getCurrentProject();
      try {
         if (id == null) {
            RegistrationForm f =
                    project.getRegistrationForms().stream().filter(x -> x.getOfficerName().equals(identifier)).findFirst().get();
            f.setStatus(status);
            return f.getOfficerName();
         }
         else {
            Integer finalId = id;//lamda only allow final variable in lambda expression
            RegistrationForm f =
                    project.getRegistrationForms().stream().filter(x -> x.getId().equals(finalId)).findFirst().get();
            f.setStatus(status);
            return f.getOfficerName();
         }

      }
      catch (Exception e) {
         throw new Exception("Registration form not found, please check your input");
      }
   }

   @Override
   public void addToOfficersList(String officerStr) {
      Project project = manager.getCurrentProject();
      String[] officerArr = officerStr.split(",");
      for (String officer : officerArr) {
         project.getOfficers().add(officer);
      }
   }

   public void setOfficerCurrentProject(String officerName) {
      // TODO: Implement this method
      IOfficerService officerService = ServiceRegistry.get(IOfficerService.class);
      officerService.setOfficerCurrentProject(officerName, manager.getCurrentProject());
   }
}