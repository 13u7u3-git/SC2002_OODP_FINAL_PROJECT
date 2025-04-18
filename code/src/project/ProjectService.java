package project;

import applicant.Application;
import enquiry.Enquiry;
import enums.FlatType;
import enums.MaritalStatus;
import officer.RegistrationForm;
import user.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProjectService {

    public ProjectService() {
    }




    // --- Filtering ---

    // Methods receive projects because we allow user to chain filters
    public List<Project> getVisibleProjects(List<Project> projects) {
        return projects.stream()
                .filter(Project::isVisibility)
                .toList();
    }

    public List<Project> getOpenProjects(List<Project> projects) {
        LocalDate now = LocalDate.now();
        return projects.stream()
                .filter(p -> !now.isBefore(p.getApplicationOpeningDate()) && !now.isAfter(p.getApplicationClosingDate())) // if time now is not before opening date and not after closing date, then proceed
                .toList();
    }

    public List<Project> getProjectsByName(List<Project> projects, String name) {
        return projects.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<Project> getProjectsByNeighbourhood(List<Project> projects, String neighbourhood) {
        return projects.stream()
                .filter(p -> p.getNeighbourhood().equalsIgnoreCase(neighbourhood))
                .toList();
    }

    public List<Project> getProjectsByFlatType(List<Project> projects, FlatType flatType) {
        return projects.stream()
                .filter(p -> p.getAvailableFlats().containsKey(flatType))
                .toList();
    }

    public List<Project> getProjectsOpenOnDate(List<Project> projects, LocalDate date) {
        return projects.stream()
                .filter(p -> !date.isBefore(p.getApplicationOpeningDate()) && !date.isAfter(p.getApplicationClosingDate())) // we want those projects that are open on the day we input
                .toList();
    }









    // --- Manager Service coordination ---

    // We will be doing input validation in Manager Service. These methods assume inputs are correct.

    // Add
    public void addProject(ProjectRegistry projectRegistry, Project project) {
        projectRegistry.addProject(project);
    }

    // Remove
    public void removeProject(ProjectRegistry projectRegistry, Project project) {
        projectRegistry.removeProject(project);
    }

    // Update the visibility of a project
    public void onProjectVisibility(Project project) {
        project.setVisibility(true);
    }

    // Update the visibility of a project
    public void offProjectVisibility(Project project) {
        project.setVisibility(false);
    }

    // Update the application opening date
    public boolean updateApplicationOpeningDate(Project project, LocalDate newOpeningDate) {
        // Validate that the new opening date is before the closing date
        if (newOpeningDate.isAfter(project.getApplicationClosingDate())) {
            throw new IllegalArgumentException("Opening date cannot be before closing date.");
        }

        project.setApplicationOpeningDate(newOpeningDate);
        return true;
    }

    // Update the application closing date
    public boolean updateApplicationClosingDate(Project project, LocalDate newClosingDate) {
        // Validate that the new closing date is after the opening date
        if (newClosingDate.isBefore(project.getApplicationOpeningDate())) {
            throw new IllegalArgumentException("Closing date cannot be before opening date.");
        }

        project.setApplicationClosingDate(newClosingDate);
        return true;
    }

    // Update the name
    public void updateProjectName(Project project, String newName) {
        project.setName(newName);
    }

    // Update the neighbourhood
    public void updateProjectNeighbourhood(Project project, String newNeighbourhood) {
        project.setNeighbourhood(newNeighbourhood);
    }

    // Update available flats
    public void updateAvailableFlats(Project project, FlatType flatType, Integer newCount) {
        Map<FlatType, Integer> availableFlats = project.getAvailableFlats();
        availableFlats.put(flatType, newCount);
    }

    public List<Enquiry> getAllEnquiriesFromAllProjects(ProjectRegistry projectRegistry) {
        return projectRegistry.getProjects().stream()
                .flatMap(p -> p.getEnquiries().stream())
                .sorted(Comparator.comparing(e -> e.getProject().getName()))
                .toList();
    }





    // --- Applicant Service Coordination ---



    // this means our view eligible projects must filter out marital status and age.
    // we will check whether applicant already has application in applicant service.

    public void addApplicationToProject(Application application) {
        Project projectForApplication = application.getProject();
        projectForApplication.getApplications().add(application);
    }

    // Once we add application to project, there will be no more removal of applications.




    // *** IMPORTANT

    // Filter by marital status and age and date
    public List<Project> getEligibleProjects(ProjectRegistry projectRegistry, User applicant) {
        List<Project> visibleProjects = projectRegistry.getProjects().stream()
                .filter(project -> project.isVisibility() && LocalDate.now().isBefore(project.getApplicationClosingDate()))
                .toList();

        if ((applicant.getAge() < 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) ||
                (applicant.getAge() < 21 && applicant.getMaritalStatus() == MaritalStatus.MARRIED)) {
            return Collections.emptyList();
        }
        else if (applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
            List<Project> eligibleProjects = visibleProjects.stream()
                    .filter(project -> project.getAvailableFlats().getOrDefault(FlatType.TWO_ROOM, 0) > 0)
                    .toList();

            if (eligibleProjects.isEmpty()) {
                return Collections.emptyList();
            }
            else {
                return eligibleProjects;
            }
        }
        else {
            if (visibleProjects.isEmpty()) {
                return Collections.emptyList();
            }
            else {
                return visibleProjects;
            }
        }
    }




    // --- Enquiry Service Coordination ---

    public void addEnquiryToProject(Enquiry enquiry) {
         Project projectForEnquiry = enquiry.getProject();
         projectForEnquiry.getEnquiries().add(enquiry);
    }

    public void removeEnquiryFromProject(Enquiry enquiry) {
        Project projectBelongingToEnquiry = enquiry.getProject();
        projectBelongingToEnquiry.getEnquiries().remove(enquiry);
    }

    public List<Enquiry> getAllEnquiriesFrom(Project project) {
        return project.getEnquiries();
    }





    // --- Officer Service Coordination ---

    public boolean hasOfficerSlot(Project project) {
        return project.getAvailableOfficerSlots() > 0;
    }

    // officer service will check selected project he wants to register for with officer's list of registrations (not a officer for another project within an application period)
    // and officer's list of applications to ensure no clash in timing.
    public void addOfficerRegReqToProject(RegistrationForm form) {
        Project projectForForm = form.getProject();
        projectForForm.getRegistrationForms().add(form);
    }




    // --- Validation ---

    //validate project Manager and officer
    public boolean isProjectStaff(User user, Project project) {
        return project.getOfficers().contains(user) || project.getManager().equals(user);
    }
}
