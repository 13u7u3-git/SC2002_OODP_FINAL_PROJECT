package applicant;

import enquiry.Enquiry;
import project.FlatType;
import project.String;
import user.IPasswordValidationService;
import user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class ApplicantService implements IApplicantService {
   private Applicant applicant;

   public ApplicantService() {
   }

   @Override
   public User getUser() {
      return this.applicant;
   }

   @Override
   public void setUser(User applicant) {
      this.applicant = (Applicant) applicant;
   }

   @Override
   public IPasswordValidationService getPasswordValidationService() {
      return null;
   }

   @Override
   public Predicate<String> isEligibleForApplicant(Applicant applicant) {
      // Only projects with visibility turned on are shown
      // Only projects with open application periods are shown
      // Singles (35+ years) can only see projects with 2-Room flats
      // Married applicants (21+ years) can see all project
      // the applicant has already applied for are always visible, regardless of visibility setting

      return project -> {
         // Check if applicant has already applied for this project
         boolean hasApplied = project.getApplications().stream()
                 .anyMatch(app -> app.getApplicant().equals(applicant));

         // If already applied, always show the project
         if (hasApplied) {
            return true;
         }

         // Check visibility
         if (!project.isVisibility()) {
            return false;
         }

         // Check if application period is open
         LocalDate currentDate = LocalDate.now();
         if (currentDate.isBefore(project.getApplicationOpeningDate()) ||
                 currentDate.isAfter(project.getApplicationClosingDate())) {
            return false;
         }

         // Check eligibility based on age and marital status
         // Married applicants (21+ years) can see all projects
         if (applicant.getAge() >= 35) {
            // Singles (35+ years) can only see projects with 2-Room flats
            return project.getAvailableFlats().containsKey(FlatType.TWO_ROOM) &&
                    project.getAvailableFlats().get(FlatType.TWO_ROOM) > 0;
         }
         else {
            return applicant.getAge() >= 21;
         }

         // Applicants under 21 are not eligible
      };
   }

   @Override
   public List<Application> getApplicationsByApplicant(Applicant user) {
      return user.getMyApplications();
   }

   @Override
   public List<Enquiry> getEnquiriesByApplicant(Applicant user) {
      return user.getEnquiries();
   }
}
