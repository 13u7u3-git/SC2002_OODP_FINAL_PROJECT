package applicant;

import UniqueID.IUniqueIdService;
import enquiry.Enquiry;
import enquiry.EnquiryService;
import project.FlatType;
import project.String;
import project.ProjectService;
import system.ServiceRegistry;
import system.SessionManager;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ApplicantController{
   private final IApplicantService applicantService;
   private final ProjectService projectService;
   private final EnquiryService enquiryService;
   private final SessionManager sessionManager= ServiceRegistry.get(SessionManager.class);
   private final IUniqueIdService uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
   public ApplicantController() {
      this.applicantService = ServiceRegistry.get(IApplicantService.class);
      this.projectService = ServiceRegistry.get(ProjectService.class);
      this.enquiryService = ServiceRegistry.get(EnquiryService.class);
   }

   public List<String> getEligibleProjects() {
      Applicant applicant = (Applicant) sessionManager.getCurrentUser();
      Predicate<String> predicate = applicantService.isEligibleForApplicant(applicant);
      return projectService.getFilteredProjects(predicate);
   }

   public void applyForProject(int projectId, FlatType flatType) {
      Applicant applicant = (Applicant) sessionManager.getCurrentUser();
      String project = projectService.getProjectById(projectId);

      // check again just in case
      boolean alreadyApplied = applicant.getMyApplications().stream()
              .anyMatch(app -> app.getProjectName().getId().equals(projectId));
      if (alreadyApplied) {
         // Or handle differently if re-application is allowed under certain conditions
         throw new IllegalArgumentException("You have already applied for this project.");
      }

      // Check remaining flats again right before creating the application
      // This adds a layer of safety against race conditions, though not fully eliminating them
      // without more complex locking mechanisms.
      int remainingUnits = project.getRemainingFlats().getOrDefault(flatType, 0);
      if (remainingUnits <= 0) {
         throw new IllegalArgumentException("No " + flatType + " units remaining for project '" + project.getProjectName() + "'.");
      }

      Application application = new Application(, applicant, project, flatType);

      // Decrement remaining flats count *before* adding the application
      // This is a simple way to handle concurrency, though not foolproof.
      // A more robust solution might involve database transactions or synchronized blocks
      // if high concurrency is expected.
      project.getRemainingFlats().computeIfPresent(flatType, (key, value) -> value > 0 ? value - 1 : 0);

      projectService.addApplicationToProject(application); // Adds to project's list
      applicantService.addApplicationToApplicant(applicant, application); // Adds to applicant's list
   }


   public List<Application> getMyApplications() {
      Applicant applicant = (Applicant) sessionManager.getCurrentUser();
      return applicant.getMyApplications();
   }

   public void requestWithdrawal(int applicationId) {
      Application application = getApplicationById(applicationId);
      if (application == null) {
         throw new IllegalArgumentException("Application not found.");
      }
      application.setWithdrawalRequestStatus(WithdrawalRequestStatus.PENDING);
   }

   public void submitEnquiry(int projectId, java.lang.String enquiryText) {
    /*  Applicant applicant = (Applicant) sessionManager.getCurrentUser();
      Project project = projectService.getProjectById(projectId);
      if (project == null) {
         throw new IllegalArgumentException("Project not found.");
      }

      int enquiryId = uniqueIdService.generateUniqueId(IdType.);
      Enquiry enquiry = enquiryService.createEnquiry(enquiryId, project, applicant, enquiryText);
      enquiryService.submitEnquiry(enquiry);*/
   }


   public List<Enquiry> getMyEnquiries() {
     /* Applicant applicant = (Applicant) sessionManager.getCurrentUser();
      return enquiryService.getEnquiriesForApplicant(applicant);*/
   }

   public void editEnquiry(Enquiry enquiry, java.lang.String newEnquiryText) {
      Applicant applicant = (Applicant) sessionManager.getCurrentUser();

      if (enquiry == null) {
         throw new IllegalArgumentException("Enquiry not found.");
      }
     // enquiryService.editEnquiry(applicant, enquiry, newEnquiryText);
   }

   public boolean deleteEnquiryIfAllowed(Enquiry enquiry) {
      Applicant applicant = (Applicant) sessionManager.getCurrentUser();

      if (enquiry == null) {
         throw new IllegalArgumentException("Enquiry not found.");
      }

      //return enquiryService.deleteEnquiry(applicant, enquiry);
      return true;
   }


   public void handlePasswordChange(java.lang.String oldPass, java.lang.String newPass1, java.lang.String newPass2) {
      applicantService.changePassword( oldPass, newPass1, newPass2); // Uses IUserService default
   }

   private Application getApplicationById(int applicationId) {
      Applicant applicant = (Applicant) sessionManager.getCurrentUser();
      return applicant.getMyApplications().stream()
              .filter(app -> app.getId() == applicationId)
              .findFirst()
              .orElse(null);
   }

   public boolean hasCurrentApplication() {
      User user = sessionManager.getCurrentUser();
      if (!(user instanceof Applicant)) {
         throw new IllegalStateException("Current user is not an applicant.");
      }

      List<Application> applications = applicantService.getApplicationsByApplicant((Applicant) user);

      for (Application app : applications) {
         if (app.getApplicationStatus() == ApplicationStatus.PENDING ||
                 app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL ||
                 app.getBookingStatus() == BookingStatus.BOOKED ||
                 app.getBookingStatus() == BookingStatus.PENDING) {
            return true;
         }
      }
      return false;
   }

   public Application getPendingApplication() {
      User user = sessionManager.getCurrentUser();
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

   public List<List<java.lang.String>> getMyEnquiriesAsTableData() {
      User user = sessionManager.getCurrentUser();
      if (!(user instanceof Applicant)) {
         throw new IllegalStateException("Current user is not an applicant.");
      }

      List<Enquiry> enquiries = applicantService.getEnquiriesByApplicant((Applicant) user);
      List<List<java.lang.String>> tableData = new ArrayList<>();

      // Add header row
      tableData.add(List.of("ID", "Project", "Date", "Question", "Reply"));

      // Add each enquiry row
      for (Enquiry e : enquiries) {
         tableData.add(List.of(
                 java.lang.String.valueOf(e.getId()),
                 e.getProject().getProjectName(),
                 e.getDateEnquired().toString(),
                 e.getEnquiry() != null ? e.getEnquiry() : "-",
                 e.getReply() != null ? e.getReply() : "-"
         ));
      }

      return tableData;
   }

   public Optional<String> validateUserApplicationInput(List<String> eligibleProjects, java.lang.String input) {
      if (input == null || input.isBlank() || eligibleProjects == null || eligibleProjects.isEmpty()) {
         return Optional.empty(); // Return empty if input or list is invalid
      }

      final java.lang.String trimmedInput = input.trim(); // Trim whitespace once

      // Try finding by ID first
      try {
         int projectId = Integer.parseInt(trimmedInput);
         // Search within the eligibleProjects list by ID
         Optional<String> projectById = eligibleProjects.stream()
                 .filter(p -> p.getId().equals(projectId))
                 .findFirst();
         // If found by ID, return it immediately
         if (projectById.isPresent()) {
            return projectById;
         }
      } catch (NumberFormatException ignored) {
         // Input was not a valid integer, proceed to check by name
      }

      // If not found by ID (or input wasn't an ID), find by name (case-insensitive)
      // Search within the eligibleProjects list by name
      return eligibleProjects.stream()
              .filter(p -> p.getProjectName().equalsIgnoreCase(trimmedInput))
              .findFirst();
   }

   public void addApplicationToApplicant(Applicant applicant, Application application) {
      applicantService.addApplicationToApplicant(applicant, application);
   }
}
