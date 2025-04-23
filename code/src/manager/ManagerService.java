// ManagerService.java with full method implementations, preserving existing class references
package manager;

import enquiry.Enquiry;
import interfaces.StaffService;
import officer.IOfficerService;
import officer.RegistrationForm;
import officer.RegistrationStatus;
import project.FlatType;
import project.ProjectService;
import project.Project;
import system.ServiceRegistry;
import user.IPasswordValidationService;
import user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerService implements StaffService {
    private final ProjectService projectService;
    private final IPasswordValidationService passwordValidationService;
    private Manager manager;

    public ManagerService() {
        this.projectService = ServiceRegistry.get(ProjectService.class);
        this.passwordValidationService = ServiceRegistry.get(IPasswordValidationService.class);
    }

    @Override
    public List<Enquiry> getCurrentProjectEnquiries() {
        return manager.getCurrentProject() != null ? manager.getCurrentProject().getEnquiries() : new ArrayList<>();
    }

    @Override
    public void createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                               Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                               LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                               List<String> officers) throws IllegalArgumentException {
        projectService.createProject(projectName, neighbourhood, twoRoomUnits, twoRoomPrice,
                threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate,
                manager, availableOfficerSlots, officers);
    }

    @Override
    public void updateProject(Project project, String name, String neighbourhood, Map<FlatType, Integer> availableFlats,
                               LocalDate openingDate, LocalDate closingDate, boolean visibility) {
        // Not used
    }

    @Override
    public void deleteProject(Integer projectId) {
        Project project = projectService.getProjectById(projectId);
        projectService.deleteProject(project);
    }

    @Override
    public List<Project> getMyProjects() {
        return projectService.getFilteredProjects(project -> project.getManager().equals(manager.getName()));
    }

    public boolean deleteProjectSafe(Integer projectId) {
        try {
            deleteProject(projectId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Project getCurrentProject() {
        return manager.getCurrentProject();
    }

    @Override
    public void setCurrentProject(Project project) {
        manager.setCurrentProject(project);
    }

    @Override
    public void viewAllEnquiries() {
        // unused
    }

    @Override
    public User getUser() {
        return manager;
    }

    @Override
    public void setUser(Manager manager) {
        this.manager = manager;
    }

    @Override
    public IPasswordValidationService getPasswordValidationService() {
        return passwordValidationService;
    }

    @Override
    public List<RegistrationForm> getPendingOfficerRegistrations() throws Exception {
        Project project = manager.getCurrentProject();
        if (project == null) throw new Exception("No current project");
        return project.getRegistrationForms().stream().filter(RegistrationForm::isPending).toList();
    }

    @Override
    public String setRegistrationStatus(String identifier, RegistrationStatus status) throws Exception {
        Project project = manager.getCurrentProject();
        if (project == null) throw new Exception("No current project");
        try {
            RegistrationForm form = identifier.matches("\\d+")
                    ? project.getRegistrationForms().stream().filter(f -> f.getId().toString().equals(identifier)).findFirst().orElseThrow()
                    : project.getRegistrationForms().stream().filter(f -> f.getOfficerName().equals(identifier)).findFirst().orElseThrow();
            form.setStatus(status);
            return form.getOfficerName();
        } catch (Exception e) {
            throw new Exception("Registration not found");
        }
    }

    @Override
    public void addToOfficersList(String officerStr) {
        Project project = manager.getCurrentProject();
        if (project != null) {
            for (String officer : officerStr.split(",")) {
                project.getOfficers().add(officer);
            }
        }
    }

    public void setOfficerCurrentProject(String officerName) {
        IOfficerService officerService = ServiceRegistry.get(IOfficerService.class);
        officerService.setOfficerCurrentProject(officerName, manager.getCurrentProject());
    }

    public List<List<String>> getAllProjectsTableData() throws Exception {
        List<Project> projects = projectService.getAllProjects();
        if (projects == null || projects.isEmpty()) return null;
        List<List<String>> tableData = new ArrayList<>();
        tableData.add(List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers"));
        for (Project p : projects) tableData.add(p.toStringAsList());
        return tableData;
    }

    public List<List<String>> getMyProjectsTableData() {
        List<Project> myProjects = getMyProjects();
        if (myProjects == null || myProjects.isEmpty()) return null;
        List<List<String>> tableData = new ArrayList<>();
        tableData.add(List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers"));
        for (Project p : myProjects) tableData.add(p.toStringAsList());
        return tableData;
    }

    public boolean toggleVisibility(Integer projectId) {
        try {
            Project project = projectService.getProjectById(projectId);
            project.setVisibility(!project.isVisibility());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void editProject(Integer projectId, String option, Object value) {
        Project project = projectService.getProjectById(projectId);
        switch (option) {
            case "1" -> project.setProjectName((String) value);
            case "2" -> project.setNeighborhood((String) value);
            case "3" -> project.setApplicationOpeningDate((LocalDate) value);
            case "4" -> project.setApplicationClosingDate((LocalDate) value);
            case "5" -> project.setVisibility((Boolean) value);
            default -> throw new IllegalArgumentException("Invalid option");
        }
    }

    public List<List<String>> getApplicantApplications() {
        Project project = manager.getCurrentProject();
        if (project == null || project.getApplications() == null) return null;
        List<List<String>> table = new ArrayList<>();
        table.add(List.of("App ID", "Applicant Name", "Flat Type", "Status"));
        project.getApplications().forEach(app -> table.add(List.of(
                app.getId().toString(),
                app.getApplicantName(),
                app.getFlatType().toString(),
                app.getStatus().toString()
        )));
        return table;
    }

    public boolean processApplicantApplication(String appId, String action) {
        Project project = manager.getCurrentProject();
        if (project == null || project.getApplications() == null) return false;
        return project.getApplications().stream()
                .filter(app -> app.getId().toString().equals(appId))
                .findFirst()
                .map(app -> {
                    switch (action) {
                        case "A" -> app.approve();
                        case "R" -> app.reject();
                        default -> throw new IllegalArgumentException("Invalid action");
                    }
                    return true;
                }).orElse(false);
    }

    public List<List<String>> getWithdrawalRequests() {
        Project project = manager.getCurrentProject();
        if (project == null || project.getWithdrawalRequests() == null) return null;
        List<List<String>> table = new ArrayList<>();
        table.add(List.of("Request ID", "Applicant", "Reason", "Status"));
        project.getWithdrawalRequests().forEach(req -> table.add(List.of(
                req.getId().toString(),
                req.getApplicantName(),
                req.getReason(),
                req.getStatus().toString()
        )));
        return table;
    }

    public boolean processWithdrawalRequest(String id, String action) {
        Project project = manager.getCurrentProject();
        if (project == null || project.getWithdrawalRequests() == null) return false;
        return project.getWithdrawalRequests().stream()
                .filter(req -> req.getId().toString().equals(id))
                .findFirst()
                .map(req -> {
                    switch (action) {
                        case "A" -> req.approve();
                        case "R" -> req.reject();
                        default -> throw new IllegalArgumentException("Invalid action");
                    }
                    return true;
                }).orElse(false);
    }

    public List<Enquiry> getAllEnquiries() {
        Project project = manager.getCurrentProject();
        return (project == null) ? new ArrayList<>() : project.getEnquiries();
    }

    public List<List<String>> formatEnquiries(List<Enquiry> enquiries) {
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("ID", "Applicant", "Message", "Reply"));
        for (Enquiry e : enquiries) {
            data.add(List.of(
                    e.getId().toString(),
                    e.getApplicantName(),
                    e.getMessage(),
                    e.getReply() != null ? e.getReply() : "No reply"
            ));
        }
        return data;
    }

    public boolean replyToEnquiry(String enquiryId, String reply) {
        Project project = manager.getCurrentProject();
        if (project == null || project.getEnquiries() == null) return false;
        return project.getEnquiries().stream()
                .filter(e -> e.getId().toString().equals(enquiryId))
                .findFirst()
                .map(e -> {
                    e.setReply(reply);
                    return true;
                }).orElse(false);
    }

    public String generateBookingReport() {
        Project project = manager.getCurrentProject();
        if (project == null || project.getApplications() == null) return "";
        StringBuilder report = new StringBuilder("Booking Report\n====================\n");
        for (var app : project.getApplications()) {
            report.append("Applicant: ").append(app.getApplicantName())
                    .append(" | Flat Type: ").append(app.getFlatType())
                    .append(" | Status: ").append(app.getStatus()).append("\n");
        }
        return report.toString();
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New passwords don't match");
        }
        passwordValidationService.validate(oldPassword, manager.getPassword());
        manager.setPassword(newPassword);
    }
}
