package applicant;

import enquiry.Enquiry;
import project.String;
import user.IUserService;
import user.User;

import java.util.List;
import java.util.function.Predicate;

public interface IApplicantService extends IUserService {

   User getUser();

   void setUser(User applicant);

   Predicate<String> isEligibleForApplicant(Applicant applicant);

   List<Application> getApplicationsByApplicant(Applicant user);

   List<Enquiry> getEnquiriesByApplicant(Applicant user);
}
