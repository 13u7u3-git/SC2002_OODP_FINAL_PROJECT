package applicant;

import enquiry.Enquiry;
import enquiry.EnquiryService;
import project.FlatType;
import project.Project;
import project.ProjectService;
import user.IPasswordService;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ApplicantController {
   private final IApplicantService applicantService;
   private final IPasswordService passwordService;
   private final ProjectService projectService;
   private final EnquiryService enquiryService;

   public ApplicantController(IApplicantService applicantService, IPasswordService passwordService, ProjectService projectService, EnquiryService enquiryService) {
      this.applicantService = applicantService;
      this.passwordService = passwordService;
      this.projectService = projectService;
      this.enquiryService = enquiryService;
   }

   public List<Project> getEligibleProjects() {
      Applicant applicant = (Applicant) applicantService.getUser();
      Predicate<Project> predicate = applicantService.isEligibleForApplicant(applicant);
      return projectService.getFilteredProjects(predicate);
   }

//   public List<List<String>> getEligibleProjectsTableData() {
//      List<Project> projects = getEligibleProjects();
//
//      if (projects == null || projects.isEmpty()) {
//         return List.of();
//      }
//
//      List<String> headerRow = List.of(
//              "Project ID", "Project Name", "Neighbourhood", "Visibility",
//              "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price",
//              "Appln..Opening Date", "Appln..Closing Date"
//      );
//
//      List<List<String>> tableData = new ArrayList<>();
//      tableData.add(headerRow);
//
//      for (Project p : projects) {
//         tableData.add(p.toTableRow().subList(0, 10)); // be sure this order is consistent
//      }
//
//      return tableData;
//   }
   public void applyForProject(int projectId, FlatType flatType) {
      Applicant applicant = (Applicant) applicantService.getUser();
      Project project = projectService.getProjectById(projectId);
      if (project == null) {
         throw new IllegalArgumentException("Project not found.");
      }
      Application application = new Application(uniqueID_here, applicant, project, flatType);
      projectService.addApplicationToProject(application);
   }

   public List<Application> getMyApplications() {
      Applicant applicant = (Applicant) applicantService.getUser();
      return applicant.getMyApplications();
   }

   public void requestWithdrawal(int applicationId) {
      Application application = getApplicationById(applicationId);
      if (application == null) {
         throw new IllegalArgumentException("Application not found.");
      }
      application.setWithdrawalRequestStatus(WithdrawalRequestStatus.PENDING);
   }

   public void submitEnquiry(int projectId, String enquiryText) {
      Applicant applicant = (Applicant) applicantService.getUser();
      Project project = projectService.getProjectById(projectId);
      if (project == null) {
         throw new IllegalArgumentException("Project not found.");
      }
      Enquiry enquiry = enquiryService.createEnquiry(69, project, applicant, enquiryText);
      enquiryService.submitEnquiry(enquiry);
   }

   public List<Enquiry> getMyEnquiries() {
      Applicant applicant = (Applicant) applicantService.getUser();
      return enquiryService.getEnquiriesForApplicant(applicant);
   }

   public void editEnquiry(Enquiry enquiry, String newEnquiryText) {
      Applicant applicant = (Applicant) applicantService.getUser();

      if (enquiry == null) {
         throw new IllegalArgumentException("Enquiry not found.");
      }
      enquiryService.editEnquiry(applicant, enquiry, newEnquiryText);
   }

   public boolean deleteEnquiryIfAllowed(Enquiry enquiry) {
      Applicant applicant = (Applicant) applicantService.getUser();

      if (enquiry == null) {
         throw new IllegalArgumentException("Enquiry not found.");
      }

       return enquiryService.deleteEnquiry(applicant, enquiry);
   }

   public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
      passwordService.changePassword(oldPassword, newPassword, confirmPassword);
   }

   private Application getApplicationById(int applicationId) {
      Applicant applicant = (Applicant) applicantService.getUser();
      return applicant.getMyApplications().stream()
              .filter(app -> app.getId() == applicationId)
              .findFirst()
              .orElse(null);
   }

   public boolean hasSuccessfulOrBookedApplication() {
      User user = applicantService.getUser();
      if (!(user instanceof Applicant)) {
         throw new IllegalStateException("Current user is not an applicant.");
      }

      List<Application> applications = applicantService.getApplicationsByApplicant((Applicant) user);

      for (Application app : applications) {
         if (app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL ||
                 app.getBookingStatus() == BookingStatus.BOOKED) {
            return true;
         }
      }

      return false;
   }

   public Application getPendingApplication() {
      User user = applicantService.getUser();
      if (!(user instanceof Applicant)) {
         throw new IllegalStateException("Current user is not an applicant.");
      }

      List<Application> applications = applicantService.getApplicationsByApplicant((Applicant) user);

      for (Application app : applications) {
         if (app.getApplicationStatus() == ApplicationStatus.PENDING) {
            return app;
         }
      }

      return null; // No pending application found
   }

   public List<List<String>> getMyEnquiriesAsTableData() {
      User user = applicantService.getUser();
      if (!(user instanceof Applicant)) {
         throw new IllegalStateException("Current user is not an applicant.");
      }

      List<Enquiry> enquiries = applicantService.getEnquiriesByApplicant((Applicant) user);
      List<List<String>> tableData = new ArrayList<>();

      // Add header row
      tableData.add(List.of("ID", "Project", "Date", "Question", "Reply"));

      // Add each enquiry row
      for (Enquiry e : enquiries) {
         tableData.add(List.of(
                 String.valueOf(e.getId()),
                 e.getProject().getProjectName(),
                 e.getDateEnquired().toString(),
                 e.getEnquiry() != null ? e.getEnquiry() : "-",
                 e.getReply() != null ? e.getReply() : "-"
         ));
      }

      return tableData;
   }


}

