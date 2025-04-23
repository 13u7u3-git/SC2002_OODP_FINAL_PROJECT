package applicant;

import enquiry.Enquiry;
import project.Project;
import user.User;

import java.util.List;
import java.util.function.Predicate;

public interface IApplicantService {

    User getUser();

    void setUser(User applicant);

    Predicate<Project> isEligibleForApplicant(Applicant applicant);

    List<Application> getApplicationsByApplicant(Applicant user);

    List<Enquiry> getEnquiriesByApplicant(Applicant user);
}
