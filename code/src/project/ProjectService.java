// updated, will not be using the interfaces in the projects previously 
package project;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import applicant.Application;
import enquiry.Enquiry;
import helper.Color;
import helper.TablePrinter;
import officer.IRegistrationValidationService;
import officer.RegistrationForm;
import officer.RegistrationValidationService;
import system.ServiceRegistry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProjectService {
   private final ProjectRegistry projectRegistry;
   private final IUniqueIdService uniqueIdService;
   private final ProjectValidationService projectValidationService;
   private final IRegistrationValidationService registrationValidationService;

   public ProjectService(ProjectRegistry projectRegistry) {
      this.projectRegistry = projectRegistry;
      this.uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
      this.projectValidationService = new ProjectValidationService();
      this.registrationValidationService = new RegistrationValidationService(projectRegistry);
   }

   public Project getProjectById(Integer projectId) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getId().equals(projectId))
              .findFirst()
              .orElse(null);
   }

   public Project getProjectByName(String projectName) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getProjectName().equals(projectName))
              .findFirst()
              .orElse(null);
   }

   public List<Project> getAllProjects() {
      return projectRegistry.getProjects();
   }

   public Project createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                                Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                                LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                                List<String> officers) {
      Project project = new Project(uniqueIdService.generateUniqueId(IdType.PROJECT_ID), projectName, neighbourhood,
              twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);
      validateNewProject(project);
      addProjectToRegistry(project);
      return project;
   }

   public void deleteProject(Project project) {
      projectRegistry.removeProject(project);
   }

   public String returnNameIfProjectExists(String projectName) {
      String returnName = projectRegistry.getProjects().stream()
              .filter(p -> p.getProjectName().equals(projectName))
              .findFirst()
              .map(Project::getProjectName)
              .orElse(null);
      if (returnName == null) {
         try {
            returnName = projectRegistry.getProjects().stream()
                    .filter(p -> p.getId().equals(Integer.parseInt(projectName)))
                    .findFirst()
                    .map(Project::getProjectName)
                    .orElse(null);
         } catch (NumberFormatException ignored) {}
      }
      return returnName;
   }

   public void addProjectToRegistry(Project project) {
      projectRegistry.addProject(project);
   }

   public void removeProjectFromRegistry(Project project) {
      projectRegistry.removeProject(project);
   }

   public List<Project> getFilteredProjects(Predicate<Project> predicate) {
      return projectRegistry.filter(predicate);
   }

   public void validateNewProject(Project project) throws IllegalArgumentException {
      projectValidationService.validateProjectNameUnique(project, getAllProjects());
      projectValidationService.validateApplicationDates(project.getApplicationOpeningDate(),
              project.getApplicationClosingDate());
      projectValidationService.validateNeighborhood(project.getNeighborhood());
      projectValidationService.validateFlatUnitsAndPrices(project.getTwoRoomUnits(), project.getTwoRoomPrice(),
              project.getThreeRoomUnits(), project.getThreeRoomPrice());
      projectValidationService.validateOfficerSlots(project.getAvailableOfficerSlots());
   }

   public void addRegistrationToProject(RegistrationForm form) throws IllegalArgumentException {
      registrationValidationService.validateRegistration(form);
      Project project = getProjectById(form.getProjectId());
      project.addRegistrationForm(form);
   }

   public void addApplicationToProject(Application app) throws IllegalArgumentException {
      Project project = getProjectById(app.getId());
      project.getApplications().add(app);
   }

   public void addEnquiryToProject(Enquiry enquiry) {
      // Placeholder for future implementation
   }

   public void removeEnquiryFromProject(Enquiry enquiry) {
      // Placeholder for future implementation
   }
}
