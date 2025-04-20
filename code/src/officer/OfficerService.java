package officer;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import enquiry.Enquiry;
import interfaces.StaffService;
import project.IProjectService;
import project.Project;
import user.IUserService;

import java.util.List;

public class OfficerService implements IOfficerService, StaffService {
   private final IProjectService projectService;
   private final IUniqueIdService uniqueIdService;
   private final Officer officer;
   private final IUserService userService;

   public OfficerService(IProjectService projectService, IUniqueIdService uniqueIdService, Officer officer, IUserService userService) {
      this.projectService = projectService;
      this.uniqueIdService = uniqueIdService;
      this.officer = officer;
      this.userService = userService;
   }

   @Override
   public List<Enquiry> getCurrentProjectEnquiries() {
      return List.of();
   }

   @Override
   public RegistrationForm createRegistrationForm(String projectName) {
      return new RegistrationForm(uniqueIdService.generateUniqueId(IdType.REGISTRATION_FORM_ID), officer.getName(), officer.getNric(), projectService.getProjectByName(projectName).getId(), projectName);
   }

   @Override
   public void sendRegistrationRequest(RegistrationForm form) throws IllegalArgumentException {
      projectService.addRegistrationToProject(form);
   }

   @Override
   public OfficerStatus getOfficerStatus() {
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
   public Project getCurrentProject(Officer officer) {
      return null;
   }

   @Override
   public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
      userService.changePassword(oldPassword, newPassword, confirmPassword);
   }
}
