//package service;
//
//import enums.ApplicationStatus;
//import enums.FlatType;
//import enums.RegistrationStatus;
//import enums.WithdrawalStatus;
//import model.enquiry.Enquiry;
//import model.enquiry.Reply;
//import model.form.Application;
//import model.form.RegistrationForm;
//import model.project.Project;
//import model.project.ProjectRegistry;
//import model.user.Manager;
//import util.UniqueId;
//
//import java.time.LocalDate;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ManagerService {
//    private ManagerService() {}
//
//    private static class Holder {
//        private static final ManagerService INSTANCE = new ManagerService();
//    }
//
//    public static ManagerService getInstance() {
//        return ManagerService.Holder.INSTANCE;
//    }
//
//    /**
//     * Creates a new project listing.
//     */
//    public Project createProject(Manager manager, String name, String neighbourhood,
//                                 java.util.Map<FlatType, Integer> availableFlats,
//                                 LocalDate applicationOpeningDate, LocalDate applicationClosingDate,
//                                 int officerSlots) {
//        UniqueId idGen = new UniqueId();
//        int projectId = idGen.getNextProjectId();
//        // Set visibility to "on" by default.
//        Project project = new Project(projectId, name, neighbourhood, availableFlats,
//                applicationOpeningDate, applicationClosingDate, manager, true, officerSlots);
//        ProjectRegistry.getInstance().addProject(project);
//        System.out.println("Project created successfully.");
//        return project;
//    }
//
//    /**
//     * Edits an existing project. Only the manager in charge can edit.
//     */
//    public boolean editProject(Manager manager, Project project, String newName, String newNeighbourhood,
//                               LocalDate openingDate, LocalDate closingDate, int officerSlots,
//                               java.util.Map<FlatType, Integer> availableFlats) {
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to edit this project.");
//            return false;
//        }
//        project.setName(newName);
//        project.setNeighbourhood(newNeighbourhood);
//        project.setApplicationOpeningDate(openingDate);
//        project.setApplicationClosingDate(closingDate);
//        project.setAvailableOfficerSlots(officerSlots);
//        // Update available flat counts.
//        project.getAvailableFlats().clear();
//        project.getAvailableFlats().putAll(availableFlats);
//        System.out.println("Project edited successfully.");
//        return true;
//    }
//
//    /**
//     * Deletes a project listing.
//     */
//    public boolean deleteProject(Manager manager, Project project) {
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to delete this project.");
//            return false;
//        }
//        ProjectRegistry.getInstance().getProjectList().remove(project);
//        System.out.println("Project deleted successfully.");
//        return true;
//    }
//
//    /**
//     * Toggles the project visibility.
//     */
//    public void toggleProjectVisibility(Manager manager, Project project, boolean visibility) {
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to change the visibility of this project.");
//            return;
//        }
//        project.setVisibility(visibility);
//        System.out.println("Project visibility updated.");
//    }
//
//    /**
//     * Returns projects created by the specified manager.
//     */
//    public List<Project> viewProjectsByManager(Manager manager) {
//        return ProjectRegistry.getInstance().getProjectList().stream()
//                .filter(p -> p.getManager().equals(manager))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Returns the officer registration forms for a given project.
//     */
//    public List<RegistrationForm> viewOfficerRegistrations(Project project) {
//        return project.getOfficerRegistrationFormList();
//    }
//
//    /**
//     * Approves an officer's registration request.
//     */
//    public boolean approveOfficerRegistration(Manager manager, Project project, RegistrationForm registrationForm) {
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to approve registrations for this project.");
//            return false;
//        }
//        registrationForm.setRegistrationStatus(RegistrationStatus.APPROVED);
//        project.setAvailableOfficerSlots(project.getAvailableOfficerSlots() - 1);
//        System.out.println("Officer registration approved.");
//        return true;
//    }
//
//    /**
//     * Rejects an officer's registration request.
//     */
//    public boolean rejectOfficerRegistration(Manager manager, Project project, RegistrationForm registrationForm) {
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to reject registrations for this project.");
//            return false;
//        }
//        registrationForm.setRegistrationStatus(RegistrationStatus.REJECTED);
//        System.out.println("Officer registration rejected.");
//        return true;
//    }
//
//    /**
//     * Approves an applicant's BTO application, reducing the available flat count.
//     */
//    public boolean approveApplication(Manager manager, Application application) {
//        Project project = application.getProject();
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to approve applications for this project.");
//            return false;
//        }
//        FlatType flatType = application.getFlatType();
//        int available = project.getAvailableFlats().getOrDefault(flatType, 0);
//        if (available <= 0) {
//            System.out.println("No available units for the requested flat type.");
//            return false;
//        }
//        application.setApplicationStatus(ApplicationStatus.SUCCESSFUL);
//        project.getAvailableFlats().put(flatType, available - 1);
//        System.out.println("Application approved.");
//        return true;
//    }
//
//    /**
//     * Rejects an applicant's BTO application.
//     */
//    public boolean rejectApplication(Manager manager, Application application) {
//        Project project = application.getProject();
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to reject applications for this project.");
//            return false;
//        }
//        application.setApplicationStatus(ApplicationStatus.UNSUCCESSFUL);
//        System.out.println("Application rejected.");
//        return true;
//    }
//
//    /**
//     * Approves an applicant's withdrawal request.
//     */
//    public boolean approveWithdrawalRequest(Manager manager, Application application) {
//        Project project = application.getProject();
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to approve withdrawal requests for this project.");
//            return false;
//        }
//        // Business logic: set the application status to indicate withdrawal.
//        application.setApplicationStatus(ApplicationStatus.UNSUCCESSFUL);
//        System.out.println("Withdrawal request approved.");
//        return true;
//    }
//
//    /**
//     * Rejects an applicant's withdrawal request.
//     */
//    public boolean rejectWithdrawalRequest(Manager manager, Application application) {
//        Project project = application.getProject();
//        if (!project.getManager().equals(manager)) {
//            System.out.println("You are not authorized to reject withdrawal requests for this project.");
//            return false;
//        }
//        // Revert withdrawal request.
//        application.setWithdrawalRequestStatus(WithdrawalStatus.NONE);
//        System.out.println("Withdrawal request rejected.");
//        return true;
//    }
//
//    /**
//     * Generates a report of applicants whose applications are booked.
//     */
//    public void generateFlatBookingReport(Manager manager, Project project) {
//        List<Application> bookedApps = project.getApplicationList().stream()
//                .filter(app -> app.getApplicationStatus() == ApplicationStatus.BOOKED)
//                .sorted(Comparator.comparing(app -> app.getApplicant().getName()))
//                .collect(Collectors.toList());
//        System.out.println("Flat Booking Report for Project: " + project.getName());
//        for (Application app : bookedApps) {
//            System.out.println("Applicant: " + app.getApplicant().getName() +
//                    ", NRIC: " + app.getApplicant().getNric() +
//                    ", Age: " + app.getApplicant().getAge() +
//                    ", Marital Status: " + app.getApplicant().getMaritalStatus() +
//                    ", Flat Type: " + app.getFlatType());
//        }
//    }
//
//    /**
//     * Allows the manager to reply to an enquiry for a project they are handling.
//     */
//    public void replyToEnquiry(Manager manager, Enquiry enquiry, String replyMessage) {
//        if (!enquiry.getProject().getManager().equals(manager)) {
//            System.out.println("You are not authorized to reply to this enquiry.");
//            return;
//        }
//        UniqueId idGen = new UniqueId();
//        int replyId = idGen.getNextReplyId();
//        Reply reply = new Reply(replyId, replyMessage, manager, LocalDate.now());
//        enquiry.addReply(reply);
//        System.out.println("Reply added.");
//    }
//}
