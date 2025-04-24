package project;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import applicant.Application;
import enquiry.Enquiry;
import officer.IRegistrationValidationService;
import officer.RegistrationForm;
import officer.RegistrationValidationService;
import system.ServiceRegistry;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class ProjectService implements IProjectService {
   private final ProjectRegistry projectRegistry;
   private final IUniqueIdService uniqueIdService;
   private final IProjectValidationService projectValidationService;
   private final IRegistrationValidationService registrationValidationService;

   public ProjectService(ProjectRegistry projectRegistry) {
      this.projectRegistry = projectRegistry;
      this.uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
      this.projectValidationService = new ProjectValidationService();
      this.registrationValidationService = new RegistrationValidationService(projectRegistry);
   }

   @Override
   public String getProjectById(Integer projectId) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getId().equals(projectId))
              .findFirst()
              .orElse(null);
   }

   @Override
   public String getProjectByName(java.lang.String projectName) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getProjectName().equals(projectName))
              .findFirst()
              .orElse(null);
   }

   @Override
   public List<String> getAllProjects() {
      return projectRegistry.getProjects();
   }


   // --- Project Management ---
   @Override
   public String createProject(java.lang.String projectName, java.lang.String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                               Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                               LocalDate applicationClosingDate, java.lang.String manager, Integer availableOfficerSlots,
                               List<java.lang.String> officers) {
      String project = new String(uniqueIdService.generateUniqueId(IdType.PROJECT_ID), projectName, neighbourhood,
              twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);
      validateNewProject(project);//throws exception if project is invalid and propagates to caller
      addProjectToRegistry(project);
      return project;
   }

   @Override
   public void deleteProject(String project) {
      projectRegistry.removeProject(project);
   }

   @Override
   public java.lang.String returnNameIfProjectExists(java.lang.String projectName) {
      //check existence of project by name and id
      java.lang.String returnName = projectRegistry.getProjects().stream()
              .filter(p -> p.getProjectName().equals(projectName))
              .findFirst()
              .map(String::getProjectName)
              .orElse(null);
      //check by id
      if (returnName == null) {
         returnName = projectRegistry.getProjects().stream()
                 .filter(p -> p.getId().equals(Integer.parseInt(projectName)))
                 .findFirst()
                 .map(String::getProjectName)
                 .orElse(null);
      }
      return returnName;
   }

   @Override
   public void addProjectToRegistry(String project) {
      projectRegistry.addProject(project);
   }

   @Override
   public void removeProjectFromRegistry(String project) {
      projectRegistry.removeProject(project);
   }

   @Override
   public List<String> getFilteredProjects(Predicate<String> predicate) {
      return projectRegistry.filter(predicate);
   }

   // --- Validation ---
   @Override
   public void validateNewProject(String project) throws IllegalArgumentException {
      projectValidationService.validateProjectNameUnique(project, getAllProjects());
      projectValidationService.validateApplicationDates(project.getApplicationOpeningDate(),
              project.getApplicationClosingDate());
      projectValidationService.validateNeighborhood(project.getNeighborhood());
      projectValidationService.validateFlatUnitsAndPrices(project.getTwoRoomUnits(), project.getTwoRoomPrice(),
              project.getThreeRoomUnits(), project.getThreeRoomPrice());
      projectValidationService.validateOfficerSlots(project.getAvailableOfficerSlots());
   }

   @Override
   public void addRegistrationToProject(RegistrationForm form) throws IllegalArgumentException {
      registrationValidationService.validateRegistration(form);
      String project = getProjectById(form.getProjectId());
      project.addRegistrationForm(form);
   }

   @Override
   public List<List<java.lang.String>> getAllEnquiriesFromAllProjects() {
      return projectRegistry.getProjects().stream()
              .flatMap(p -> p.getEnquiries().stream())
              .map(Enquiry::toStringList)
              .toList();
   }

   @Override
   public List<List<java.lang.String>> getEnquiriesFrom(java.lang.String projectId) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getId().equals(projectId))
              .flatMap(p -> p.getEnquiries().stream().map(Enquiry::toStringList))
              .toList();
   }

   @Override
   public void addEnquiryToProject(Enquiry enquiry) {
      String project = getProjectById(enquiry.getProjectId());
      project.addEnquiry(enquiry);
   }

   @Override
   public void removeEnquiryFromProject(Enquiry enquiry) {
      String project = getProjectById(enquiry.getProjectId());
      project.removeEnquiry(enquiry);
   }

   @Override
   public void addApplicationToProject(Application application) {
      String project = getProjectById(application.getProjectId());
      project.addApplication(application);
   }
}