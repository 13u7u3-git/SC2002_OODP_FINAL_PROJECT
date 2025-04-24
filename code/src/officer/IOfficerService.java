package officer;

import project.String;
import user.IUserService;

public interface IOfficerService extends IUserService {
   // Officer Registration
   RegistrationForm createRegistrationForm(java.lang.String projectId);

   void sendRegistrationRequest(RegistrationForm registrationForm);

   OfficerStatus getOfficerStatus();

   void setOfficerStatus(OfficerStatus status);

   RegistrationForm getCurrentRegistrationForm();

   void setCurrentRegistrationForm(RegistrationForm form);

   void addToMyRegistrations(RegistrationForm form);

   void removeRegistrationForm(RegistrationForm form);

   // Project Handling
   String getCurrentProject();

   void setUser(Officer officer);

   void setOfficerCurrentProject(java.lang.String officerName, String currentProject);


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
}