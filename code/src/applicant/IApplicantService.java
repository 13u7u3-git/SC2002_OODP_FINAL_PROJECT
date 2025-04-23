package applicant;

import project.Project;

import java.util.function.Predicate;

public interface IApplicantService {

   void setUser(Applicant applicant);

   Predicate<Project> isEligibleForApplicant(Applicant applicant);
}
