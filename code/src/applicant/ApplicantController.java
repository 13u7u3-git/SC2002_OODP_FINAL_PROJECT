package applicant;

import enquiry.Enquiry;
import enquiry.EnquiryService;
import project.IProjectService;
import project.Project;
import project.ProjectService;
import system.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ApplicantController {
   private final IApplicantService applicantService;
   private final ProjectService projectService;
   private final EnquiryService enquiryService;

   public ApplicantController(IApplicantService applicantService, ProjectService projectService, EnquiryService enquiryService) {
      this.applicantService = applicantService;
      this.projectService = projectService;
      this.enquiryService = enquiryService;
   }

   public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
      applicantService.changePassword(oldPassword, newPassword, confirmPassword);
   }

   public boolean deleteEnquiryIfAllowed(int enquiryId) {
      Applicant currentUser = sessionManager.getLoggedInApplicant();
      return applicantService.deleteEnquiryIfAllowed(currentUser.getId(), enquiryId);
   }

   public List<List<String>> getEligibleProjectsTableData() {
      Applicant applicant = (Applicant) applicantService.getUser();

      Predicate<Project> predicate = applicantService.isEligibleForApplicant(applicant);
      List<Project> projects = projectService.getFilteredProjects(predicate);
      if (projects == null || projects.isEmpty()) {
         return null;
      }
      else {
         List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
         List<List<String>> tableData = new ArrayList<>();
         tableData.add(headerRow.subList(0, 10));
         for (Project p : projects) {
            List<String> fromEachProject = p.toStringAsList().subList(0, 10);
            tableData.add(fromEachProject);
         }
         return tableData;
      }
   }

   public List<Enquiry> getMyEnquiries() {
      return enquiryService.getEnquiriesForApplicant();
   }
}

