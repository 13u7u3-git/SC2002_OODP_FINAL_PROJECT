package project;

import applicant.Application;
import enquiry.Enquiry;
import enquiry.EnquiryService;
import officer.Officer;
import officer.RegistrationForm;
import user.User;

import java.time.LocalDate;
import java.util.List;

public class ProjectService {
    private static ProjectService instance = null;
    EnquiryService enquiryService = EnquiryService.getInstance();

    private ProjectService() {}

    public static ProjectService getInstance() {
        if (instance == null) {
            instance = new ProjectService();
        }
        return instance;
    }

    public boolean isApplicationOpen(Project project) {
        if(!project.getApplicationOpeningDate().isAfter(LocalDate.now()) && !project.getApplicationClosingDate().isBefore(LocalDate.now())){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean addApplicationToProject(Application application) {
        Project project = application.getProject();
        if (!isApplicationOpen(project)) {
            return false;
        }
        project.getApplications().add(application);
        return true;
    }

    public void addEnquiryToProject(Enquiry enquiry) {
        // TODO: Implement adding enquiry to project
        // Current implementation is commented out and incomplete
        //enquiry.getProject().getEnquiries().add(enquiry);
    }

    public void removeEnquiryFromProject(Enquiry enquiry) {
        enquiry.getProject().getEnquiries().remove(enquiry);
    }

    public List<Enquiry> getAllEnquiriesFrom(Project project) {
        return project.getEnquiries();
    }
    public List<Enquiry> getAllEnquiresFromAllProjects() {
        return ProjectRegistry.getProjects().stream()
                .flatMap(project -> project.getEnquiries().stream())
                .sorted((e1, e2) -> e1.getProject().getName().compareTo(e2.getProject().getName()))
                .toList();
    }

    public boolean isOfficerFreeSlotAvailable(Project project) {
        return project.getAvailableOfficerSlots() > project.getOfficers().size();
    }
    public void addOfficerRegReqToProject(RegistrationForm form) {
        Project project = form.getProject();
        project.getRegistrationForms().add(form);
    }

    public List<Project> getEligibleProjects(User applicant){
        // TODO: Implement method to return projects that the applicant is eligible for
        // Current implementation returns null and needs proper implementation
        return null;
    }

    //validate project Manager and officer
    public boolean isProjectStaff(User user, Project project) {
        return project.getOfficers().contains(user) || project.getManager().equals(user);
    }
}
