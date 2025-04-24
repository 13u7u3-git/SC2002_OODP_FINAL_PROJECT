package officer;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import enquiry.Enquiry;
import interfaces.StaffService;
import project.IProjectService;
import project.String;
import system.ServiceRegistry;
import user.IPasswordValidationService;
import user.User;
import user.UserRegistry;

import java.util.List;

public class OfficerService implements IOfficerService, StaffService {
   private final IProjectService projectService;
   private final IUniqueIdService uniqueIdService;
   private final IPasswordValidationService passwordValidationService;
   private Officer officer;

   public OfficerService() {

      this.projectService = ServiceRegistry.get(IProjectService.class);
      this.uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
      this.passwordValidationService = ServiceRegistry.get(IPasswordValidationService.class);
   }

   private void atLogin() {
      // Check if officer object is initialized
      if (officer == null) {
         System.out.println("Error: Officer not initialized");
         return;
      }

      String currentProject = officer.getCurrentProject();
      RegistrationForm currentRegistrationForm = getCurrentRegistrationForm();

      if (currentProject != null) {
         //System.outprintln("Restoring officer status to ACTIVE");
         setOfficerStatus(OfficerStatus.ACTIVE);
         return;
      }

      if (currentRegistrationForm == null) {
         System.out.println("No pending registration forms found");
         return; // don't do anything
      }

      if (getOfficerStatus() == OfficerStatus.PENDING && !currentRegistrationForm.isPending()) {
         // simulating active listener
         if (currentRegistrationForm.isApproved()) {
            //System.outprintln("Registration form approved. Setting status to ACTIVE");
            setOfficerStatus(OfficerStatus.ACTIVE);
            String project = projectService.getProjectById(currentRegistrationForm.getProjectId());
            if (project != null) {
               officer.setCurrentProject(project);
               //System.outprintln("Assigned to project: " + project.getProjectName());
            }
            else {
               System.out.println("Warning: Approved project not found");
            }
         }
         else {
            //System.outprintln("Registration form rejected. Setting status to INACTIVE");
            setOfficerStatus(OfficerStatus.INACTIVE);
         }
      }
   }

   @Override
   public List<Enquiry> getCurrentProjectEnquiries() {
      return List.of();
   }

   @Override
   public RegistrationForm createRegistrationForm(java.lang.String projectName) {
      return new RegistrationForm(uniqueIdService.generateUniqueId(IdType.REGISTRATION_FORM_ID), officer.getName(), officer.getNric(), projectService.getProjectByName(projectName).getId(), projectName);
   }

   @Override
   public void sendRegistrationRequest(RegistrationForm form) throws IllegalArgumentException {
      projectService.addRegistrationToProject(form);
   }

   @Override
   public OfficerStatus getOfficerStatus() {
      //System.out.println("Officer status: " + officer.getOfficerStatus());
      return officer.getOfficerStatus();
   }

   @Override
   public void setOfficerStatus(OfficerStatus status) {
      officer.setOfficerStatus(status);
   }

   @Override
   public RegistrationForm getCurrentRegistrationForm() {
      return officer.getCurrentRegistrationForm();
   }

   @Override
   public void setCurrentRegistrationForm(RegistrationForm form) {
      officer.setCurrentRegistrationForm(form);
   }

   @Override
   public void addToMyRegistrations(RegistrationForm form) {
      officer.addRegistrationForm(form);
   }

   @Override
   public void removeRegistrationForm(RegistrationForm form) {
      officer.removeRegistrationForm(form);
   }

   @Override
   public String getCurrentProject() {
      return officer.getCurrentProject();
   }

   @Override
   public User getUser() {
      return this.officer;
   }

   @Override
   public void setUser(Officer officer) {
      this.officer = officer;
      this.atLogin();
   }

   @Override
   public void setOfficerCurrentProject(java.lang.String officerName, String currentProject) {
      UserRegistry userRegistry = ServiceRegistry.get(UserRegistry.class);
      Officer officer = (Officer) userRegistry.getUser(officerName);
      officer.setCurrentProject(currentProject);
   }

   @Override
   public IPasswordValidationService getPasswordValidationService() {
      return this.passwordValidationService;
   }
}