package officer;

import project.Project;

public interface IOfficerService {
   // Officer Registration
   RegistrationForm createRegistrationForm(String projectId);

   void sendRegistrationRequest(RegistrationForm registrationForm);

   OfficerStatus getOfficerStatus();

   void setOfficerStatus(OfficerStatus status);

   RegistrationForm getCurrentRegistrationForm();

   void setCurrentRegistrationForm(RegistrationForm form);

   void addToMyRegistrations(RegistrationForm form);

   void removeRegistrationForm(RegistrationForm form);

   // Project Handling
   Project getCurrentProject(Officer officer);

   void changePassword(String oldPassword, String newPassword, String confirmPassword);

   // Application Management
//   List<Application> getApplicationsForCurrentProject();

//   void updateFlatAvailability(FlatType flatType, int quantityChange);
//
//   void updateApplicantStatus(Application application, ApplicationStatus newStatus);
//
//   String generateBookingReceipt(Application application);
//
//   // Enquiry Handling
//   List<Enquiry> getEnquiriesForHandledProject();
//
//   void replyToEnquiry(Enquiry enquiry, String response);
//
//   // Account Management
//   void changePassword(User officer, String newPassword);
}